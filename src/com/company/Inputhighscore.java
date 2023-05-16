package com.company;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Inputhighscore {
    private final SimpleStringProperty id;
    private final SimpleStringProperty nickname;
    private final SimpleIntegerProperty score;


    Inputhighscore(String id, String fnickname, Integer fscore)
    {
        this.id=new SimpleStringProperty(id);
        this.nickname= new SimpleStringProperty(fnickname);
        this.score= new SimpleIntegerProperty(fscore);

    }
    public String getId()
    {
        return id.get();
    }
    public void setId(String fid)
    {
        id.set(fid);
    }
    public String getNickname()
    {
        return nickname.get();
    }
    public void setNickname(String fnickname)
    {
        nickname.set(fnickname);
    }
    public Integer getScore()
    {
        return score.get();
    }
    public void setScore(Integer fscore)
    {
        score.set(fscore);
    }


}
