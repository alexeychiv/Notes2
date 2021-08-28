package gb.android.notes2.model

interface NoteListItemSource {
    fun init(noteListSourceResponse: NoteListSourceResponse?): NoteListItemSource?
    fun updateData()
    fun size(): Int
    fun getNoteListItemByPos(position: Int): NoteListItem?
    fun requestNoteListItemById(id: String?)
    fun setSort(sortType: Int)
    fun addNote()
    fun deleteNote(position: String?)
    fun deleteAll()
    fun updateNoteItemById(id: String?, title: String?, date: String?)
    fun updateNoteTextById(id: String?, text: String?)
}