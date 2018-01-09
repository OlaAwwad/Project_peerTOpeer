package Client;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Project name : PeerToPeer Class : Button
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Button used in the project
 * 
 * @author Vlado Mitrovic
 */


public class Button extends JButton{

	private Font font = new Font ("Century Gothic",Font.BOLD, 15);
	private Color color = new Color(255, 229, 204);
	private Color colorMouse = new Color(255, 204, 153);


	public Button(String text) {
		super(text);
		setBackground(this.color);
		
		setFocusable(false);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setPreferredSize(new Dimension(205, 70));	
		setFont(font);	
		setOpaque(true);
		addMouseListener(new Mouse());
		
	}
	
	public Button(String text, ImageIcon icon) {
		super(text, icon);
		setBackground(this.color);
		
		setFocusable(false);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setPreferredSize(new Dimension(205, 70));	
		setFont(font);	
		setOpaque(true);
		addMouseListener(new Mouse());
		
		
	}
	
	class Mouse extends MouseAdapter{
		
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			super.mouseEntered(e);
			if(isEnabled()){
				setBackground(colorMouse);
				setOpaque(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			setBackground(color);
		}
		
	}
	
	public void setmouseColor(int r, int j, int b) {
		
		this.colorMouse=new Color(r, j, b);
		
	}
	
	

}
