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

    /**
     * Геттер свойства moodScore
     * @return moodScore - оценка текущего настроения спортсмена
     */
    public int getMoodScore() { return moodScore; }
    /**
     * Сеттер свойства moodScore
     */
    public void setMoodScore(int moodScore) { this.moodScore = moodScore; }

    /**
     * Геттер свойства situationText
     * @return situationText - текст о текущей ситуации спортсмена
     */
    public String getSituationText() { return situationText; }
    /**
     * Сеттер свойства situationText
     */
    public void setSituationText(String situationText) { this.situationText = situationText; }

    /**
     * Геттер свойства thoughtsText
     * @return thoughtsText - текст о текущих мыслях спортсмена
     */
    public String getThoughtsText() { return thoughtsText; }
    /**
     * Сеттер свойства thoughtsText
     */
    public void setThoughtsText(String thoughtsText) { this.thoughtsText = thoughtsText; }

    /**
     * Геттер свойства emotionsText
     * @return emotionsText - текст о текущих эмоциях спортсмена
     */
    public String getEmotionsText() { return emotionsText; }
    /**
     * Сеттер свойства emotionsText
     */
    public void setEmotionsText(String emotionsText) { this.emotionsText = emotionsText; }

    /**
     * Геттер свойства reactionsText
     * @return reactionsText - текст о текущей реакции спортсмена
     */
    public String getReactionsText() { return reactionsText; }
    /**
     * Сеттер свойства reactionsText
     */
    public void setReactionsText(String reactionsText) { this.reactionsText = reactionsText; }

    /**
     * Геттер свойства commentText
     * @return commentText - текст комментария, добавленного спортсменом к созданной заметке
     */
    public String getCommentText() { return commentText; }
    /**
     * Сеттер свойства commentText
     */
    public void setCommentText(String commentText) { this.commentText = commentText; }
}
