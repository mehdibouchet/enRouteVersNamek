package com.rvn;

import android.os.Handler;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    public int score, niveau;
    public float obst_speed;
    public boolean state;

    public InGameActivity gameActivity;
    public ArrayList<Meteores> meteores;
    public VaisseauView vaisseau;
    public Timer timer;
    public Chronometer chrono;
    public Handler handler;


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

        runMeteoresManager();
        runUpdateManager();
        chrono.start();

        state= true;
    }

    private void runMeteoresManager() {
        MeteoresWaveManager metTask= new MeteoresWaveManager(this, handler);
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
        timer.scheduleAtFixedRate(metTask, 0, 10);
    }

    public void addMeteore(int nb){ addMeteore(nb,obst_speed,1); }
    private void addMeteore(int nb, float speed, float size){                

        int nbObstacle= meteores.size();
        for(int i=nbObstacle; i<nbObstacle+nb;i++) {
            Meteores meteore= new Meteores(this, speed, size);
            meteores.add(meteore);          
            addView(meteore);
        }
    }

    public void removeMeteore(Meteores m) {  
        meteores.remove(m);
        removeView(m);
    }
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

        for(int i=nbObstacle-nb;i<nbObstacle;i--) {
            Meteores m= meteores.get(nbObstacle-nb);
            removeMeteore(m);
        }
    }



    public boolean hasCollision(){ return hasCollision(vaisseau); }
    public boolean hasCollision(ObjectView m){
        for(int i=0; i<meteores.size(); i++){
            if( meteores.get(i).hasCollision(m) )
                return true;
        }
        return false;
    }


    public void clearAnimation() {
        for (int i = 0; i < meteores.size(); i++) {
            Meteores m = meteores.get(i);
            m.clearAnimation();
        }
    }
    public void updateLevel(){
        if( score > 20 && niveau == 1)          niveau++;
        else if( score > 45 && niveau == 2)     niveau++;
        else if( score > 70 && niveau == 3)     niveau++;
        updateMeteoresSpeed();
    }
    public void updateMeteoresSpeed(){
        double sp;
        switch(niveau){
            case 1: sp= 1;  break;
            case 2: sp= 1.5;  break;
            case 3: sp= 2;  break;
            default: sp= 2.5; break;
        }
        for(int i=0; i< meteores.size(); i++)
            meteores.get(i).updateSpeed(sp);
    }
    public void updateMeteoresPosition(){
        for(int i=0; i<meteores.size();i++){
            Meteores meteore= meteores.get(i);
            meteore.updatePosition();
            if(meteore.hasCollision()) showEndGame();
        }
    }

    public void endGame(){
        state= false; chrono.stop();
        timer.cancel();// timer=null;
        clearAnimation();
        removeMeteore(-1);
    }

    public int getMaxMeteore(){
        int maxMeteore;
        switch(niveau){
            case 1: maxMeteore= 5;  break;
            case 2: maxMeteore= 7;  break;
            case 3: maxMeteore= 8;  break;
            default: maxMeteore= 9; break;
        }
        return (int) Math.floor( Math.random()*(maxMeteore)+1);
    }
    public int getChoice(){
        int maxChoise;
        switch(niveau){
            case 1: maxChoise=0;    break;
            case 2: maxChoise=1;    break;
            case 3: maxChoise=2;    break;
            default: maxChoise= 3;  break;
        }
        return (int) Math.floor( Math.random()*maxChoise );
    }
    public int getWaveDuration(){
        int ms;
        switch(niveau){
            case 1:   ms= 5000; break;
            case 2:   ms= 2000; break;
            case 3:   ms= 1000; break;
            default:  ms= 1500; break;
        }
        return ms;
    }
    public int getRotateMs(){
        int ms;
        switch(niveau){
            case 1:   ms= 3000; break;
            case 2:   ms= 2500; break;
            case 3:   ms= 2000; break;
            default:  ms= 1500; break;
        }
        return ms;
    }
    public int getScore(){ return score; }
    public int getLevel(){ return niveau; }

    public void showEndGame(){
        endGame();
        gameActivity.showEndGameModal();
    }

    public void repaint(){
        repaintMeteores();
        gameActivity.repaint();
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
