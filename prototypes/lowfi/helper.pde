void scaledShape(PShape shp, float x, float y, float desiredWidth) {
    x = (int)x;
    y = (int)y;
    desiredWidth = (int)desiredWidth;
    float scaleFactor = (desiredWidth / shp.width);
    shapeMode(CENTER);
    shape(shp, x, y, shp.width*scaleFactor, shp.height*scaleFactor);
}


void scaledImage(PImage shp, float x, float y, float desiredWidth) {

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




int responsiveInt(int i) {
    return max(1, int(i*screenResolutionFactor));
}



void centeredText(String t) {
    textAlign(LEFT, CENTER);
    textFont(blenderPro.getFont("bold", "normal", responsiveInt(32)));
    fill(0, 0, 80, 100);
    rectMode(CENTER);
    textLeading(responsiveInt(36));
    text(t, width/2, 8*(height/9), width*0.8, height/4.5);  // Text wraps within text box
}



// Returns the x-coord of a point on a circle by given degree and radius
float getCirclePointX(float d, int r) { return (int)(r *  cos(radians(d))); }
// Returns the y-coord of a point on a circle by given degree and radius
float getCirclePointY(float d, int r) { return (int)(r *  sin(radians(d))); }




void gradientRect(int x, int y, float w, float h, color c1, color c2, boolean vertical) {

    noFill();

    if (vertical) {  // Top to bottom gradient
        for (int i = y; i <= y+h; i++) {
            float inter = map(i, y, y+h, 0, 1);
            color c = lerpColor(c1, c2, inter);
            stroke(c);
            line(x, i, x+w, i);
        }
    }
    else {  // Left to right gradient
        for (int i = x; i <= x+w; i++) {
            float inter = map(i, x, x+w, 0, 1);
            color c = lerpColor(c1, c2, inter);
            stroke(c);
            line(i, y, i, y+h);
        }
    }

}








/*---------------------------------------------------------------
Keyboard Events
----------------------------------------------------------------*/

void keyPressed() {

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
void onNewUser(SimpleOpenNI curContext, int userId) {
    // print me a status
    println("New User Detected - userId: " + userId);
    // start tracking of user id
    curContext.startTrackingSkeleton(userId);
}


/*---------------------------------------------------------------
Print when user is lost. Input is int userId of user lost
----------------------------------------------------------------*/
void onLostUser(SimpleOpenNI curContext, int userId) {
    // print me a status
    println("User Lost - userId: " + userId);
}

/*---------------------------------------------------------------
Called when a user is tracked.
----------------------------------------------------------------*/
void onVisibleUser(SimpleOpenNI curContext, int userId) {}
