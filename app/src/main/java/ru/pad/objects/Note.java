package ru.pad.objects;

/**
 * Класс объекта заметки со свойствами:
 * <b>moodScore</b>, <b>situationText</b>, <b>thoughtsText</b>, <b>emotionsText</b>, <b>reactionsText</b>, <b>commentText</b>
 */
public class Note {
    private int moodScore;

    private String situationText, thoughtsText, emotionsText, reactionsText, commentText;

    /**
     * Конструктор - создание нового объекта с неинициализированными свойствами
     */
    public Note() {}

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param moodScore оценка текущего настроения спортсмена
     * @param situationText текст о текущей ситуации спортсмена
     * @param thoughtsText текст о текущих мыслях спортсмена
     * @param emotionsText текст о текущих эмоциях спортсмена
     * @param reactionsText текст о текущей реакции спортсмена
     * @param commentText текст комментария, добавленного спортсменом к созданной заметке
     */
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
