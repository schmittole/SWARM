class FlowerToroid extends Flower {

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    Toroid[] myShapes;

    FlowerToroid(lowfi s, PVector p) { super(p); amount = 5; }
    FlowerToroid(lowfi s, PVector p, BoidList bl) { super(p, bl); amount = 5; }
    FlowerToroid(lowfi s, PVector p, BoidList bl, int amount) {
        super(p, bl);
        this.amount = amount;
        myShapes = new Toroid[amount];
        for(int i=0; i<amount; i++) {
            myShapes[i] = new Toroid(s, 30, 30);
            myShapes[i].setTexture("texture01.jpg", 6, 1);
            myShapes[i].drawMode(S3D.TEXTURE);
        }
        vel = 0.0001;
    }

    FlowerToroid(PVector p, BoidList bl, int amount, float vel, int aSolution, int bSolution) {
        super(p, bl);
        this.amount = amount;
        myShapes = new Toroid[amount];
        for(int i=0; i<amount; i++) {
            myShapes[i] = new Toroid(main, aSolution, bSolution);
            myShapes[i].setTexture("texture01.jpg", 6, 1);
            myShapes[i].drawMode(S3D.TEXTURE);
        }
        this.vel = vel;
    }


    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void render() {

        super.render();

        pushMatrix();

            float inc = 360/amount;

            for(int i = 0; i < myShapes.length; i++) {
                speed = speed + vel;
                if(speed >= 1) {
                    speed = 0;
                }

                int baseSize = 1;
                int size = responsiveInt(2);

                int strengthX = 2*size;
                int strengthY = 2*size;
                int radius = int(baseSize + i*inc*(size/10.0));

                myShapes[i].setRadius(strengthX, strengthY, radius);

                myShapes[i].rotateToX(90);
                myShapes[i].rotateToZ(angle1+speed*i*inc);
                myShapes[i].moveTo(pos);
                tint(0, 0, 100, 100);
                myShapes[i].draw();
            }

        popMatrix();

    }


}
