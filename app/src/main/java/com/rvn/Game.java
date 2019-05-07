package com.rvn;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

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

    public Game(InGameActivity g){
        gameActivity= g;
        niveau= 1;
        score= 0; invincible = false;
        meteores= new ArrayList<Meteores>();
        state= false;
        timer= new Timer();
        handler= new Handler();

        startGame();
    }
    public void startGame(){
        //addMeteore(1);
        runMeteoresManager();
        //runTimerManager();
        state= true;
    }
    private void runTimerManager() {
        timer.schedule(new TimerTask() {
            int min=1;
            @Override
            public void run() {
                if(state) {
                    if(min == 10){   time++; min =1; Log.v(TAG, Integer.toString(time)); }
                    updateState();
                    if(hasCollision()) showEndGame();
                }
            }
        }, 0, 100);
    }
    private void runMeteoresManager() {
        MeteoresTasks metTask= new MeteoresTasks(this, handler);
        timer.schedule(metTask, 0, 100);
    }

    public void addMeteore(int nb){ addMeteore(nb,1,obst_speed); }
    public void addMeteore(int nb, float size, float speed){                //méthode pour ajouter des météores

        int nbObstacle= meteores.size();
        nb= (nb > MAX_METEORES-nbObstacle ? MAX_METEORES-nbObstacle : nb);
        for(int i=nbObstacle; i<nbObstacle+nb;i++) {
            Meteores meteore= new Meteores(this);
            meteores.add(meteore);          //remplissage du tableau de météores
            gameActivity.addMeteoreView(meteore);
        }
        //repaintMeteores();
    }
    public void deleteMeteore() {  //méthode de suppresion de météore à certains niveau du jeu
        deleteMeteore(-1);
    }
    public void deleteMeteore(Meteores m) {  //méthode de suppresion de météore à certains niveau du jeu
        meteores.remove(m);
        gameActivity.removeMeteoreView(m);
    }

    // TO EDIT
    public void deleteMeteore(int nb) {
        int nbObstacle= meteores.size();
        if (nb == -1) {
            for(int i = 0; i < nbObstacle; i++)
                meteores.remove(0);
            return;
        }
        if(nbObstacle<nb)
            nb=nbObstacle;

        for(int i=nbObstacle;i>nbObstacle-nb;i--)
            meteores.remove(i);
    }

    public boolean hasCollision(){
        for(int i=0; i<meteores.size(); i++){
            if( meteores.get(i).hasCollision(this) ) return true;
        }
        return false;
    }

    public void updatePosition(){}
    public void updateBackgroundPosition(){}
    public void updateMeteoresPosition(){
        for(int i=0; i<meteores.size();i++){
            Meteores meteore= meteores.get(i);
            meteore.updatePosition();
            if(meteore.isOut()) meteores.remove(meteore);
        }
    }
    public void updateState(){
        updatePosition(); updateBackgroundPosition(); repaint();
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

    public void repaint(){
        repaintMeteores();
        vaisseau.repaint();
        repaintBackground();
    }
    public void repaintMeteores(){
        for(int i=0; i<meteores.size(); i++) meteores.get(i).repaint();
    }
    public void repaintBackground(){}

    public void onClickMeteore(final Meteores m){
        handler.post(new Runnable(){
         @Override
            public void run() {
             deleteMeteore(m);
             score++;
             repaintMeteores();
            }
        });

    }

}
