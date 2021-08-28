package gb.android.notes2.observer

import gb.android.notes2.observer.DataChangeObserver
import gb.android.notes2.observer.NoteReadyObserver
import gb.android.notes2.model.NoteListItem
import java.util.ArrayList

class Publisher {

    private val dataChangeObservers: MutableList<DataChangeObserver>
    private val noteReadyObservers: MutableList<NoteReadyObserver>

    fun subscribeDataChange(observer: DataChangeObserver) {
        dataChangeObservers.add(observer)
    }

    fun unsubscribeDataChange(observer: DataChangeObserver) {
        dataChangeObservers.remove(observer)
    }

    fun subscribeNoteReady(observer: NoteReadyObserver) {
        noteReadyObservers.add(observer)
    }

    fun unsubscribeNoteReady(observer: NoteReadyObserver) {
        noteReadyObservers.remove(observer)
    }

    fun notifyDataChanged() {
        for (observer in dataChangeObservers) {
            observer.update()
        }
    }

    fun notifyNoteReady(noteListItem: NoteListItem?, text: String?) {
        for (observer in noteReadyObservers) {
            observer.receiveNote(noteListItem, text)
        }
    }

    init {
        dataChangeObservers = ArrayList()
        noteReadyObservers = ArrayList()
    }
}