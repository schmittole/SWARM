class FinalMode extends Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PImage final_image;

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FinalMode() { super(); }
    FinalMode(BoidList b) { super(b); }
    FinalMode(BoidList b, InteractionController c) { super(b, c); }
    FinalMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void setup() {
        final_image = loadImage("final-mode.png");
    }

    void render() {
        imageMode(CENTER);
        scaledImage(final_image, width/2, height/2, width);
    }

    void intro() {
        ctrl.setPosXYZ(width/2, height/2, 0);
    }

}
