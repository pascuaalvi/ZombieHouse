package data;

import gui.MainFrame;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class StartScreenData {
	
	private MainFrame frame;
	private boolean showSingleMenu = false;
	private boolean showMultiMenu = false;
	
	private Image titleImg = new ImageIcon("images/startScreen/TitleImage.JPG")
			.getImage();

	private Image singleImg = new ImageIcon(
			"images/startScreen/SinglePlayer_.png").getImage();
	private Image multiImg = new ImageIcon(
			"images/startScreen/Multiplayer_.png").getImage();
	private Image quitImg = new ImageIcon("images/startScreen/Quit_.png")
			.getImage();
	private Image pointerImg = new ImageIcon("images/startScreen/pointer_.png")
			.getImage();

	private Image newGameImg = new ImageIcon("images/startScreen/NewGame_.png")
			.getImage();
	private Image loadGameImg = new ImageIcon(
			"images/startScreen/LoadGame_.png").getImage();

	private Image serverImg = new ImageIcon("images/startScreen/Server_.png")
			.getImage();
	private Image clientImg = new ImageIcon("images/startScreen/Client_.png")
			.getImage();

	private Rectangle singleRect;
	private Rectangle multiRect;
	private Rectangle quitRect;
	private Rectangle serverRect;
	private Rectangle clientRect;
	private Rectangle newGameRect;
	private Rectangle loadGameRect;
	
	private int selectedIndex = 0;
	
	public StartScreenData(MainFrame frame){
		this.frame = frame;
		assembleRectangles();
	}

	private void assembleRectangles() {
		int img1X = frame.getWidth() - 400;
		int img2X = frame.getWidth() - 800;
		int imgHeight = 50;
		
		this.singleRect = new Rectangle(img1X,400,200,imgHeight);
		this.multiRect = new Rectangle(img1X,470,190,imgHeight);
		this.quitRect = new Rectangle(img1X, 540, 80, imgHeight);

		this.clientRect = new Rectangle(img2X, 450, 150, imgHeight);
		this.serverRect = new Rectangle(img2X, 520, 150, imgHeight);

		this.newGameRect = new Rectangle(img2X, 450, 170, imgHeight);
		this.loadGameRect = new Rectangle(img2X, 520, 170, imgHeight);
	}

	public Image getTitleImg() {
		return titleImg;
	}

	public Image getSingleImg() {
		return singleImg;
	}

	public Image getMultiImg() {
		return multiImg;
	}

	public Image getQuitImg() {
		return quitImg;
	}

	public Image getPointerImg() {
		return pointerImg;
	}

	public Image getNewGameImg() {
		return newGameImg;
	}

	public Image getLoadGameImg() {
		return loadGameImg;
	}

	public Image getServerImg() {
		return serverImg;
	}

	public Image getClientImg() {
		return clientImg;
	}

	public Rectangle getSingleRect() {
		return singleRect;
	}

	public Rectangle getMultiRect() {
		return multiRect;
	}

	public Rectangle getQuitRect() {
		return quitRect;
	}

	public Rectangle getServerRect() {
		return serverRect;
	}

	public Rectangle getClientRect() {
		return clientRect;
	}

	public Rectangle getNewGameRect() {
		return newGameRect;
	}

	public Rectangle getLoadGameRect() {
		return loadGameRect;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public boolean isSingleVisible(){
		return this.showSingleMenu;
	}
	
	public boolean isMultiVisible(){
		return this.showMultiMenu;
	}
	
	public void showSingleMenu(){
		this.showSingleMenu = true;
	}
	
	public void showMultiMenu(){
		this.showMultiMenu = true;
	}
	
	public void resetMenus(){
		this.showMultiMenu = false;
		this.showSingleMenu = false;
	}
}
