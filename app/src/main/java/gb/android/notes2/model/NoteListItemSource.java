package gb.android.notes2.model;

public interface NoteListItemSource {
    NoteListItemSource init (NoteListSourceResponse noteListSourceResponse);

    void updateData();

    int size();

    NoteListItem getNoteListItemByPos(int position);

    void requestNoteListItemById(String id);

    void setSort(int sortType);

    void addNote();

    void deleteNote(String position);

    void deleteAll();

    void updateNoteItemById(String id, String title, String date);

    void updateNoteTextById(String id, String text);
}
