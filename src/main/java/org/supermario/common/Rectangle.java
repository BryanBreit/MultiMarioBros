package org.supermario.common;

public class Rectangle {
	private Vector2D topLeft;
	private Vector2D bottomRight;
	private int width;
	private int height;
	
	public Rectangle(Vector2D centerPosition, int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.computeBoundaries(centerPosition);
	}
	
	public Vector2D getTopLeft() {
		return this.topLeft;
	}
	
	public Vector2D getBottomRight() {
		return this.bottomRight;
	}

	public void moveAbsolute(Vector2D centerPosition) {
		this.computeBoundaries(centerPosition);
	}
	
	public boolean collideWith(Rectangle target) {
		return ! ( bottomRight.getX() < target.topLeft.getX() 
				|| bottomRight.getY() < target.topLeft.getY()
				|| topLeft.getX() > target.bottomRight.getX()
				|| topLeft.getY() > target.bottomRight.getY()); 
	}
	
	public boolean onFloor(Rectangle target) {
		return (this.bottomRight.getY() > target.topLeft.getY()-10 && target.topLeft.getY()>=this.bottomRight.getY() &&
				((this.bottomRight.getX() >= target.topLeft.getX() && this.bottomRight.getX() <= target.bottomRight.getX()) ||
				 (this.topLeft.getX() <= target.bottomRight.getX() && this.topLeft.getX() >= target.topLeft.getX()))); 
	}
	
	private void computeBoundaries(Vector2D centerPosition) {
		int x = centerPosition.getX() - (width/2);
		int y = centerPosition.getY() - (height/2);
		this.topLeft = new Vector2D(x, y);
		this.bottomRight = new Vector2D(x + width, y + height);		
	}

	public void moveRelative(Vector2D movement) {
		Vector2D center = this.topLeft.sum(new Vector2D(width/2, height/2));
		this.computeBoundaries(center.sum(movement));
	}
}
