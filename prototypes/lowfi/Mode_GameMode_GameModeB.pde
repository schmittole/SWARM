class GameModeB extends TimeTriggeredGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/
    int boidIterator = 0;
    JSONObject json = new JSONObject();

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeB() { super(); }
    GameModeB(BoidList b) { super(b); }
    GameModeB(BoidList b, InteractionController c) { super(b, c); }
    GameModeB(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        bl.configBoid(boidIterator+1, json);
        bl.configBoid(boidIterator+2, json);
        boidIterator = boidIterator+2;

        // set swarmCenter to fixed position
        ctrl.setPosXYZ(width/2, height/2, 0);

    }

    void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("But everything got divided…");

        // disable tracking
        ctrl.disableTracking();

        boidIterator = 0;

        // timer that moves to next step
        timer.set(15);
        timer.start();

        for (int i=1; i<bl.getBoidList().size(); i++) {
            Boid b = (Boid)bl.getBoidList().get(i);
            b.letItDieInSec(int(random(8, 15)));
        }

        json.setInt("neighborhoodRadius", 150);
        json.setInt("maxSpeed", 8);
        json.setInt("alignmentScalar", 2);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 500);
        json.setString("HSB", "200/40/100");
        bl.configAllBoids(json);

    }







}
