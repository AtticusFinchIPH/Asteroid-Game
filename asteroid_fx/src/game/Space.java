package game;


import tools.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Space contains all the information determining the current state of
 * the game, and methods implementing how the game state changes, and how
 * the game ends (basically the rules of the game).
 */
public class Space {

  public static final double SPACE_WIDTH = 800;
  public static final double SPACE_HEIGHT = 800;

  public static final int INITIAL_ASTEROID_COUNT = 10;
  public static final double INITIAL_ASTEROID_SIZE = 3;

  /**
   * We don't want asteroids to spawn on the spaceship. This parameter
   * controls how close an asteroid can be from the spaceship initially,
   * in pixels.
   */
  private static final double STARTING_SECURITY_DISTANCE = 80;

  /**
   * An object able to create random items, like asteroids or positions.
   */
  public static final RandomGenerator generator = new RandomGenerator();


  private Spaceship spaceship;
  private List<Asteroid> asteroids;
  private double score = 0;
  private List<Projectile> projectiles;

  public Spaceship getSpaceship() {
    return spaceship;
  }

  public List<Asteroid> getAsteroids() {
    return asteroids;
  }

  public double getScore() {
    return score;
  }
  
  public List<Projectile> getProjectiles() {
	return projectiles;
  }

  public Space() {
    spaceship = new Spaceship();
    asteroids = new ArrayList<>(INITIAL_ASTEROID_COUNT);
    for (int i = 0; i < INITIAL_ASTEROID_COUNT; i++) {
      asteroids.add(generateInitialAsteroid());
    }
    projectiles = new ArrayList<Projectile>();
  }


  public void update(double dt) {
    updateScore(dt);
    for (Asteroid asteroid : asteroids) {
      asteroid.update(dt);
    }
    spaceship.update(dt);
    for (Projectile projectile : projectiles) {
      projectile.update(dt);
	}
    removeDeadProjectiles();
  }

  private void updateScore(double dt) {
    score = score + 10 * dt;
  }

  private boolean hasCollision() {
	  for (Asteroid asteroid : asteroids) {
		if(spaceship.collides(asteroid)) {
			return true;
		}
	}
	return false;
  }

  public boolean isGameOver() {
	if(hasCollision() && spaceship.getLife() == 0) return true;
    return false;
  }


  /**
   * Generates a random asteroid with standard parameters, whose distance
   * to the spaceship is large enough.
   *
   * @return a random asteroid
   */
  public Asteroid generateInitialAsteroid() {
    Asteroid asteroid = generator.asteroid(INITIAL_ASTEROID_SIZE);
    double distanceFromSpaceship =
      asteroid.getPosition().distanceTo(spaceship.getPosition());
    if (distanceFromSpaceship < STARTING_SECURITY_DISTANCE) {
      return generateInitialAsteroid();
    }
    return asteroid;
  }

  public void addProjectile(Projectile newProjectile) {
	projectiles.add(newProjectile);
  }
  
  private List<Projectile> getDeadProjectiles() {
	  List<Projectile> deadProjectiles = new ArrayList<Projectile>();
	  for (Projectile projectile : deadProjectiles) {
		if(!projectile.isAlive()) deadProjectiles.add(projectile);
	  }
	  return deadProjectiles;
  }
  
  private void removeDeadProjectiles() {
	  Iterator<Projectile> currentIterator = projectiles.iterator();
	  while(currentIterator.hasNext()) {
		  Projectile curentProjectile = currentIterator.next();
		  Iterator<Projectile> deadIterator = getDeadProjectiles().iterator();
		  while(deadIterator.hasNext()) {
			  deadIterator.next().equals(curentProjectile);
			  currentIterator.remove();
		  }
	  }
  }

  /**
   * Because the space is toric (things leaving the window on one side
   * reappear on the other side), we need to compute the positions of items
   * leaving the screen to get them back on the other side. This method takes
   * an arbitrary vector and maps it to valid toric coordinates.
   *
   * @param position any position
   * @return the same position with canonical toric coordinates
   */
  public static Vector toricRemap(Vector position) {
    return new Vector(
      clamp(position.getX(), SPACE_WIDTH),
      clamp(position.getY(), SPACE_HEIGHT)
    );
  }


  /**
   * Used by remapPosition to compute coordinates between 0 and a bound.
   *
   * @param value the coordinate to recompute
   * @param bound the maximum value allowed for this coordinate
   * @return the corrected coordinate
   */
  private static double clamp(double value, double bound) {
    return value - Math.floor( value / bound) * bound;
  }
}
