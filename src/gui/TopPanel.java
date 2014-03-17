package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

public class TopPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	private MainFrame frame;

	private float alpha = 0.5f;
	private boolean hoverEnd = false;
	private boolean hoverSave = false;
	
	// dimensions
	private int endX;
	private int endY;
	private int endWidth;
	private int endHeight;

	private int endTextX;
	private int endTextY;

	private int saveX;
	private int saveY;
	private int saveWidth;
	private int saveHeight;

	private int saveTextX;
	private int saveTextY;

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		

		setDimensions();

		drawSaveButton(g);
		drawEndButton(g);
		drawHover(g);
	}
	
	private void drawHover(Graphics2D g) {
		g.setColor(Color.white);
		if(hoverEnd){
			g.fillRoundRect(endX, endY, endWidth, endHeight, 5, 5);
		}else if(hoverSave){
			g.fillRoundRect(saveX, saveY, saveWidth, saveHeight, 5, 5);
		}
	}
	
	private void drawSaveButton(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRoundRect(saveX, saveY, saveWidth, saveHeight, 5, 5);
		g.setColor(Color.white);
		g.drawString("Save", saveTextX, saveTextY);
	}
	
	private void drawEndButton(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRoundRect(endX, endY, endWidth, endHeight, 5, 5);
		g.setColor(Color.white);
		g.drawString("End", endTextX, endTextY);
	}

	public TopPanel(MainFrame mf) {
		this.frame = mf;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		repaint();
	}

	private void setDimensions() {
		this.endX = getWidth() - 43;
		this.endY = 5;
		this.endWidth = 35;
		this.endHeight = 20;
		this.endTextX = getWidth() - 36;
		this.endTextY = 20;

		this.saveX = getWidth() - 135;
		this.saveY = 5;
		this.saveWidth = 40;
		this.saveHeight = 20;
		this.saveTextX = getWidth() - 128;
		this.saveTextY = 20;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.hoverEnd = false;
		this.hoverSave = false;
		if (x > endX && x < endX+endWidth && y > endY && y < endY+endHeight) {
			this.hoverEnd = true;
			//this.repaint();
		}else if (x > saveX && x < saveX+saveWidth && y > saveY && y < saveY+saveHeight) {
			this.hoverSave = true;
			//this.repaint();
		}
		frame.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Pressed");
		int x = e.getX();
		int y = e.getY();
		
		if (x > endX && x < endX+endWidth && y > endY && y < endY+endHeight) {
			int i = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to end?");
			if (i == 0) {
				System.exit(1);
			}
		}else if(x > saveX && x < saveX+saveWidth && y > saveY && y < saveY+saveHeight){
			/*
			SaveLoad saveLoad = new SaveLoad();
			try {
				saveLoad.saveXML(frame);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
