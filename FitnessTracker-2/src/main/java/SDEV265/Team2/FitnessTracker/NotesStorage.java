package SDEV265.Team2.FitnessTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NotesStorage {
    private static List<Note> notes = new ArrayList<>();
    
    public static void addNote(String category, String date, String title, String content) {
        notes.add(new Note(category, date, title, content, new Date()));
    }
    
    public static List<Note> getNotesByCategory(String category) {
        List<Note> filtered = new ArrayList<>();
        for (Note note : notes) {
            if (note.category.equals(category)) {
                filtered.add(note);
            }
        }
        Collections.sort(filtered, (a, b) -> b.timestamp.compareTo(a.timestamp));
        return filtered;
    }
    
    public static List<Note> getAllNotes() {
        List<Note> sorted = new ArrayList<>(notes);
        Collections.sort(sorted, (a, b) -> b.timestamp.compareTo(a.timestamp));
        return sorted;
    }
    
    public static void deleteNote(Note note) {
        notes.remove(note);
    }
    
    public static class Note {
        public String category;
        public String date;
        public String title;
        public String content;
        public Date timestamp;
        
        public Note(String category, String date, String title, String content, Date timestamp) {
            this.category = category;
            this.date = date;
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
}
