/*---------------------------------------------------------------
Imports
----------------------------------------------------------------*/
// import kinect library
import SimpleOpenNI.*;

// import utils
import java.util.Map.*;
import processing.sound.*;
import java.util.stream.*;
import de.looksgood.ani.*;

import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;





/*---------------------------------------------------------------
Variables
----------------------------------------------------------------*/

// KINECT VARS

// create kinect object
SimpleOpenNI kinect;

// Font Familys
FontFamily blenderPro;

// hand static vars
final static int LEFT = 101;
final static int RIGHT = 102;




// BOID VARS

int initBoidNum = 500; //amount of boids to start the program with
TriangleBoidList flock1;

boolean avoidWalls = false;


// GENERAL VARS

int lowerZBorder = -300;
int upperZBorder = 400;

boolean frameIsTracked = false;

// Is the kinect attached?
boolean useKinect = false;

// Make nice objects
InteractionController interactionController;
ModeController modeController;
Sound mySound;

Timer speedSoundTimer = new Timer(5);
Timer divideSoundTimer = new Timer(5);
Timer explosionSoundTimer = new Timer(5);
Timer gestureRecTimer = new Timer(0);

JSONObject myOldValues;

String lastGesture;

String currentGesture;

FlowerToroid toroid;
PApplet main;



float screenResolutionFactor;
int screenResolutionBaseWidth = 1920;
int screenResolutionBaseHeight = 1080;



/*---------------------------------------------------------------
Settings~
----------------------------------------------------------------*/

void settings() {

    // use fullscreen Window and OpenGL
    fullScreen(P3D);
    //size(1920, 1080, P3D);


    // smooth out drawing
    smooth();

    // Pulling the display's density dynamically
    // pixelDensity(displayDensity());

}





/*---------------------------------------------------------------
Setup
----------------------------------------------------------------*/
void setup() {

    screenResolutionFactor = min((width/float(screenResolutionBaseWidth)), (height/float(screenResolutionBaseHeight)));
    println("screenResolutionFactor: " + screenResolutionFactor);

    main = this;

    // set ColorMode to HSB
    colorMode(HSB, 360, 100, 100, 100);

    // Font
    blenderPro = new FontFamily();
    blenderPro.addFile("bold", "normal", "BlenderPro-Bold.ttf");

    // init sound
    mySound = new Sound(this);

    // Animation library
    Ani.init(this);
    Ani.noAutostart();



    // init GestureRecognition
    currentGesture = "NONE";

    // start Timers
    speedSoundTimer.start();
    divideSoundTimer.start();
    explosionSoundTimer.start();
    gestureRecTimer.start();

    // choose interactionController
    if (useKinect) { interactionController = new KinectController(this, mySound); }
    else           { interactionController = new MouseController(this, mySound); }

    // Boid Setup
    flock1 = new TriangleBoidList(initBoidNum, 255);


    myOldValues = new JSONObject();
    myOldValues.setInt("maxSpeed", flock1.getRandomBoid().getMaxSpeed());
    myOldValues.setInt("neighborhoodRadius", flock1.getRandomBoid().getNeighborhoodRadius());
    myOldValues.setInt("alignmentScalar",  flock1.getRandomBoid().getAlignmentScalar());
    myOldValues.setInt("cohesionScalar",  flock1.getRandomBoid().getCohesionScalar());
    myOldValues.setInt("seperationScalar",  flock1.getRandomBoid().getSeperationScalar());

    //toroid = new FlowerToroid(this, new PVector(width/2, height/2, 100), flock1, 5);


    // Init ModeController
    modeController = new ModeController();

    // Add AppModes
    modeController.addAppMode("introMode",    new IntroMode(flock1, interactionController, modeController));
    modeController.addAppMode("trackingMode", new TrackingMode(flock1, interactionController, modeController));
    modeController.addAppMode("finalMode",    new FinalMode(flock1, interactionController, modeController));

    // Setups
    modeController.getAppMode("introMode").setup();
    modeController.getAppMode("trackingMode").setup();
    modeController.getAppMode("finalMode").setup();

    // Add GameModes
    modeController.addGameMode(new GameModeA(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeB(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeD(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeE(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeF(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeG(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeH(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeI(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeJ(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeL(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeM(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeN(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeP(flock1, interactionController, modeController));
    modeController.addGameMode(new GameModeQ(flock1, interactionController, modeController));

    // Set StartMode
    modeController.setActiveAppMode("introMode");
    modeController.getActiveAppMode().intro();

}







/*---------------------------------------------------------------
Draw
----------------------------------------------------------------*/
void draw() {
    //noLights();
    // we don't wanna have a cursor
    noCursor();

    if (interactionController.speedGestureTriggered()) {
        mySound.playSpeedUp();
        myOldValues.setInt("maxSpeed", flock1.getRandomBoid().getMaxSpeed());
        JSONObject jsonSpeed = new JSONObject();
        jsonSpeed.setInt("maxSpeed", 50);
        flock1.configAllBoids(jsonSpeed);
    }

    if (interactionController.divideGestureTriggered()) {
        mySound.playDivide();
        flock1.divide();
    }

    if (interactionController.explosionGestureTriggered()) {
        mySound.playExplosion();
        myOldValues.setInt("neighborhoodRadius", flock1.getRandomBoid().getNeighborhoodRadius());
        myOldValues.setInt("alignmentScalar",  flock1.getRandomBoid().getAlignmentScalar());
        myOldValues.setInt("cohesionScalar",  flock1.getRandomBoid().getCohesionScalar());
        myOldValues.setInt("seperationScalar",  flock1.getRandomBoid().getSeperationScalar());
        JSONObject jsonExplosion = new JSONObject();
        jsonExplosion.setInt("neighborhoodRadius", 200);
        jsonExplosion.setInt("alignmentScalar", 100);
        jsonExplosion.setInt("cohesionScalar", 300);
        jsonExplosion.setInt("seperationScalar", 100);
        flock1.configAllBoids(jsonExplosion);
    }

    if (interactionController.gestureReseted()) {
        flock1.configAllBoids(myOldValues);
        myOldValues = new JSONObject();
        flock1.reunion(RIGHT);
    }


    // play the sound
    mySound.playMusicDependingOnGameState();

    // draw a black background to make old drawings disapear
    background(0, 0, 0);
    rectMode(CORNER);
    fill(0, 0, 0, 90);

    //toroid.render();

    // run flock1
    flock1.run(avoidWalls);
    // render activeMode
    modeController.getActiveAppMode().render();

    // light
    //directionalLight(0, 0, 100, 0, 1, -100);
    //spotLight(0, 0, 100, width/2, height/2, -200, -1, -1, -1, PI/20, 900);

    //pointLight(0, 0, 100, width, 0, -200);

    ambientLight(128,128,128);
    directionalLight(128,128,128,0,0,-1);
    lightFalloff(1,0,0);


}