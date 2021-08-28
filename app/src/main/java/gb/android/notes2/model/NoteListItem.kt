package gb.android.notes2.model

import java.util.*

class NoteListItem(val id: String?, val title: String?, val date: String?) : Comparable<NoteListItem> {

    //===============================================================================================
    // COMPARATORS

    override fun compareTo(other: NoteListItem): Int {
        if (title == other?.title) return 0
        return if (title?.uppercase(Locale.ROOT) == other?.title?.uppercase(Locale.ROOT)) title!!.compareTo(
            other?.title.toString()
        ) else title?.uppercase(
            Locale.ROOT
        )!!.compareTo(other?.title?.uppercase(Locale.ROOT).toString())
    }

    fun compareTitleAsc(o: NoteListItem): Int {
        return compareTo(o)
    }

    fun compareTitleDesc(o: NoteListItem): Int {
        return -compareTo(o)
    }

    fun compareIdAsc(o: NoteListItem?): Int {
        return if (id == o?.id) 0 else id!!.compareTo(o?.id.toString())
    }
}