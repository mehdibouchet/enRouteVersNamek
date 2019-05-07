package com.rvn;

import android.os.Handler;
import android.util.Log;

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

    public boolean invincible;
    public VaisseauView vaisseau;

    private InGameActivity gameActivity;
    private final String TAG="Game";

    public boolean isMeteoreTaskActive= false;

    public static final int MAX_METEORES= 4;

    private Handler handler;

    public Game(InGameActivity g, Handler handler){
        gameActivity= g;
        niveau= 1;
        score= 0; invincible = false;
        meteores= new ArrayList<Meteores>();
        state= false;
        timer= new Timer();
        obst_speed= 1;

        this.handler= handler;

        startGame();

    }
    public void startGame(){
        vaisseau= new VaisseauView(this);
        gameActivity.addView(vaisseau);
        //vaisseau.centerView();
        //addMeteore(1);
        runMeteoresManager();
        //runTimerManager();

        state= true;
    }
    private void runTimerManager() {
        timer.schedule(new TimerTask() {
            int min=1;
            @Override
            public void run() { time++; Log.v(TAG, Integer.toString(time)); }
        }, 0, 1000);
    }
    private void runMeteoresManager() {
        MeteoresTasks metTask= new MeteoresTasks(this, handler);

        timer.schedule(metTask, 0, 10);
        //handler.postDelayed(updateMeteoreTask,1000);
    }

    public void addMeteore(int nb){ addMeteore(nb,obst_speed,1); }
    public void addMeteore(int nb, float speed, float size){                //méthode pour ajouter des météores

        int nbObstacle= meteores.size();
        //nb= (nb > MAX_METEORES-nbObstacle ? MAX_METEORES-nbObstacle : nb);
        for(int i=nbObstacle; i<nbObstacle+nb;i++) {
            Meteores meteore= new Meteores(this, speed, size);
            meteores.add(meteore);          //remplissage du tableau de météores
            gameActivity.addView(meteore);
        }
        //repaintMeteores();
    }
    public void removeMeteore() {  //méthode de suppresion de météore à certains niveau du jeu
        removeMeteore(-1);
    }
    public void removeMeteore(Meteores m) {  //méthode de suppresion de météore à certains niveau du jeu
        meteores.remove(m);
        gameActivity.removeView(m);
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

    public void updateLevel(){}
    public void updateBackgroundPosition(){}
    public void updateMeteoresPosition(){
        for(int i=0; i<meteores.size();i++){
            Meteores meteore= meteores.get(i);
            meteore.updatePosition();
            //if(meteore.isOut() &&) removeMeteore(meteore);
            if(meteore.hasCollision()) showEndGame();
        }
    }
    public void updateState(){
        updateMeteoresPosition(); repaint(); //updateBackgroundPosition(); repaint();
    }
    public void updateNiveau(){                                  //mise à jour du niveauen fonction du temps
        /*int ex_niv = niveau;
        //niveau=int(timer.chrono/1000/5);
        if(ex_niv == niveau)
            return;
        if(niveau%6 != 0){
            bg_speed+=0.3;
            obst_speed+=0.5;
            updateMeteoreSpeed();
            addMeteore(1,obst_speed);
        }
        else{
            deleteMeteore(2);
            obst_speed-=1;
            bg_speed-=0.4;
            updateMeteoreSpeed();

        }*/
    }

    public void showEndGame(){ state= false; gameActivity.showEndGame(); }

    public int getMaxMeteore(){
        int maxMeteore;
        switch(niveau){
            case 1:   maxMeteore= 5; break;
            case 2:   maxMeteore= 8; break;
            case 3:   maxMeteore= 10;break;
            default:  maxMeteore= 7; break;
        }
        return maxMeteore;
    }
    public int getMs(){
        int ms;
        switch(niveau){
            case 1:   ms= 5000; break;//ms= (int) Math.floor( Math.random()*999 +1 ); break;   //0-1000ms
            case 2:   ms= 2000; break;//(int) Math.floor( 2*(Math.random()*999* +1 ) ); break;   //0-2000ms
            case 3:   ms= 1000; break;
            default:  ms= 1500; break;
        }
        return ms;
    }

    public void repaint(){
        //repaintMeteores();
        //vaisseau.repaint();
        //repaintBackground();
    }
    public void repaintMeteores(){
        for(int i=0; i<meteores.size(); i++) meteores.get(i).repaint();
    }
    public void repaintBackground(){}

    public void onClickMeteore(final Meteores m){
        handler.post(new Runnable(){
         @Override
            public void run() {
             removeMeteore(m);
             score++;
            }
        });

    }

}
