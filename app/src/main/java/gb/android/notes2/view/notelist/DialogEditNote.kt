package gb.android.notes2.view.notelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import gb.android.notes2.App;
import gb.android.notes2.R;
import gb.android.notes2.model.NoteListItem;
import gb.android.notes2.view.ViewManager;
import observer.NoteReadyObserver;

public class DialogEditNote extends DialogFragment implements View.OnClickListener, NoteReadyObserver {

    private String id;

    private AppCompatEditText et_title_note;
    private AppCompatTextView tv_date_note;
    private AppCompatImageButton btn_save_note;
    private AppCompatImageButton btn_close_note;
    private AppCompatEditText etml_text_note;

    //================================================================================================
    // EVENTS

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_note_editor, container, false);

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewManager.getPublisher().subscribeNoteReady(this);
        loadNote();
    }

    @Override
    public void onPause() {
        ViewManager.getPublisher().unsubscribeNoteReady(this);
        super.onPause();
    }

    //================================================================================================
    // INIT

    private void init(View view) {
        et_title_note = view.findViewById(R.id.et_title_note);
        tv_date_note = view.findViewById(R.id.tv_date_note);
        btn_save_note = view.findViewById(R.id.btn_save_note);
        btn_close_note = view.findViewById(R.id.btn_close_note);
        etml_text_note = view.findViewById(R.id.etml_text_note);

        btn_save_note.setOnClickListener(this);
        btn_close_note.setOnClickListener(this);
    }

    private void loadNote() {
        id = App.getInstance().getStrPref("id_dialog");

        if (id.equals("empty"))
            dismiss();

        App.getNoteListItemSource().requestNoteListItemById(id);
    }

    //================================================================================================
    // ON CLICK

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_note) {
            saveNote();
        }

        App.getInstance().setStrPref("id_dialog", "empty");

        dismiss();
    }

    //================================================================================================
    // ON CLICK METHODS

    private void saveNote() {
        App.getNoteListItemSource().updateNoteItemById(id, et_title_note.getText().toString(), tv_date_note.getText().toString());
        App.getNoteListItemSource().updateNoteTextById(id, etml_text_note.getText().toString());
    }

    //================================================================================================
    //OBSERVER

    @Override
    public void receiveNote(NoteListItem noteListItem, String text) {
        this.id = noteListItem.getId();

        et_title_note.setText(noteListItem.getTitle());
        tv_date_note.setText(noteListItem.getDate());
        etml_text_note.setText(text);
    }
}
