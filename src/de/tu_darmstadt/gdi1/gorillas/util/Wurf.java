package de.tu_darmstadt.gdi1.gorillas.util;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

public class Wurf extends Movement implements Action {

	private int angle, speed;
	  
	public Wurf (float speed) {
	    super(speed/25);
	}

	  @Override
	  public Vector2f getNextPosition(Vector2f position, float speed,
	      float rotation, int delta) {
	    // retrieve the current position (x, y)
	    Vector2f pos = new Vector2f(position);
	    
	    // update the y position by the displacement
	    pos.x += speed * delta;
	    pos.y += speed * delta;
	    
	    // return the new position
	    return pos;
	  }

}