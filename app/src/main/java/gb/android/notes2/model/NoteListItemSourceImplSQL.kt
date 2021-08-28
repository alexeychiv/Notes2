package gb.android.notes2.model

import android.content.Context
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.sqlite.DataBase
import gb.android.notes2.view.ViewManager.publisher

class NoteListItemSourceImplSQL(context: Context?) : NoteListItemSource {

    private val db: DataBase
    var listNotes: MutableList<NoteListItem>? = null

    // ===================================================================================================
    // INIT

    init {
        db = DataBase(context)
        loadList()
    }


    // ===================================================================================================
    // UTILS

    private fun loadList() {
        listNotes = db.notesList
        sortList()
        publisher!!.notifyDataChanged()
    }

    private fun sortList() {

        when (instance!!.getIntPref("sort")) {
            -1 -> listNotes!!.sortWith(java.util.Comparator { obj: NoteListItem, o: NoteListItem? ->
                obj.compareIdAsc(
                    o!!
                )
            })
            1 -> listNotes!!.sortWith(java.util.Comparator { obj: NoteListItem, o: NoteListItem? ->
                obj.compareTitleAsc(
                    o!!
                )
            })
            2 -> listNotes!!.sortWith(java.util.Comparator { obj: NoteListItem, o: NoteListItem? ->
                obj.compareTitleDesc(
                    o!!
                )
            })
        }
    }

    // ===================================================================================================
    // NoteListItemSource Methods

    override fun init(noteListSourceResponse: NoteListSourceResponse?): NoteListItemSource? {
        if (noteListSourceResponse != null) noteListSourceResponse.initialized(this)
        return this
    }

    override fun updateData() {
        loadList()
    }

    override fun size(): Int {
        return listNotes!!.size
    }

    override fun getNoteListItemByPos(position: Int): NoteListItem {
        return listNotes!![position]
    }

    override fun requestNoteListItemById(id: String?) {
        val noteListItem = db.getNoteListItem(id!!.toInt())
        val text = db.getNoteText(id.toInt())
        publisher!!.notifyNoteReady(noteListItem, text)
    }

    override fun setSort(sortType: Int) {
        instance!!.setIntPref("sort", sortType)
        loadList()
    }

    override fun addNote() {
        db.addNote()
        loadList()
    }

    override fun deleteNote(id: String?) {
        db.deleteNote(id!!.toInt())
        loadList()
    }

    override fun deleteAll() {
        db.deleteAll()
        loadList()
    }

    override fun updateNoteItemById(id: String?, title: String?, date: String?) {
        db.updateNoteItem(id!!.toInt(), title, date)
        loadList()
    }

    override fun updateNoteTextById(id: String?, text: String?) {
        db.updateNoteText(id!!.toInt(), text)
    }
}