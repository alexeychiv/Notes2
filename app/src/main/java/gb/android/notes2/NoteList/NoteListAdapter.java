package gb.android.notes2.NoteList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.Model.NoteListItem;
import gb.android.notes2.R;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListViewHolder> {

    //================================================================================================
    // RecyclerView.Adapter Methods

    @Override
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        App.setNoteListAdapter(this);
        return new NoteListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteListViewHolder holder, int position) {
        NoteListItem noteListItem = App.getNoteListItemSource().getNoteListItemByPos(position);

        holder.adapter = this;

        holder.id = noteListItem.getId();

        holder.tv_title_line.setText(noteListItem.getTitle());
        holder.tv_date_line.setText(noteListItem.getDate());

        holder.parentLayout.setOnClickListener(holder);
        holder.btn_delete_note.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return App.getNoteListItemSource().size();
    }
}