class GameModeI extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeI() { super(); }
    GameModeI(BoidList b) { super(b); }
    GameModeI(BoidList b, InteractionController c) { super(b, c); }
    GameModeI(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

    }

    void intro() {

        // run the super intro
        super.intro();

        ctrl.enableSpeedGesture();
        ctrl.enableDivideGesture();

    }



}
