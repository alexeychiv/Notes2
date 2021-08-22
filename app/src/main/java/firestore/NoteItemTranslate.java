package firestore;

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
        return new NoteListItem(id, (String) doc.get(Fields.TITLE), (String) doc.get(Fields.DATE));
    }

    public static Map<String, Object> noteListItemToDoc(NoteListItem noteListItem) {
        Map<String, Object> result = new HashMap<>();

        result.put(Fields.TITLE, noteListItem.getTitle());
        result.put(Fields.DATE, noteListItem.getDate());

        return result;
    }
}
