package de.tu_darmstadt.gdi1.gorillas.util;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

public class Jubel extends Movement implements Action {
	public float timer;
	public Jubel(float speed) {
		super(speed);
	}

	@Override
	public Vector2f getNextPosition(Vector2f position, float speed,
			float rotation, int delta) {
		// retrieve the current position (x, y)
		Vector2f pos = new Vector2f(position);
		// update the x+y position by the displacement
		pos.y -= speed * delta * (timer%2==0?1:(-1));
		pos.x -= speed * delta * (timer%2==0?1:(-1));
		++timer;
		// return the new position
		return pos;
	}
}