package gb.android.notes2.firestore

import gb.android.notes2.model.NoteListItem
import java.util.*

object NoteItemTranslate {
    @JvmStatic
    fun docToNoteListItem(id: String?, doc: Map<String?, Any?>): NoteListItem {
        return NoteListItem(id!!, doc[Fields.TITLE] as String?, doc[Fields.DATE] as String?)
    }

    @JvmStatic
    fun noteListItemToDoc(noteListItem: NoteListItem): Map<String, Any> {
        val result: MutableMap<String, Any> = HashMap()

        result[Fields.TITLE] = noteListItem.title!!
        result[Fields.DATE] = noteListItem.date!!

        return result
    }

    object Fields {
        const val TITLE = "title"
        const val DATE = "date"
        const val TEXT_ID = "textId"
    }
}