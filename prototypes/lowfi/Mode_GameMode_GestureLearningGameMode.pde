class GestureLearningGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Ani animationA, animationB;
    int animationA_Value, animationB_Value;

    AniSequence outroSeq;

    boolean runOutro = false;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GestureLearningGameMode() { super(); }
    GestureLearningGameMode(BoidList b) { super(b); }
    GestureLearningGameMode(BoidList b, InteractionController c) { super(b, c); }
    GestureLearningGameMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render(int gestureID) {

        // run the super render
        super.render();

        // modeImage
        tint(0, 0, 100, animationA_Value);
        scaledImage(modeImage, width/2, height/2, width);

        // well doe image
        tint(0, 0, 100, animationB_Value);
        scaledImage(well_done, width/2, height/2, width);

        //if (timer.startedAndFinished()) {
        if (ctrl.gesturePerformedLast(gestureID)) {
            if (animationA.isEnded() && animationA_Value == 0) {
                animationA.reverse();
                animationA.start();
                animationB.start();
            }
        }

        if (animationB.isEnded()) {
            if (animationB_Value == 0) {
                modeCtrl.nextGameMode();
            } else {
                animationB.reverse();
                animationB.start();
            }
        }


    }

    void intro() {

        // run the super intro
        super.intro();

        //timer.setAndStart(5);

        animationA_Value = 0;
        animationA = new Ani(this, 2, "animationA_Value", 50);
        animationA.start();

        animationB_Value = 0;
        animationB = new Ani(this, 2, "animationB_Value", 50);
        animationB.setDelay(3);

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 10);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

    }

}
