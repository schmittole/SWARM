class FreePlayGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    ArrayList<Flower> flowers = new ArrayList<Flower>();


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FreePlayGameMode() { super(); }
    FreePlayGameMode(BoidList b) { super(b); }
    FreePlayGameMode(BoidList b, InteractionController c) { super(b, c); }
    FreePlayGameMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

        // handle the flowers
        for (int i=flowers.size()-1; i>=0; i--) {
            Flower fl = flowers.get(i);
            if (!fl.isCollected()) { fl.render(); }
            else { flowers.remove(i); }
        }

        // Set life duration to boids
        for (int i=0; i<bl.getBoidList().size(); i++) {
            Boid b = (Boid)bl.getBoidList().get(i);
            b.letItDieInSec(int(random(50, 100)));
        }

        // use timer to jump into next gameMode after last flower collected
        if (!timer.started()) {
            if (flowers.size() == 0) { timer.setAndStart(5); }
        } else if (timer.finished()) { modeCtrl.nextGameMode(); }

        // go to final screen if all boids died
        if (bl.getBoidList().size() == 0) { modeCtrl.nextAppMode(); }

    }

    void intro() {

        // run the super intro
        super.intro();

        timer = new Timer();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 10);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        ctrl.enableAllGestures();

    }

}
