class GameMode extends Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/
    StagedText st, st2;
    PImage game_corners, game_glow, game_pattern, well_done, modeImage;

    Timer timer = new Timer();
    Timer timer2 = new Timer();
    Timer timer3 = new Timer();
    Timer timer4 = new Timer();

    boolean showModeImage = true;
    boolean introduced = false;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameMode() { super(); }
    GameMode(BoidList b) { super(b); }
    GameMode(BoidList b, InteractionController c) { super(b, c); }
    GameMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        // Got to Tracking Mode if Tracking lost
        if (!ctrl.isTracking()) {
            modeCtrl.previousAppMode();
        }

        ctrl.draw();

    }


    void intro() {

        st = new StagedText(4);
        st2 = new StagedText(4);

        ctrl.disableSpeedGesture();
        ctrl.disableDivideGesture();
        ctrl.disableExplosionGesture();

        well_done = loadImage("mode-finished.png");
        game_corners = loadImage("game-corners.png");
        game_glow = loadImage("game-glow.png");
        game_pattern = loadImage("game-pattern.png");

    }


}
