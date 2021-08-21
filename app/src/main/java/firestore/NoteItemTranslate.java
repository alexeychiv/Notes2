package firestore;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import gb.android.notes2.model.NoteListItem;

public class NoteItemTranslate {

    public static class Fields {
        public final static String TITLE = "title";
        public final static String DATE = "date";
        public final static String TEXT_ID = "textId";
    }

    public static NoteListItem docToNoteListItem(String id, Map<String, Object> doc) {

        Log.d("BLAH", "NoteItemTranslate --> id = " + id
                + " title = " + (String) doc.get(Fields.TITLE)
                + " date = " + (String) doc.get(Fields.DATE)
                + " textId = " + (String) doc.get(Fields.TEXT_ID)
        );

        return new NoteListItem(id, (String) doc.get(Fields.TITLE), (String) doc.get(Fields.DATE));
    }

    public static Map<String, Object> noteListItemToDoc(NoteListItem noteListItem) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put(Fields.TITLE, noteListItem.getTitle());
        result.put(Fields.DATE, noteListItem.getDate());

        return result;
    }
}
