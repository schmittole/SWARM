class FlowerB extends Flower {




    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerB(PVector p) { super(p); amount = 7;}
    FlowerB(PVector p, BoidList bl) { super(p, bl); amount = 7;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

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
