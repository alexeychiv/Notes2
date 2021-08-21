package observer;

import gb.android.notes2.model.NoteListItem;

public interface NoteReadyObserver {
    void receiveNote(NoteListItem noteListItem, String text);
}
