class KinectController extends InteractionController {

    // create kinect object
    SimpleOpenNI kinect;
    // int of each user being tracked
    int[] kinectUsers;
    // user colors
    color[] userColors = new color[] { color(255,0,0), color(0,255,0), color(0,0,255), color(255,255,0), color(255,0,255), color(0,255,255) };

    int[] trackingArray = new int[20];
    int trackingArrayIterator = 0;

    int trackingCounter = 0;


    boolean isTrackingValue = false;


    float getNormalizedPosZ(float z) {

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
        scaleFactor = max(float(width)/kinect.depthWidth(), float(height)/kinect.depthHeight());

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

    void draw() {

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




    boolean isTracking() {

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
    void getCoordinates(int userID) {
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


    PVector getKinectCoords(int userID, int skeleton) {
        PVector position3d = new PVector();
        PVector position2d = new PVector();
        kinect.getJointPositionSkeleton(userID, skeleton, position3d);
        kinect.convertRealWorldToProjective(position3d, position2d);
        return position2d;
    }






}
