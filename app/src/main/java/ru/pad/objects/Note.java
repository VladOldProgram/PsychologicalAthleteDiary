package ru.pad.objects;

public class Note {
    private int moodScore;

    private String situationText, thoughtsText, emotionsText, reactionsText, commentText;

    public Note() {}

    public Note(int moodScore, String situationText, String thoughtsText, String emotionsText, String reactionsText, String commentText) {
        this.moodScore = moodScore;
        this.situationText = situationText;
        this.thoughtsText = thoughtsText;
        this.emotionsText = emotionsText;
        this.reactionsText = reactionsText;
        this.commentText = commentText;
    }

    public int getMoodScore() { return moodScore; }
    public void setMoodScore(int moodScore) { this.moodScore = moodScore; }

    public String getSituationText() { return situationText; }
    public void setSituationText(String situationText) { this.situationText = situationText; }

    public String getThoughtsText() { return thoughtsText; }
    public void setThoughtsText(String thoughtsText) { this.thoughtsText = thoughtsText; }

    public String getEmotionsText() { return emotionsText; }
    public void setEmotionsText(String emotionsText) { this.emotionsText = emotionsText; }

    public String getReactionsText() { return reactionsText; }
    public void setReactionsText(String reactionsText) { this.reactionsText = reactionsText; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }
}
