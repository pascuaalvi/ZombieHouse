package gui;

//import game.Game;

import game.Game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

//import control.SoundThread;

//import data.GameData;



public class MainFrame extends JFrame{

	private MainPanel mp;
	private TopPanel tp;
	private BottomPanel bp;
	private InventoryPanel ip;
	
	private Game game;

	public MainFrame(){
		frameSetup();
		setupPanel();
		looks();
		this.setVisible(true);
		
		//this.st = new SoundThread("audio/ambience.wav", true);
		//st.start();
	}

	private void looks() {
		LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
		int index = 1;
		try {
			UIManager.setLookAndFeel(laf[index].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void frameSetup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setLayout(null);
		/*
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		*/
		this.setSize(1300, 750);
	}

	private void setupPanel() {
		this.tp = new TopPanel(this);
		this.tp.setBounds(0, 0, this.getWidth(), 30);
		this.bp = new BottomPanel(this);
		this.bp.setBounds(0, this.getHeight()-60-30, this.getWidth(), 60);
		this.mp = new MainPanel(this);
		this.mp.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.ip = new InventoryPanel(this);
		this.ip.setBounds(0, this.getHeight()- 400, 110, 310);

		this.add(ip);
		this.add(tp);
		this.add(bp);
		this.add(mp);
		ip.setVisible(false);
		bp.setVisible(false);
	}

	
	
	/*
public Game getGame(){
		return this.game;
	}	

	public void setGame(Game game){
		this.game = game;
	}

	public void setGameData(GameData gd){
		this.gsd = gd;
	}

	public GameData getGameData(){
		return this.gsd;
	}
	*/
	
	public Game getGame(){
		return this.game;
	}	

	public void setGame(Game game){
		this.game = game;
	}

	public void setKeyListeners(KeyListener... k){
		for(KeyListener keyL : k){
			this.addKeyListener(keyL);
		}
	}

	public void toggleBp() {
		if(bp.isVisible()){
			bp.setVisible(false);
		}else{
			bp.setVisible(true);
		}
	}

	public void toggleIp(){
		if(ip.isVisible()){
			ip.setVisible(false);
		}else{
			ip.setVisible(true);
		}
	}

	public void closeInventory() {
		ip.setVisible(false);
	}

	/*
	public void stopAmbience(){
		st.stopLoop();
	}
	*/

	public MainPanel getMp(){
		return this.mp;
	}

	public InventoryPanel getIp() {
		return this.ip;
	}

	public boolean isIpVisible() {
		return ip.isVisible();
	}
}
