package gb.android.notes2.model;

public interface NoteListItemSource {
    void updateData();

    int size();

    NoteListItem getNoteListItemByPos(int position);

    NoteListItem getNoteListItemById(int id);

    void setSort(int sortType);

    void addNote();

    void deleteNote(int position);

    void deleteAll();

    String getNoteTextById(int id);

    void updateNoteItemById(int id, String title, String date);

    void updateNoteTextById(int id, String text);
}
