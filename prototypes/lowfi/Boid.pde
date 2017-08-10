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
    color hStart = color(0, 0, 100);
    color hFinish = color(colorH, colorS, colorB);
    color hHighlight = color(colorH, colorS, colorB);
    color h;
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

    void setNeighborhoodRadius(int value)   { neighborhoodRadius = value; }
    void setMaxSpeed(int value)             { maxSpeed = value; }
    void setMaxSteerForce(int value)        { maxSteerForce = value; }
    void setColorH(int value)               { colorH = value; hFinish = color(colorH, colorS, colorB); }
    void setColorS(int value)               { colorS = value; hFinish = color(colorH, colorS, colorB); }
    void setColorB(int value)               { colorB = value; hFinish = color(colorH, colorS, colorB); }
    void setMass(float value)               { mass = value; }
    void setScale(float value)              { scale = value; }
    void setAvoidWalls(boolean value)       { avoidWalls = value; }
    void setMyFlock(BoidList value)         { myFlock = value; }
    void setAlignmentScalar(int value)      { alignmentScalar = value; }
    void setCohesionScalar(int value)       { cohesionScalar = value; }
    void setSeperationScalar(int value)     { seperationScalar = value; }
    void resetColorStep()                   { colorStep = 0; }
    void setHandToFollow(int hand)          { handToFollow = hand; }
    void setLifeDuration(int v)             { lifeDuration = v; }
    void setDyingThreshold(int v)           { dyingThreshold = v; lifeFactorDecrement = 1/dyingThreshold; }

    void setHSB(String hsb) {
        String[] hsbVals = hsb.split("/");
        setColorH(Integer.parseInt(hsbVals[0]));
        setColorS(Integer.parseInt(hsbVals[1]));
        setColorB(Integer.parseInt(hsbVals[2]));
        colorStep = 0;
    }

    void setHighlightHSB(String hsb) {
        String[] hsbVals = hsb.split("/");
        hHighlight = color(Integer.parseInt(hsbVals[0]), Integer.parseInt(hsbVals[1]), Integer.parseInt(hsbVals[2]));
        highlightColorMode = true;
        colorStep = 0;
        colorTimer1.setAndStart(1.5);
    }

    void letItDieInSec(int sec) {
        if (!letItDie) {
            setLifeDuration(sec*60);
            setDyingThreshold(sec*60);
            letItDie = true;
        }
    }




    /*---------------------------------------------------------------
    GETTER
    ----------------------------------------------------------------*/

    int getNeighborhoodRadius()   { return neighborhoodRadius; }
    int getMaxSpeed()             { return maxSpeed; }
    int getMaxSteerForce()        { return maxSteerForce; }
    int getColorH()               { return colorH; }
    int getColorS()               { return colorS; }
    int getColorB()               { return colorB; }
    float getMass()               { return mass; }
    float getScale()              { return scale; }
    boolean getAvoidWalls()       { return avoidWalls; }
    BoidList getMyFlock()         { return myFlock; }
    int getAlignmentScalar()      { return alignmentScalar; }
    int getCohesionScalar()       { return cohesionScalar; }
    int getSeperationScalar()     { return seperationScalar; }
    int getHandToFollow()         { return handToFollow; }
    color getHSB()                { return hFinish; }
    float getX()                  { return pos.x; }
    float getY()                  { return pos.y; }
    float getZ()                  { return pos.z; }
    PVector getPos()              { return pos; }




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



    int interpolate(color oldColor, color newColor, float incre) {

        if (colorStep<1) {
            colorStep += incre;
            return lerpColor(oldColor, newColor, colorStep);
        } else {
            hStart = newColor;
            return newColor;
        }

    }


    int interpolateHighlightColor(color oldColor, color newColor) {

        float progress = colorTimer1.progress();
        float progressBorder = 0.1;

        if (progress <= progressBorder) {
            return lerpColor(oldColor, newColor, map(progress, 0, progressBorder, 0, 1));
        } else if (progress < 1) {
            return lerpColor(oldColor, newColor, map(progress, progressBorder, 1.0, 1, 0));
        } else {
            highlightColorMode = false;
            return oldColor;
        }

    }



    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/


    void run (ArrayList bl) {

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
            h = interpolate(hStart, hFinish, 0.005);
        }

        //h = interpolate(hStart, hFinish, 0.005);

        activeSpeed = maxSpeed * ((lifeFactor/2) + 0.5);

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

    void flock(ArrayList bl) {

        acc.add(seek(interactionController.getPosition(handToFollow)));

        ali = alignment(bl);
        coh = cohesion(bl);
        sep = seperation(bl);
        acc.add(PVector.mult(ali, alignmentScalar));
        acc.add(PVector.mult(coh, cohesionScalar));
        acc.add(PVector.mult(sep, seperationScalar));

    }


    // move
    void move() {
        vel.add(acc);        //add acceleration to velocity
        vel.limit(activeSpeed); //make sure the velocity vector magnitude does not exceed maxSpeed
        pos.add(vel);        //add velocity to position
        acc.mult(0);         //reset acceleration
    }



    // has the boid died?
    boolean died() {
        if (lifeDuration <= 0) { return true; }
        else { return false; }
    }


    // render
    void render() {}




    /*---------------------------------------------------------------
    HELPER
    ----------------------------------------------------------------*/


    // cohesion
    PVector cohesion(ArrayList boids) {
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
    PVector steer(PVector target,boolean arrival) {

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
    PVector seek(PVector target) {
        PVector desired = PVector.sub(target, pos);
        desired.normalize();
        desired.mult(activeSpeed);
        PVector steer = PVector.sub(desired, vel);
        steer.limit(0.8);
        //applyForce(steer);
        return steer;
    }


    //avoid. If weight == true avoidance vector is larger the closer the boid is to the target
    PVector avoid(PVector target, boolean weight) {
        PVector steer = new PVector(); //creates vector for steering
        steer.set(PVector.sub(pos,target)); //steering vector points away from target
        if (weight) { steer.mult(1/sq(PVector.dist(pos,target))); }
        //steer.limit(maxSteerForce); //limits the steering force to maxSteerForce
        return steer;
    }


    // separation
    PVector seperation(ArrayList boids) {
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
    PVector alignment(ArrayList boids) {
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