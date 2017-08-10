class FlowerC extends Flower {



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerC(PVector p) { super(p); amount = 5;}
    FlowerC(PVector p, BoidList bl) { super(p, bl); amount = 5;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

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
