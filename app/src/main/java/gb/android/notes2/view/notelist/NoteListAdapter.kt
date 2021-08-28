package gb.android.notes2.view.notelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import gb.android.notes2.App.Companion.getNoteListItemSource
import gb.android.notes2.R

class NoteListAdapter(var parentFragment: Fragment) : RecyclerView.Adapter<NoteListViewHolder>() {

    //================================================================================================
    // POSITION

    var position = 0


    //================================================================================================
    // RecyclerView.Adapter Methods

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        return NoteListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val noteListItem = getNoteListItemSource()!!.getNoteListItemByPos(position)
        holder.adapter = this
        holder.id = noteListItem?.id
        holder.tv_title_line.text = noteListItem?.title
        holder.tv_date_line.text = noteListItem?.date
        holder.parentLayout.setOnClickListener(holder)
        holder.btn_delete_note.setOnClickListener(holder)
        parentFragment.registerForContextMenu(holder.parentLayout)
        holder.itemView.setOnLongClickListener(null)
    }

    override fun getItemCount(): Int {
        return getNoteListItemSource()!!.size()
    }
}