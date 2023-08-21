package org.battelle.katana.object;
import org.battelle.katana.mechanics.Cut;
import org.battelle.katana.mechanics.Cutter;
import org.battelle.katana.mechanics.Launcher;
import org.battelle.katana.mechanics.Timer;
import org.battelle.katana.model.GameModel;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class KatanaObject implements IKatanaObject {
	/** The backend for our game */
	private GameModel model;
	/** Boolean for if the sphere has been cut */
	private boolean hasBeenCut = false;
	/** The SoundFactory that generates sounds to be played */
	private SoundFactory soundFactory = SoundFactory.getInstance();
	/** Graphical representation of fruit */
	private MTComponent component;
	/** The initial position on our canvas of this sphere object */
	private Vector3D position;
	/** The initial velocity of this sphere object */
	private Vector3D velocity;
	/** The current position of this sphere object */
	private Vector3D currentPosition;
	/** The current velocity on our canvas of this sphere object */
	private Vector3D currentVelocity;
	/** The global launcher that will fire all elements */
	private Launcher launcher;
	/** Change in x position on last physics update */
	private float deltaX;
	/** Change in y position on last physics update */
	private float deltaY;
	/** Slope based on last x and y delta */
	private float slope;
	/** The type of katana object we are tracking */
	private ObjectType type;
	/** The timer we are using for our powerups */
	private Timer timer;
	/** The reference to the component as an AbstractShape for position methods */
	private AbstractShape shape;
	private MTTriangleMesh poly;
	private float radius;

	/**
	 * Constructor initializing all values
	 * 
	 * @param component The sphere we wish to track
	 * @param launcher The launcher our fruits will be launching from
	 * @param model The game model we are updating when fruits get cut
	 */
	public KatanaObject(MTComponent component, Launcher launcher, GameModel model, ObjectType type, Timer timer) {

		//Initialize our variables
		this.type = type;
		this.component = component;
		this.model = model;
		this.launcher = launcher;
		this.timer = timer;
		if (component instanceof MTTriangleMesh)
		{
			this.poly = (MTTriangleMesh) component;
			this.poly.setPositionGlobal(new Vector3D(-1000, -1000, 0));
		}
		else
		{
			this.shape = (AbstractShape) component;
			// Set the object's position to way off the screen
			shape.setPositionGlobal(new Vector3D(-1000, -1000, 0));
			this.shape.setNoStroke(true);
		}
		
		this.radius = computeRadius();
	}
	
	public float getRadius()
	{
		return this.radius;
	}
	
	private float computeRadius()
	{
		Vector3D maxPoint = new Vector3D(0,0,0);
		if (this.poly != null)
		{
			for (Vector3D vec : this.poly.getVerticesLocal())
			{
				if (vec.distance(this.poly.getCenterPointLocal()) > maxPoint.distance(this.poly.getCenterPointLocal()))
				{
					maxPoint = vec;
				}
			}
			
			return maxPoint.distance(this.poly.getCenterPointLocal());
		}
		else
		{
			for (Vector3D vec : this.shape.getVerticesLocal())
			{
				if (vec.distance(this.shape.getCenterPointLocal()) > maxPoint.distance(this.shape.getCenterPointLocal()))
				{
					maxPoint = vec;
				}
			}
			
			return maxPoint.distance(this.shape.getCenterPointLocal());
		}
		
	}

	public ObjectType getType() {
		return this.type;
	}

	@Override
	public void performOnCut() {
		switch (this.type){
			case FRUIT_ONE:
			case FRUIT_THREE:
			case FRUIT_TWO:
			case RANDOMFRUIT:
				this.hasBeenCut = true;
				// Update the score based on how many combos we've achieved
				model.updateScore(100 + (100 * model.getComboCount()));
				// Update the total number of fruits cut by one
				model.updateNumberOfFruitsCut();
				// Update the number of fruits we've cut in a row by one
				model.updateNumberOfFruitsCutInRow();
				// Play a random fruit hit sound
				soundFactory.generateSound(SoundType.HIT).start();
				// Destroy the sphere
				if (this.shape == null)
				{
					this.destroy();
				}
				
			break;
			case TIMEWARP:
				timer.pause();
				soundFactory.generateSound(SoundType.TIMEWARP).start();
				if (this.shape == null)
				{
					this.destroy();
				}
			break;
			case FRENZY:
				model.setFrenzyMode(true);
				soundFactory.generateSound(SoundType.FRENZY).start();
				if (this.shape == null)
				{
					this.destroy();
				}
			break;
			case MULTIPLIER:
				timer.subTimerStart();
				soundFactory.generateSound(SoundType.MULTIPLIER).start();
				model.setDoubleScore(true);
				if (this.shape == null)
				{
					this.destroy();
				}
			break;
			case ARCADEBOMB:
				// Reset the number of fruits cut in a row
				model.resetNumberOfFruitsCutInRow();
				// Remove 1000 points for cutting
				model.updateScore(-1000);
				// Play bomb explosion sound
				soundFactory.generateSound(SoundType.BOMB).start();
				// Destroy the sphere
				if (this.shape == null)
				{
					this.destroy();
				}
			break;
			case CLASSICBOMB:
				// Reset the number of fruits cut in a row
				model.resetNumberOfFruitsCutInRow();
				// Takes away all of your hearts. Emo much?
				while (model.getHearts() != 0) {
					model.removeHeart();
				}
				soundFactory.generateSound(SoundType.BOMB).start();
				// Destroy the sphere
				if (this.shape == null)
				{
					this.destroy();
				}
			break;
			
		}
		
	}

	/**
	 * Accessor for texture wrapped to the fruit
	 * 
	 * @return The texture wrapped to the fruit
	 */
	public PImage getTexture() {
		if (shape != null) {
			return shape.getTexture();
		} else {
			return poly.getTexture();
		}
	}
	
	/**
	 * Checks whether or not the fruit has been cut
	 * 
	 * @return Boolean for if the fruit has been cut
	 */
	public boolean hasBeenCut() {
		return this.hasBeenCut;
	}

	/**
	 * Sets that the fruit has been cut
	 */
	public void setCut() {
		this.hasBeenCut = true;
	}

	@Override
	public void setCenterPointLocal(Vector3D centerPointLocal) {
		// Never used
	}

	@Override
	public void setCenterPointGlobal(Vector3D centerPointGlobal) {

		shape.setPositionGlobal(centerPointGlobal);
	}

	@Override
	public Vector3D getPositionGlobal() {
		return this.shape.getCenterPointGlobal();
	}

	@Override
	public Vector3D getVelocity() {
		return this.velocity;
	}

	@Override
	public void setVelocity(Vector3D velocity) {
		this.velocity = velocity;
	}

	@Override
	public Vector3D getCurrentVelocity() {
		return currentVelocity;
	}

	@Override
	public void setCurrentVelocity(Vector3D currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	@Override
	public Vector3D getPosition() {
		return this.position;
	}

	@Override
	public void setPosition(Vector3D position) {
		this.position = position;
	}

	@Override
	public Vector3D getCurrentPosition() {
		return currentPosition;
	}

	@Override
	public void setCurrentPosition(Vector3D currentPosition) {
		this.currentPosition = currentPosition;
	}

	@Override
	public void destroy() {
		if (this.shape != null)
		{
			MTColor shapeColor = this.shape.getFillColor();
			shapeColor.setAlpha(255);
			this.shape.setFillColor(shapeColor);
			this.shape.destroy();
		}
		else
		{
			this.poly.destroy();
		}

		this.component.destroy();
		
	}

	@Override
	public KatanaObject launch() {
		return (KatanaObject) this.launcher.fire(this);

	}

	@Override
	public MTComponent getComponent() {
		return this.component;
	}

	@Override
	public void setComponent(MTComponent component) {
		this.component = component;
	}

	/**
	 * Checks for a valid cut and determines the change in x and change in y from the 5th last point (if it exists) to the last point in the cut
	 * 
	 * @return true if cut is valid
	 */
	public boolean checkValidCut(Cutter cutter) {
		Cut[] allCuts = cutter.getCuts();
		if (this.poly != null)
		{
			for (Cut aCut : allCuts) {
				float XRange = this.poly.getCenterPointGlobal().getX();
				float YRange = this.poly.getCenterPointGlobal().getY();
				Vector3D currentCut = aCut.getLast();
				if (currentCut.getX() < XRange+radius && currentCut.getX() > XRange-radius 
						&& currentCut.getY() < YRange + radius && currentCut.getY() > YRange-radius)
				{
					deltaX = (aCut.getLast().getX() - aCut.getOffsetFromLast(5).getX());
					deltaY = (-(aCut.getLast().getY() - aCut.getOffsetFromLast(5).getY()));
					slope = deltaY / deltaX;
					return true;
				}
			}
			return false;
		}
		else
		{
			for (Cut aCut : allCuts) {
				float XRange = this.shape.getCenterPointGlobal().getX();
				float YRange = this.shape.getCenterPointGlobal().getY();
				Vector3D currentCut = aCut.getLast();
				if (currentCut.getX() < XRange+radius && currentCut.getX() > XRange-radius 
						&& currentCut.getY() < YRange + radius && currentCut.getY() > YRange-radius)
				{
					deltaX = (aCut.getLast().getX() - aCut.getOffsetFromLast(5).getX());
					deltaY = (-(aCut.getLast().getY() - aCut.getOffsetFromLast(5).getY()));
					slope = deltaY / deltaX;
					return true;
				}
			}
			return false;
		}
		
	}
	
	/**
	 * Converts the delta x and delta y to a vector
	 * 
	 * @return returns a vector of the change in x and change in y
	 */
	public Vector3D deltaXdeltaYToVector() {
		if (Math.abs(deltaX) > 20)
			deltaX = Math.copySign(20, deltaX);
		if (Math.abs(deltaY) > 20)
			deltaY = Math.copySign(20, deltaY);
		if (Math.abs(slope) <= .25f) {
			deltaX = Math.copySign(1, deltaX);
			deltaY = Math.copySign(30, deltaY);
		}
		if (Math.abs(slope) >= 2.5f) {
			deltaX = Math.copySign(30, deltaX);
			deltaY = Math.copySign(1, deltaY);
		}

		if (deltaX < 0) {
			deltaX = -1;
		} else {
			deltaX = 1;
		}
		if (deltaY < 0) {
			deltaY = -1;
		} else {
			deltaY = 1;
		}
		
		
		return new Vector3D(deltaX, deltaY, 0);
	}

	@Override
	public void rotateX(Vector3D rotationPoint, float degrees, TransformSpace transformSpace) {
		component.rotateX(rotationPoint, degrees, transformSpace);
	}

	@Override
	public void rotateY(Vector3D rotationPoint, float degrees, TransformSpace transformSpace) {
		component.rotateY(rotationPoint, degrees, transformSpace);
	}

	@Override
	public Vector3D getCenterPointLocal() {
		if (this.shape != null)
		{
			return this.shape.getCenterPointLocal();
		}
		else
		{
			return this.poly.getCenterPointLocal();
		}
	}

	@Override
	public Vector3D getCenterPointGlobal() {
		if (this.shape != null)
		{
			return this.shape.getCenterPointGlobal();
		}
		else
		{
			return this.poly.getCenterPointGlobal();
		}
		
	}

	@Override
	public void setPositionGlobal(Vector3D positionGlobal) {
		if (this.shape != null)
		{
			this.shape.setPositionGlobal(positionGlobal);
		}
		else
		{
			this.poly.setPositionGlobal(positionGlobal);
		}
	}

	@Override
	public void setTexture(PImage texture) {
		if (this.shape != null)
		{
			this.shape.setTexture(texture);
		}
		else
		{
			this.poly.setTexture(texture);
		}
	}
}
