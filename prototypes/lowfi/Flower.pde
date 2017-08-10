class Flower {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    PVector pos;

    float angle1;
    float angle2;

    float angleForLevitate = random(0, TWO_PI);

    float angle1Inc = 0.007;
    float angle2Inc = 0.06;

    float angleForLevitateInc = random(0.02, 0.06);

    float scalar = 100;

    boolean collected = false;

    float collectDis = width*0.05;

    int sizeA = 5;
    int sizeB = 30;
    int sizeC = 15;

    BoidList boidList;

    int amount;

    float speed = 0.1;
    float vel = 0.02;

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

    void setPos(PVector p) { pos.set(p); }
    PVector getPos() { return pos; }
    boolean isCollected() { return collected; }



    void render() {

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



    void detect(PVector swarmCenter) {
        if (dist(pos.x, pos.y, pos.z, swarmCenter.x, swarmCenter.y, swarmCenter.z) < collectDis) {
            hitDetected();
        } else {
            collected = false;
        }
    }


    void hitDetected() {
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
