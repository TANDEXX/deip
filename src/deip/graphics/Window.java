package deip.graphics;

import deip.*;
import deip.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window {
	
	public static boolean cursorHoveringMenu = false;
	public static boolean mouseClicked = false;
	public static Point mousePos = new Point(0, 0);
	public static JFrame w;
	public static JPanel p;
	public static Toolkit t;
	public static Dimension ss;
	public static WindowListener wl = new WindowListener() {
		
		public void windowOpened(WindowEvent we) {}
		public void windowClosed(WindowEvent we) {}
		public void windowIconified(WindowEvent we) {}
		public void windowDeiconified(WindowEvent we) {}
		public void windowActivated(WindowEvent we) {}
		public void windowDeactivated(WindowEvent we) {}
		public void windowClosing(WindowEvent we) {
			
			Root.shutdown();
			
		}
		
	};
	public static KeyListener kbdl = new KeyListener() {
		
		public void keyTyped(KeyEvent ke) {}
		public void keyPressed(KeyEvent ke) {
			
			kbdMatch(ke.getKeyChar(), ke.getKeyCode(), true);
			
		}
		public void keyReleased(KeyEvent ke) {
			
			kbdMatch(ke.getKeyChar(), ke.getKeyCode(), false);
			
		}

	};
	public static MouseListener muel = new MouseListener() {
		public void mouseClicked(MouseEvent me) {}
		public void mousePressed(MouseEvent me) {
			
			mouseAction(me, true);
			
		}
		public void mouseReleased(MouseEvent me) {
			
			mouseAction(me, false);
			
		}
		public void mouseEntered(MouseEvent me) {}
		public void mouseExited(MouseEvent me) {}
	};
	public static MouseMotionListener mueml = new MouseMotionListener() {
		public void mouseDragged(MouseEvent me) {
			
			mouseAction(me, true);
			
		}
		public void mouseMoved(MouseEvent me) {
			
			mouseAction(me, false);
			
		}
	};
	
	public static void mouseAction(MouseEvent me, boolean clicked) {
		
		mouseClicked = clicked;
		boolean widgetClicked = clicked;
		if (cursorHoveringMenu) {
			widgetClicked = false;
		}
		mousePos = me.getPoint();
		Root.game.mouseAction(me.getX() - (p.getWidth() / 2), me.getY() - (p.getHeight() / 2), widgetClicked);
		
	}
	
	public static void init() throws Throwable {
		
		t = Toolkit.getDefaultToolkit();
		ss = t.getScreenSize();
		//w = new Paint.CFrame();
		w = new JFrame();
		p = new Paint.CFrame();
		w.add(p);
		w.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		w.setTitle(Conf.winTitle);
		int locX = ss.width / 10;
		int locY = ss.height / 10;
		w.setLocation(locX, locY);
		w.setSize(ss.width - (locX * 2), ss.height - (locY * 2));
		
		w.addWindowListener(wl);
		w.addKeyListener(kbdl);
		p.addMouseListener(muel);
		p.addMouseMotionListener(mueml);
		
		w.setVisible(true);
		
	}
	
	public static void destroy() {
		
		w.setVisible(false);
		
	}
	
	private static void kbdMatch(char key, int code, boolean press) {
		
		if (key == Input.upKey || code == KeyEvent.VK_UP) {
			Input.up = press;
		} else if (key == Input.downKey || code == KeyEvent.VK_DOWN) {
			Input.down = press;
		} else if (key == Input.leftKey || code == KeyEvent.VK_LEFT) {
			Input.left = press;
		} else if (key == Input.rightKey || code == KeyEvent.VK_RIGHT) {
			Input.right = press;
		}
		
	}
	
}
