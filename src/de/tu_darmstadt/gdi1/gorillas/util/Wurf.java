package de.tu_darmstadt.gdi1.gorillas.util;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 */
public class Wurf extends Movement implements Action {
	public int angle;
	public Vector2f startPos;
	public float timer;
	public float gravity = 10F;
	public float deltat = 100;
	public float wind = 0;
	public float wscale = 0.1F;
	public Wurf(float speed) {
		super(speed);
	}

	@Override
	public Vector2f getNextPosition(Vector2f position, float speed,
			float rotation, int delta) {
		// retrieve the current position (x, y)
		Vector2f pos = position;
		// update the x+y position by the displacement
		pos.x = (float) (startPos.x + getX(angle, speed, timer/deltat));
		pos.y = (float) (startPos.y - getY(angle, speed, timer/deltat));
		timer+=delta;
		// return the new position
		return pos;
	}
	
	/** Calculates speed Vx.
	 * @param angle the starting angle
	 * @param speed the start speed
	 * 
	 * @return the speed Vx
	 */
	private double getVx(int angle, float speed) {
		return Math.cos(Math.toRadians(angle))*speed;
	}

	/** Calculates speed Vy.
	 * @param angle the starting angle
	 * @param speed the start speed
	 * 
	 * @return the speed Vy
	 */
	private double getVy(int angle, float speed) {
		return Math.sin(Math.toRadians(angle))*speed;
	}

	/** Returns the x-Position.
	 * @param angle the starting angle
	 * @param speed the start speed
	 * @param delta the time
	 * 
	 * @return the x-position
	 */
	private double getX(int angle, float speed, float delta) {
		return getVx(angle,speed) * delta + (0.5 * wind * wscale * delta * delta);
	}

	/** Returns the y-Position.
	 * @param angle the starting angle
	 * @param speed the start speed
	 * @param delta the time
	 * 
	 * @return the y-position
	 */
	private double getY(int angle, float speed, float delta) {
		return getVy(angle,speed) * delta + (0.5 * -gravity * delta * delta);
	}
}