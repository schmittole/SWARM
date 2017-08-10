class GameModeE extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeE() { super(); }
    GameModeE(BoidList b) { super(b); }
    GameModeE(BoidList b, InteractionController c) { super(b, c); }
    GameModeE(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


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

    }

}
