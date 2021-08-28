package gb.android.notes2.observer

import gb.android.notes2.model.NoteListItem

interface NoteReadyObserver {
    fun receiveNote(noteListItem: NoteListItem?, text: String?)
}