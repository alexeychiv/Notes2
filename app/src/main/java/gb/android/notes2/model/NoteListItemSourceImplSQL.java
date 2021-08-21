package gb.android.notes2.model;

import android.content.Context;

import java.util.List;

import gb.android.notes2.App;
import gb.android.notes2.sqlite.DataBase;

public class NoteListItemSourceImplSQL /*implements NoteListItemSource*/ {
/*
    private DataBase db;

    private List<NoteListItem> listNotes;

    // ===================================================================================================
    // CONSTRUCTOR

    public NoteListItemSourceImplSQL(Context context) {
        db = new DataBase(context);
        loadList();
    }

    // ===================================================================================================
    // UTILS

    private void loadList() {
        listNotes = db.getNotesList();

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

    // ===================================================================================================
    // NoteListItemSource Methods

    @Override
    public NoteListItemSource init(NoteListSourceResponse noteListSourceResponse) {

        if (noteListSourceResponse != null)
            noteListSourceResponse.initialized(this);

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
    public NoteListItem getNoteListItemById(String id) {
        return db.getNoteListItem(Integer.parseInt(id));
    }

    @Override
    public void setSort(int sortType) {
        App.getInstance().setIntPref("sort", sortType);
        loadList();
    }

    @Override
    public void addNote() {
        db.addNote();
        listNotes = db.getNotesList();
    }

    @Override
    public void deleteNote(String id) {
        db.deleteNote(Integer.parseInt(id));
        listNotes = db.getNotesList();
    }

    @Override
    public void deleteAll() {
        db.deleteAll();
        listNotes.clear();
    }

    @Override
    public String getNoteTextById(String id) {
        return db.getNoteText(Integer.parseInt(id));
    }

    @Override
    public void updateNoteItemById(String id, String title, String date) {
        db.updateNoteItem(Integer.parseInt(id), title, date);
    }

    @Override
    public void updateNoteTextById(String id, String text) {
        db.updateNoteText(Integer.parseInt(id), text);
    }

 */
}
