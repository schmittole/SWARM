/*BoidList object class
* Matt Wetmore
* Changelog
* ---------
* 12/18/09: Started work
*/

class BoidList {



    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    ArrayList boids; //will hold the boids in this BoidList
    int colorH; //for Hue
    int colorS; //for Saturation
    int colorB; //for Brightness
    color h;
    boolean isDivided = false;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    BoidList() {
        boids = new ArrayList();
    }

    BoidList(int n, int ih) {
        boids = new ArrayList();
        h = ih; 
        for (int i=0; i<n; i++) {
            boids.add(new TriangleBoid(new PVector(width/2, height/2, 0)));
        }
    }



    /*---------------------------------------------------------------
    Funcionality
    ----------------------------------------------------------------*/

    PVector getCenterOfSwarm() {
        float x = 0;
        float y = 0;
        float z = 0;
        for (Object o : boids) {
            Boid b = (Boid)o;
            x += b.getX();
            y += b.getY();
            z += b.getZ();
        }
        int size = boids.size();
        return new PVector(x/size, y/size, z/size);
    }



    /*---------------------------------------------------------------
    Add Boids
    ----------------------------------------------------------------*/

    // Add one boid
    void add() { boids.add(new TriangleBoid(new PVector(width/2, height/2, 0))); }
    void add(PVector pos) { boids.add(new TriangleBoid(pos)); }

    // add a given number of boids
    void addBoids(int num) { for (int i=0; i<num; i++) { add(); } }
    void addBoidsCurrentPos(int num) { for (int i=0; i<num; i++) { add(getCenterOfSwarm()); } }

    // add a specific boid
    void addBoid(Boid b) { boids.add(b); }




    /*---------------------------------------------------------------
    Run
    ----------------------------------------------------------------*/

    void run(boolean aW) {

        for (int i=0; i<boids.size(); i++) {
            //iterate through the list of boids
            Boid tempBoid = (Boid)boids.get(i); //create a temporary boid to process and make it the current boid in the list
            tempBoid.run(boids); //tell the temporary boid to execute its run method
        }

    }



    /*---------------------------------------------------------------
    Behaviour
    ----------------------------------------------------------------*/

    // divide swarm
    void divide() {
        isDivided = true;
        for (int i=0; i<(int)(boids.size()/2); i++) {
            Boid b = (Boid)boids.get(i);
            b.setHandToFollow(LEFT);
        }
    }

    // reunion swarm
    void reunion(int hand) {
        isDivided = false;
        for (int i=0; i<(int)(boids.size()); i++) {
            Boid b = (Boid)boids.get(i);
            b.setHandToFollow(hand);
        }
    }



    /*---------------------------------------------------------------
    Getter
    ----------------------------------------------------------------*/

    // returns a random boid from the collection
    Boid getRandomBoid() {
        return (Boid)boids.get(int(random(0, boids.size())));
    }

    // returns the boid list
    ArrayList<Boid> getBoidList() { return boids; }

    boolean isDivided() { return isDivided; }



    /*---------------------------------------------------------------
    Config
    ----------------------------------------------------------------*/

    // Config all boids in the list with a json
    void configAllBoids(JSONObject config) {
        for (int i=0; i<boids.size(); i++) { configBoid(i, config); }
    }


    // Config a boid with a json
    void configBoid(int i, JSONObject config) {
        if (i < boids.size()) {
            Boid b = (Boid)boids.get(i);
            if (!config.isNull("neighborhoodRadius")) { b.setNeighborhoodRadius(config.getInt("neighborhoodRadius")); }
            if (!config.isNull("colorH"))             { b.setColorH(config.getInt("colorH")); }
            if (!config.isNull("colorS"))             { b.setColorS(config.getInt("colorS")); }
            if (!config.isNull("colorB"))             { b.setColorB(config.getInt("colorB")); }
            if (!config.isNull("HSB"))                { b.setHSB(config.getString("HSB")); }
            if (!config.isNull("maxSpeed"))           { b.setMaxSpeed(config.getInt("maxSpeed")); }
            if (!config.isNull("alignmentScalar"))    { b.setAlignmentScalar(config.getInt("alignmentScalar")); }
            if (!config.isNull("cohesionScalar"))     { b.setCohesionScalar(config.getInt("cohesionScalar")); }
            if (!config.isNull("seperationScalar"))   { b.setSeperationScalar(config.getInt("seperationScalar")); }
            if (!config.isNull("maxSteerForce"))      { b.setMaxSteerForce(config.getInt("maxSteerForce")); }
            if (!config.isNull("scale"))              { b.setScale(config.getInt("scale")); }
            if (!config.isNull("mass"))               { b.setMass(config.getInt("mass")); }
            if (!config.isNull("resetColorStep"))     { b.resetColorStep(); }
            if (!config.isNull("avoidWalls"))         { b.setAvoidWalls(config.getBoolean("avoidWalls")); }
        }
    }






}
