package BattleShip;

import java.awt.Point;

public class Ship {
	Point topLeft = new Point();
	Point bottomRight = new Point();
	int size;
	int x; int y;
	
	public Ship(int size, int x, int y){
		this.topLeft.x = x;
		this.topLeft.y = y;
		this.bottomRight.x = x+30;
		this.bottomRight.y = y+(size*30);
		this.size = size;
		this.x=x;
		this.y = y;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setPoint(Point topLeft) {
		this.topLeft = topLeft;
		this.bottomRight.x = topLeft.x+30;
		this.bottomRight.y = topLeft.y+(size*30);
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}

}

