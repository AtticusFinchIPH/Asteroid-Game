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

  /**
   * The forward direction for the spaceship, encoding the rotation
   * from horizontal of its image and the direction of acceleration.
   */
  private Vector direction = new Vector(1, 0);

  /**
   * Controls if the main engine, with forward acceleration, is powered on.
   */
  private boolean isMainEngineOn = false;


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


  /**
   * @return whether the main engine is on (forward acceleration).
   */
  public boolean isMainEngineOn() {
    return isMainEngineOn;
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
      position = position.add(direction.multiply(100 * dt));
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
