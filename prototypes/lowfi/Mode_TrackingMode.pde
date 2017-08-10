class TrackingMode extends Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PImage tracking_image;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    TrackingMode() { super(); }
    TrackingMode(BoidList b) { super(b); }
    TrackingMode(BoidList b, InteractionController c) { super(b, c); }
    TrackingMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void setup() {
        tracking_image = loadImage("tracking-mode.png");
    }

    void render() {

        ctrl.draw();

        // draw the shape
        scaledImage(tracking_image, width/2, height/2, width);

        // Go to TrackingMode if user is tracked
        if (ctrl.isTracking()) { modeCtrl.nextAppMode(); }

    }


    void intro() {

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 200);
        json.setInt("maxSpeed", 12);
        json.setInt("alignmentScalar", 100);
        json.setInt("cohesionScalar", 300);
        json.setInt("seperationScalar", 100);
        json.setString("HSB", "300/100/100");
        bl.configAllBoids(json);

        ctrl.setPosXYZ(width/2, height/2, 0);

    }

}
