package com.rvn;

import android.os.Handler;

import java.util.TimerTask;

public class MeteoresWaveManager extends TimerTask {
    private Game game;
    private final Handler handler;
    private int choiceTask;
    private int nbMeteores;
    private int ms;
    final private int PAUSE_ALEA= 1500;

    private boolean taskBeingRunning;

    public MeteoresWaveManager(Game game, Handler handler) {
        this.game= game;
        this.taskBeingRunning= false;
        this.handler= handler;
    }

    private void initNewTask(){
        choiceTask= game.getChoice();
        nbMeteores= game.getMaxMeteore();
        ms= game.getWaveDuration();
    }

    public void startTask(){
        taskBeingRunning= true;
        int pauseBetweenWaves= (choiceTask == 0 || choiceTask == 2 ? ms+ 2000 : ms/2);

        switch(choiceTask){
            case 0: runContinueMeteoreTask(ms, nbMeteores); break;
            case 1: runEveryMeteoreTask(nbMeteores); break;
            case 2: runAleatoireMeteoreTask(ms, nbMeteores); break;
            default:runAleatoireMeteoreTask(ms, nbMeteores); break;
        }

        handler.postDelayed(new Runnable(){
            @Override
            public void run() { taskBeingRunning= false; }
        }, pauseBetweenWaves);

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

    private void runEveryMeteoreTask(final int nbMeteores){
        handler.post(new Runnable(){
        @Override
        public void run() { game.addMeteore(nbMeteores); }
        });
    }

    private void runAleatoireMeteoreTask(int seconde, int nbMeteores){
        runAleatoireMeteoreTask(seconde, nbMeteores,0);
    }
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

    @Override
    public void run() {
        if (game.state && !taskBeingRunning){
            initNewTask();
            startTask();
        }
    }
}
