class GameModeG extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeG() { super(); }
    GameModeG(BoidList b) { super(b); }
    GameModeG(BoidList b, InteractionController c) { super(b, c); }
    GameModeG(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


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

    }

}
