package BattleShip;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy {
	static int shipIterator = 1;
	Rectangle ship1 = new Rectangle(30, 90);
	Rectangle ship2 = new Rectangle(30, 90);
	Rectangle ship3 = new Rectangle(30, 90);
	Rectangle ship4 = new Rectangle(30, 120);
	Rectangle ship5 = new Rectangle(30, 120);
	Rectangle ship6 = new Rectangle(30, 150);
	Rectangle ship7 = new Rectangle(30, 180);
	ArrayList<Rectangle> shipLocations = new ArrayList<Rectangle>();
	ArrayList<Rectangle> sunkenShips = new ArrayList<Rectangle>();
	static int sinks = 0;
	
	int recentX;
	int recentY;
	int initialX;
	int initialY;
	boolean targetLock = false;
	int targetLockDirection = 1; //1 is down, 2 is right, 3 is up, 4 is left
	int hit;
	
	Enemy(){
		shipLocations.add(ship1);shipLocations.add(ship2);shipLocations.add(ship3);
		shipLocations.add(ship4);shipLocations.add(ship5);shipLocations.add(ship6);
		shipLocations.add(ship7);
	}
	
	public void EnemyPlaceShips() {
		
		for(int i = 0; i<7; i++) {
				int rotate = (int)(Math.random()*2) +1; 				//50% chance to rotate the ship.
				int in1 = ((int)(Math.random() *14) + 1)*30;			//choose random x coordinate
				
				//check if ship is rotated and rotate if true
				if(rotate == 1) {
					int tmp =  shipLocations.get(i).height;
					shipLocations.get(i).height = shipLocations.get(i).width;
					shipLocations.get(i).width = tmp;
				}
				
				int in2 = ((int)(Math.random() *14) + 1)*30;
				shipLocations.get(i).setLocation(in1, in2);
				if(checkOutOfBounds(in1, in2, i) || checkOccupied(shipLocations.get(i), i)) {
					i--;
				}
				else {
					System.out.println("SUCCESS");
				}
				
			}
	}
	
	
	public  boolean checkOutOfBounds(int x, int y, int index) {
		if(x+shipLocations.get(index).getWidth() > 450 || y+shipLocations.get(index).getHeight() > 450) {
			return true;
		}
		return false;
	}
		
	public  boolean checkOccupied(Rectangle r, int index) {
		for(int i = 0; i < 7; i++) {
			Rectangle comparator = new Rectangle();
			comparator.x = shipLocations.get(i).x-30;
			comparator.y = shipLocations.get(i).y-30;
			comparator.height = shipLocations.get(i).height+60;
			comparator.width = shipLocations.get(i).width+60;
			if(r.intersects(comparator)&& i != index) {
				return true;
			}
		}
		return false;
	}
	public ArrayList<Rectangle> checkSink(ArrayList<Point> hits) {
		sinks = 0;
		sunkenShips.clear();
		for(int i = 0; i < shipLocations.size(); i++) {
			int NOH = 0;
			if(shipLocations.get(i).getHeight() > shipLocations.get(i).getWidth()) {
				for(int y = shipLocations.get(i).y; y< (shipLocations.get(i).y+shipLocations.get(i).height); y += 30) {
					for(Point p : hits) {
						if(p.getX() == shipLocations.get(i).getX() && p.getY() == y) {
							NOH++;
						}
					}
				}
			}
			else{
				for(int x = shipLocations.get(i).x; x< (shipLocations.get(i).x+shipLocations.get(i).width); x += 30) {
					for(Point p : hits) {
						if(p.getX() == x && p.getY() == shipLocations.get(i).getY()) {
							NOH++;
						}
					}
				}

			}
			
			if(shipLocations.get(i).getHeight() > shipLocations.get(i).getWidth()) {
				if(NOH == (shipLocations.get(i).getHeight()/30)) {
					sinks++;
					sunkenShips.add(shipLocations.get(i));
				}
			}
			else{
				if(NOH == (shipLocations.get(i).getWidth()/30)) {
					sinks++;
					sunkenShips.add(shipLocations.get(i));
				}
			}
		}
		return sunkenShips;
	}


}
