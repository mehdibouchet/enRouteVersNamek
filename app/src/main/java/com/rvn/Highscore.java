package com.rvn;

public class Highscore{
    private String name;
    private int score;
    private int ID;

    public Highscore(String name, int score){
        this(-1, name,score);
    }
    public Highscore(int ID, String name, int score){
        this.name= name;
        this.score= score; this.ID=ID;
    }

    public String getName(){ return name; }
    public int getScore(){ return score; }
    public int getID(){ return ID; }

    public void setName(String name){this.name= name;}
    public void setScore(int score){this.score= score;}
    public void setID(int ID){this.ID= ID;}

}