class GameModeD extends TimeTriggeredGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Timer timer2 = new Timer();


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeD() { super(); }
    GameModeD(BoidList b) { super(b); }
    GameModeD(BoidList b, InteractionController c) { super(b, c); }
    GameModeD(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        if (timer2.finished()) {
            // update text
            st.setText("Raise your right hand…\nDo you feel it?");
        }

    }

    void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("Raise your right hand…");

        // enable the tracking
        ctrl.enableTracking();

        // timer that moves to next step
        timer.set(18);
        timer.start();

        // timer that changes text
        timer2.set(6);
        timer2.start();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 20);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

    }

}
