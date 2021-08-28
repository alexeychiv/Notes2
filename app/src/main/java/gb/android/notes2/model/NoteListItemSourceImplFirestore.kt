package gb.android.notes2.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import firestore.NoteItemTranslate;
import gb.android.notes2.App;
import gb.android.notes2.utils.NoteDate;
import gb.android.notes2.view.ViewManager;

public class NoteListItemSourceImplFirestore implements NoteListItemSource {

    private static final String NOTES_COLLECTION = "notes";

    private final CollectionReference collectionReferenceNotes;

    List<NoteListItem> listNotes;

    // ===================================================================================================
    // CONSTRUCTOR

    public NoteListItemSourceImplFirestore() {
        collectionReferenceNotes = FirebaseFirestore.getInstance().collection(NOTES_COLLECTION);
        updateData();
    }

    // ===================================================================================================
    // UTILS

    private void loadList() {
        listNotes = new ArrayList<>();

        collectionReferenceNotes
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult())
                                listNotes.add(NoteItemTranslate.docToNoteListItem(doc.getId(), doc.getData()));

                            sortList();

                            ViewManager.getPublisher().notifyDataChanged();
                        }
                    }
                });
    }

    private void sortList() {
        switch (App.getInstance().getIntPref("sort")) {
            case -1:
                listNotes.sort(NoteListItem::compareIdAsc);
                break;
            case 1:
                listNotes.sort(NoteListItem::compareTitleAsc);
                break;
            case 2:
                listNotes.sort(NoteListItem::compareTitleDesc);
                break;
        }
    }

    private void setTextForNote(String id, String text) {
        HashMap<String, String> textMap = new HashMap<>();
        textMap.put("text", text);

        collectionReferenceNotes
                .document(id)
                .collection("text")
                .document("text")
                .set(textMap, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadList();
                    }
                });
    }

    private void getText(NoteListItem noteListItem) {
        collectionReferenceNotes
                .document(noteListItem.getId())
                .collection("text")
                .document("text")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String text = (String) task.getResult().getData().get("text");
                        ViewManager.getPublisher().notifyNoteReady(noteListItem, text);
                    }
                });
    }

    private int deleteCounter;

    private void deleteNoteById(String id) {
        collectionReferenceNotes
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        --deleteCounter;
                        if (deleteCounter < 1) {
                            deleteCounter = 0;
                            loadList();
                        }
                    }
                });
    }

    // ===================================================================================================
    // NoteListItemSource Methods

    @Override
    public NoteListItemSource init(NoteListSourceResponse noteListSourceResponse) {
        listNotes = new ArrayList<>();

        collectionReferenceNotes
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult())
                                listNotes.add(NoteItemTranslate.docToNoteListItem(doc.getId(), doc.getData()));

                            noteListSourceResponse.initialized(NoteListItemSourceImplFirestore.this);
                        }
                    }
                });

        return this;
    }

    @Override
    public void updateData() {
        loadList();
    }

    @Override
    public int size() {
        return listNotes.size();
    }

    @Override
    public NoteListItem getNoteListItemByPos(int position) {
        return listNotes.get(position);
    }

    @Override
    public void requestNoteListItemById(String id) {
        collectionReferenceNotes
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().getData() != null)
                            getText(NoteItemTranslate.docToNoteListItem(task.getResult().getId(), task.getResult().getData()));
                    }
                });
    }

    @Override
    public void setSort(int sortType) {
        App.getInstance().setIntPref("sort", sortType);
        sortList();
        ViewManager.getPublisher().notifyDataChanged();
    }

    @Override
    public void addNote() {
        collectionReferenceNotes
                .add(NoteItemTranslate.noteListItemToDoc(new NoteListItem("", "New Note", NoteDate.getCurrentDateString())))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        setTextForNote(task.getResult().getId(), "");
                    }
                });
    }

    @Override
    public void deleteNote(String id) {
        deleteCounter = 1;
        deleteNoteById(id);
    }

    @Override
    public void deleteAll() {
        deleteCounter = listNotes.size();
        for (NoteListItem note : listNotes) {
            deleteNoteById(note.getId());
        }
    }

    @Override
    public void updateNoteItemById(String id, String title, String date) {
        HashMap<String, Object> noteHash = new HashMap<>();
        noteHash.put(NoteItemTranslate.Fields.TITLE, title);
        noteHash.put(NoteItemTranslate.Fields.DATE, date);

        collectionReferenceNotes
                .document(id)
                .set(noteHash, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadList();
                    }
                });
    }

    @Override
    public void updateNoteTextById(String textId, String text) {
        HashMap<String, Object> noteTextHash = new HashMap<>();
        noteTextHash.put("text", text);

        collectionReferenceNotes
                .document(textId)
                .collection("text")
                .document("text")
                .set(noteTextHash, SetOptions.merge());
    }
}
