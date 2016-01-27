package org.supermario.model.elements;

import java.util.Observable;

import org.supermario.common.Direction;
import org.supermario.common.Rectangle;
import org.supermario.common.Vector2D;
import org.supermario.model.Game;
import org.supermario.model.GameElementVisitor;

public abstract class GameElement extends Observable {
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	private boolean left=false;
	
	public GameElement(int x, int y) {
		this.position = new Vector2D(x,y);
		this.velocity = new Vector2D(0,0);
		this.acceleration = new Vector2D(0,0);
	}

	public abstract void addToGame(Game game);
	public abstract void accept(GameElementVisitor visitor);

	public boolean checkCollision(GameElement targetObj) {
		for (Rectangle r1 : this.getBoundaries()) {
			for (Rectangle r2: targetObj.getBoundaries()) {
				if (r1.collideWith(r2))
					return true;
			}
		}
		return false;
	}

	public abstract Rectangle[] getBoundaries() ;
	protected abstract void changeBoundariesPosition(Vector2D newPosition);

	public boolean isOnFloor(GameElement targetObj) {
		for (Rectangle r1 : this.getBoundaries()) {
			for (Rectangle r2: targetObj.getBoundaries()) {
				if (r1.onFloor(r2))
					return true;
			}
		}
		return false;
	}
	
	public void resolveCollisionWith(GameElement rightObj) {
		this.undoMove();
	}
	public void move() {
		Vector2D newPosition = this.position.sum(this.velocity);
		this.velocity = this.velocity.sum(this.acceleration);
		changePosition(newPosition);
	}
	public void undoMove() {
		int h = GameConstants.MARIO_HEIGHT - GameConstants.BOUNDARIES_TOLERANCE;
		this.velocity = this.velocity.substract(this.acceleration);
		Vector2D newPosition=this.position;
		for (Rectangle r1 : rightObj.getBoundaries()) {
			if (this.position.getY()+h/2>r1.getTopLeft().getY()-10 && this.position.getY()+h/2<r1.getBottomRight().getY()-10) {
				newPosition = this.position.substract(this.velocity);
				newPosition.setY(r1.getTopLeft().getY()-2-h/2);
			}
		else {newPosition = this.position.substract(this.velocity);}
		}
		changePosition(newPosition);
	}

	private void changePosition(Vector2D newPosition) {
		if (! this.position.equals(newPosition)) {			
			this.changeBoundariesPosition(newPosition);
			this.position = newPosition;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	protected void changeAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
	}
	
	protected void changeVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	protected void addToAcceleration(Vector2D delta) {
		this.acceleration = this.acceleration.sum(delta);
	}

	protected void addToVelocity(Vector2D delta) {
		this.velocity = this.velocity.sum(delta);
	}
	
	public Direction getDirection() {
		if (this.left)
			return Direction.LEFT;
		else
			return Direction.RIGHT;
	}

	public Vector2D getPosition() {
		return this.position;
	}
	
	public Vector2D getVelocity() {
		return this.velocity;
	}
	
	public Vector2D getAcceleration() {
		return this.acceleration;
	}
	
	public void setLeft(boolean bool) {
		this.left=bool;
	}
	
	public boolean getLeft() {
		return this.left;
	}
}
