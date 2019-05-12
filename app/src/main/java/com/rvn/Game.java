package com.rvn;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    public ArrayList<Meteores> meteores;
    private int score;
    private Timer timer;
    private int time;
    public int niveau;
    private float bg_speed;
    private float obst_speed;
    public boolean state;

    public VaisseauView vaisseau;

    private Chronometer chrono;

    private InGameActivity gameActivity;
    private final String TAG="Game";

    public boolean isMeteoreTaskActive= false;

    public static final int MAX_METEORES= 4;

    private Handler handler;

    public Game(InGameActivity g, Handler handler, Chronometer chrono){
        gameActivity= g;
        niveau= 1;
        score= 0;
        meteores= new ArrayList<>();
        state= false;
        timer= new Timer();
        obst_speed= 1;

        this.chrono= chrono;
        this.handler= handler;

        vaisseau= new VaisseauView(this);

    }
    public void startGame(){
        addView(vaisseau);
        //addMeteore(1);

        runMeteoresManager();
        runUpdateManager();
        chrono.start();

        state= true;
    }
    private void runMeteoresManager() {
        MeteoresTasks metTask= new MeteoresTasks(this, handler);
        timer.scheduleAtFixedRate(metTask, 0, 10);
    }
    private void runUpdateManager(){
        TimerTask metTask= new TimerTask() {
            @Override
            public void run() {
                if(state)
                    gameActivity.updateMeteoresPosition();
            }
        };
        (new Timer()).scheduleAtFixedRate(metTask, 0, 10);
    }
    public void addMeteore(int nb){ addMeteore(nb,obst_speed,1); }
    public void addMeteore(int nb, float speed, float size){                //méthode pour ajouter des météores

        int nbObstacle= meteores.size();
        for(int i=nbObstacle; i<nbObstacle+nb;i++) {
            Meteores meteore= new Meteores(this, speed, size);
            meteores.add(meteore);          //remplissage du tableau de météores
            addView(meteore);
        }
        //repaintMeteores();
    }

    public void removeMeteore(Meteores m) {  //méthode de suppresion de météore à certains niveau du jeu
        meteores.remove(m);
        removeView(m);
    }

    // TO EDIT
    public void removeMeteore(int nb) {
        int nbObstacle= meteores.size();
        if (nb == -1) {
            for(int i = 0; i < nbObstacle; i++) {
                Meteores m= meteores.get(0);
                removeMeteore(m);
            }
            return;
        }
        if(nbObstacle<nb)
            nb=nbObstacle;

        for(int i=nbObstacle;i>nbObstacle-nb;i--) {
            Meteores m= meteores.get(nbObstacle);
            removeMeteore(m);
        }
    }

    public boolean hasCollision(){
        for(int i=0; i<meteores.size(); i++){
            if( meteores.get(i).hasCollision() )
                return true;
        }
        return false;
    }

    public void updateLevel(){
        if( score > 20 && niveau == 1)
            niveau++;
        if( score > 45 && niveau == 2)  niveau++;
        //if( score > 70 && niveau == 3)  niveau++;
    }
    public void clearAnimation() {
        for (int i = 0; i < meteores.size(); i++) {
            Meteores m = meteores.get(i);
            m.clearAnimation();
        }
    }
    public void updateMeteoresPosition(){
        for(int i=0; i<meteores.size();i++){
            Meteores meteore= meteores.get(i);
            meteore.updatePosition();
            //if(meteore.isOut() &&) removeMeteore(meteore);
            if(meteore.hasCollision()) showEndGame();
        }
    }
    public void endGame(){
        removeMeteore(-1);
    }
    public void showEndGame(){
        clearAnimation();
        //endGame();
        state= false;
        gameActivity.showEndGame();
    }

    public int getWaveDuration(){
        int ms;
        switch(niveau){
            case 1:   ms= 5000; break;//ms= (int) Math.floor( Math.random()*999 +1 ); break;   //0-1000ms
            case 2:   ms= 2000; break;//(int) Math.floor( 2*(Math.random()*999* +1 ) ); break;   //0-2000ms
            case 3:   ms= 1000; break;
            default:  ms= 1500; break;
        }
        return ms;
    }
    public int getScore(){ return score; }
    public int getLevel(){ return niveau; }

    public void repaint(){
        repaintMeteores();
        gameActivity.repaint();
        //vaisseau.repaint();
        //repaintBackground();
    }
    public void repaintMeteores(){
        for(int i=0; i<meteores.size(); i++) meteores.get(i).repaint();
    }

    public void addView(ObjectView v){ gameActivity.addView(v); }
    public void removeView(ObjectView v){ gameActivity.removeView(v); }

    public void onClickMeteore(final Meteores m){
        removeMeteore(m);
        score++;
        updateLevel();
        repaint();
    }

}
