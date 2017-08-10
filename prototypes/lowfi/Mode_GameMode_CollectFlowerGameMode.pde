class CollectFlowerGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    ArrayList<Flower> flowers = new ArrayList<Flower>();

    Ani animationA;
    int animationA_Value;

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    CollectFlowerGameMode() { super(); }
    CollectFlowerGameMode(BoidList b) { super(b); }
    CollectFlowerGameMode(BoidList b, InteractionController c) { super(b, c); }
    CollectFlowerGameMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        tint(0, 0, 100, animationA_Value);
        scaledImage(well_done, width/2, height/2, width);

        // handle the flowers
        for (int i=flowers.size()-1; i>=0; i--) {
            Flower fl = flowers.get(i);
            if (!fl.isCollected()) { fl.render(); }
            else {
                flowers.remove(i);
                if (flowers.size() == 0) {
                    animationA.start();
                }
            }
        }

        if (animationA.isEnded()) {
            if (animationA_Value == 0) {
                modeCtrl.nextGameMode();
            } else {
                animationA.setDelay(3);
                animationA.reverse();
                animationA.start();
            }
        }

    }


    void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("Try to find more of your kind!");

        animationA_Value = 0;
        animationA = new Ani(this, 2, "animationA_Value", 50);

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

            PVector abc = new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 100);
            flowers.add(new FlowerToroid(abc, bl, int(random(2, 5)), random(0.00001, 0.0005), int(random(3, 30)), int(random(3, 30))));
            abc = new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 100);
            flowers.add(new FlowerToroid(abc, bl, int(random(2, 5)), random(0.00001, 0.0005), int(random(3, 30)), int(random(3, 30))));
            abc = new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 100);
            flowers.add(new FlowerToroid(abc, bl, int(random(2, 5)), random(0.00001, 0.0005), int(random(3, 30)), int(random(3, 30))));
            abc = new PVector(int(random(width*0.2, width*0.8)), int(random(height*0.2, height*0.8)), 100);
            flowers.add(new FlowerToroid(abc, bl, int(random(2, 5)), random(0.00001, 0.0005), int(random(3, 30)), int(random(3, 30))));

            introduced = true;
        }


    }

}
