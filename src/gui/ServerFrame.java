package gui;

import game.Game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thread.ClockThread;

import control.Master;

public class ServerFrame extends JFrame implements ActionListener {

	private int numOfPlayers = 0;
	private JTextArea log;
	private JButton start;
	private JButton back;

	private ClockThread clk;
	private int port;
	private int gameClock;
	private int broadcastClock;
	private Game game;
	private MainFrame frame;

	/**
	 * frame will have display the... - Port number, - Number of players
	 * connected to server, - list of players that are connected a button to
	 * initiate the game.
	 * 
	 * @param game
	 * @param broadcastClock
	 * @param gameClock
	 * @param port
	 */
	public ServerFrame(ClockThread clk, int port, int gameClock,
			int broadcastClock, Game game) {
		this.clk = clk;
		this.port = port;
		this.gameClock = gameClock;
		this.broadcastClock = broadcastClock;
		this.game = game;
		this.frame = game.getFrame();
		frameSetup();
		panelSetup();
		this.setFocusable(true);
		this.requestFocus();
		this.setVisible(true);
	}

	private void panelSetup() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(100, 100, 100));
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());

		this.log = new JTextArea();
		this.log.setBounds(50, 150, this.getWidth() - 120,
				this.getHeight() - 300);
		this.log.setEditable(false);
		this.log.append("SERVER WILL LISTEN ON PORT " + port + "\n");
		panel.add(log);

		this.start = new JButton("Start");
		this.start.setBounds(this.getWidth() - 180, this.getHeight() - 100, 80,
				32);
		this.start.addActionListener(this);
		panel.add(start);

		this.back = new JButton("Back");
		this.back.setBounds(80, this.getHeight() - 100, 80, 32);
		this.back.addActionListener(this);
		panel.add(back);

		this.add(panel);
	}

	private void frameSetup() {
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setTitle("Server Status");
		this.setLayout(null);
		this.setSize(600, 550);
		this.setLocation(300, 100);
	}

	public void awaitConnections() {
		int nClients = -1; // or prompt user for numOfPlayers
		while(nClients<0){
			String str = JOptionPane.showInputDialog(null, "How many players? (Up to 4 only)");
			try{
				int num = Integer.parseInt(str);
				if(0<num && num<=4){
					nClients = num;
					break;
				}
				JOptionPane.showMessageDialog(null, "Please enter a number between\n1 and 4");
				continue;
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, "Please enter a number between\n1 and 4");
				continue;
			}
		}
		numOfPlayers = 0;
		//sat.start();
		try {
			Master[] connections = new Master[nClients];
			// Wait for connections
			ServerSocket ss = new ServerSocket(port);
			//Socket connectionToServer = new Socket("localhost",port);
			while (true) {
				// Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: "
						+ s.getInetAddress() + " Number of Players: "
						+ (numOfPlayers + 1) + "\n");
				//sat.notify();
				
				numOfPlayers++;
				int uid = game.getData().createPlayer();
				game.getData().getHero(uid).initialise(game.getData());
				connections[--nClients] = new Master(s, uid, broadcastClock,
						game);
				connections[nClients].start();
				if (nClients == 0) {
					//JOptionPane.showConfirmDialog(null, "Start game?");
					System.out.println("Game Begins\n");
					multiUserGame(clk,game,connections);
					System.out.println("Game Finished\n");
					return;
				}
			}
		} catch (Exception e) {
			System.err.println("I/O error: " + e.getMessage());
		}
	}

	/**
	 * Begin the multiplayer game
	 */
	public void multiUserGame(ClockThread clk, Game game, Master... connections) throws IOException{
		byte[] state = game.toByteArray();
		clk.start();
		
		while(atleastOneConnection(connections)) {
			game.setState(Game.READY);
			pause(3000);
			game.setState(Game.PLAYING);
			// now, wait for the game to finish
			while(game.state == Game.PLAYING) {
				Thread.yield();
			}
			// If we get here, then we're in game over mode
			pause(3000);
			// Reset board state
			game.setState(Game.WAITING);
			game.fromByteArray(state);
		}
		// when we get here, the game is finished
	}
	
	private void pause(int i) {
		try {
			Thread.currentThread().sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Check whether or not there is at least one connection alive.
	 *
	 * @param connections
	 * @return
	 */
	private static boolean atleastOneConnection(Master... connections) {
		for (Master m : connections) {
			if (m.isAlive()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			frame.setVisible(false);
			this.start.setEnabled(false);
			this.back.setEnabled(false);
			//this.frame.stopAmbience();
			this.awaitConnections();
		} else if (e.getSource() == back) {
			this.dispose();
		}
	}

}
