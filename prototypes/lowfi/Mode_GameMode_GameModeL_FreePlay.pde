class GameModeL extends FreePlayGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeL() { super(); }
    GameModeL(BoidList b) { super(b); }
    GameModeL(BoidList b, InteractionController c) { super(b, c); }
    GameModeL(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



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
        json.setInt("maxSpeed", 10);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        myOldValues = json;

        // Add targets

        if (!introduced) {
            flowers.add(new FlowerA(new PVector(height*0.1, height*0.1), bl));
            flowers.add(new FlowerA(new PVector(width-height*0.1, height*0.9), bl));
            flowers.add(new FlowerA(new PVector(width-height*0.1, height*0.1), bl));
            flowers.add(new FlowerA(new PVector(height*0.1, height*0.9), bl));
            introduced = true;
        }

    }

}
