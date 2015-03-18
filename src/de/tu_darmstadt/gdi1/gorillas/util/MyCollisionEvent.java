package de.tu_darmstadt.gdi1.gorillas.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;

/**
 * AboutState
 * 
 * @author Deniz Tobias Buruncuk, Dennis Hasenstab, Philip Stauder, Marcel Dieter
 * @version 1.0
 * 
 * Adjusted by Dennis Hasenstab
 */
public class MyCollisionEvent extends Event {

	/**
	 * the entity with which the owning entity for this event has collided
	 */
	private Entity collidedEntity;
	private StateBasedEntityManager em;

	/**
	 * Creates a new collision event.
	 */
	public MyCollisionEvent() {
		super("MyCollisionEvent");
	}

	/**
	 * checks if the action(s) associated with this event shall be performed. In
	 * this case, the action(s) are only performed if an collision between the
	 * entity that own this event object and a different entity has taken place.
	 * 
	 * @param gc
	 *            the GameContainer object that handles the game loop, recording
	 *            of the frame rate, and managing the input system
	 * @param sb
	 *            the StateBasedGame that isolates different stages of the game
	 *            (e.g., menu, ingame, highscores etc.) into different states so
	 *            they can be easily managed and maintained.
	 * @param delta
	 *            the time passed in nanoseconds (ns) since the start of the
	 *            event, used to interpolate the current target position
	 * 
	 * @return true if the action(s) associated with this event shall be
	 *         performed, else false
	 */
	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb,
			int delta) {
		// determine the first entity that has collided with the owner of the
		// event
		Entity entity = StateBasedEntityManager.getInstance().collides(
				sb.getCurrentStateID(), owner);

		// if there is such an entity, store a reference and indicate the
		// willingness
		// to perform the action(s)
		if (entity != null && !em.getEntity(2, "background").equals(entity) && (em.hasEntity(2, "arrow_wind")?!em.getEntity(2, "arrow_wind").equals(entity):true)) {
			if (em.getEntity(2, "sun_smiling").equals(entity)) {
				Gorillas.data.sunAstonished = true;
			} else {
				collidedEntity = entity;
				return true;
			}
		}

		// else, nothing is to be done
		return false;
	}

	/**
	 * this method has been deprecated and is kept only for backward
	 * compatibility. Please use {@link #getCollidedEntity()} instead.
	 * 
	 * @return the entity with which a collision took place, if any.
	 */
	@Deprecated
	public Entity getColidedEntity() {
		return getCollidedEntity();
	}

	/**
	 * returns the entity with which the entity owning this event has collided
	 * 
	 * @return the entity with which the entity owning this event has collided
	 */
	public Entity getCollidedEntity() {
		return collidedEntity;
	}

	public StateBasedEntityManager getEm() {
		return em;
	}

	public void setEm(StateBasedEntityManager em) {
		this.em = em;
	}
}
