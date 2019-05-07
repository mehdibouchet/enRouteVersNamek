package com.rvn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.TimerTask;

import static com.rvn.Game.MAX_METEORES;

public class MeteoresTasks extends TimerTask {
    private Game game;
    private final Handler handler;
    private int choiceTask;
    private int nbMeteores;
    private int ms;

    private boolean taskBeingRunning;

    public MeteoresTasks(Game game, Handler handler) {
        this.game= game;
        this.taskBeingRunning= false;
        this.handler= handler;
    }
    private void initNewTask(){
        //choiceTask= (int) Math.floor( Math.random()*3 );
        choiceTask= 0;
        int max_meteores_toAdd= MAX_METEORES- game.meteores.size();
        nbMeteores= (int) Math.floor( Math.random()*(max_meteores_toAdd)+1 );

        switch(game.niveau){
            case 1: ms= 5000; break;//ms= (int) Math.floor( Math.random()*999 +1 ); break;   //0-1000ms
            case 2: ms= 2000; break;//(int) Math.floor( 2*(Math.random()*999* +1 ) ); break;   //0-2000ms
            default: ms=1000;break;
        }
    }

    public void startTask(){
        taskBeingRunning= true;
        int max_meteores_toAdd= MAX_METEORES- game.meteores.size();
        int pauseBetweenWaves= ms+ 3000;

        if( max_meteores_toAdd > 0 ){
            switch(choiceTask){
                 case 0: runContinueMeteoreTask(ms, 5); break;
                 case 1: runAleatoireMeteoreTask(ms, nbMeteores); break;
                 case 2: runEveryMeteoreTask(nbMeteores); break;
            }
        }
        handler.postDelayed(new Runnable(){
            @Override
            public void run() { taskBeingRunning= false; }
        }, pauseBetweenWaves);

    }

    @Override
    public void run() {
        if(!taskBeingRunning){
            initNewTask(); startTask();
        }
    }

    public boolean canAddMeteore(int i){
        return (i <= MAX_METEORES- game.meteores.size() );
    }
    private void runContinueMeteoreTask(int seconde, final int nbMeteores){
        int oneInterval= (seconde/nbMeteores);
        final Runnable addMeteoreTask= new Runnable() {
            @Override
            public void run() {
                if(!canAddMeteore(1)); game.addMeteore(1);
            }
        };

        for(int i=0;i<nbMeteores;i++){
            handler.postDelayed(addMeteoreTask, i*oneInterval);
        }
    }
    private void runEveryMeteoreTask(int nbMeteores){ game.addMeteore(nbMeteores); }
    private void runAleatoireMeteoreTask(int seconde, int nbMeteores){
        if(seconde<500) return ;

        int choiceTask= (int) Math.floor( Math.random()*2 );
        int choiceNbMeteore= (int) Math.floor( Math.random()*(nbMeteores)+1 );
        int choiceSeconde= (int) ( Math.floor( Math.random()*(seconde/2)+1 ) );

        if(choiceTask == 0) runContinueMeteoreTask(seconde, choiceNbMeteore);
        else if(choiceTask == 1) runEveryMeteoreTask(choiceNbMeteore);
        runAleatoireMeteoreTask(seconde - choiceSeconde, nbMeteores - choiceNbMeteore);
    }
}
