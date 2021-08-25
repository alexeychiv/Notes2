package gb.android.notes2.view.notelist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.R;
import gb.android.notes2.view.ViewManager;

public class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    NoteListAdapter adapter;

    String id;

    AppCompatTextView tv_title_line;
    AppCompatTextView tv_date_line;
    AppCompatImageButton btn_delete_note;

    ConstraintLayout parentLayout;

    //===============================================================================================
    // CONSTRUCTOR

    public NoteListViewHolder(View itemView) {
        super(itemView);

        tv_title_line = itemView.findViewById(R.id.tv_title_line);
        tv_date_line = itemView.findViewById(R.id.tv_date_line);
        btn_delete_note = itemView.findViewById(R.id.btn_list_deleteNote);

        parentLayout = itemView.findViewById(R.id.rv_noteListItem);

        itemView.setOnCreateContextMenuListener(this);
    }

    //===============================================================================================
    // EVENTS

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ViewManager.getNoteListAdapter().setPosition(getAdapterPosition());
        ViewManager.getMainActivity().getMenuInflater().inflate(R.menu.menu_note_list_context, menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_noteListItem:
                openNoteEditor();
                break;
            case R.id.btn_list_deleteNote:
                deleteNote();
                break;
        }
    }

    //===============================================================================================
    // ONCLICK METHODS

    private void openNoteEditor() {
        FragmentManager fragmentManager = ViewManager.getMainActivity().getSupportFragmentManager();

        fragmentManager.popBackStackImmediate();

        if (!id.equals("empty")) {
            App.getInstance().setStrPref("id", id);
            ViewManager.startEditor();
        }
    }

    private void deleteNote() {
        if (App.getInstance().getStrPref("id").equals(id)) {
            App.getInstance().setStrPref("id", "empty");

            if (ViewManager.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                ViewManager.getMainActivity().getSupportFragmentManager().popBackStack();
            }
        }

        App.getNoteListItemSource().deleteNote(id);
    }
}