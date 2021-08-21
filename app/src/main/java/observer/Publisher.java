package observer;

import java.util.ArrayList;
import java.util.List;

import gb.android.notes2.model.NoteListItem;

public class Publisher {
    private List<DataChangeObserver> dataChangeObservers;
    private List<NoteReadyObserver> noteReadyObservers;

    public Publisher() {
        dataChangeObservers = new ArrayList<>();
        noteReadyObservers = new ArrayList<>();
    }

    public void subscribeDataChange(DataChangeObserver observer) {
        dataChangeObservers.add(observer);
    }

    public void unsubscribeDataChange(DataChangeObserver observer) {
        dataChangeObservers.remove(observer);
    }

    public void subscribeNoteReady(NoteReadyObserver observer) {
        noteReadyObservers.add(observer);
    }

    public void unsubscribeNoteReady(NoteReadyObserver observer) {
        noteReadyObservers.remove(observer);
    }

    public void notifyDataChanged() {
        for (DataChangeObserver observer : dataChangeObservers) {
            observer.update();
        }
    }

    public void notifyNoteReady(NoteListItem noteListItem, String text) {
        for (NoteReadyObserver observer : noteReadyObservers) {
            observer.receiveNote(noteListItem, text);
        }
    }
}
