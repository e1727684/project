package de.tu_darmstadt.gdi1.gorillas.util;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

public class Wurf extends Movement implements Action {
	public int angle;
	public Vector2f startPos;
	public float timer;
	public float gravity = -10F;
	public float deltat = 100;
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

	private double getVx(int angle, float speed) {
		return Math.cos(Math.toRadians(angle))*speed;
	}

	private double getVy(int angle, float speed) {
		return Math.sin(Math.toRadians(angle))*speed;
	}

	private double getX(int angle, float speed, float delta) {
		return getVx(angle,speed) * delta;
	}

	private double getY(int angle, float speed, float delta) {
		return getVy(angle,speed) * delta + (0.5 * gravity * delta * delta);
	}
}