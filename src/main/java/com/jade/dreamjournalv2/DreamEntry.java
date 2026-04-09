package com.jade.dreamjournalv2;

public class DreamEntry {
    private int id;
    private String title, date, encryptedContent, sleepTime, wakeTime, moodBefore, moodAfter;
    private String method, substances, signs, env, themes;
    private int hoursSlept, quality, falseAwakenings;
    private boolean isLucid, isNormal, isNightmare, isWet, isMads, isParalysis;

    public DreamEntry(int id, String title, String date, String encryptedContent, int hoursSlept,
                      int quality, String sleepTime, String wakeTime, String moodBefore,
                      String moodAfter, String method, String substances, String signs,
                      String env, String themes, int falseAwakenings, boolean isLucid,
                      boolean isNormal, boolean isNightmare, boolean isWet, boolean isMads, boolean isParalysis) {
        this.id = id; this.title = title; this.date = date; this.encryptedContent = encryptedContent;
        this.hoursSlept = hoursSlept; this.quality = quality; this.sleepTime = sleepTime;
        this.wakeTime = wakeTime; this.moodBefore = moodBefore; this.moodAfter = moodAfter;
        this.method = method; this.substances = substances; this.signs = signs; this.env = env;
        this.themes = themes; this.falseAwakenings = falseAwakenings; this.isLucid = isLucid;
        this.isNormal = isNormal; this.isNightmare = isNightmare; this.isWet = isWet;
        this.isMads = isMads; this.isParalysis = isParalysis;
    }

    // Getters for the core stuff
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getEncryptedContent() { return encryptedContent; }

    // THE MEGA HEADER BUILDER
    public String buildMetadataHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append((title != null && !title.trim().isEmpty()) ? title : "Untitled Dream").append(" ===\n");
        sb.append("Date Logged: ").append(date != null ? date.substring(0, 10) : "Unknown").append("\n");
        sb.append("Time: ").append(sleepTime).append(" to ").append(wakeTime)
                .append("  |  Hours: ").append(hoursSlept).append("  |  Quality: ").append(quality).append("/10\n");
        sb.append("Mood: [").append(moodBefore).append("] ➔ [").append(moodAfter).append("]\n");
        sb.append("Method: ").append(method).append("  |  Substances: ").append(substances).append("\n");

        // Checkboxes formatted cleanly
        sb.append("Flags: ");
        if (isLucid) sb.append("[Lucid] ");
        if (isNormal) sb.append("[Normal] ");
        if (isNightmare) sb.append("[Nightmare] ");
        if (isWet) sb.append("[Wet] ");
        if (isMads) sb.append("[MADs] ");
        if (isParalysis) sb.append("[Paralysis] ");
        sb.append("\n");

        sb.append("Environment: ").append(env).append("\n");
        sb.append("Themes: ").append(themes).append("\n");
        sb.append("Dream Signs: ").append(signs).append("\n");
        sb.append("False Awakenings: ").append(falseAwakenings).append("\n");
        sb.append("----------------------------------------------------------------------\n\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        String displayTitle = (title != null && !title.trim().isEmpty()) ? title : "Untitled Dream";
        String shortDate = (date != null && date.length() >= 10) ? date.substring(0, 10) : date;
        return shortDate + "  |  " + displayTitle;
    }
}