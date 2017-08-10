class GameModeH extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeH() { super(); }
    GameModeH(BoidList b) { super(b); }
    GameModeH(BoidList b, InteractionController c) { super(b, c); }
    GameModeH(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render(ctrl.getDivideInt());

    }





    void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-divide.png");

        // enable only the speed gesture
        ctrl.enableDivideGesture();

    }

}
