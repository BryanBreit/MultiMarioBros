package org.supermario.model.elements;

import org.supermario.common.Rectangle;
import org.supermario.common.Vector2D;
import org.supermario.model.Game;
import org.supermario.model.GameConstants;
import org.supermario.model.GameElementVisitor;


public class Player extends GameElement {
	private final static Vector2D diffVelocityLeft = new Vector2D(-GameConstants.MARIO_WALK_STEP, 0);
	private final static Vector2D diffVelocityRight = new Vector2D(GameConstants.MARIO_WALK_STEP, 0);
	private final static Vector2D diffVelocityJump = new Vector2D(0, -2 * GameConstants.MARIO_WALK_STEP);
	private final static Vector2D diffGravityAcceleration = new Vector2D(0, GameConstants.GRAVITY_ACCELERATION);

	private boolean tostop=false;
	private boolean toleft=false;
	private boolean toright=false;
	private Rectangle boundaries;

	public Player(int x, int y) {
		super(x,y);
		int w = GameConstants.MARIO_WIDTH - GameConstants.BOUNDARIES_TOLERANCE;
		int h = GameConstants.MARIO_HEIGHT - GameConstants.BOUNDARIES_TOLERANCE;
		this.boundaries = new Rectangle(this.getPosition(), w, h);
    }
	
	@Override
	public void accept(GameElementVisitor visitor) {
		visitor.visit(this);
	}
	
	public void resolveCollisionWith(GameElement rightObj) {
		super.resolveCollisionWith(rightObj);
		this.changeAcceleration(new Vector2D(0, 0));
		this.changeVelocity(new Vector2D(this.getVelocity().getX(), 0));
		if (tostop) {
			this.changeVelocity(new Vector2D(0, 0));
			tostop=false;
		}
		else {this.changeVelocity(new Vector2D(this.getVelocity().getX(), 0));}
		if (toleft) {
			this.goLeft();
			toleft=false;
		}
		else if (toright) {
			this.goRight();
			toright=false;
		}
	}

	public void goLeft() {
		if (this.getAcceleration().getY()==0 || this.getVelocity().getX()==0) {
			this.addToVelocity(diffVelocityLeft);
			this.setLeft(true);
		}
		else{toleft=true;
			toright=false;}
	}
	
	public void goRight() {
		if (this.getAcceleration().getY()==0 || this.getVelocity().getX()==0) {
			this.addToVelocity(diffVelocityRight);
			this.setLeft(false);
		}
		else{toright=true;
			toleft=false;}
	}

	public void jump() {
		if (this.getAcceleration().getY()==0) {
			this.addToVelocity(diffVelocityJump);
			fall();
		}
	}

	public void fall() {
		this.getAcceleration().setY(diffGravityAcceleration.getY());
	}
	
	public void stopLeft() {
		if (this.getAcceleration().getY()==0 && this.getVelocity().getX()<0) {
			this.addToVelocity(diffVelocityLeft.negative());
		}
		else{tostop=true;
			toright=false;
			toleft=false;}
	}
	
	public void stopRight() {
		if (this.getAcceleration().getY()==0 && this.getVelocity().getX()>0) {
			this.addToVelocity(diffVelocityRight.negative());
		}
		else{tostop=true;
			toright=false;
			toleft=false;}
	}

	@Override
	public void addToGame(Game game) {
		game.setPlayer(this);
	}

	@Override
	public Rectangle[] getBoundaries() {
		return new Rectangle[] {this.boundaries};
	}

	@Override
	protected void changeBoundariesPosition(Vector2D newPosition) {
		this.boundaries.moveAbsolute(newPosition);
	}
}
