package gb.android.notes2.Model;

import android.content.Context;

import java.util.List;

import gb.android.notes2.App;
import gb.android.notes2.SQLite.DataBase;

public class NoteListItemSourceImpl implements NoteListItemSource {

    DataBase db;

    List<NoteListItem> listNotes;

    // ===================================================================================================
    // CONSTRUCTOR

    public NoteListItemSourceImpl(Context context) {
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
    public NoteListItem getNoteListItemById(int id) {
        return db.getNoteListItem(id);
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
    public void deleteNote(int id) {
        db.deleteNote(id);
        listNotes = db.getNotesList();
    }

    @Override
    public String getNoteTextById(int id) {
        return db.getNoteText(id);
    }

    @Override
    public void updateNoteItemById(int id, String title, String date) {
        db.updateNoteItem(id, title, date);
    }

    @Override
    public void updateNoteTextById(int id, String text) {
        db.updateNoteText(id, text);
    }
}
