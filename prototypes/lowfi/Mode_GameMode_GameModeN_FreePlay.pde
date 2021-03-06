class GameModeN extends FreePlayGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeN() { super(); }
    GameModeN(BoidList b) { super(b); }
    GameModeN(BoidList b, InteractionController c) { super(b, c); }
    GameModeN(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



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

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 18);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        myOldValues = json;

        // Add targets
        if (!introduced) {
            flowers.add(new FlowerA(new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 0), bl));
            flowers.add(new FlowerB(new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 0), bl));
            introduced = true;
        }

    }

}
