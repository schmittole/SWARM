class TriangleBoid extends Boid {



    /*---------------------------------------------------------------
    Overwrite Fields
    ----------------------------------------------------------------*/
    void setParentVars() {
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
    void render() {

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
