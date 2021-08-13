package gb.android.notes2.Model;

import java.util.Locale;

public class NoteListItem implements Comparable<NoteListItem> {
    final private int id;
    final private String title;
    final private String date;

    //===============================================================================================
    // GETTERS

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    //===============================================================================================
    // CONSTRUCTOR

    public NoteListItem(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    //===============================================================================================
    // COMPARATORS

    @Override
    public int compareTo(NoteListItem o) {
        if (title.equals(o.title))
            return 0;

        if (title.toUpperCase(Locale.ROOT).equals(o.title.toUpperCase(Locale.ROOT)))
            return title.compareTo(o.title);

        return title.toUpperCase(Locale.ROOT).compareTo(o.title.toUpperCase(Locale.ROOT));
    }

    public int compareTitleAsc(NoteListItem o) {
        return compareTo(o);
    }

    public int compareTitleDesc(NoteListItem o) {
        return -compareTo(o);
    }

    public int compareIdAsc(NoteListItem o) {
        if (id == o.getId())
            return 0;

        if (id > o.getId())
            return 1;

        return -1;
    }
}
