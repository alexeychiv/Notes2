package gb.android.notes2.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gb.android.notes2.App;
import gb.android.notes2.R;
import gb.android.notes2.view.notelist.NoteListAdapter;

public class NoteListFragment extends Fragment implements View.OnClickListener {

    AppCompatButton btn_new;

    private RecyclerView rv_notesList;
    private static NoteListAdapter noteListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //================================================================================================
    // CONSTRUCTOR

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    public NoteListFragment() {

    }

    //================================================================================================
    //INIT

    private void init(View view) {
        btn_new = view.findViewById(R.id.btn_new);

        rv_notesList = view.findViewById(R.id.rv_noteList);
        rv_notesList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv_notesList.setLayoutManager(layoutManager);

        noteListAdapter = new NoteListAdapter();
        rv_notesList.setAdapter(noteListAdapter);

        App.setNoteListAdapter(noteListAdapter);

        btn_new.setOnClickListener(this);
    }

    //================================================================================================
    // EVENTS

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        init(getView());

        if (App.getInstance().getIntPref("id") > -1)
        {
            if (App.getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, NoteEditorFragment.newInstance())
                        .addToBackStack("")
                        .commit();
            } else {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_right, NoteEditorFragment.newInstance())
                        .addToBackStack("")
                        .commit();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        App.setNoteListAdapter(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new:
                App.getNoteListItemSource().addNote();
                App.getNoteListItemSource().updateData();
                noteListAdapter.notifyDataSetChanged();
                break;
        }
    }
}
