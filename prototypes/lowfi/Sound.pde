class Sound {

    /*---------------------------------------------------------------
    Audio files
    ----------------------------------------------------------------*/

    SoundFile eat, swoosh, spawn, despawn, divide, revelation, speedUp, wallHit, explosion, lightbulb;
    SoundFile[] aArray, bArray;

    int aCounter = 600;
    int aCounterLimit = 600;
    float swooshTreshold = width/2;
    int lastTrackA;
    Timer wallHitTimer, lightbulbTimer;


    /*---------------------------------------------------------------
    Sound Triggers
    ----------------------------------------------------------------*/

    void playSpawn()      { spawn.play(); }
    void playDespawn()    { despawn.play(); }
    void playSwoosh()     { swoosh.play(); }
    void playEatMusic()   { eat.play(); }
    void playDivide()     { divide.play(); }
    void playRevelation() { revelation.play(); }
    void playSpeedUp()    { speedUp.play(); }
    void playExplosion()  { explosion.play(); }


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    public Sound(lowfi sketch) {
        soundSetup(sketch);
        wallHitTimer = new Timer(1);
        lightbulbTimer = new Timer(3);
    }

    void soundSetup(lowfi sketch) {

        eat = new SoundFile(sketch, "eat.mp3");
        swoosh = new SoundFile(sketch, "swoosh.mp3");
        spawn = new SoundFile(sketch, "spawn.mp3");
        despawn = new SoundFile(sketch, "despawn.mp3");
        divide = new SoundFile(sketch, "divide.mp3");
        revelation = new SoundFile(sketch, "revelation.mp3");
        speedUp = new SoundFile(sketch, "speedUp.mp3");
        wallHit = new SoundFile(sketch, "wallHit.mp3");
        explosion = new SoundFile(sketch, "explosion.mp3");
        lightbulb = new SoundFile(sketch, "lightbulb.mp3");

        aArray = new SoundFile[7];
        aArray[0] = new SoundFile(sketch, "A_01.mp3");
        aArray[1] = new SoundFile(sketch, "A_02.mp3");
        aArray[2] = new SoundFile(sketch, "A_03.mp3");
        aArray[3] = new SoundFile(sketch, "A_04.mp3");
        aArray[4] = new SoundFile(sketch, "A_05.mp3");
        aArray[5] = new SoundFile(sketch, "A_06.mp3");
        aArray[6] = new SoundFile(sketch, "A_07.mp3");

        bArray = new SoundFile[10];
        bArray[0] = new SoundFile(sketch, "B_01.mp3");
        bArray[1] = new SoundFile(sketch, "B_02.mp3");
        bArray[2] = new SoundFile(sketch, "B_03.mp3");
        bArray[3] = new SoundFile(sketch, "B_04.mp3");
        bArray[4] = new SoundFile(sketch, "B_05.mp3");
        bArray[5] = new SoundFile(sketch, "B_06.mp3");
        bArray[6] = new SoundFile(sketch, "B_07.mp3");
        bArray[7] = new SoundFile(sketch, "B_08.mp3");
        bArray[8] = new SoundFile(sketch, "B_09.mp3");
        bArray[9] = new SoundFile(sketch, "B_10.mp3");

    }

    void playMusicDependingOnGameState() {
        playA();
    }


    void playA() {
        aCounter++;
        if (aCounter > aCounterLimit) {
            aCounter = 0;
            int track = (int)random(0, 6.4);
            //must not play same file in a row
            while (track == lastTrackA) {
                track = (int)random(0, 6.4);
            }
            lastTrackA = track;
            aArray[track].play();
        }
    }

    void playRandomB() {
        int track = (int)random(0, 9.4);
        bArray[track].play();
    }


    void playTurningMusic() {
        int track = (int)random(0, 6.4);
        bArray[track].play();
    }



    void playLightbulb() {
        if (lightbulbTimer.finished()) {
            lightbulb.play();
            lightbulbTimer.set(3);
            lightbulbTimer.start();
        }
    }

    void playWallHit() {
        if (wallHitTimer.finished()) {
            wallHit.play();
            wallHitTimer.set(1);
            wallHitTimer.start();
        }
    }

}
