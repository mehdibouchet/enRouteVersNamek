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
    private int updatePosition;

    final private int PAUSE_ALEA= 1500;

    private boolean taskBeingRunning;

    public MeteoresTasks(Game game, Handler handler) {
        this.game= game;
        this.taskBeingRunning= false;
        this.handler= handler;
        this.updatePosition= 0;
    }
    private void initNewTask(){
        //choiceTask= (int) Math.floor( Math.random()*3 );
        choiceTask= 1;
        nbMeteores= (int) Math.floor( Math.random()*(game.getMaxMeteore())+1 );
        ms= game.getMs();
    }

    public void startTask(){
        taskBeingRunning= true;
        int pauseBetweenWaves= (choiceTask == 0 || choiceTask == 2 ? ms+ 2000 : ms/2);

        switch(choiceTask){
            case 0: runContinueMeteoreTask(ms, 8); break;
            case 1: runEveryMeteoreTask(3); break;
            case 2: runAleatoireMeteoreTask(ms, nbMeteores); break;
        }

        handler.postDelayed(new Runnable(){
            @Override
            public void run() { taskBeingRunning= false; }
        }, pauseBetweenWaves);

    }

    @Override
    public void run() {
        if(!taskBeingRunning){
            handler.removeCallbacksAndMessages(null);
            initNewTask(); startTask();
        }
        if(updatePosition == 2){
            game.updateState();
            updatePosition= -1;
        }
        updatePosition++;
    }

    private void runContinueMeteoreTask(int seconde, final int nbMeteores){
        int oneInterval= (seconde/nbMeteores);
        final Runnable addMeteoreTask= new Runnable() {
            @Override
            public void run() {
               game.addMeteore(1);
            }
        };

        for(int i=1;i<=nbMeteores;i++)
            handler.postDelayed(addMeteoreTask, i*oneInterval);
    }
    private void runEveryMeteoreTask(final int nbMeteores){ handler.post(new Runnable(){
        @Override
        public void run() { game.addMeteore(nbMeteores); }
        });
    }

    private void runAleatoireMeteoreTask(int seconde, int nbMeteores){ runAleatoireMeteoreTask(seconde, nbMeteores,0); }
    private void runAleatoireMeteoreTask(int seconde, int nbMeteores, int i){
        final int choiceTask= (int) Math.floor( Math.random()*2 );
        final int choiceNbMeteore= (seconde<500 ? nbMeteores : (int) Math.floor( Math.random()*(nbMeteores)+1 ) );
        final int choiceSeconde= (seconde<500 ? seconde : (int) ( Math.floor( Math.random()*(seconde/2)+1 ) ) );

        Runnable continueMeteoreTask= new Runnable(){
            @Override
            public void run() { runContinueMeteoreTask(choiceSeconde, choiceNbMeteore); }
        };
        Runnable everyMeteoreTask= new Runnable(){
            @Override
            public void run() { runEveryMeteoreTask(choiceNbMeteore); }
        };

        if (choiceTask == 0) handler.postDelayed(continueMeteoreTask, i * PAUSE_ALEA);
        else if (choiceTask == 1) handler.postDelayed(everyMeteoreTask, i * PAUSE_ALEA);

        if(seconde > 500) runAleatoireMeteoreTask(seconde - choiceSeconde, nbMeteores - choiceNbMeteore, i + 1);
    }

}
