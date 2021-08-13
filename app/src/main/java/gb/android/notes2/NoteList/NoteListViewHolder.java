package gb.android.notes2.NoteList;

import android.content.res.Configuration;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.NoteEditorFragment;
import gb.android.notes2.R;

public class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    NoteListAdapter adapter;

    int id;

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
    }

    //===============================================================================================
    // EVENTS

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

    void openNoteEditor() {
        App.getInstance().setIntPref("id", id);

        FragmentManager fragmentManager = App.getMainActivity().getSupportFragmentManager();

        fragmentManager.popBackStackImmediate();

        if (App.getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, NoteEditorFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
        } else {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.container_right, NoteEditorFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
        }
    }

    void deleteNote() {
        if (App.getInstance().getIntPref("id") == id) {
            App.getInstance().setIntPref("id", -1);

            if (App.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                App.getMainActivity().getSupportFragmentManager().popBackStack();
            }
        }

        App.getNoteListItemSource().deleteNote(id);
        App.getNoteListItemSource().updateData();
        adapter.notifyDataSetChanged();
    }

}