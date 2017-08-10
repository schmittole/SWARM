class TriangleBoidList extends BoidList {

    //ChasingBoid chaser;


    TriangleBoidList() {
        super();
    }

    TriangleBoidList(int n, int ih) {
        boids = new ArrayList();
        h = ih;
        for (int i=0; i<n; i++) {
            boids.add(new TriangleBoid(new PVector(width/2, height/2, 0), this));
        }
    }


    void run(boolean aW, int currentGameState) {

        //run each boid with current gameState
        for (int i=0; i<boids.size(); i++) {
            //iterate through the list of boids
            TriangleBoid tempBoid = (TriangleBoid)boids.get(i); //create a temporary boid to process and make it the current boid in the list
            tempBoid.h = h;
            tempBoid.avoidWalls = aW;
            tempBoid.run(boids); //tell the temporary boid to execute its run method
            // println("TRIANGLE: "+tempBoid.pos);
        }

    }

}
