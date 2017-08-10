class TimeTriggeredGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    TimeTriggeredGameMode() {
        super();
    }

    TimeTriggeredGameMode(BoidList b) {
        super();
        bl = b;
    }

    TimeTriggeredGameMode(BoidList b, InteractionController c) {
        super();
        bl = b;
        ctrl = c;
    }

    TimeTriggeredGameMode(BoidList b, InteractionController c, ModeController m) {
        super();
        bl = b;
        ctrl = c;
        modeCtrl = m;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        super.render();

        if (timer.finished()) {
            modeCtrl.nextGameMode();
        }

    }

    void intro() {

        super.intro();

    }

}
