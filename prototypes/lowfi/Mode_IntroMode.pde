class IntroMode extends Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PImage intro_image, intro_typo, intro_corners;

    Timer timer = new Timer();
    Timer typoAnimationRunning = new Timer();
    Timer typoAnimationInterval = new Timer();

    boolean lastTypoAnimationRunningState = false;

    boolean typoFlickering;
    int typoOffsetX = 0;
    int typoOffsetY = 0;

    int intro_corners_opacity = 0;

    Ani animation;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    IntroMode() { super(); }
    IntroMode(BoidList b) { super(b); }
    IntroMode(BoidList b, InteractionController c) { super(b, c); }
    IntroMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------~
    Functions
    ----------------------------------------------------------------*/

    void setup() {
        intro_image = loadImage("start-pattern.png");
        intro_typo = loadImage("start-type.png");
        intro_corners = loadImage("start-corners.png");

        // Animation
        animation = new Ani(this, 3, "intro_corners_opacity", 100);
        animation.repeat();
        animation.setPlayMode(Ani.YOYO);
    }



    void render() {

        ctrl.draw();

        // images aligned central
        imageMode(CENTER);

        // show static background image
        scaledImage(intro_image, width/2, height/2, width);

        // draw corner image with opacity
        tint(0, 0, 100, intro_corners_opacity);
        scaledImage(intro_corners, width/2, height/2, width);
        tint(0, 0, 100, 100);

        // reset timers
        if (typoAnimationInterval.finished()) {
            typoAnimationRunning.set(int(random(1, 2)));
            typoAnimationRunning.start();
            typoAnimationInterval.set(int(random(5, 9)));
            typoAnimationInterval.start();
        }

        // is the animation currently active?
        if (!typoAnimationRunning.finished()) {

            // animation running

            mySound.playLightbulb();

            // randomize flickering
            if (int(random(0, 6)) != 0) { typoFlickering = true; }
            else                        { typoFlickering = false; }

            // randomize x-offset
            if (int(random(0, 8)) != 0) { typoOffsetX = int(random(-3, 3)); }
            else                        { typoOffsetX = 0; }

            // randomize y-offset
            if (int(random(0, 7)) != 0) { typoOffsetY = int(random(-2, 2)); }
            else                        { typoOffsetY = 0; }

            // display image with randomization
            if (typoFlickering) { scaledImage(intro_typo, width/2+typoOffsetX, height/2+typoOffsetY, width); }

        } else {
            // animation not running
            scaledImage(intro_typo, width/2, height/2, width);

        }

        // go to next state
        //if (timer.finished() || ctrl.isTracking()) { modeCtrl.nextAppMode(); }
    }

    void intro() {

        timer.set(20);
        timer.start();

        typoAnimationRunning.set(int(random(1, 2)));
        typoAnimationRunning.start();
        typoAnimationInterval.set(int(random(5, 9)));
        typoAnimationInterval.start();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 200);
        json.setInt("maxSpeed", 12);
        json.setInt("alignmentScalar", 100);
        json.setInt("cohesionScalar", 300);
        json.setInt("seperationScalar", 100);
        bl.configAllBoids(json);

        ctrl.setPosXYZ(width/2, height/2, -200);

    }


}
