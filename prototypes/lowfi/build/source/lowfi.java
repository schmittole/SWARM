import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import SimpleOpenNI.*; 
import java.util.Map.*; 
import processing.sound.*; 
import java.util.stream.*; 
import de.looksgood.ani.*; 
import shapes3d.utils.*; 
import shapes3d.animation.*; 
import shapes3d.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lowfi extends PApplet {

/*---------------------------------------------------------------
Imports
----------------------------------------------------------------*/
// import kinect library


// import utils













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
boolean useKinect = true;

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

public void settings() {

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
public void setup() {

    screenResolutionFactor = min((width/PApplet.parseFloat(screenResolutionBaseWidth)), (height/PApplet.parseFloat(screenResolutionBaseHeight)));
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
public void draw() {
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
class Boid {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PVector pos, vel, acc, ali, coh, sep; //pos, velocity, and acceleration in a vector datatype
    boolean avoidWalls = false;
    BoidList myFlock;

    Timer colorTimer1 = new Timer();



    /*---------------------------------------------------------------
    Parameters
    ----------------------------------------------------------------*/

    int neighborhoodRadius = 200; //radius in which it looks for fellow boids
    int maxSpeed = 12; //maximum magnitude for the velocity vector
    int maxSteerForce = 0; //maximum magnitude of the steering vector
    int colorH = 0; //hue
    int colorS = 0; //saturation
    int colorB = 100; //brightness
    int hStart = color(0, 0, 100);
    int hFinish = color(colorH, colorS, colorB);
    int hHighlight = color(colorH, colorS, colorB);
    int h;
    float mass;
    float scale = 2; //scale factor for the render of the boid
    int alignmentScalar = 100;
    int cohesionScalar = 300;
    int seperationScalar = 300;
    float colorStep = 0;
    int handToFollow = RIGHT;

    float lifeFactor = 1;
    float lifeDuration = 0;
    float dyingThreshold = 600;
    float lifeFactorDecrement;

    boolean letItDie = false;

    boolean highlightColorMode = false;

    float activeSpeed;

    /*---------------------------------------------------------------
    getters
    ----------------------------------------------------------------*/

    public void setNeighborhoodRadius(int value)   { neighborhoodRadius = value; }
    public void setMaxSpeed(int value)             { maxSpeed = value; }
    public void setMaxSteerForce(int value)        { maxSteerForce = value; }
    public void setColorH(int value)               { colorH = value; hFinish = color(colorH, colorS, colorB); }
    public void setColorS(int value)               { colorS = value; hFinish = color(colorH, colorS, colorB); }
    public void setColorB(int value)               { colorB = value; hFinish = color(colorH, colorS, colorB); }
    public void setMass(float value)               { mass = value; }
    public void setScale(float value)              { scale = value; }
    public void setAvoidWalls(boolean value)       { avoidWalls = value; }
    public void setMyFlock(BoidList value)         { myFlock = value; }
    public void setAlignmentScalar(int value)      { alignmentScalar = value; }
    public void setCohesionScalar(int value)       { cohesionScalar = value; }
    public void setSeperationScalar(int value)     { seperationScalar = value; }
    public void resetColorStep()                   { colorStep = 0; }
    public void setHandToFollow(int hand)          { handToFollow = hand; }
    public void setLifeDuration(int v)             { lifeDuration = v; }
    public void setDyingThreshold(int v)           { dyingThreshold = v; lifeFactorDecrement = 1/dyingThreshold; }

    public void setHSB(String hsb) {
        String[] hsbVals = hsb.split("/");
        setColorH(Integer.parseInt(hsbVals[0]));
        setColorS(Integer.parseInt(hsbVals[1]));
        setColorB(Integer.parseInt(hsbVals[2]));
        colorStep = 0;
    }

    public void setHighlightHSB(String hsb) {
        String[] hsbVals = hsb.split("/");
        hHighlight = color(Integer.parseInt(hsbVals[0]), Integer.parseInt(hsbVals[1]), Integer.parseInt(hsbVals[2]));
        highlightColorMode = true;
        colorStep = 0;
        colorTimer1.setAndStart(1.5f);
    }

    public void letItDieInSec(int sec) {
        if (!letItDie) {
            setLifeDuration(sec*60);
            setDyingThreshold(sec*60);
            letItDie = true;
        }
    }




    /*---------------------------------------------------------------
    GETTER
    ----------------------------------------------------------------*/

    public int getNeighborhoodRadius()   { return neighborhoodRadius; }
    public int getMaxSpeed()             { return maxSpeed; }
    public int getMaxSteerForce()        { return maxSteerForce; }
    public int getColorH()               { return colorH; }
    public int getColorS()               { return colorS; }
    public int getColorB()               { return colorB; }
    public float getMass()               { return mass; }
    public float getScale()              { return scale; }
    public boolean getAvoidWalls()       { return avoidWalls; }
    public BoidList getMyFlock()         { return myFlock; }
    public int getAlignmentScalar()      { return alignmentScalar; }
    public int getCohesionScalar()       { return cohesionScalar; }
    public int getSeperationScalar()     { return seperationScalar; }
    public int getHandToFollow()         { return handToFollow; }
    public int getHSB()                { return hFinish; }
    public float getX()                  { return pos.x; }
    public float getY()                  { return pos.y; }
    public float getZ()                  { return pos.z; }
    public PVector getPos()              { return pos; }




    /*---------------------------------------------------------------
    Constructors
    ----------------------------------------------------------------*/

    Boid() {
        lifeFactorDecrement = 1/dyingThreshold;
        activeSpeed = maxSpeed;
    }

    Boid (PVector inPos) {
        lifeDuration = random(1800, 7200);
        lifeFactorDecrement = 1/dyingThreshold;
        pos = new PVector();
        pos.set(inPos);
        vel = new PVector(random(-1, 1), random(-1, 1), random(1, -1));
        acc = new PVector(0, 0, 0);
        activeSpeed = maxSpeed;
    }

    Boid (PVector inPos, PVector inVel, float r) {
        lifeFactorDecrement = 1/dyingThreshold;
        pos = new PVector();
        pos.set(inPos);
        vel = new PVector();
        vel.set(inVel);
        acc = new PVector(0, 0);
        neighborhoodRadius = (int)r;
        activeSpeed = maxSpeed;
    }



    public int interpolate(int oldColor, int newColor, float incre) {

        if (colorStep<1) {
            colorStep += incre;
            return lerpColor(oldColor, newColor, colorStep);
        } else {
            hStart = newColor;
            return newColor;
        }

    }


    public int interpolateHighlightColor(int oldColor, int newColor) {

        float progress = colorTimer1.progress();
        float progressBorder = 0.1f;

        if (progress <= progressBorder) {
            return lerpColor(oldColor, newColor, map(progress, 0, progressBorder, 0, 1));
        } else if (progress < 1) {
            return lerpColor(oldColor, newColor, map(progress, progressBorder, 1.0f, 1, 0));
        } else {
            highlightColorMode = false;
            return oldColor;
        }

    }



    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    public void run (ArrayList bl) {

        if (avoidWalls) {
            acc.add(PVector.mult(avoid(new PVector( pos.x, height, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,      0, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( width,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector(     0,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   300), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   900), true), 5));
        }

        if (highlightColorMode) {
            h = interpolateHighlightColor(hStart, hHighlight);
        } else {
            h = interpolate(hStart, hFinish, 0.005f);
        }

        //h = interpolate(hStart, hFinish, 0.005);

        activeSpeed = maxSpeed * ((lifeFactor/2) + 0.5f);

        flock(bl);
        move();
        render();

        if (letItDie) {
            if (lifeDuration < dyingThreshold) {
                lifeFactor = lifeFactor - lifeFactorDecrement;
            }
            lifeDuration--;
        }

        // remove boid from list if it is dead
        if (died()) { bl.remove(this); }

    }



    /*---------------------------------------------------------------
    Behaviors
    ----------------------------------------------------------------*/

    public void flock(ArrayList bl) {

        acc.add(seek(interactionController.getPosition(handToFollow)));

        ali = alignment(bl);
        coh = cohesion(bl);
        sep = seperation(bl);
        acc.add(PVector.mult(ali, alignmentScalar));
        acc.add(PVector.mult(coh, cohesionScalar));
        acc.add(PVector.mult(sep, seperationScalar));

    }


    // move
    public void move() {
        vel.add(acc);        //add acceleration to velocity
        vel.limit(activeSpeed); //make sure the velocity vector magnitude does not exceed maxSpeed
        pos.add(vel);        //add velocity to position
        acc.mult(0);         //reset acceleration
    }



    // has the boid died?
    public boolean died() {
        if (lifeDuration <= 0) { return true; }
        else { return false; }
    }


    // render
    public void render() {}




    /*---------------------------------------------------------------
    HELPER
    ----------------------------------------------------------------*/


    // cohesion
    public PVector cohesion(ArrayList boids) {
        PVector posSum = new PVector(0, 0, 0);
        PVector steer = new PVector(0, 0, 0);
        int count = 0;
        for (int i=0; i<boids.size(); i++) {
            Boid b = (Boid)boids.get(i);
            float d = dist(pos.x, pos.y, b.pos.x, b.pos.y);

            if ((d>0) && (d <= neighborhoodRadius)) {
                posSum.add(b.pos);
                count++;
            }
        }

        if (count > 0) { posSum.div((float)count); }
        steer = PVector.sub(posSum, pos);
        steer.limit(maxSteerForce);
        return steer;
    }


    //steering. If arrival==true, the boid slows to meet the target. Credit to Craig Reynolds
    public PVector steer(PVector target,boolean arrival) {

        PVector steer = new PVector(); //creates vector for steering
        if(!arrival) {
            steer.set(PVector.sub(target, pos)); //steering vector points towards target (switch target and pos for avoiding)
            steer.limit(maxSteerForce); //limits the steering force to maxSteerForce
        } else {
            PVector targetOffset = PVector.sub(target, pos);
            float distance = targetOffset.mag();
            float rampedSpeed = activeSpeed*(distance/100);
            float clippedSpeed = min(rampedSpeed, activeSpeed);
            PVector desiredVelocity = PVector.mult(targetOffset, (clippedSpeed/distance));
            steer.set(PVector.sub(desiredVelocity, vel));
        }
        return steer;
    }


    // seek
    public PVector seek(PVector target) {
        PVector desired = PVector.sub(target, pos);
        desired.normalize();
        desired.mult(activeSpeed);
        PVector steer = PVector.sub(desired, vel);
        steer.limit(0.8f);
        //applyForce(steer);
        return steer;
    }


    //avoid. If weight == true avoidance vector is larger the closer the boid is to the target
    public PVector avoid(PVector target, boolean weight) {
        PVector steer = new PVector(); //creates vector for steering
        steer.set(PVector.sub(pos,target)); //steering vector points away from target
        if (weight) { steer.mult(1/sq(PVector.dist(pos,target))); }
        //steer.limit(maxSteerForce); //limits the steering force to maxSteerForce
        return steer;
    }


    // separation
    public PVector seperation(ArrayList boids) {
        PVector posSum = new PVector(0, 0, 0);
        PVector repulse;
        for (int i=0; i<boids.size(); i++) {
            Boid b = (Boid)boids.get(i);
            float d = PVector.dist(pos, b.pos);
            if ((d > 0) && (d <= neighborhoodRadius)) {
                repulse = PVector.sub(pos, b.pos);
                repulse.normalize();
                repulse.div(d);
                posSum.add(repulse);
            }
        }
        return posSum;
    }


    // alignment
    public PVector alignment(ArrayList boids) {
        PVector velSum = new PVector(0,0,0);
        int count = 0;
        for (int i=0; i<boids.size(); i++) {
            Boid b = (Boid)boids.get(i);
            float d = PVector.dist(pos, b.pos);

            if ((d > 0) && (d <= neighborhoodRadius)) {
                velSum.add(b.vel);
                count++;
            }
        }
        if (count > 0) {
            velSum.div((float)count);
            velSum.limit(maxSteerForce);
        }
        return velSum;
    }


}
/*BoidList object class
* Matt Wetmore
* Changelog
* ---------
* 12/18/09: Started work
*/

class BoidList {



    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    ArrayList boids; //will hold the boids in this BoidList
    int colorH; //for Hue
    int colorS; //for Saturation
    int colorB; //for Brightness
    int h;
    boolean isDivided = false;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    BoidList() {
        boids = new ArrayList();
    }

    BoidList(int n, int ih) {
        boids = new ArrayList();
        h = ih; 
        for (int i=0; i<n; i++) {
            boids.add(new TriangleBoid(new PVector(width/2, height/2, 0)));
        }
    }



    /*---------------------------------------------------------------
    Funcionality
    ----------------------------------------------------------------*/

    public PVector getCenterOfSwarm() {
        float x = 0;
        float y = 0;
        float z = 0;
        for (Object o : boids) {
            Boid b = (Boid)o;
            x += b.getX();
            y += b.getY();
            z += b.getZ();
        }
        int size = boids.size();
        return new PVector(x/size, y/size, z/size);
    }



    /*---------------------------------------------------------------
    Add Boids
    ----------------------------------------------------------------*/

    // Add one boid
    public void add() { boids.add(new TriangleBoid(new PVector(width/2, height/2, 0))); }
    public void add(PVector pos) { boids.add(new TriangleBoid(pos)); }

    // add a given number of boids
    public void addBoids(int num) { for (int i=0; i<num; i++) { add(); } }
    public void addBoidsCurrentPos(int num) { for (int i=0; i<num; i++) { add(getCenterOfSwarm()); } }

    // add a specific boid
    public void addBoid(Boid b) { boids.add(b); }




    /*---------------------------------------------------------------
    Run
    ----------------------------------------------------------------*/

    public void run(boolean aW) {

        for (int i=0; i<boids.size(); i++) {
            //iterate through the list of boids
            Boid tempBoid = (Boid)boids.get(i); //create a temporary boid to process and make it the current boid in the list
            tempBoid.run(boids); //tell the temporary boid to execute its run method
        }

    }



    /*---------------------------------------------------------------
    Behaviour
    ----------------------------------------------------------------*/

    // divide swarm
    public void divide() {
        isDivided = true;
        for (int i=0; i<(int)(boids.size()/2); i++) {
            Boid b = (Boid)boids.get(i);
            b.setHandToFollow(LEFT);
        }
    }

    // reunion swarm
    public void reunion(int hand) {
        isDivided = false;
        for (int i=0; i<(int)(boids.size()); i++) {
            Boid b = (Boid)boids.get(i);
            b.setHandToFollow(hand);
        }
    }



    /*---------------------------------------------------------------
    Getter
    ----------------------------------------------------------------*/

    // returns a random boid from the collection
    public Boid getRandomBoid() {
        return (Boid)boids.get(PApplet.parseInt(random(0, boids.size())));
    }

    // returns the boid list
    public ArrayList<Boid> getBoidList() { return boids; }

    public boolean isDivided() { return isDivided; }



    /*---------------------------------------------------------------
    Config
    ----------------------------------------------------------------*/

    // Config all boids in the list with a json
    public void configAllBoids(JSONObject config) {
        for (int i=0; i<boids.size(); i++) { configBoid(i, config); }
    }


    // Config a boid with a json
    public void configBoid(int i, JSONObject config) {
        if (i < boids.size()) {
            Boid b = (Boid)boids.get(i);
            if (!config.isNull("neighborhoodRadius")) { b.setNeighborhoodRadius(config.getInt("neighborhoodRadius")); }
            if (!config.isNull("colorH"))             { b.setColorH(config.getInt("colorH")); }
            if (!config.isNull("colorS"))             { b.setColorS(config.getInt("colorS")); }
            if (!config.isNull("colorB"))             { b.setColorB(config.getInt("colorB")); }
            if (!config.isNull("HSB"))                { b.setHSB(config.getString("HSB")); }
            if (!config.isNull("maxSpeed"))           { b.setMaxSpeed(config.getInt("maxSpeed")); }
            if (!config.isNull("alignmentScalar"))    { b.setAlignmentScalar(config.getInt("alignmentScalar")); }
            if (!config.isNull("cohesionScalar"))     { b.setCohesionScalar(config.getInt("cohesionScalar")); }
            if (!config.isNull("seperationScalar"))   { b.setSeperationScalar(config.getInt("seperationScalar")); }
            if (!config.isNull("maxSteerForce"))      { b.setMaxSteerForce(config.getInt("maxSteerForce")); }
            if (!config.isNull("scale"))              { b.setScale(config.getInt("scale")); }
            if (!config.isNull("mass"))               { b.setMass(config.getInt("mass")); }
            if (!config.isNull("resetColorStep"))     { b.resetColorStep(); }
            if (!config.isNull("avoidWalls"))         { b.setAvoidWalls(config.getBoolean("avoidWalls")); }
        }
    }






}
class TriangleBoidList extends BoidList {

    //ChasingBoid chaser;


    TriangleBoidList() {
        super();
    }

    TriangleBoidList(int n, int ih) {
        boids = new ArrayList();
        h = ih;
        for (int i=0; i<n; i++) {
            boids.add(new TriangleBoid(new PVector(width/2, height/2, 0), this));
        }
    }


    public void run(boolean aW, int currentGameState) {

        //run each boid with current gameState
        for (int i=0; i<boids.size(); i++) {
            //iterate through the list of boids
            TriangleBoid tempBoid = (TriangleBoid)boids.get(i); //create a temporary boid to process and make it the current boid in the list
            tempBoid.h = h;
            tempBoid.avoidWalls = aW;
            tempBoid.run(boids); //tell the temporary boid to execute its run method
            // println("TRIANGLE: "+tempBoid.pos);
        }

    }

}
class TriangleBoid extends Boid {



    /*---------------------------------------------------------------
    Overwrite Fields
    ----------------------------------------------------------------*/
    public void setParentVars() {
        // User setter functions of parent class
        //super.setNeighborhoodRadius(10);
        //super.setMaxSpeed(16);
        //super.setMaxSteerForce(0);
        super.setScale(2);
        super.setAvoidWalls(false);
        super.setMyFlock(myFlock);
    }



    /*---------------------------------------------------------------
    Constructors
    ----------------------------------------------------------------*/


    TriangleBoid(PVector inPos) {
        super(inPos);
        setParentVars();
    }

    TriangleBoid(PVector inPos, BoidList myFlock) {
        super(inPos);
        setParentVars();
        super.setMyFlock(myFlock);
    }

    TriangleBoid(PVector inPos, PVector inVel, float r) {
        super(inPos, inVel, r);
        setParentVars();
    }

    TriangleBoid(PVector inPos, PVector inVel, float r, BoidList myFlock) {
        super(inPos, inVel, r);
        setParentVars();
    }




    /*---------------------------------------------------------------
    Overwrite Functions
    ----------------------------------------------------------------*/

    // render
    public void render() {

        super.render();

        pushMatrix();

            translate(pos.x, pos.y, pos.z);
            rotateY(atan2(-vel.z, vel.x));
            rotateZ(asin(vel.y/vel.mag()));
            stroke(h);
            noFill();
            noStroke();
            fill(h, lifeFactor*100);

            //draw bird

            beginShape(TRIANGLES);

                vertex( 3*scale,        0,        0);
                vertex(-3*scale,  2*scale,        0);
                vertex(-3*scale, -2*scale,        0);

                vertex( 3*scale,        0,        0);
                vertex(-3*scale,  2*scale,        0);
                vertex(-3*scale,        0,  2*scale);

                vertex( 3*scale,        0,     0   );
                vertex(-3*scale,        0,  2*scale);
                vertex(-3*scale, -2*scale,        0);

                vertex(-3*scale,        0,  2*scale);
                vertex(-3*scale,  2*scale,        0);
                vertex(-3*scale, -2*scale,        0);

            endShape();

        popMatrix();

    }



}
class TriangleBoidWithExtent extends TriangleBoid {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Extent extent;

    float radiusExt = 20;
    float sizeExt = 50;
    int cExt = color(255,0,255);
    int amountExt = 1;
    float velExt = 0.5f;


    /*---------------------------------------------------------------
    Overwrite Fields
    ----------------------------------------------------------------*/
    public void setParentVars() {
        // User setter functions of parent class
        //super.setNeighborhoodRadius(10);
        //super.setMaxSpeed(16);
        //super.setMaxSteerForce(0);
        super.setScale(2);
        super.setAvoidWalls(false);
        super.setMyFlock(myFlock);
    }

    /*---------------------------------------------------------------
    Constructors
    ----------------------------------------------------------------*/

    TriangleBoidWithExtent(PVector inPos) {
        super(inPos);
        // init guide
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        setParentVars();
    }

    TriangleBoidWithExtent(PVector inPos, BoidList myFlock) {
        super(inPos);
        // init extent
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        setParentVars();
        super.setMyFlock(myFlock);
    }

    TriangleBoidWithExtent(PVector inPos, PVector inVel, float r) {
        super(inPos, inVel, r);
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        // init extent
        setParentVars();
    }

    TriangleBoidWithExtent(PVector inPos, PVector inVel, float r, BoidList myFlock) {
        super(inPos, inVel, r);
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        // init extent
        setParentVars();
    }




    /*---------------------------------------------------------------
    Overwrite Functions
    ----------------------------------------------------------------*/

    public void run (ArrayList bl) {

        if (avoidWalls) {
            acc.add(PVector.mult(avoid(new PVector( pos.x, height, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,      0, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( width,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector(     0,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   300), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   900), true), 5));
        }

        flock(bl);
        move();
        render();

        extent.render(pos);

    }


}
// Declaration of the guideBoid class
class Extent {
  PVector pos;
  PVector posCenter;
  int c;
  int amount = 1;
  float radius = 500;
  float vel = 0.1f;
  float angleNew;
  float size = 50;
  float sizeB = 50;
  float speed = 0;
  boolean up = true;
  float angle = 0;
  float sizeInc = 0.01f;

  // boolean up = true;

// Constructor with incoming arguments
  Extent(PVector inPos, float _radius, float _size, int _amount, float _vel, int _c) {
    posCenter = new PVector();
    vel = _vel;
    pos = new PVector();
    posCenter.set(inPos);
    c = _c;
    radius = _radius;
    size = _size;
    sizeB = _size;
    amount = _amount;
  }

  public void render(PVector posCenterNew) {
    rotateZ(angle);

    sphereDetail(5);
    noStroke();
    fill(c);

    speed =+ speed + vel;

    float iteration = TWO_PI / amount;

    for (angle = 0; angle < TWO_PI; angle+= iteration) {

      pushMatrix();

      pos.x = cos(angle + speed) * radius;
      pos.y = sin(angle + speed) * radius;

      translate(posCenterNew.x + pos.x, posCenterNew.y + pos.y);
      // rotateY(angle/10);
      // rotateX(angle/20);
      noFill();
      stroke(50,20,100);
      ellipse(0,0,size,size);
      popMatrix();
      println(size);

      if ( up) {
        size += sizeInc;
        if(size > sizeB * 5) {
          up = false;
        }
      } else if (!up) {
        size -= sizeInc;
        if(size < 10) {
          up = true;
        }
      }

    }
  }
}
class Flower {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PVector pos;

    float angle1;
    float angle2;

    float angleForLevitate = random(0, TWO_PI);

    float angle1Inc = 0.007f;
    float angle2Inc = 0.06f;

    float angleForLevitateInc = random(0.02f, 0.06f);

    float scalar = 100;

    boolean collected = false;

    float collectDis = width*0.05f;

    int sizeA = 5;
    int sizeB = 30;
    int sizeC = 15;

    BoidList boidList;

    int amount;

    float speed = 0.1f;
    float vel = 0.02f;

    Ani animationA;
    int faderOpacity = 0;

    int appearCounter = 0;
    int appearCounterStep = 10;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    Flower(PVector p) {
        pos = new PVector();
        pos.set(p);
        animationA = new Ani(this, 2, "faderOpacity", 100);
        animationA.start();
    }

    Flower(PVector p, BoidList bl) {
        pos = new PVector();
        pos.set(p);
        boidList = bl;
        animationA = new Ani(this, 2, "faderOpacity", 100);
        animationA.start();
    }




    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void setPos(PVector p) { pos.set(p); }
    public PVector getPos() { return pos; }
    public boolean isCollected() { return collected; }



    public void render() {

        angle1 += angle1Inc;
        angle2 += angle2Inc;
        angleForLevitate += angleForLevitateInc;

        if (angle1 >= TWO_PI) { angle1 = 0; }
        if (angle2 >= TWO_PI) { angle2 = 0; }
        if (angleForLevitate >= TWO_PI) { angleForLevitate = 0; }

        //pos.x += sin(angle1);
        pos.y += sin(angleForLevitate);
        //pos.z += sin(angle1);

        if (boidList != null && !collected) { detect(boidList.getCenterOfSwarm()); }

    }



    public void detect(PVector swarmCenter) {
        if (dist(pos.x, pos.y, pos.z, swarmCenter.x, swarmCenter.y, swarmCenter.z) < collectDis) {
            hitDetected();
        } else {
            collected = false;
        }
    }


    public void hitDetected() {
        mySound.playRandomB();
        collected = true;
        boidList.addBoidsCurrentPos(10);
        boidList.configAllBoids(myOldValues);
        // Set highlight color
        for (int i=0; i<boidList.getBoidList().size(); i++) {
            Boid b = (Boid)boidList.getBoidList().get(i);
            b.setHighlightHSB("167/36/90");
        }
    }

}
class FlowerA extends Flower {



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerA(PVector p) { super(p); amount = 5; }
    FlowerA(PVector p, BoidList bl) { super(p, bl); amount = 5; }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        noFill();

        pushMatrix();

            translate(pos.x, pos.y, pos.z);

            int inc = 360/amount;

            for(int i = 0; i < 360; i += inc) {
                float line = (1+i/10000) < 2 ? 2 : (1+i/20);
                strokeWeight(line);
                stroke(167, 36, 90, faderOpacity);
                //rotateX(angle1);
                rotateY((angle1)%360);
                ellipse(0, 0, sizeA+i/10, sizeA+i/10);
            }

        popMatrix();

    }


}
class FlowerB extends Flower {




    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerB(PVector p) { super(p); amount = 7;}
    FlowerB(PVector p, BoidList bl) { super(p, bl); amount = 7;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        noFill();

        pushMatrix();

            //translate(pos.x, pos.y, pos.z);

            rotateZ(angle1);

                strokeWeight(1);
                stroke(167, 36, 255, faderOpacity);
                noFill();

                speed = speed + vel;
                if(speed >= 360) {
                    speed = 0;
                }

                float iteration = TWO_PI / amount;

                for (angle1 = 0; angle1 < TWO_PI; angle1+= iteration) {

                  pushMatrix();
                  translate(pos.x, pos.y, pos.z);
                  rotateY(angle1 + speed);
                  // rotateX(angle/20);
                  ellipse(0,0,sizeB,sizeB);
                  popMatrix();



                }
    popMatrix();
            }

}
class FlowerC extends Flower {



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerC(PVector p) { super(p); amount = 5;}
    FlowerC(PVector p, BoidList bl) { super(p, bl); amount = 5;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        noFill();

        pushMatrix();

            translate(pos.x, pos.y, pos.z);
            rotateZ(180);
            int inc = 360/amount;

            for (int i = 0; i < 360; i += inc) {
                float line = (1+i/10000) < 2 ? 2 : (1+i/20);
                strokeWeight(line);
                stroke(167, 36, 90, faderOpacity);
                //rotateX(angle1);
                rotateY((angle1)%360);
                float mySize = sizeA+i/10;
                rect(-mySize/2, -mySize/2, mySize, mySize);
            }

        popMatrix();

    }


}
class FlowerD extends Flower {


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerD(PVector p) { super(p); amount = 5;}
    FlowerD(PVector p, BoidList bl) { super(p, bl); amount = 5;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        noFill();

        pushMatrix();

            translate(pos.x, pos.y, pos.z);
            int inc = 360/amount;

            for (int i = 0; i < 360; i += inc) {
                float line = (1+i/10000) < 2 ? 2 : (1+i/20);
                strokeWeight(line);
                stroke(167, 36, 90, faderOpacity);
                //rotateX(angle1);
                rotateY((angle1)%360);
                float mySize = sizeA+i/10;
                beginShape(TRIANGLES);
                vertex(-mySize/2, mySize*0.1f);
                vertex(0, -mySize*1.5f);
                vertex(mySize/2, mySize*0.1f);
                endShape();
            }

        popMatrix();

    }


}
class FlowerE extends Flower {


    float radiusSwitch;
    float radius = 10;

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerE(PVector p) { super(p); amount = 35;}
    FlowerE(PVector p, BoidList bl) { super(p, bl); amount = 35;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();
        pushMatrix();
        rotateZ(angle1);
        stroke(167, 36, 90, faderOpacity);
        strokeWeight(2);
        noFill();

        speed =+ speed + vel;

        float iteration = TWO_PI / amount;

        for (angle1 = 0; angle1 < TWO_PI; angle1+= iteration) {

          radius += radius + 0.1f;

          radiusSwitch = angle1 * sizeC;

          pushMatrix();
          translate(pos.x, pos.y);
          rotateY(angle1 + speed);
          ellipse(0,0,sizeC + radiusSwitch ,sizeC + radiusSwitch);
          popMatrix();

        }
        popMatrix();

    }

    public void detect(PVector swarmCenter) {
        if (!flock1.isDivided()) { collected = false; }
        else { super.detect(swarmCenter); }
    }


}
class FlowerToroid extends Flower {

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    Toroid[] myShapes;

    FlowerToroid(lowfi s, PVector p) { super(p); amount = 5; }
    FlowerToroid(lowfi s, PVector p, BoidList bl) { super(p, bl); amount = 5; }
    FlowerToroid(lowfi s, PVector p, BoidList bl, int amount) {
        super(p, bl);
        this.amount = amount;
        myShapes = new Toroid[amount];
        for(int i=0; i<amount; i++) {
            myShapes[i] = new Toroid(s, 30, 30);
            myShapes[i].setTexture("texture01.jpg", 6, 1);
            myShapes[i].drawMode(S3D.TEXTURE);
        }
        vel = 0.0001f;
    }

    FlowerToroid(PVector p, BoidList bl, int amount, float vel, int aSolution, int bSolution) {
        super(p, bl);
        this.amount = amount;
        myShapes = new Toroid[amount];
        for(int i=0; i<amount; i++) {
            myShapes[i] = new Toroid(main, aSolution, bSolution);
            myShapes[i].setTexture("texture01.jpg", 6, 1);
            myShapes[i].drawMode(S3D.TEXTURE);
        }
        this.vel = vel;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        pushMatrix();

            float inc = 360/amount;

            for(int i = 0; i < myShapes.length; i++) {
                speed = speed + vel;
                if(speed >= 1) {
                    speed = 0;
                }

                int baseSize = 1;
                int size = responsiveInt(2);

                int strengthX = 2*size;
                int strengthY = 2*size;
                int radius = PApplet.parseInt(baseSize + i*inc*(size/10.0f));

                myShapes[i].setRadius(strengthX, strengthY, radius);

                myShapes[i].rotateToX(90);
                myShapes[i].rotateToZ(angle1+speed*i*inc);
                myShapes[i].moveTo(pos);
                tint(0, 0, 100, 100);
                myShapes[i].draw();
            }

        popMatrix();

    }


}
class FontFamily {

    HashMap<String, String> fontFiles = new HashMap<String, String>();
    HashMap<String, PFont> generatedFonts = new HashMap<String, PFont>();

    FontFamily() {}

    public void addFile(String weight, String style, String file) {
        fontFiles.put(weight+"_"+style, file);
    }

    public String getFile(String weight, String style) {
        return fontFiles.get(weight+"_"+style);
    }

    public PFont getFont(String weight, String style, int size) {

        PFont font = generatedFonts.get(weight+"_"+style+"_"+size);

        if (font == null) {
            generatedFonts.put(weight+"_"+style+"_"+size, createFont(this.getFile(weight, style), size));
            font = generatedFonts.get(weight+"_"+style+"_"+size);
        }

        return font;
    }

}
class InteractionController {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    float scaleFactor = 1;
    int radius = width;
    float deg = 0;
    boolean trackingEnabled = false;
    boolean speedEnabled = false;
    boolean divideEnabled = false;
    boolean explosionEnabled = false;
    int frame = 0;

    Sound mySound;

    PVector leftHand = new PVector();
    PVector rightHand = new PVector();

    // Gestures
    PVector[] handHistoryRight = new PVector[20];
    PVector[] handHistoryLeft = new PVector[20];
    PVector rightVec, leftVec, frontVec, backVec;
    PVector thresholds = new PVector(height/10, width/10, 50);
    float explosionGestureMinDistance = 1.5f;
    int gestureThreshold = 5;
    int lastFramesConsidered = 5;

    Timer gestureRecTimer = new Timer();

    final static int NONE = 200;
    final static int SPEED = 201;
    final static int EXPLOSION = 202;
    final static int DIVIDE = 203;
    int lastGesture;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    InteractionController(Sound s) {

        mySound = s;

        // init for gestures
        for (int i = 0; i < handHistoryRight.length; i++) { handHistoryRight[i] = new PVector(); }
        for (int i = 0; i < handHistoryLeft.length; i++)  { handHistoryLeft[i] = new PVector(); }
        rightVec = new PVector((width/4)*3, height/4, 0);
        leftVec  = new PVector(width/4, height/4, 0);
        frontVec = new PVector(width/2, height/2, upperZBorder - thresholds.z);
        backVec  = new PVector(width/2, height/2, lowerZBorder + thresholds.z);

    }



    /*---------------------------------------------------------------
    Getter
    ----------------------------------------------------------------*/

    public float getPosX()                       { return rightHand.x; }
    public float getPosY()                       { return rightHand.y; }
    public float getPosZ()                       { return rightHand.z; }
    public float getScaleFactor()                { return scaleFactor; }
    public float getNormalizedPosZ()             { return rightHand.z; }
    public float getNormalizedPosZ(int a, int b) { return rightHand.z; }

    public PVector getPosition(int hand) {
        if      (hand == LEFT)  { return leftHand; }
        else if (hand == RIGHT) { return rightHand; }
        return null;
    }



    /*---------------------------------------------------------------
    Setter
    ----------------------------------------------------------------*/

    public void setPosX(float value) { rightHand.x = (int)value; }
    public void setPosY(float value) { rightHand.y = (int)value; }
    public void setPosZ(float value) { rightHand.z = (int)value; }
    public void setPosXY(float a, float b) { setPosX(a); setPosY(b); }
    public void setPosXYZ(float a, float b, float c) { setPosXY(a, b); setPosZ(c); }




    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void draw() {

        if (trackingEnabled) { highlightBorders(); }
        if (frameIsTracked) { updateHandHistory(); }

    }


    public void setup() { }





    public void enableTracking()  { trackingEnabled = true; }
    public void disableTracking() { trackingEnabled = false; }

    public void enableSpeedGesture()  { speedEnabled = true; }
    public void disableSpeedGesture() { speedEnabled = false; }

    public void enableDivideGesture()  { divideEnabled = true; }
    public void disableDivideGesture() { divideEnabled = false; }

    public void enableExplosionGesture()  { explosionEnabled = true; }
    public void disableExplosionGesture() { explosionEnabled = false; }

    public void enableAllGestures() {
        enableSpeedGesture();
        enableDivideGesture();
        enableExplosionGesture();
    }

    public void disableAllGestures() {
        disableSpeedGesture();
        disableDivideGesture();
        disableExplosionGesture();
    }


    public boolean isTracking() { return true; }



    public void highlightBorders() {

        float highlightPos = 0.2f;

        if (rightHand.x < width*highlightPos) {
            float distanceFromBorderPercentage = rightHand.x/width;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(0, 0, width*highlightPos, height, color(200, 60, 100, opacity), color(200, 60, 100, 0), false);
            mySound.playWallHit();
        }

        if (rightHand.x > width*(1-highlightPos)) {
            float distanceFromBorderPercentage = 1-rightHand.x/width;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(PApplet.parseInt(width*(1-highlightPos)), 0, width*highlightPos, height, color(200, 60, 100, 0), color(200, 60, 100, opacity), false);
            mySound.playWallHit();
        }

        if (rightHand.y < height*highlightPos) {
            float distanceFromBorderPercentage = rightHand.y/height;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(0, 0, width, height*highlightPos, color(200, 60, 100, opacity), color(200, 60, 100, 0), true);
            mySound.playWallHit();
        }

        if (rightHand.y > height*(1-highlightPos)) {
            float distanceFromBorderPercentage = 1-rightHand.y/height;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(0, PApplet.parseInt(height*(1-highlightPos)), width, height*highlightPos, color(200, 60, 100, 0), color(200, 60, 100, opacity), true);
            mySound.playWallHit();
        }

    }



    public int calcOpacity(float dist, float hP) {
        return (int)(((hP-dist)*100)*0.5f);
    }





    /*---------------------------------------------------------------
    Gestures
    ----------------------------------------------------------------*/


    public void updateHandHistory() {

        //updadate handHistoryRight
        for (int i = handHistoryRight.length-1; i > 0; i--) {
            handHistoryRight[i] = handHistoryRight[i-1];
            handHistoryRight[0] = interactionController.getPosition(RIGHT);
        }

        //update handHistoryLeft
        for (int i = handHistoryLeft.length-1; i > 0; i--) {
            handHistoryLeft[i] = handHistoryLeft[i-1];
            handHistoryLeft[0] = interactionController.getPosition(LEFT);
        }

    }


    public void swoosh() {
        //play swoosh if appropriate
        if (dist(handHistoryRight[0].x, handHistoryRight[0].y, handHistoryRight[0].z, handHistoryRight[handHistoryRight.length-1].x, handHistoryRight[handHistoryRight.length-1].y,  handHistoryRight[handHistoryRight.length-1].z) > mySound.swooshTreshold) {
            mySound.playSwoosh();
        }
    }


    public int getSpeedInt() { return SPEED; }
    public int getDivideInt() { return DIVIDE; }
    public int getExplosionInt() { return EXPLOSION; }

    public boolean gesturePerformedLast(int gest) {
        return (lastGesture == gest);
    }


    public boolean speedGestureTriggered() {
        if (gestureRecTimer.finished() && speedEnabled && detectSpeedGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = SPEED;
            return true;
        } else { return false; }
    }

    public boolean divideGestureTriggered() {
        if (gestureRecTimer.finished() && divideEnabled && detectDivideGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = DIVIDE;
            return true;
        } else { return false; }
    }

    public boolean explosionGestureTriggered() {
        if (gestureRecTimer.finished() && explosionEnabled && detectExplosionGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = EXPLOSION;
            return true;
        } else { return false; }
    }

    public boolean gestureReseted() {
        if (gestureRecTimer.finished() && (lastGesture != NONE)) {
            lastGesture = NONE;
            return true;
        } else { return false; }
    }


    public boolean detectSpeedGesture() {

        boolean left = false;
        boolean right = false;
        PVector aux = new PVector();

        // look at first frames considered in handHistoryRight
        for (int i = 0; i < lastFramesConsidered; i++) {
            //is there any vector close to starting x left ?
            if (abs(handHistoryRight[i].x - leftVec.x) < thresholds.y) {
                left = true;
                //set current starting vector of gesture
                aux.set(handHistoryRight[i]);
                break;
            }
            //is there any vector close to starting x right ?
            if (abs(handHistoryRight[i].x - rightVec.x) < thresholds.y) {
                right = true;
                //set current starting vector of gesture
                aux.set(handHistoryRight[i]);
                break;
            }
        }

        // look at last frames considered in handHistoryRight
        for (int i = handHistoryRight.length-1; i > handHistoryRight.length - 1 - lastFramesConsidered; i--) {
            if (left && abs(handHistoryRight[i].x - rightVec.x) < thresholds.y && (abs(handHistoryRight[i].y - aux.y) < thresholds.x)) {
                right = true;
                break;
            }
            if (right && (abs(handHistoryRight[i].x - leftVec.x) < thresholds.y) && (abs(handHistoryRight[i].y - aux.y) < thresholds.x)) {
                left = true;
                break;
            }
        }

        return left && right;

    }


    public boolean detectDivideGesture() {

        boolean rightInMid = false;
        boolean leftInMid = false;
        boolean rightIsRight = false;
        boolean leftIsLeft = false;
        PVector aux = new PVector();

        // look at first frames considered in both handHistories
        for (int i = handHistoryRight.length-1; i > handHistoryRight.length - 1 - lastFramesConsidered; i--) {
            if (abs(handHistoryRight[i].x - handHistoryLeft[i].x) < width/20) {
                if (abs(handHistoryRight[i].x - width/2) < thresholds.y) {
                    rightInMid = true;
                    leftInMid = true;
                    aux.set(handHistoryRight[i]);
                    break;
                }
            }
        }

        // look at last frames considered in both handHistories
        for (int i = 0; i < lastFramesConsidered; i++) {
            if (rightInMid && (abs(handHistoryRight[i].x - rightVec.x) < thresholds.y) && abs(handHistoryRight[i].y - aux.y) < thresholds.x) {
                rightIsRight = true;
            }
            if (leftInMid && (abs(handHistoryLeft[i].x - leftVec.x) < thresholds.y) && abs(handHistoryLeft[i].y - aux.y) < thresholds.x) {
                leftIsLeft = true;
            }
        }

        return rightInMid && leftInMid && rightIsRight && leftIsLeft;

    }


/**
    boolean detectExplosionGesture() {

        // back
        boolean back = false;
        for (int i = 0; i < lastFramesConsidered; i++) {
            if((abs(handHistoryRight[i].z - backVec.z) < thresholds.z)) {
                back = true;
                break;
            }
        }

        // front
        boolean front = false;
        for(int i = handHistoryRight.length-1; i > handHistoryRight.length -1 - lastFramesConsidered; i--) {
            if((abs(handHistoryRight[i].z - frontVec.z) < thresholds.z)) {
                front = true;
                break;
            }
        }

        return back && front;

    }

*/

    public boolean detectExplosionGesture() {

        for(int i = 0; i < lastFramesConsidered; i++) {
            for(int j = handHistoryRight.length-1; j > handHistoryRight.length -1 - lastFramesConsidered; j--) {
                //if dist between last frames and first frames is bigger than z-depth/2
                if((abs(handHistoryRight[i].z - handHistoryRight[j].z) > ((abs(upperZBorder) + abs(lowerZBorder))/explosionGestureMinDistance))) {
                    return true;
                }
            }
        }
        return false;
    }



}
class KinectController extends InteractionController {

    // create kinect object
    SimpleOpenNI kinect;
    // int of each user being tracked
    int[] kinectUsers;
    // user colors
    int[] userColors = new int[] { color(255,0,0), color(0,255,0), color(0,0,255), color(255,255,0), color(255,0,255), color(0,255,255) };

    int[] trackingArray = new int[20];
    int trackingArrayIterator = 0;

    int trackingCounter = 0;


    boolean isTrackingValue = false;


    public float getNormalizedPosZ(float z) {

        int upperDepthBorder = 2200;
        int lowerDepthBorder = 800;

        if (upperDepthBorder < z) { z = upperDepthBorder; }
        if (lowerDepthBorder > z) { z = lowerDepthBorder; }

        return (int)map(z, lowerDepthBorder, upperDepthBorder, lowerZBorder, upperZBorder);

    }





    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    KinectController(PApplet pa, Sound s) {

        super(s);

        // start a new kinect object
        kinect = new SimpleOpenNI(pa);

        // enable depth sensor
        kinect.enableDepth();

        // enable kinect webcam
        kinect.enableRGB();

        // mirror the kinects signal to make it work like a mirror
        kinect.setMirror(true);

        // enable skeleton generation for all jointVectors
        kinect.enableUser();

        // calculate scaleFactor
        scaleFactor = max(PApplet.parseFloat(width)/kinect.depthWidth(), PApplet.parseFloat(height)/kinect.depthHeight());

        println("scaleFactor: " + scaleFactor);
        println("depthWidth: " + kinect.depthWidth());
        println("depthHeight: " + kinect.depthHeight());
        println("width: " + pa.width);
        println("height: " + pa.height);

        for (int i=0; i<trackingArray.length; i++) {
          trackingArray[i] = 0;
        }

    }




    /*---------------------------------------------------------------
    Setup
    ----------------------------------------------------------------*/






    /*---------------------------------------------------------------
    Draw
    ----------------------------------------------------------------*/

    public void draw() {

        frame++;

        if (frame == 3) {
            frameIsTracked = true;
        } else {
            frameIsTracked = false;
        }

        if(frameIsTracked) {
            // update the camera
            kinect.update();

            if (trackingEnabled) {
                // check if the skeleton is being tracked for user 1 (the first user that detected)
                if (kinect.isTrackingSkeleton(1)) {
                    getCoordinates(1);
                }
            }

            frame = 0;
        }

        super.draw();

    }




    public boolean isTracking() {

        if(frameIsTracked) {

            if (kinect.isTrackingSkeleton(1)) {
                if (trackingCounter <= 40) { trackingCounter++; }
            } else {
                if (trackingCounter > 0) { trackingCounter--; }
            }

            if (trackingCounter > 20) { isTrackingValue = true; }
            else                      { isTrackingValue = false; }

        }

        return isTrackingValue;

    }


    /*boolean isTracking() {

        //boolean r = false;

        if(frameIsTracked) {

            println(trackingArray);

            int trueCounter = 0;

            if (kinect.isTrackingSkeleton(1)) {
                trackingArray[trackingArrayIterator] = 1;
            } else {
                trackingArray[trackingArrayIterator] = 0;
            }

            trueCounter = IntStream.of(trackingArray).sum();

            trackingArrayIterator = trackingArrayIterator+1;
            if (trackingArrayIterator == trackingArray.length) {
                trackingArrayIterator = 0;
            }

            if (trueCounter >= (int)(trackingArray.length/2)) { isTrackingValue = true; }
            else                                              { isTrackingValue = false; }

        }

        return isTrackingValue;


        //if (kinect.isTrackingSkeleton(1)) { r = true; }
        //return r;

    }*/



    /*for (int i=0; i<trackingArray.size(); i++) {
        boolean tracking = false
        if (trackingArray[i]) {

        }
    }*/


    /*---------------------------------------------------------------
    Gets XYZ coordinates of all jointVectors of tracked user and draws
    a small circle on each joint
    ----------------------------------------------------------------*/
    public void getCoordinates(int userID) {
        // right Hand
        rightHand = getKinectCoords(userID, SimpleOpenNI.SKEL_RIGHT_HAND);
        rightHand.x = rightHand.x * scaleFactor;
        rightHand.y = rightHand.y * scaleFactor;
        rightHand.z = getNormalizedPosZ(rightHand.z);
        //left Hand
        leftHand = getKinectCoords(userID, SimpleOpenNI.SKEL_LEFT_HAND);
        leftHand.x = leftHand.x * scaleFactor;
        leftHand.y = leftHand.y * scaleFactor;
        leftHand.z = getNormalizedPosZ(leftHand.z);
    }


    public PVector getKinectCoords(int userID, int skeleton) {
        PVector position3d = new PVector();
        PVector position2d = new PVector();
        kinect.getJointPositionSkeleton(userID, skeleton, position3d);
        kinect.convertRealWorldToProjective(position3d, position2d);
        return position2d;
    }






}
class MouseController extends InteractionController {

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    MouseController(PApplet pa, Sound s) {
        super(s);
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void draw() {

        frame++;

        if (frame == 3) {
            frameIsTracked = true;
        } else {
            frameIsTracked = false;
        }

        if(frameIsTracked) { frame = 0; }

        if (trackingEnabled) {

            rightHand.x = mouseX;
            rightHand.y = mouseY;

            if (keyPressed) {

                if (key == 'e'||key == 'E') {
                    rightHand.z -= 10;
                    if(rightHand.z < lowerZBorder) { rightHand.z = lowerZBorder; }
                }

                if (key == 'd'||key == 'D') {
                    rightHand.z += 10;
                    if (rightHand.z > upperZBorder) { rightHand.z = upperZBorder; }
                }
            }

            leftHand.x = width-mouseX;
            leftHand.y = height-mouseY;
            leftHand.z = rightHand.z;

        }

        super.draw();

    }

}
class Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    BoidList bl;
    InteractionController ctrl;
    ModeController modeCtrl;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/


    Mode() { }

    Mode(BoidList b) {
        bl = b;
    }

    Mode(BoidList b, InteractionController c) {
        bl = b;
        ctrl = c;
    }

    Mode(BoidList b, InteractionController c, ModeController m) {
        bl = b;
        ctrl = c;
        modeCtrl = m;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void setup() {}
    public void render() {}
    public void intro() {}

}
class ModeController {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Mode activeMode;
    int currentAppMode = 0;
    //String currentGameMode = "A";
    int currentGameMode = 0;
    //HashMap<String, GameMode> gameModes = new HashMap<String, GameMode>();
    HashMap<String, Mode> appModes = new HashMap<String, Mode>();
    ArrayList<GameMode> gameModes = new ArrayList<GameMode>();

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    ModeController() {
        //activeMode = "introMode";
    }


    public void addGameMode(GameMode g) {
        gameModes.add(g);
    }

    /*void addGameMode(String s, GameMode g) {
        gameModes.put(s, g);
    }*/

    public void addAppMode(String s, Mode g) {
        appModes.put(s, g);
    }

    /*GameMode getGameMode(String s) {
        return gameModes.get(s);
    }*/

    public GameMode getGameMode(int i) {
        return gameModes.get(i);
    }

    public Mode getAppMode(String s) {
        return appModes.get(s);
    }

    public Mode getActiveAppMode() {
        return activeMode;
    }

    public void setActiveAppMode(String s) {
        activeMode = getAppMode(s);
    }

    /*void setCurrentGameMode(String s) {
        currentGameMode = s;
        changeAppMode(currentAppMode);
    }*/

    public void setCurrentGameMode(int i) {
        currentGameMode = i;
        changeAppMode(currentAppMode);
    }

    public void nextAppMode() {
        if (currentAppMode < appModes.size()) { currentAppMode++; }
        changeAppMode(currentAppMode);
    }

    public void previousAppMode() {
        if (currentAppMode > 0) { currentAppMode--; }
        changeAppMode(currentAppMode);
    }


    public void nextGameMode() {
        if (currentGameMode < gameModes.size()-1) { currentGameMode++; }
        changeAppMode(currentAppMode);
    }

    public void previousGameMode() {
        if (currentGameMode > 0) { currentGameMode--; }
        changeAppMode(currentAppMode);
    }


    public void changeAppMode(int newMode) {

        if      (newMode == 0) { activeMode = getAppMode("introMode"); }
        else if (newMode == 1) { activeMode = getAppMode("trackingMode"); }
        else if (newMode == 2) { activeMode = getGameMode(currentGameMode); }
        else if (newMode == 3) { activeMode = getAppMode("finalMode"); }

        // intro function of activeMode
        activeMode.intro();

    }

}
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

    public void setup() {
        final_image = loadImage("final-mode.png");
    }

    public void render() {
        imageMode(CENTER);
        scaledImage(final_image, width/2, height/2, width);
    }

    public void intro() {
        ctrl.setPosXYZ(width/2, height/2, 0);
    }

}
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

    public void render() {

        // Got to Tracking Mode if Tracking lost
        if (!ctrl.isTracking()) {
            modeCtrl.previousAppMode();
        }

        ctrl.draw();

    }


    public void intro() {

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

    public void render() {

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


    public void intro() {

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

            PVector abc = new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 100);
            flowers.add(new FlowerToroid(abc, bl, PApplet.parseInt(random(2, 5)), random(0.00001f, 0.0005f), PApplet.parseInt(random(3, 30)), PApplet.parseInt(random(3, 30))));
            abc = new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 100);
            flowers.add(new FlowerToroid(abc, bl, PApplet.parseInt(random(2, 5)), random(0.00001f, 0.0005f), PApplet.parseInt(random(3, 30)), PApplet.parseInt(random(3, 30))));
            abc = new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 100);
            flowers.add(new FlowerToroid(abc, bl, PApplet.parseInt(random(2, 5)), random(0.00001f, 0.0005f), PApplet.parseInt(random(3, 30)), PApplet.parseInt(random(3, 30))));
            abc = new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 100);
            flowers.add(new FlowerToroid(abc, bl, PApplet.parseInt(random(2, 5)), random(0.00001f, 0.0005f), PApplet.parseInt(random(3, 30)), PApplet.parseInt(random(3, 30))));

            introduced = true;
        }


    }

}
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

    public void render() {

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
            b.letItDieInSec(PApplet.parseInt(random(50, 100)));
        }

        // use timer to jump into next gameMode after last flower collected
        if (!timer.started()) {
            if (flowers.size() == 0) { timer.setAndStart(5); }
        } else if (timer.finished()) { modeCtrl.nextGameMode(); }

        // go to final screen if all boids died
        if (bl.getBoidList().size() == 0) { modeCtrl.nextAppMode(); }

    }

    public void intro() {

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

    public void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        // set swarmCenter to fixed position
        ctrl.setPosXYZ(width/2, height/2, 0);

    }

    public void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("In the beginning, everything was one\u2026");

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

    public void render() {

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

    public void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("But everything got divided\u2026");

        // disable tracking
        ctrl.disableTracking();

        boidIterator = 0;

        // timer that moves to next step
        timer.set(15);
        timer.start();

        for (int i=1; i<bl.getBoidList().size(); i++) {
            Boid b = (Boid)bl.getBoidList().get(i);
            b.letItDieInSec(PApplet.parseInt(random(8, 15)));
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
class GameModeD extends TimeTriggeredGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Timer timer2 = new Timer();


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeD() { super(); }
    GameModeD(BoidList b) { super(b); }
    GameModeD(BoidList b, InteractionController c) { super(b, c); }
    GameModeD(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

        // display text
        centeredText(st.getCurrentText());

        if (timer2.finished()) {
            // update text
            st.setText("Raise your right hand\u2026\nDo you feel it?");
        }

    }

    public void intro() {

        // run the super intro
        super.intro();

        // set text to be displayed
        st.setText("Raise your right hand\u2026");

        // enable the tracking
        ctrl.enableTracking();

        // timer that moves to next step
        timer.set(18);
        timer.start();

        // timer that changes text
        timer2.set(6);
        timer2.start();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 20);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

    }

}
class GameModeE extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeE() { super(); }
    GameModeE(BoidList b) { super(b); }
    GameModeE(BoidList b, InteractionController c) { super(b, c); }
    GameModeE(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

    }

}
class GameModeF extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeF() { super(); }
    GameModeF(BoidList b) { super(b); }
    GameModeF(BoidList b, InteractionController c) { super(b, c); }
    GameModeF(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render(ctrl.getSpeedInt());

    }





    public void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-speed.png");

        // enable only the speed gesture
        ctrl.enableSpeedGesture();

    }

}
class GameModeG extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeG() { super(); }
    GameModeG(BoidList b) { super(b); }
    GameModeG(BoidList b, InteractionController c) { super(b, c); }
    GameModeG(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

        ctrl.enableSpeedGesture();

    }

}
class GameModeH extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeH() { super(); }
    GameModeH(BoidList b) { super(b); }
    GameModeH(BoidList b, InteractionController c) { super(b, c); }
    GameModeH(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render(ctrl.getDivideInt());

    }





    public void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-divide.png");

        // enable only the speed gesture
        ctrl.enableDivideGesture();

    }

}
class GameModeI extends CollectFlowerGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeI() { super(); }
    GameModeI(BoidList b) { super(b); }
    GameModeI(BoidList b, InteractionController c) { super(b, c); }
    GameModeI(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

        ctrl.enableSpeedGesture();
        ctrl.enableDivideGesture();

    }



}
class GameModeJ extends GestureLearningGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeJ() { super(); }
    GameModeJ(BoidList b) { super(b); }
    GameModeJ(BoidList b, InteractionController c) { super(b, c); }
    GameModeJ(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render(ctrl.getExplosionInt());

    }





    public void intro() {

        // run the super intro
        super.intro();

        modeImage = loadImage("gestures-explode.png");

        // enable only the speed gesture
        ctrl.enableExplosionGesture();

    }

}
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

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

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
            flowers.add(new FlowerA(new PVector(height*0.1f, height*0.1f), bl));
            flowers.add(new FlowerA(new PVector(width-height*0.1f, height*0.9f), bl));
            flowers.add(new FlowerA(new PVector(width-height*0.1f, height*0.1f), bl));
            flowers.add(new FlowerA(new PVector(height*0.1f, height*0.9f), bl));
            introduced = true;
        }

    }

}
class GameModeM extends FreePlayGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeM() { super(); }
    GameModeM(BoidList b) { super(b); }
    GameModeM(BoidList b, InteractionController c) { super(b, c); }
    GameModeM(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 14);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        myOldValues = json;

        // Add targets
        if (!introduced) {
            flowers.add(new FlowerA(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            flowers.add(new FlowerB(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            flowers.add(new FlowerC(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            introduced = true;
        }

    }

}
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

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

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
            flowers.add(new FlowerA(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            flowers.add(new FlowerB(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            introduced = true;
        }

    }

}
class GameModeP extends FreePlayGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeP() { super(); }
    GameModeP(BoidList b) { super(b); }
    GameModeP(BoidList b, InteractionController c) { super(b, c); }
    GameModeP(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 22);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        myOldValues = json;

        // Add targets
        if (!introduced) {
            flowers.add(new FlowerB(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            flowers.add(new FlowerC(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            introduced = true;
        }
        
    }

}
class GameModeQ extends FreePlayGameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GameModeQ() { super(); }
    GameModeQ(BoidList b) { super(b); }
    GameModeQ(BoidList b, InteractionController c) { super(b, c); }
    GameModeQ(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        // run the super render
        super.render();

    }

    public void intro() {

        // run the super intro
        super.intro();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 26);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

        myOldValues = json;

        // Add targets
        if (!introduced) {
            flowers.add(new FlowerA(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            flowers.add(new FlowerC(new PVector(PApplet.parseInt(random(width*0.2f, width*0.8f)), PApplet.parseInt(random(height*0.2f, height*0.8f)), 0), bl));
            introduced = true;
        }

    }

}
class GestureLearningGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Ani animationA, animationB;
    int animationA_Value, animationB_Value;

    AniSequence outroSeq;

    boolean runOutro = false;


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    GestureLearningGameMode() { super(); }
    GestureLearningGameMode(BoidList b) { super(b); }
    GestureLearningGameMode(BoidList b, InteractionController c) { super(b, c); }
    GestureLearningGameMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render(int gestureID) {

        // run the super render
        super.render();

        // modeImage
        tint(0, 0, 100, animationA_Value);
        scaledImage(modeImage, width/2, height/2, width);

        // well doe image
        tint(0, 0, 100, animationB_Value);
        scaledImage(well_done, width/2, height/2, width);

        //if (timer.startedAndFinished()) {
        if (ctrl.gesturePerformedLast(gestureID)) {
            if (animationA.isEnded() && animationA_Value == 0) {
                animationA.reverse();
                animationA.start();
                animationB.start();
            }
        }

        if (animationB.isEnded()) {
            if (animationB_Value == 0) {
                modeCtrl.nextGameMode();
            } else {
                animationB.reverse();
                animationB.start();
            }
        }


    }

    public void intro() {

        // run the super intro
        super.intro();

        //timer.setAndStart(5);

        animationA_Value = 0;
        animationA = new Ani(this, 2, "animationA_Value", 50);
        animationA.start();

        animationB_Value = 0;
        animationB = new Ani(this, 2, "animationB_Value", 50);
        animationB.setDelay(3);

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 50);
        json.setInt("maxSpeed", 10);
        json.setInt("alignmentScalar", 1);
        json.setInt("cohesionScalar", 3);
        json.setInt("seperationScalar", 1);
        json.setString("HSB", "0/0/100");
        bl.configAllBoids(json);

    }

}
class TimeTriggeredGameMode extends GameMode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    TimeTriggeredGameMode() {
        super();
    }

    TimeTriggeredGameMode(BoidList b) {
        super();
        bl = b;
    }

    TimeTriggeredGameMode(BoidList b, InteractionController c) {
        super();
        bl = b;
        ctrl = c;
    }

    TimeTriggeredGameMode(BoidList b, InteractionController c, ModeController m) {
        super();
        bl = b;
        ctrl = c;
        modeCtrl = m;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void render() {

        super.render();

        if (timer.finished()) {
            modeCtrl.nextGameMode();
        }

    }

    public void intro() {

        super.intro();

    }

}
class IntroMode extends Mode {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PImage intro_image, intro_typo, intro_corners;

    Timer timer = new Timer();
    Timer typoAnimationRunning = new Timer();
    Timer typoAnimationInterval = new Timer();

    boolean lastTypoAnimationRunningState = false;

    boolean typoFlickering;
    int typoOffsetX = 0;
    int typoOffsetY = 0;

    int intro_corners_opacity = 0;

    Ani animation;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    IntroMode() { super(); }
    IntroMode(BoidList b) { super(b); }
    IntroMode(BoidList b, InteractionController c) { super(b, c); }
    IntroMode(BoidList b, InteractionController c, ModeController m) { super(b, c, m); }


    /*---------------------------------------------------------------~
    Functions
    ----------------------------------------------------------------*/

    public void setup() {
        intro_image = loadImage("start-pattern.png");
        intro_typo = loadImage("start-type.png");
        intro_corners = loadImage("start-corners.png");

        // Animation
        animation = new Ani(this, 3, "intro_corners_opacity", 100);
        animation.repeat();
        animation.setPlayMode(Ani.YOYO);
    }



    public void render() {

        ctrl.draw();

        // images aligned central
        imageMode(CENTER);

        // show static background image
        scaledImage(intro_image, width/2, height/2, width);

        // draw corner image with opacity
        tint(0, 0, 100, intro_corners_opacity);
        scaledImage(intro_corners, width/2, height/2, width);
        tint(0, 0, 100, 100);

        // reset timers
        if (typoAnimationInterval.finished()) {
            typoAnimationRunning.set(PApplet.parseInt(random(1, 2)));
            typoAnimationRunning.start();
            typoAnimationInterval.set(PApplet.parseInt(random(5, 9)));
            typoAnimationInterval.start();
        }

        // is the animation currently active?
        if (!typoAnimationRunning.finished()) {

            // animation running

            mySound.playLightbulb();

            // randomize flickering
            if (PApplet.parseInt(random(0, 6)) != 0) { typoFlickering = true; }
            else                        { typoFlickering = false; }

            // randomize x-offset
            if (PApplet.parseInt(random(0, 8)) != 0) { typoOffsetX = PApplet.parseInt(random(-3, 3)); }
            else                        { typoOffsetX = 0; }

            // randomize y-offset
            if (PApplet.parseInt(random(0, 7)) != 0) { typoOffsetY = PApplet.parseInt(random(-2, 2)); }
            else                        { typoOffsetY = 0; }

            // display image with randomization
            if (typoFlickering) { scaledImage(intro_typo, width/2+typoOffsetX, height/2+typoOffsetY, width); }

        } else {
            // animation not running
            scaledImage(intro_typo, width/2, height/2, width);

        }

        // go to next state
        //if (timer.finished() || ctrl.isTracking()) { modeCtrl.nextAppMode(); }
    }

    public void intro() {

        timer.set(20);
        timer.start();

        typoAnimationRunning.set(PApplet.parseInt(random(1, 2)));
        typoAnimationRunning.start();
        typoAnimationInterval.set(PApplet.parseInt(random(5, 9)));
        typoAnimationInterval.start();

        JSONObject json = new JSONObject();
        json.setInt("neighborhoodRadius", 200);
        json.setInt("maxSpeed", 12);
        json.setInt("alignmentScalar", 100);
        json.setInt("cohesionScalar", 300);
        json.setInt("seperationScalar", 100);
        bl.configAllBoids(json);

        ctrl.setPosXYZ(width/2, height/2, -200);

    }


}
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

    public void setup() {
        tracking_image = loadImage("tracking-mode.png");
    }

    public void render() {

        ctrl.draw();

        // draw the shape
        scaledImage(tracking_image, width/2, height/2, width);

        // Go to TrackingMode if user is tracked
        if (ctrl.isTracking()) { modeCtrl.nextAppMode(); }

    }


    public void intro() {

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
class Sound {

    /*---------------------------------------------------------------
    Audio files
    ----------------------------------------------------------------*/

    SoundFile eat, swoosh, spawn, despawn, divide, revelation, speedUp, wallHit, explosion, lightbulb;
    SoundFile[] aArray, bArray;

    int aCounter = 600;
    int aCounterLimit = 600;
    float swooshTreshold = width/2;
    int lastTrackA;
    Timer wallHitTimer, lightbulbTimer;


    /*---------------------------------------------------------------
    Sound Triggers
    ----------------------------------------------------------------*/

    public void playSpawn()      { spawn.play(); }
    public void playDespawn()    { despawn.play(); }
    public void playSwoosh()     { swoosh.play(); }
    public void playEatMusic()   { eat.play(); }
    public void playDivide()     { divide.play(); }
    public void playRevelation() { revelation.play(); }
    public void playSpeedUp()    { speedUp.play(); }
    public void playExplosion()  { explosion.play(); }


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    public Sound(lowfi sketch) {
        soundSetup(sketch);
        wallHitTimer = new Timer(1);
        lightbulbTimer = new Timer(3);
    }

    public void soundSetup(lowfi sketch) {

        eat = new SoundFile(sketch, "eat.mp3");
        swoosh = new SoundFile(sketch, "swoosh.mp3");
        spawn = new SoundFile(sketch, "spawn.mp3");
        despawn = new SoundFile(sketch, "despawn.mp3");
        divide = new SoundFile(sketch, "divide.mp3");
        revelation = new SoundFile(sketch, "revelation.mp3");
        speedUp = new SoundFile(sketch, "speedUp.mp3");
        wallHit = new SoundFile(sketch, "wallHit.mp3");
        explosion = new SoundFile(sketch, "explosion.mp3");
        lightbulb = new SoundFile(sketch, "lightbulb.mp3");

        aArray = new SoundFile[7];
        aArray[0] = new SoundFile(sketch, "A_01.mp3");
        aArray[1] = new SoundFile(sketch, "A_02.mp3");
        aArray[2] = new SoundFile(sketch, "A_03.mp3");
        aArray[3] = new SoundFile(sketch, "A_04.mp3");
        aArray[4] = new SoundFile(sketch, "A_05.mp3");
        aArray[5] = new SoundFile(sketch, "A_06.mp3");
        aArray[6] = new SoundFile(sketch, "A_07.mp3");

        bArray = new SoundFile[10];
        bArray[0] = new SoundFile(sketch, "B_01.mp3");
        bArray[1] = new SoundFile(sketch, "B_02.mp3");
        bArray[2] = new SoundFile(sketch, "B_03.mp3");
        bArray[3] = new SoundFile(sketch, "B_04.mp3");
        bArray[4] = new SoundFile(sketch, "B_05.mp3");
        bArray[5] = new SoundFile(sketch, "B_06.mp3");
        bArray[6] = new SoundFile(sketch, "B_07.mp3");
        bArray[7] = new SoundFile(sketch, "B_08.mp3");
        bArray[8] = new SoundFile(sketch, "B_09.mp3");
        bArray[9] = new SoundFile(sketch, "B_10.mp3");

    }

    public void playMusicDependingOnGameState() {
        playA();
    }


    public void playA() {
        aCounter++;
        if (aCounter > aCounterLimit) {
            aCounter = 0;
            int track = (int)random(0, 6.4f);
            //must not play same file in a row
            while (track == lastTrackA) {
                track = (int)random(0, 6.4f);
            }
            lastTrackA = track;
            aArray[track].play();
        }
    }

    public void playRandomB() {
        int track = (int)random(0, 9.4f);
        bArray[track].play();
    }


    public void playTurningMusic() {
        int track = (int)random(0, 6.4f);
        bArray[track].play();
    }



    public void playLightbulb() {
        if (lightbulbTimer.finished()) {
            lightbulb.play();
            lightbulbTimer.set(3);
            lightbulbTimer.start();
        }
    }

    public void playWallHit() {
        if (wallHitTimer.finished()) {
            wallHit.play();
            wallHitTimer.set(1);
            wallHitTimer.start();
        }
    }

}
class StagedText {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    String finalText;
    int stepCounter = 0;
    int interval = 1;
    int counter = 0;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    StagedText() {}
    StagedText(String v) { finalText = v; }
    StagedText(int v) { interval = v; }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void setInterval(int v) {
        interval = v;
    }

    public void setText(String s) {
        finalText = s;
        stepCounter = min(stepCounter, finalText.length());
    }

    public String getCurrentText() {
        if (counter%interval == 1) {
            if (stepCounter < finalText.length()) { stepCounter++; }
        }
        counter++;
        return finalText.substring(0, stepCounter);
    }

}
class Timer {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    int endtime, duration;
    boolean started = false;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    Timer() { }
    Timer(float d) { set(d); }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    public void start() {
        started = true;
        endtime = millis() + duration;
    }

    public boolean started() {
        return started;
    }

    public boolean startedAndFinished() {
        return (started() && finished());
    }

    public void set(float d) {
        duration = PApplet.parseInt(d*1000);
    }

    public void setAndStart(float d) {
        set(d);
        start();
    }

    public boolean finished() {
        boolean r = false;
        if (endtime <= millis()) { r = true; }
        return r;
    }

    public float progress() {
        return map(millis(), endtime-duration, endtime, 0, 1);
    }

}
public void scaledShape(PShape shp, float x, float y, float desiredWidth) {
    x = (int)x;
    y = (int)y;
    desiredWidth = (int)desiredWidth;
    float scaleFactor = (desiredWidth / shp.width);
    shapeMode(CENTER);
    shape(shp, x, y, shp.width*scaleFactor, shp.height*scaleFactor);
}


public void scaledImage(PImage shp, float x, float y, float desiredWidth) {

    x = (int)x;
    y = (int)y;
    desiredWidth = (int)desiredWidth;

    if (desiredWidth == shp.width) {
        image(shp, x, y);
    } else {
        float scaleFactor = (desiredWidth / shp.width);
        imageMode(CENTER);
        image(shp, x, y, shp.width*scaleFactor, shp.height*scaleFactor);
    }

}




public int responsiveInt(int i) {
    return max(1, PApplet.parseInt(i*screenResolutionFactor));
}



public void centeredText(String t) {
    textAlign(LEFT, CENTER);
    textFont(blenderPro.getFont("bold", "normal", responsiveInt(32)));
    fill(0, 0, 80, 100);
    rectMode(CENTER);
    textLeading(responsiveInt(36));
    text(t, width/2, 8*(height/9), width*0.8f, height/4.5f);  // Text wraps within text box
}



// Returns the x-coord of a point on a circle by given degree and radius
public float getCirclePointX(float d, int r) { return (int)(r *  cos(radians(d))); }
// Returns the y-coord of a point on a circle by given degree and radius
public float getCirclePointY(float d, int r) { return (int)(r *  sin(radians(d))); }




public void gradientRect(int x, int y, float w, float h, int c1, int c2, boolean vertical) {

    noFill();

    if (vertical) {  // Top to bottom gradient
        for (int i = y; i <= y+h; i++) {
            float inter = map(i, y, y+h, 0, 1);
            int c = lerpColor(c1, c2, inter);
            stroke(c);
            line(x, i, x+w, i);
        }
    }
    else {  // Left to right gradient
        for (int i = x; i <= x+w; i++) {
            float inter = map(i, x, x+w, 0, 1);
            int c = lerpColor(c1, c2, inter);
            stroke(c);
            line(i, y, i, y+h);
        }
    }

}








/*---------------------------------------------------------------
Keyboard Events
----------------------------------------------------------------*/

public void keyPressed() {

    if (keyPressed) {

        // controll App modes
        if (key == 'n'||key == 'N') { modeController.nextAppMode(); }
        if (key == 'b'||key == 'B') { modeController.previousAppMode(); }

        // control game modes
        if (key == '1') { modeController.previousGameMode(); }
        if (key == '2') { modeController.nextGameMode(); }

    }

}






/*---------------------------------------------------------------
Kinect Events
----------------------------------------------------------------*/


/*---------------------------------------------------------------
When a new user is found, print new user detected along with
userID and start pose detection. Input is userID
----------------------------------------------------------------*/
public void onNewUser(SimpleOpenNI curContext, int userId) {
    // print me a status
    println("New User Detected - userId: " + userId);
    // start tracking of user id
    curContext.startTrackingSkeleton(userId);
}


/*---------------------------------------------------------------
Print when user is lost. Input is int userId of user lost
----------------------------------------------------------------*/
public void onLostUser(SimpleOpenNI curContext, int userId) {
    // print me a status
    println("User Lost - userId: " + userId);
}

/*---------------------------------------------------------------
Called when a user is tracked.
----------------------------------------------------------------*/
public void onVisibleUser(SimpleOpenNI curContext, int userId) {}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lowfi" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
