package gb.android.notes2.view.notelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.R;
import gb.android.notes2.model.NoteListItem;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListViewHolder> {

    Fragment parentFragment;

    //================================================================================================
    // CONSTRUCTOR

    public NoteListAdapter(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    //================================================================================================
    // POSITION

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //================================================================================================
    // RecyclerView.Adapter Methods

    @Override
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        parentFragment.registerForContextMenu(holder.parentLayout);

        holder.itemView.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return App.getNoteListItemSource().size();
    }
}