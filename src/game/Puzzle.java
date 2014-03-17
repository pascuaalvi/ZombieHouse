package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Puzzle {
	private static int[][] puzzle = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
	private static ArrayList<Integer> shuff  = new ArrayList<Integer>();
	static JPanel panel = new JPanel();

	public static ImageIcon getImage(int file){
		return new ImageIcon("images/puzzle/puzzle"+file+".png");
	}
	static JLabel[][] images = new JLabel[puzzle.length][puzzle.length];
	public static void fill() {
		
		panel.removeAll();
		GridLayout grid = new GridLayout(puzzle.length,puzzle.length,1,1);
		panel.setLayout(grid);
		
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle.length; j++) {
				images[i][j] = new JLabel();
				panel.add(images[i][j]);
				images[i][j].setIcon(getImage(puzzle[i][j]));
			}
		}

	}
	
	static void shuffle(){
		for(int x =0;x<9;x++){
			shuff.add(x+1);
		}
		Collections.shuffle(shuff);
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle.length; j++) {
				puzzle[i][j] = shuff.get((puzzle[i][j])-1);
				
			}}
	}
	
	public static void move(String dir){
		if(!puzzleWon()){
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle.length; j++) {
			if (puzzle[i][j]==9	){
				int swap = puzzle[i][j];
				if(dir!=null){
				if (dir.equals("left") && inLimits(i,j-1)){
					puzzle[i][j] = puzzle[i][j-1];
					puzzle[i][j-1] = swap;
				}
				else if (dir.equals("right") && inLimits(i,j+1)){
					puzzle[i][j] = puzzle[i][j+1];
					puzzle[i][j+1] = swap;
				}
				else if (dir.equals("up") && inLimits(i-1,j)){
					puzzle[i][j] = puzzle[i-1][j];
					puzzle[i-1][j] = swap;
				}
				else if (dir.equals("down") && inLimits(i+1,j)){
					puzzle[i][j] = puzzle[i+1][j];
					puzzle[i+1][j] = swap;
					
				}
				dir=null;
				fill();
			}}
			}
			}
		}
		if(puzzleWon()){
			panel.removeAll();
			JLabel won = new JLabel();
			panel.add(won);
			panel.setLayout(new FlowLayout());
			won.setIcon(getImage(0));
		}
	}
	
	public static void autoSolve(){
		puzzle[0][0]=1; puzzle[0][1]=2; puzzle[0][2]=3;
				puzzle[1][0]=4; puzzle[1][1]=5;puzzle[1][2]=6;
				puzzle[2][0]=7;puzzle[2][1]=8;puzzle[2][2]=9;
				fill();
				if(puzzleWon()){ System.out.println("WOOP");
				panel.removeAll();
				JLabel won = new JLabel();
				panel.add(won);
				panel.setLayout(new FlowLayout());
				won.setIcon(getImage(0));
				}
	}
	
	public static boolean puzzleWon(){
		if(puzzle[0][0]==1 && puzzle[0][1]==2 && puzzle[0][2]==3&&
				puzzle[1][0]==4 &&puzzle[1][1]==5 &&puzzle[1][2]==6 &&
				puzzle[2][0]==7 &&puzzle[2][1]==8 &&puzzle[2][2]==9){
			return true;
		}
		else return false;
	}
	public static boolean inLimits(int i, int j){
		
		return 
				i>=0 && i<=2 && j>=0 && j<=2;
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(300,300));
		shuffle();
		fill();
		 frame.addKeyListener(new KeyAdapter() {
	            public void keyPressed(KeyEvent e) {
	            	 int keyCode = e.getKeyCode();
	            	    switch( keyCode ) { 
	            	        case KeyEvent.VK_UP:
	            	        	 move("up");
	            	            break;
	            	        case KeyEvent.VK_DOWN:
	            	        	 move("down");
	            	            break;
	            	        case KeyEvent.VK_LEFT:
	            	            move("left");
	            	            break;
	            	        case KeyEvent.VK_RIGHT :
	            	        	 move("right");
	            	            break;
	            	     }
	            }
	        });
		JPanel blah = new JPanel();
		
		 JButton solve = new JButton("Solve");
		 solve.setFocusable(false);
		solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                autoSolve();
            }
        }); 
		
		blah.add(panel);
		JPanel bottom = new JPanel();
		bottom.add(solve);
		frame.add(blah);
		frame.add(bottom, BorderLayout.SOUTH);
		frame.setVisible(true);
		
		
	}
	
}
