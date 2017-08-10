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

    void draw() {

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
