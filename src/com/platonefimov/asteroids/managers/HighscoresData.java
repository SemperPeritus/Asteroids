package com.platonefimov.asteroids.managers;


import java.io.Serializable;



public class HighscoresData implements Serializable {

    private static final long serialVersionUID = 1;

    private final int MAX_SCORES = 10;
    private long[] highScores;
    private String[] names;

    private long tentativeScore;


    public HighscoresData() {
        highScores = new long[MAX_SCORES];
        names = new String[MAX_SCORES];
    }


    public void init() {
        for (int i = 0; i < MAX_SCORES; i++) {
            highScores[i] = 0;
            names[i] = "---";
        }
    }


    public long[] getHighScores() {
        return highScores;
    }

    public String[] getNames() {
        return names;
    }

    public long getTentativeScore() {
        return tentativeScore;
    }


    public void setTentativeScore(long score) {
        tentativeScore = score;
    }


    public boolean isHighScore(long score) {
        return score > highScores[MAX_SCORES - 1];
    }


    public void setHighScore(long score, String name) {
        if(tentativeScore > highScores[MAX_SCORES - 1]) {
            highScores[MAX_SCORES - 1] = score;
            names[MAX_SCORES - 1] = name;
            sortHighScores();
        }
    }

    private void sortHighScores() {
        long tmpScore;
        String tmpName;
        for (int i = 0; i < MAX_SCORES; i++)
            for (int j = MAX_SCORES  - 1; j > i; j--)
                if (highScores[j-1] < highScores[j]) {
                    tmpScore = highScores[j-1];
                    highScores[j-1] = highScores[j];
                    highScores[j] = tmpScore;
                    tmpName = names[j-1];
                    names[j-1] = names[j];
                    names[j] = tmpName;
                }
    }

}
