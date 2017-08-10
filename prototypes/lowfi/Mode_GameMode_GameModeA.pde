class GameModeA extends TimeTriggeredGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeA() { super(); }
    GameModeA(BoidList b) { super(b); }
    GameModeA(BoidList b, InteractionController c) { super(b, c); }
    GameModeA(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        // set swarmCenter to fixed position
        ctrl.setPosXYZ(width/2, height/2, 0);

    }

    void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("In the beginning, everything was one…");

        // disable tracking
        ctrl.disableTracking();

        // timer that moves to next step
        timer.set(7);
        timer.start();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 80);
        json.setInt("maxSpeed", 20);
        json.setInt("alignmentScalar", 2);
        json.setInt("cohesionScalar", 5);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "200/40/100");
        bl.configAllBoids(json);

    }







}
