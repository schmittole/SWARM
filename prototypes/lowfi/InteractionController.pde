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
    float explosionGestureMinDistance = 1.5;
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

    float getPosX()                       { return rightHand.x; }
    float getPosY()                       { return rightHand.y; }
    float getPosZ()                       { return rightHand.z; }
    float getScaleFactor()                { return scaleFactor; }
    float getNormalizedPosZ()             { return rightHand.z; }
    float getNormalizedPosZ(int a, int b) { return rightHand.z; }

    PVector getPosition(int hand) {
        if      (hand == LEFT)  { return leftHand; }
        else if (hand == RIGHT) { return rightHand; }
        return null;
    }



    /*---------------------------------------------------------------
    Setter
    ----------------------------------------------------------------*/

    void setPosX(float value) { rightHand.x = (int)value; }
    void setPosY(float value) { rightHand.y = (int)value; }
    void setPosZ(float value) { rightHand.z = (int)value; }
    void setPosXY(float a, float b) { setPosX(a); setPosY(b); }
    void setPosXYZ(float a, float b, float c) { setPosXY(a, b); setPosZ(c); }




    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void draw() {

        if (trackingEnabled) { highlightBorders(); }
        if (frameIsTracked) { updateHandHistory(); }

    }


    void setup() { }





    void enableTracking()  { trackingEnabled = true; }
    void disableTracking() { trackingEnabled = false; }

    void enableSpeedGesture()  { speedEnabled = true; }
    void disableSpeedGesture() { speedEnabled = false; }

    void enableDivideGesture()  { divideEnabled = true; }
    void disableDivideGesture() { divideEnabled = false; }

    void enableExplosionGesture()  { explosionEnabled = true; }
    void disableExplosionGesture() { explosionEnabled = false; }

    void enableAllGestures() {
        enableSpeedGesture();
        enableDivideGesture();
        enableExplosionGesture();
    }

    void disableAllGestures() {
        disableSpeedGesture();
        disableDivideGesture();
        disableExplosionGesture();
    }


    boolean isTracking() { return true; }



    void highlightBorders() {

        float highlightPos = 0.2;

        if (rightHand.x < width*highlightPos) {
            float distanceFromBorderPercentage = rightHand.x/width;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(0, 0, width*highlightPos, height, color(200, 60, 100, opacity), color(200, 60, 100, 0), false);
            mySound.playWallHit();
        }

        if (rightHand.x > width*(1-highlightPos)) {
            float distanceFromBorderPercentage = 1-rightHand.x/width;
            int opacity = calcOpacity(distanceFromBorderPercentage, highlightPos);
            gradientRect(int(width*(1-highlightPos)), 0, width*highlightPos, height, color(200, 60, 100, 0), color(200, 60, 100, opacity), false);
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
            gradientRect(0, int(height*(1-highlightPos)), width, height*highlightPos, color(200, 60, 100, 0), color(200, 60, 100, opacity), true);
            mySound.playWallHit();
        }

    }



    int calcOpacity(float dist, float hP) {
        return (int)(((hP-dist)*100)*0.5);
    }





    /*---------------------------------------------------------------
    Gestures
    ----------------------------------------------------------------*/


    void updateHandHistory() {

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


    void swoosh() {
        //play swoosh if appropriate
        if (dist(handHistoryRight[0].x, handHistoryRight[0].y, handHistoryRight[0].z, handHistoryRight[handHistoryRight.length-1].x, handHistoryRight[handHistoryRight.length-1].y,  handHistoryRight[handHistoryRight.length-1].z) > mySound.swooshTreshold) {
            mySound.playSwoosh();
        }
    }


    int getSpeedInt() { return SPEED; }
    int getDivideInt() { return DIVIDE; }
    int getExplosionInt() { return EXPLOSION; }

    boolean gesturePerformedLast(int gest) {
        return (lastGesture == gest);
    }


    boolean speedGestureTriggered() {
        if (gestureRecTimer.finished() && speedEnabled && detectSpeedGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = SPEED;
            return true;
        } else { return false; }
    }

    boolean divideGestureTriggered() {
        if (gestureRecTimer.finished() && divideEnabled && detectDivideGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = DIVIDE;
            return true;
        } else { return false; }
    }

    boolean explosionGestureTriggered() {
        if (gestureRecTimer.finished() && explosionEnabled && detectExplosionGesture()) {
            gestureRecTimer.setAndStart(10);
            lastGesture = EXPLOSION;
            return true;
        } else { return false; }
    }

    boolean gestureReseted() {
        if (gestureRecTimer.finished() && (lastGesture != NONE)) {
            lastGesture = NONE;
            return true;
        } else { return false; }
    }


    boolean detectSpeedGesture() {

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


    boolean detectDivideGesture() {

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

    boolean detectExplosionGesture() {

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
