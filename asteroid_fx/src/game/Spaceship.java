package game;

import tools.Vector;

import java.util.List;

/**
 * Models a spaceship controlled by a player.
 */
public class Spaceship {


  /**
   * The position of the center of the spaceship
   */
  private Vector position;
  
  private Vector velocity = Vector.ZERO;

  /**
   * The forward direction for the spaceship, encoding the rotation
   * from horizontal of its image and the direction of acceleration.
   */
  private Vector direction = new Vector(1, 0);
  
  private static final double MAIN_ENGINE_POWER = 50;
  private static final double COEFF_OF_ROTATION = 10;
  private static final double REVERSE_ENGINE_POWER = 30;

  /**
   * Controls if the main engine, with forward acceleration, is powered on.
   */
  private boolean isMainEngineOn = false;
  private boolean isLeftEngineOn = false;
  private boolean isRightEngineOn = false;

  /**
   * @return the position of the spaceship
   */
  public Vector getPosition() {
    return position;
  }

  /**
   * @return the angle of the spaceship in degree, where 0 is facing right.
   */
  public double getDirectionAngle() {
    return direction.angle();
  }

  public Vector getAcceleration() {
	  if(isMainEngineOn()) {
		  return direction.multiply(MAIN_ENGINE_POWER);
	  } else {
		  return Vector.ZERO;
	  }
  }

  /**
   * @return whether the main engine is on (forward acceleration).
   */
  public boolean isMainEngineOn() {
    return isMainEngineOn;
  }

  public boolean isLeftEngineOn() {
	  if(isMainEngineOn()) {
		  return isLeftEngineOn;
	  } else {
	    return false;
	  }
  }
  
  public boolean isRightEngineOn() {
	  if(isMainEngineOn()) {
		  return isRightEngineOn;
	  } else {
	    return false;
	  }
  }
  
  public void startLeftEngine() {
	if(isMainEngineOn()) {
		isLeftEngineOn = true;
		isRightEngineOn = false;
	}
  }
  
  public void stopLeftEngine() {
	isLeftEngineOn = false;
  }
  
  public void startRightEngine() {
	if(isMainEngineOn()) {
		isLeftEngineOn = false;
		isRightEngineOn = true;
	}
  }
  
  public void stopRightEngine() {
	isRightEngineOn = false;
  }
  
  public void updateDirection(double dt) {
	  double angle = dt * COEFF_OF_ROTATION;
	  if(isRightEngineOn()) direction = direction.rotate(angle);
	  if(isLeftEngineOn()) direction = direction.rotate(-angle);
  }

  /**
   * Initially the spaceship will be positioned at the center of space.
   */
  public Spaceship() {
    this.position =
      new Vector(
        Space.SPACE_HEIGHT / 2,
        Space.SPACE_WIDTH / 2
      );
  }


  /**
   * The spaceship is a moving object. Every now and then, its position
   * must be updated, as well as other parameters evolving with time. This
   * method simulates the effects of a delay <em>dt</em> over the spaceship.
   * For good accuracy this delay should be kept small.
   *
   * @param dt the time delay to simulate.
   */
  public void update(double dt) {
    if (isMainEngineOn()) {
    	updateDirection(dt);
    	velocity = velocity.add(this.getAcceleration().multiply(dt));
        position = position.add(velocity.multiply(dt));
    }
    position = Space.toricRemap(position);
  }


  /**
   * Switches the main engine (powering forward acceleration) on.
   */
  public void startMainEngine() {
    isMainEngineOn = true;
  }

  /**
   * Switches the main engine (powering forward acceleration) off.
   */
  public void stopMainEngine() {
    isMainEngineOn = false;
  }


  /**
   * A list of points on the boundary of the spaceship, used
   * to detect collision with other objects.
   */
  private static final List<Vector> contactPoints =
    List.of(
      new Vector(0,0),
      new Vector(27,0),
      new Vector(14.5,1.5),
      new Vector(2,3),
      new Vector(0,18),
      new Vector(-13,18),
      new Vector(-14,2),
      new Vector(-14,-2),
      new Vector(-13,-18),
      new Vector(0,-18),
      new Vector(2,-3),
      new Vector(14.5,-1.5)
    );

  public static List<Vector> getContactPoints() {
    return contactPoints;
  }
}
