package gb.android.notes2.model

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import gb.android.notes2.App.Companion.instance
import gb.android.notes2.firestore.NoteItemTranslate
import gb.android.notes2.firestore.NoteItemTranslate.docToNoteListItem
import gb.android.notes2.firestore.NoteItemTranslate.noteListItemToDoc
import gb.android.notes2.utils.NoteDate.currentDateString
import gb.android.notes2.view.ViewManager.publisher
import java.util.*

class NoteListItemSourceImplFirestore : NoteListItemSource {

    private val collectionReferenceNotes: CollectionReference

    var listNotes: MutableList<NoteListItem>? = null

    // ===================================================================================================
    // INIT

    init {
        collectionReferenceNotes = FirebaseFirestore.getInstance().collection(NOTES_COLLECTION)
        updateData()
    }

    // ===================================================================================================
    // UTILS
    private fun loadList() {
        listNotes = ArrayList()
        collectionReferenceNotes
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result!!) listNotes?.add(docToNoteListItem(doc.id, doc.data))
                    sortList()
                    publisher!!.notifyDataChanged()
                }
            }
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

    private fun setTextForNote(id: String, text: String) {
        val textMap = HashMap<String, String>()
        textMap["text"] = text
        collectionReferenceNotes
            .document(id)
            .collection("text")
            .document("text")
            .set(textMap, SetOptions.merge())
            .addOnCompleteListener { loadList() }
    }

    private fun getText(noteListItem: NoteListItem) {
        collectionReferenceNotes
            .document(noteListItem.id!!)
            .collection("text")
            .document("text")
            .get()
            .addOnCompleteListener { task ->
                val text = task.result!!.data!!["text"] as String?
                publisher!!.notifyNoteReady(noteListItem, text)
            }
    }

    private var deleteCounter = 0
    private fun deleteNoteById(id: String?) {
        collectionReferenceNotes
            .document(id!!)
            .delete()
            .addOnCompleteListener {
                --deleteCounter
                if (deleteCounter < 1) {
                    deleteCounter = 0
                    loadList()
                }
            }
    }


    // ===================================================================================================
    // NoteListItemSource Methods

    override fun init(noteListSourceResponse: NoteListSourceResponse?): NoteListItemSource? {
        listNotes = ArrayList()
        collectionReferenceNotes
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result!!) listNotes?.add(docToNoteListItem(doc.id, doc.data))
                    noteListSourceResponse!!.initialized(this@NoteListItemSourceImplFirestore)
                }
            }
        return this
    }

    override fun updateData() {
        loadList()
    }

    override fun size(): Int {
        return listNotes!!.size
    }

    override fun getNoteListItemByPos(position: Int): NoteListItem? {
        return listNotes!![position]
    }

    override fun requestNoteListItemById(id: String?) {
        collectionReferenceNotes
            .document(id!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.result!!.data != null) getText(
                    docToNoteListItem(
                        task.result!!.id, task.result!!.data!!
                    )
                )
            }
    }

    override fun setSort(sortType: Int) {
        instance!!.setIntPref("sort", sortType)
        sortList()
        publisher!!.notifyDataChanged()
    }

    override fun addNote() {
        collectionReferenceNotes
            .add(noteListItemToDoc(NoteListItem("", "New Note", currentDateString)))
            .addOnCompleteListener { task -> setTextForNote(task.result!!.id, "") }
    }

    override fun deleteNote(id: String?) {
        deleteCounter = 1
        deleteNoteById(id)
    }

    override fun deleteAll() {
        deleteCounter = listNotes!!.size
        for (note in listNotes!!) {
            deleteNoteById(note.id)
        }
    }

    override fun updateNoteItemById(id: String?, title: String?, date: String?) {
        val noteHash = HashMap<String, Any?>()
        noteHash[NoteItemTranslate.Fields.TITLE] = title
        noteHash[NoteItemTranslate.Fields.DATE] = date
        collectionReferenceNotes
            .document(id!!)
            .set(noteHash, SetOptions.merge())
            .addOnCompleteListener { loadList() }
    }

    override fun updateNoteTextById(textId: String?, text: String?) {
        val noteTextHash = HashMap<String, Any?>()
        noteTextHash["text"] = text
        collectionReferenceNotes
            .document(textId!!)
            .collection("text")
            .document("text")[noteTextHash] = SetOptions.merge()
    }

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }
}