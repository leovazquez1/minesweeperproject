import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyMouseAdapter extends MouseAdapter {
	private int Flags = 0;


	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;

		case 3:		//Right mouse button
			Component r = e.getComponent();
			while (!(r instanceof JFrame)) {
				r = r.getParent();
				if (r == null) {
					return;
				}
			}
			JFrame myFrame1 = (JFrame) r;
			MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);
			Insets myInsets1 = myFrame1.getInsets();
			int x2 = myInsets1.left;
			int y2 = myInsets1.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel1.x = x3;
			myPanel1.y = y3;
			myPanel1.mouseDownGridX = myPanel1.getGridX(x3, y3);
			myPanel1.mouseDownGridY = myPanel1.getGridY(x3, y3);
			myPanel1.repaint();
			break;

		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {

		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if(!myPanel.winGame()){
				if(!myPanel.endGame()){
					if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
						//Had pressed outside
						//Do nothing
					} else {
						if ((gridX == -1) || (gridY == -1)) {
							//Is releasing outside
							//Do nothing
						} else {
							if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
								//Released the mouse button on a different cell where it was pressed
								//Do nothing
							} else {
								//Released the mouse button on the same cell where it was pressed

								if(!myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED)){  //if square is flagged cannot color over it

									if(!myPanel.mineCompare(myPanel.mouseDownGridX, myPanel.mouseDownGridY)){

										myPanel.isBlank(myPanel.mouseDownGridX, myPanel.mouseDownGridY);
										myPanel.HiddenNumber[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = true;
										myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.LIGHT_GRAY;

										myPanel.repaint();

									}
								}
 							}

						}
					}
				}
				if(myPanel.endGame()){
					JOptionPane.showMessageDialog(null, "GameOver. YOU LOSE", "Game Over", 0);
				}
			}
			if(myPanel.winGame()){
				URL imagelocation = null;
				try {
					imagelocation = new URL ("http://www.clipartbest.com/cliparts/pc5/eya/pc5eyaGMi.png");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Congratulations, YOU WIN!!", "Game Over", JOptionPane.PLAIN_MESSAGE, new ImageIcon(imagelocation));
			}
			//myPanel.repaint();
			break;

		case 3:		//Right mouse button
			Component r = e.getComponent();
			while (!(r instanceof JFrame)) {
				r = r.getParent();
				if (r == null) {
					return;
				}
			}
			JFrame myFrame1 = (JFrame) r;
			MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);
			Insets myInsets1 = myFrame1.getInsets();
			int x2 = myInsets1.left;
			int y2 = myInsets1.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel1.x = x3;
			myPanel1.y = y3;
			int gridX1 = myPanel1.getGridX(x3, y3);
			int gridY1 = myPanel1.getGridY(x3, y3);
			if(!myPanel1.endGame()){
				if ((myPanel1.mouseDownGridX == -1) || (myPanel1.mouseDownGridY == -1)) {
					//Had pressed outside
					//Do nothing
				} else {
					if ((gridX1 == -1) || (gridY1 == -1)) {
						//Is releasing outside
						//Do nothing
					} else {
						if ((myPanel1.mouseDownGridX != gridX1) || (myPanel1.mouseDownGridY != gridY1)) {
							//Released the mouse button on a different cell where it was pressed
							//Do nothing
						} else {
							//Released the mouse button on the same cell where it was pressed

							if(!myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY].equals(Color.RED)){ //Controls the amount of possible red flags
								if(Flags<10){
									Flags++;
									myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = Color.RED;
									myPanel1.repaint();
								}

							}
							else{ //Reduces counter when red flag is removed
								Flags--;
								myPanel1.colorArray[myPanel1.mouseDownGridX][myPanel1.mouseDownGridY] = Color.WHITE;
								myPanel1.repaint();
							}


						}
					}
				}
			}


			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}

}
