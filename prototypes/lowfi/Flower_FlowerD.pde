class FlowerD extends Flower {


    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerD(PVector p) { super(p); amount = 5;}
    FlowerD(PVector p, BoidList bl) { super(p, bl); amount = 5;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

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
                vertex(-mySize/2, mySize*0.1);
                vertex(0, -mySize*1.5);
                vertex(mySize/2, mySize*0.1);
                endShape();
            }

        popMatrix();

    }


}
