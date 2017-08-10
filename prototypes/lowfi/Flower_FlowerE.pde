class FlowerE extends Flower {


    float radiusSwitch;
    float radius = 10;

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    FlowerE(PVector p) { super(p); amount = 35;}
    FlowerE(PVector p, BoidList bl) { super(p, bl); amount = 35;}


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        super.render();
        pushMatrix();
        rotateZ(angle1);
        stroke(167, 36, 90, faderOpacity);
        strokeWeight(2);
        noFill();

        speed =+ speed + vel;

        float iteration = TWO_PI / amount;

        for (angle1 = 0; angle1 < TWO_PI; angle1+= iteration) {

          radius += radius + 0.1;

          radiusSwitch = angle1 * sizeC;

          pushMatrix();
          translate(pos.x, pos.y);
          rotateY(angle1 + speed);
          ellipse(0,0,sizeC + radiusSwitch ,sizeC + radiusSwitch);
          popMatrix();

        }
        popMatrix();

    }

    void detect(PVector swarmCenter) {
        if (!flock1.isDivided()) { collected = false; }
        else { super.detect(swarmCenter); }
    }


}
