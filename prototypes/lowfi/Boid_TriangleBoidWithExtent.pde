class TriangleBoidWithExtent extends TriangleBoid {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Extent extent;

    float radiusExt = 20;
    float sizeExt = 50;
    color cExt = color(255,0,255);
    int amountExt = 1;
    float velExt = 0.5;


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

    TriangleBoidWithExtent(PVector inPos) {
        super(inPos);
        // init guide
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        setParentVars();
    }

    TriangleBoidWithExtent(PVector inPos, BoidList myFlock) {
        super(inPos);
        // init extent
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        setParentVars();
        super.setMyFlock(myFlock);
    }

    TriangleBoidWithExtent(PVector inPos, PVector inVel, float r) {
        super(inPos, inVel, r);
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        // init extent
        setParentVars();
    }

    TriangleBoidWithExtent(PVector inPos, PVector inVel, float r, BoidList myFlock) {
        super(inPos, inVel, r);
        extent = new Extent(inPos,radiusExt, sizeExt, amountExt, velExt, cExt);
        // init extent
        setParentVars();
    }




    /*---------------------------------------------------------------
    Overwrite Functions
    ----------------------------------------------------------------*/

    void run (ArrayList bl) {

        if (avoidWalls) {
            acc.add(PVector.mult(avoid(new PVector( pos.x, height, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,      0, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( width,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector(     0,  pos.y, pos.z), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   300), true), 5));
            acc.add(PVector.mult(avoid(new PVector( pos.x,  pos.y,   900), true), 5));
        }

        flock(bl);
        move();
        render();

        extent.render(pos);

    }


}
