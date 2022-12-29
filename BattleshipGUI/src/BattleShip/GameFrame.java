package BattleShip;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GameFrame(){
		GamePanel ip = new GamePanel();
		this.add(ip);
		this.setTitle("BattleShip");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	    this.setContentPane(ip);


	    


	}

		


}