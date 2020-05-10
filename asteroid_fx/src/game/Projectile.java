package game;

import tools.Vector;

public class Projectile {
	private Vector position, velocity;
	private int lifeDuration;
	
	private boolean isAlive;
	
	private static final int INITIAL_LIFE_DURATION = 10;
	
	public Projectile(Vector position, Vector velocity) {
		this.position = position;
		this.velocity = velocity;
		lifeDuration = INITIAL_LIFE_DURATION;
		isAlive = true;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public boolean hits(Asteroid asteroid) {
		return asteroid.contains(position);
	}
	
	public void update(double dt) {
		updateLifespan(dt);
		updatePosition(dt);
		position = Space.toricRemap(position);
	}
	
	private void updateLifespan(double dt) {
		lifeDuration -= 1;
		if(lifeDuration <= 0) isAlive = false;
	}
	
	private void updatePosition(double dt) {
		position = position.add(velocity).multiply(dt);
	}
}
