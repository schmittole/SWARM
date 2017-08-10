class GameModeJ extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeJ() { super(); }
    GameModeJ(BoidList b) { super(b); }
    GameModeJ(BoidList b, InteractionController c) { super(b, c); }
    GameModeJ(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render(ctrl.getExplosionInt());

    }





    void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-explode.png");

        // enable only the speed gesture
        ctrl.enableExplosionGesture();

    }

}
