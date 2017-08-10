// Declaration of the guideBoid class
class Extent {
  PVector pos;
  PVector posCenter;
  color c;
  int amount = 1;
  float radius = 500;
  float vel = 0.1;
  float angleNew;
  float size = 50;
  float sizeB = 50;
  float speed = 0;
  boolean up = true;
  float angle = 0;
  float sizeInc = 0.01;

  // boolean up = true;

// Constructor with incoming arguments
  Extent(PVector inPos, float _radius, float _size, int _amount, float _vel, color _c) {
    posCenter = new PVector();
    vel = _vel;
    pos = new PVector();
    posCenter.set(inPos);
    c = _c;
    radius = _radius;
    size = _size;
    sizeB = _size;
    amount = _amount;
  }

  void render(PVector posCenterNew) {
    rotateZ(angle);

    sphereDetail(5);
    noStroke();
    fill(c);

    speed =+ speed + vel;

    float iteration = TWO_PI / amount;

    for (angle = 0; angle < TWO_PI; angle+= iteration) {

      pushMatrix();

      pos.x = cos(angle + speed) * radius;
      pos.y = sin(angle + speed) * radius;

      translate(posCenterNew.x + pos.x, posCenterNew.y + pos.y);
      // rotateY(angle/10);
      // rotateX(angle/20);
      noFill();
      stroke(50,20,100);
      ellipse(0,0,size,size);
      popMatrix();
      println(size);

      if ( up) {
        size += sizeInc;
        if(size > sizeB * 5) {
          up = false;
        }
      } else if (!up) {
        size -= sizeInc;
        if(size < 10) {
          up = true;
        }
      }

    }
  }
}
