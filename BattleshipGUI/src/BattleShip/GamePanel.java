package BattleShip;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel implements ActionListener{

	/**
	 * POSITION BUTTON, GET RID OF HIT MARKER ON RESTART, REPOSITION BATTLE BUTTON
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 480;
	static final int SCREEN_HEIGHT = 900;
	static final int UNIT_SIZE = 30;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	boolean rotate = false;
	int index;
	int hitSize = 0;
	int missSize = 0;
	int animationFrame = 0;
	int playerSinks = 0;
	static boolean backgroundDirection = true;
	static boolean hit = false;
	String state = "start";
	Timer timer;
	
	Image ship3x1 = new ImageIcon("./Images/testPng.png").getImage();
	Image ship3x1_2 = new ImageIcon("./Images/testPng.png").getImage();
	Image ship3x1_3 = new ImageIcon("./Images/testPng.png").getImage();
	Image ship4x1 = new ImageIcon("./Images/ship4x1.png").getImage();
	Image ship4x1_2 = new ImageIcon("./Images/ship4x1.png").getImage();
	Image ship5x1 = new ImageIcon("./Images/ship5x1.png").getImage();
	Image ship6x1 = new ImageIcon("./Images/ship6x1.png").getImage();

	Image ship3x1HOR = new ImageIcon("./Images/testPngHOR.png").getImage();
	Image ship3x1_2HOR = new ImageIcon("./Images/testPngHOR.png").getImage();
	Image ship3x1_3HOR = new ImageIcon("./Images/testPngHOR.png").getImage();
	Image ship4x1HOR = new ImageIcon("./Images/ship4x1HOR.png").getImage();
	Image ship4x1_2HOR = new ImageIcon("./Images/ship4x1HOR.png").getImage();
	Image ship5x1HOR = new ImageIcon("./Images/ship5x1HOR.png").getImage();
	Image ship6x1HOR = new ImageIcon("./Images/ship6x1HOR.png").getImage();
	
	
	Image background = new ImageIcon("./Images/background.png").getImage();
	
	Rectangle ship1 = new Rectangle(30,225, 30, 90);
	Rectangle ship2 = new Rectangle(90,225, 30, 90);
	Rectangle ship3 = new Rectangle(150,225, 30, 90);
	Rectangle ship4 = new Rectangle(210,225, 30, 120);
	Rectangle ship5 = new Rectangle(270,225, 30, 120);
	Rectangle ship6 = new Rectangle(330,225, 30, 150);
	Rectangle ship7 = new Rectangle(390,225, 30, 180);
	final Rectangle PLAYER_GRID = new Rectangle(30,450,420,420);
	final Rectangle ENEMY_GRID = new Rectangle(30,30,420,420);
	Point prevPoint;
	ArrayList<Rectangle> shipLocations = new ArrayList<Rectangle>();
	ArrayList<Rectangle> sunkenShips = new ArrayList<Rectangle>();
	ArrayList<Rectangle> playerSunkenShips = new ArrayList<Rectangle>();
	Rectangle selectedShip;
	ArrayList<Image> displayedShips = new ArrayList<Image>();
	ArrayList<Image> hiddenShips = new ArrayList<Image>();
	ArrayList<ImageIcon> hits = new ArrayList<ImageIcon>();
	ArrayList<Point> hitLocations = new ArrayList<Point>();
	ArrayList<ImageIcon> miss = new ArrayList<ImageIcon>();
	ArrayList<Point> missLocations = new ArrayList<Point>();
	Rectangle r = new Rectangle();
	final JButton playButton = new JButton("PLAY");
	final JButton instructionsButton = new JButton("INSTRUCTIONS");
	final JButton exitButton = new JButton("EXIT");
	final JButton startGame = new JButton("BATTLE!");
	final JButton restartGame = new JButton("PLAY AGAIN");
	
	Enemy cpu = new Enemy();
	
	Point player = new Point();
	
	Timer t = new Timer(1, this);
	Image b = new ImageIcon("./Images/missile.png").getImage();
	Point bp = new Point(240, 450);
	double xDistance = 0;
	double yDistance = 0;
	double xInterval = 0;
	double yInterval = 0;
	boolean animationFinished = true;
	boolean enemyTurn = false;
	int iterations  = 0;
	Thread gameThread;
	
	GamePanel(){
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.white);
		this.setFocusable(true);

		ClickListener cl = new ClickListener();
		this.addMouseListener(cl);
		
		DragListener dl = new DragListener();
		this.addMouseMotionListener(dl);
		
	    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());

		shipLocations.add(ship1);shipLocations.add(ship2);shipLocations.add(ship3);
		shipLocations.add(ship4);shipLocations.add(ship5);shipLocations.add(ship6);
		shipLocations.add(ship7);
		
		displayedShips.add(ship3x1);displayedShips.add(ship3x1_2);displayedShips.add(ship3x1_3);
		displayedShips.add(ship4x1);displayedShips.add(ship4x1_2);displayedShips.add(ship5x1);
		displayedShips.add(ship6x1);
		
		hiddenShips.add(ship3x1HOR);hiddenShips.add(ship3x1_2HOR);hiddenShips.add(ship3x1_3HOR);
		hiddenShips.add(ship4x1HOR);hiddenShips.add(ship4x1_2HOR);hiddenShips.add(ship5x1HOR);
		hiddenShips.add(ship6x1HOR);
		gameThread = new Thread ();
        gameThread.start();
		startGame();
	}

	
	public void startGame() {
		state = "start";
		this.setLayout(new FlowLayout(5, 5,0));
	    this.setBorder(new EmptyBorder(10, 10, 10, 10));



	        playButton.setFont(new Font("Bowlby One SC", Font.PLAIN, 30));
	        playButton.setBackground(new Color(0x1B7DF9));
	        playButton.setForeground(Color.white);
	        playButton.setUI(new StyledButtonUI());
	        instructionsButton.setFont(new Font("Bowlby One SC", Font.PLAIN, 18));
	        instructionsButton.setBackground(new Color(0x1B7DF9));
	        instructionsButton.setForeground(Color.white);
	        instructionsButton.setUI(new StyledButtonUI());
	        exitButton.setFont(new Font("Bowlby One SC", Font.PLAIN, 30));
	        exitButton.setBackground(new Color(0x1B7DF9));
	        exitButton.setForeground(Color.white);
	        exitButton.setUI(new StyledButtonUI());
	        this.add(playButton);this.add(instructionsButton);this.add(exitButton);
	        
	        playButton.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent evt) {
	                playButton.setBackground(new Color(0x019FFF));
	            }

	            public void mouseExited(java.awt.event.MouseEvent evt) {
	                playButton.setBackground(new Color(0x1B7DF9));
	            }
	            public void mousePressed(MouseEvent e) {
	            	playButton.setBackground(new Color(0x0068A7));
	            }
	            public void mouseReleased(MouseEvent e) {
	            	playButton.setBackground(new Color(0x019FFF));
	            }
	            public void mouseClicked(MouseEvent e) {
	            	state = "setup";
	            	setup();
	            	
	            }
	        });
	        instructionsButton.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent evt) {
	            	instructionsButton.setBackground(new Color(0x019FFF));
	            }

	            public void mouseExited(java.awt.event.MouseEvent evt) {
	            	instructionsButton.setBackground(new Color(0x1B7DF9));
	            }
	            public void mousePressed(MouseEvent e) {
	            	instructionsButton.setBackground(new Color(0x0068A7));
	            }
	            public void mouseReleased(MouseEvent e) {
	            	instructionsButton.setBackground(new Color(0x019FFF));
	            }
	        });
	        exitButton.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent evt) {
	            	exitButton.setBackground(new Color(0x019FFF));
	            }

	            public void mouseExited(java.awt.event.MouseEvent evt) {
	            	exitButton.setBackground(new Color(0x1B7DF9));
	            }
	            public void mousePressed(MouseEvent e) {
	            	exitButton.setBackground(new Color(0x0068A7));
	            }
	            public void mouseReleased(MouseEvent e) {
	            	exitButton.setBackground(new Color(0x019FFF));
	            }
	            public void mouseClicked(MouseEvent e) {
	            	System.exit(0);
	            }
	        });
		
	}
	public void setup(){
		this.remove(restartGame);
		
    	sunkenShips.clear();
    	cpu.sunkenShips.clear();
    	playerSunkenShips.clear();
    	hits.clear();
    	hitLocations.clear();
    	missLocations.clear();
    	miss.clear();
    	rotate = false;
    	hitSize = 0;
    	missSize = 0;
    	animationFrame = 0;
    	playerSinks = 0;
    	ship1 = new Rectangle(30,225, 30, 90);
    	ship2 = new Rectangle(90,225, 30, 90);
    	ship3 = new Rectangle(150,225, 30, 90);
    	ship4 = new Rectangle(210,225, 30, 120);
    	ship5 = new Rectangle(270,225, 30, 120);
    	ship6 = new Rectangle(330,225, 30, 150);
    	ship7 = new Rectangle(390,225, 30, 180);
    	bp = new Point(240, 450);
    	animationFinished = true;
    	enemyTurn = false;
    	cpu = new Enemy();
    	shipLocations.clear();
		shipLocations.add(ship1);shipLocations.add(ship2);shipLocations.add(ship3);
		shipLocations.add(ship4);shipLocations.add(ship5);shipLocations.add(ship6);
		shipLocations.add(ship7);
		displayedShips.clear();
		displayedShips.add(ship3x1);displayedShips.add(ship3x1_2);displayedShips.add(ship3x1_3);
		displayedShips.add(ship4x1);displayedShips.add(ship4x1_2);displayedShips.add(ship5x1);
		displayedShips.add(ship6x1);
		hiddenShips.clear();
		hiddenShips.add(ship3x1HOR);hiddenShips.add(ship3x1_2HOR);hiddenShips.add(ship3x1_3HOR);
		hiddenShips.add(ship4x1HOR);hiddenShips.add(ship4x1_2HOR);hiddenShips.add(ship5x1HOR);
		hiddenShips.add(ship6x1HOR);
		
		this.setLayout(new FlowLayout(5, 5,0));
	    this.setBorder(new EmptyBorder(10, 10, 10, 10));
	    this.setBackground(new Color(0x0561B2));
        startGame.setFont(new Font("Bowlby One SC", Font.PLAIN, 30));
        startGame.setBackground(new Color(0x1B7DF9));
        startGame.setForeground(Color.white);
        startGame.setUI(new StyledButtonUI());
        this.add(startGame);
        startGame.setBounds(140,30,200,50);
        super.paintComponent(getGraphics());
        startGame.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
            	startGame.setBackground(new Color(0x019FFF));
            }

            
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	startGame.setBackground(new Color(0x1B7DF9));
            }
            public void mousePressed(MouseEvent e) {
            	boolean incompletePlacement = false;
            	for(Rectangle r : shipLocations) {
            		if(!PLAYER_GRID.contains(r)) {
            			incompletePlacement = true;
            		}
            	}
            	if(incompletePlacement) {
            		startGame.setBackground(Color.red);
            	}
            	else {
            	startGame.setBackground(new Color(0x0068A7));
            	}
            }
            public void mouseReleased(MouseEvent e) {
            	startGame.setBackground(new Color(0x019FFF));
            }
            public void mouseClicked(MouseEvent e) {
            	boolean incompletePlacement = false;
            	for(Rectangle r : shipLocations) {
            		if(!PLAYER_GRID.contains(r)) {
            			incompletePlacement = true;
            		}
            	}
            	if(incompletePlacement) {
            		startGame.setBackground(Color.red);
            	}
            	else {
            		state = "in game";
            		inGame();
            	}
            	
            }
        });
	

	}
	public void inGame() {
		cpu.EnemyPlaceShips();
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(state.equals("setup")) {
			this.remove(restartGame);
			this.remove(playButton);
			this.remove(instructionsButton);
			this.remove(exitButton);
			
			
			for(int i = 0; i<7; i++) {
				if(i == shipLocations.size()-1) {
					if(rotate) {
						g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX(), (int)shipLocations.get(i).getY()-10, null);
					}
					else {
						g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX()-10, (int)shipLocations.get(i).getY(), null);
					}
				}
				else {
					g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX(), (int)shipLocations.get(i).getY(), null);
				}
				
			}
			
			this.addMouseListener(new MouseAdapter() {
				@Override //I override only one method for presentation
				public void mouseReleased(MouseEvent e) {
					
					Point current = e.getPoint();

					
						int x = (int) shipLocations.get(index).getX()+15;
						int y = (int) shipLocations.get(index).getY()+15;
						x = 30*(Math.round(x/30));
						y = 30*(Math.round(y/30));
						shipLocations.get(index).setLocation(x, y);

	

						prevPoint = current;
						boolean overlap = false;		

				
						for(int i = 0; i < 7; i++) {
							Rectangle comparator = new Rectangle();
							comparator.y = shipLocations.get(i).y-30;
							comparator.height = shipLocations.get(i).height+60;
							comparator.width = shipLocations.get(i).width+60;
							comparator.x = shipLocations.get(i).x-30;
								if(shipLocations.get(index).intersects(comparator)&& i != index) {
								overlap = true;
							}
						}
						if(!PLAYER_GRID.contains(shipLocations.get(index))||overlap) {
							shipLocations.get(index).setLocation((index*60)+30,225);
							if(shipLocations.get(index).getWidth() > shipLocations.get(index).getHeight()) {
								switchImage();
							}
						}
					
					repaint();
				}
			});

		}
		else if(state.equals("in game")) {
			this.remove(startGame);
			for(int i = 0; i<7; i++) {
				if(i == shipLocations.size()-1) {
					if(rotate) {
						g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX(), (int)shipLocations.get(i).getY()-10, null);
					}
					else {
						g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX()-10, (int)shipLocations.get(i).getY(), null);
					}
				}
				else {
					g.drawImage(displayedShips.get(i), (int)shipLocations.get(i).getX(), (int)shipLocations.get(i).getY(), null);
				}
				
			}
				if(animationFinished == false){
					AffineTransform rot = AffineTransform.getTranslateInstance(bp.getX(), bp.getY());
					if(enemyTurn) {
						rot.rotate(Math.toRadians(180)+Math.atan(xDistance/yDistance), 15, 15);
					}
					else {
						rot.rotate(Math.atan(xDistance/yDistance), 15,15);
					}
					BufferedImage missile = load();
					
					Graphics2D g2 = (Graphics2D)g;
					g2.drawImage(missile, rot, null);
				}
			    	   fire(g);

					sunkenShips = cpu.checkSink(hitLocations);
					playerSunkenShips = checkSink(hitLocations);
					if(!playerSunkenShips.isEmpty()) {
						for(Rectangle r2 : playerSunkenShips) {
							g.fillRect((int)r2.getX(), (int)r2.getY(), (int)r2.getWidth(), (int)r2.getHeight());
						}
					}
					if(!sunkenShips.isEmpty()) {
						for(Rectangle r : sunkenShips) {
							g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
						}
					}
					if(cpu.sunkenShips.size() == 7 || this.sunkenShips.size() == 7) {
						state = "endGame";
					}
		}
		else if(state == "endGame"){
			this.remove(startGame);
			endGame(g);
		}


			draw(g);	
	}
	public void endGame(Graphics g){ 
	    restartGame.setFont(new Font("Bowlby One SC", Font.PLAIN, 30));
	    restartGame.setBackground(new Color(0x1B7DF9));
        restartGame.setForeground(Color.white);
        restartGame.setUI(new StyledButtonUI());
	    
		g.setColor(Color.blue);
		g.drawImage(background, -260, 0, null);
		g.setFont(new Font("Bowlby One SC",Font.BOLD, 50));
		FontMetrics metrics = getFontMetrics(g.getFont());
		 this.add(restartGame);
	        
	        restartGame.setBounds(120,30,250,50);
	        restartGame.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent evt) {
	            	restartGame.setBackground(new Color(0x019FFF));
	            }

	            
	            public void mouseExited(java.awt.event.MouseEvent evt) {
	            	restartGame.setBackground(new Color(0x1B7DF9));
	            }
	            public void mousePressed(MouseEvent e) {
	            	restartGame.setBackground(new Color(0x0068A7));
	            }
	            public void mouseReleased(MouseEvent e) {
	            	restartGame.setBackground(new Color(0x019FFF));
	            }
	            public void mouseClicked(MouseEvent e) {
	            	state = "setup";
	            	setup();
	            }
	        });
		if(sunkenShips.size() == 7) {
			g.drawString("YOU", (SCREEN_WIDTH - metrics.stringWidth("YOU"))/2, 390);
			g.drawString("WIN", (SCREEN_WIDTH - metrics.stringWidth("WIN"))/2, 450);
		}
		else {
			g.drawString("YOU", (SCREEN_WIDTH - metrics.stringWidth("YOU"))/2, 390);
			g.drawString("LOSE", (SCREEN_WIDTH - metrics.stringWidth("LOSE"))/2, 450);
		}
	

		repaint();
	}
	BufferedImage load () {
		BufferedImage img = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
		try {
			if(!enemyTurn) {
				if(iterations == 26) {
					img = ImageIO.read(new File("./Images/blank.png"));
				}
				else {
					iterations++;
					img = ImageIO.read(new File("./Images/missile.png"));
				}
			}
			else {
				if(iterations == 25) {
					img = ImageIO.read(new File("./Images/blank.png"));
				}
				else {
					iterations++;
					img = ImageIO.read(new File("./Images/missile.png"));
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	public void fire (Graphics g) {
		boolean copy = false;
		hitSize = hits.size();
		missSize = miss.size();
		for(Point i : hitLocations) {
			if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
				copy = true;
			}
		}
		for(Point i : missLocations) {
			if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
				copy = true;
			}
		}
		if(hit && copy==false && (animationFinished)) {
			hits.add(new ImageIcon("./Images/hit.png")); 
			hitLocations.add(new Point ((int)player.getX(), (int)player.getY()));
			hitSize++;

		}
		else if(copy==false && (animationFinished)){
			miss.add(new ImageIcon("./Images/miss.png")); 
			missLocations.add(new Point ((int)player.getX(), (int)player.getY()));
			missSize++;
			animationFrame = 1;


		}
		for(int i = 0; i<hits.size(); i++) {
				hits.get(i).paintIcon(this, g, (int)hitLocations.get(i).getX(), (int)hitLocations.get(i).getY());
		}
		for(int i = 0; i<miss.size(); i++) {
			if(missLocations.get(i).getX()!=0) {
					miss.get(i).paintIcon(this, g, (int)missLocations.get(i).getX(), (int)missLocations.get(i).getY());			    			
				}
		}

			if ((missSize+hitSize) % 2 == 0) {
				enemyTurn();
				
			}

        
	}
	public void enemyTurn() {
		enemyTurn = true;
		int x=0; int y=0;
		Point enemyTarget = new Point();
		boolean copy2 = true;
		if(animationFinished) {
			while(copy2) {
				if(cpu.targetLock == false) {
				enemyTarget = new Point(((int)(Math.random() *14) + 1)*30, ((int)(Math.random() *14) + 15)*30);

				
				x = (int) enemyTarget.getX();
				y = (int) enemyTarget.getY();
				x = 30*(Math.round(x/30));
				y = 30*(Math.round(y/30));
				player.setLocation(x, y);
			
			cpu.initialX = x;
			cpu.initialY = y;
			cpu.recentX = x;
			cpu.recentY = y;
			}
			else {
				if(cpu.targetLockDirection == 1) {
					if(cpu.recentX == 14*30) {
						cpu.targetLockDirection = 2;
						cpu.recentX = cpu.initialX;
						cpu.recentY = cpu.initialY;
					}
					else {

						x = 30*(Math.round((cpu.recentX+30)/30));
						y = 30*(Math.round(cpu.recentY/30));
						cpu.recentX = x;
						cpu.recentY = y;
					}
				}
				if(cpu.targetLockDirection == 2) {
					if(cpu.recentX == 30) {
						cpu.targetLockDirection = 3;
						cpu.recentX = cpu.initialX;
						cpu.recentY = cpu.initialY;
					}
					else {
						x = 30*(Math.round((cpu.recentX-30)/30));
						y = 30*(Math.round(cpu.recentY/30));
						cpu.recentX = x;
						cpu.recentY = y;
					}
				}
				if(cpu.targetLockDirection == 3) {
					if(cpu.recentY == 28*30) {
						cpu.targetLockDirection = 4;
						cpu.recentX = cpu.initialX;
						cpu.recentY = cpu.initialY;
					}
					else {
						x = 30*(Math.round(cpu.recentX/30));
						y = 30*(Math.round((cpu.recentY+30)/30));
						cpu.recentX = x;
						cpu.recentY = y;
					}
				}
				if(cpu.targetLockDirection == 4) {
					if(cpu.recentY == 450) {
						cpu.targetLockDirection = 1;
						cpu.recentX = cpu.initialX;
						cpu.recentY = cpu.initialY;
					}
					else {
						x = 30*(Math.round(cpu.recentX/30));
						y = 30*(Math.round((cpu.recentY-30)/30));
						cpu.recentX = x;
						cpu.recentY = y;
					}
				}
			}
				

				player.setLocation(x, y);
				copy2 = false;
				for(Point i : hitLocations) {
					if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
						copy2 = true;
						break;
					}
					else {
						copy2 = false;
					}
				}
				if(!copy2) {
					for(Point i : missLocations) {
						if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
							copy2 = true;
							break;
						}
						else {
							copy2 = false;
						}
					}
				}

			}
			enemyTarget.setLocation(x,y);

			
			for(int i = 0; i < 7; i++) {
				if(shipLocations.get(i).contains(enemyTarget)) {
					cpu.targetLock = true;
					hit = true;
					break;
				}
				else if(PLAYER_GRID.contains(enemyTarget)){
					hit = false;
				}
			}
			if(!hit) {
				cpu.recentX = cpu.initialX;
				cpu.recentY = cpu.initialY;
					if(cpu.targetLockDirection == 4) {
						cpu.targetLockDirection = 1;
							
					}
					else {
						cpu.targetLockDirection++;
					}
				}

			
				
				
			bp.setLocation((int) (((Math.random()*14)+1)*30)+15, (int) (((Math.random()*14)+1)*30)+15);
			xDistance = (player.getX() - bp.getX());
			yDistance = (bp.getY() - player.getY());
			yInterval = 0;
			xInterval = 0;
			t.start();
		}
		missSize++;
		System.out.println(missSize);
	}

	public void draw(Graphics g){
		if(state.equals("setup")) {
			g.setColor(Color.BLACK);
			for(int i = 1; i<((SCREEN_HEIGHT/UNIT_SIZE));i++) {
				g.drawLine(i*UNIT_SIZE, SCREEN_HEIGHT/2, i*UNIT_SIZE, SCREEN_HEIGHT-30);
				if(i>14) {
					g.drawLine(30, i*UNIT_SIZE, SCREEN_WIDTH-30, i*UNIT_SIZE);
				}
			}
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(10));
			g2.draw(new Line2D.Float(30,SCREEN_WIDTH-30 , SCREEN_HEIGHT/2, SCREEN_HEIGHT/2));
		}
		else if(state.equals("start")){
			startScreen(g);
		}
		else if(state.equals("in game")) {
			g.setColor(Color.BLACK);
			for(int i = 1; i<((SCREEN_HEIGHT/UNIT_SIZE));i++) {
				g.drawLine(i*UNIT_SIZE, 30, i*UNIT_SIZE, SCREEN_HEIGHT-30);
				g.drawLine(30, i*UNIT_SIZE, SCREEN_WIDTH-30, i*UNIT_SIZE);
				}
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(10));
			g2.draw(new Line2D.Float(30,SCREEN_WIDTH-30 , SCREEN_HEIGHT/2, SCREEN_HEIGHT/2));
		}
		
	
	}
	public void startScreen(Graphics g){
		g.setColor(Color.blue);
		g.drawImage(background, -260, 0, null);
		g.setFont(new Font("Bowlby One SC",Font.BOLD, 50));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("BATTLESHIP", (SCREEN_WIDTH - metrics.stringWidth("BATTLESHIP"))/2, 90);
        playButton.setBounds(140,375,200,50);
        instructionsButton.setBounds(140,435,200,50);
        exitButton.setBounds(140,495,200,50);
	

		repaint();
	}
	public void switchImage() {
		hiddenShips.add(index, displayedShips.get(index));
		displayedShips.remove(index);
		displayedShips.add(index, hiddenShips.get(index+1));
		hiddenShips.remove(index+1);
		
		int tmp = shipLocations.get(index).width;
		shipLocations.get(index).width = shipLocations.get(index).height;
		shipLocations.get(index).height = tmp;
		repaint();
	}

	private class ClickListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			if(state.equals("setup")) {

					prevPoint = e.getPoint();
					for(int i =0; i < 7; i++) {
						if(shipLocations.get(i).contains(prevPoint)) {
							index  = i;
							break;
						}
					}

			}
			else if (state.equals("in game") && animationFinished) {
				for(int i = 0; i < 7; i++) {
					if(cpu.shipLocations.get(i).contains(e.getPoint())) {
						int x = (int) e.getX();
						int y = (int) e.getY();
						x = 30*(Math.round(x/30));
						y = 30*(Math.round(y/30));
						player.setLocation(x, y);
						hit = true;
						break;
					}
					else if(ENEMY_GRID.contains(e.getPoint())){
						int x = (int) e.getX();
						int y = (int) e.getY();
						x = 30*(Math.round(x/30));
						y = 30*(Math.round(y/30));
						player.setLocation(x, y);
						hit = false;
					}
				}
				int index = (int)(Math.random()*6);
				bp.setLocation(shipLocations.get(index).getCenterX()-15, shipLocations.get(index).getCenterY());
				xDistance = (player.getX() - bp.getX());
				yDistance = (bp.getY() - player.getY());
				yInterval = 0;
				xInterval = 0;
				boolean copy = false;
				for(Point i : hitLocations) {
					if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
						copy = true;
					}
				}
				for(Point i : missLocations) {
					if((int)i.getX() == (int)player.getX() && (int)i.getY() == (int)player.getY()) {
						copy = true;
					}
				}
				if(!copy) {
					t.start();
				}
		}
	}		
}
	private class DragListener extends MouseMotionAdapter{
		
		public void mouseDragged(MouseEvent e) {

			if(state.equals("setup")) {
				Point current = e.getPoint();
				shipLocations.get(index).translate((int)(current.getX()-prevPoint.getX()), (int)(current.getY()-prevPoint.getY()));
				prevPoint = current;	
			}
				
			repaint();
			
		}
	}
	private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	if(e.getKeyChar() == 'r') {
            		switchImage();
            		if(rotate && index == 6) {
            			rotate = false;
            		}
            		else if(index == 6){
            			rotate = true;
            		}
            	}
            }
            return false;
        }
    }
	@Override
	public void actionPerformed(ActionEvent e) {
			animationFinished = false;
			bp.translate((int)xInterval, (int)-yInterval);
			yInterval = (yInterval + yDistance/1000)*1.1;
			xInterval = (xInterval + xDistance/1000)*1.1;
			repaint();
			Rectangle r = new Rectangle((int)player.getX(), (int)player.getY(), 30,30);
			if(r.contains((int)bp.getX()+15,(int)bp.getY()+15)) {
				t.stop();
				animationFinished = true;
				iterations = 0;
				enemyTurn = false;
			}
			
	}
	public ArrayList<Rectangle> checkSink(ArrayList<Point> hits) {
		int sinkSize = playerSunkenShips.size();
		playerSinks = 0;
		playerSunkenShips.clear();
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
					playerSinks++;
					playerSunkenShips.add(shipLocations.get(i));
					if(sinkSize < playerSunkenShips.size()) {
						cpu.targetLock = false;
					}
				}
				
			}
			else{
				if(NOH == (shipLocations.get(i).getWidth()/30)) {
					playerSinks++;
					playerSunkenShips.add(shipLocations.get(i));
					if(sinkSize < playerSunkenShips.size()) {
						cpu.targetLock = false;
					}
				}
			}
		}
		return playerSunkenShips;
	}

}
