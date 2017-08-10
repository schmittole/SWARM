class GameModeF extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeF() { super(); }
    GameModeF(BoidList b) { super(b); }
    GameModeF(BoidList b, InteractionController c) { super(b, c); }
    GameModeF(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render(ctrl.getSpeedInt());

    }





    void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-speed.png");

        // enable only the speed gesture
        ctrl.enableSpeedGesture();

    }

}
