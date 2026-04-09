package com.jade.dreamjournalv2;

public class DreamEntry {
    private int id;
    private String title;
    private String date;
    private String encryptedContent;

    /**
     * Constructor
     * @param id dream id
     * @param title dream title
     * @param date the date of dream entry
     * @param encryptedContent dream content but encrypted (cannot be null)
     */
    public DreamEntry(int id, String title, String date, String encryptedContent) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.encryptedContent = encryptedContent;
    }

    // Getters (So our UI can access the data)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getEncryptedContent() { return encryptedContent; }

    // This method tells Java exactly what to print
    // when this dream shows up in your UI's scrolling list!
    @Override
    public String toString() {
        String displayTitle = (title != null && !title.trim().isEmpty()) ? title : "Untitled Dream";
        // We only want to show the date (YYYY-MM-DD), not the exact millisecond time
        String shortDate = (date != null && date.length() >= 10) ? date.substring(0, 10) : date;
        return shortDate + "  |  " + displayTitle;
    }
}