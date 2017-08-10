class Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    BoidList bl;
    InteractionController ctrl;
    ModeController modeCtrl;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/


    Mode() { }

    Mode(BoidList b) {
        bl = b;
    }

    Mode(BoidList b, InteractionController c) {
        bl = b;
        ctrl = c;
    }

    Mode(BoidList b, InteractionController c, ModeController m) {
        bl = b;
        ctrl = c;
        modeCtrl = m;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void setup() {}
    void render() {}
    void intro() {}

}
