package deip.graphics;

import deip.*;
import deip.data.*;
import deip.lib.*;
import java.awt.*;

public class Button {
	
	public String text;
	public int x = 0;
	public int y = 0;
	public int width = 400;
	public int height = 400;
	public int gridWidth;
	public int gridHeight;
	public byte trans = (byte) 0xff;
	public boolean hover = false;
	public boolean[] grid;
	public Util.Press press = new Util.Press();
	
	public Button() {}
	
	public Button(String text) {
		
		this.text = text;
		
	}
	
	public void paintCycle(Graphics g, int mx, int my, boolean press) {
		
		g.setColor(Paint.colorTrans(Conf.buttonOutlineColor, trans));
		g.fillRect(x, y, width, height);
		g.setColor(Paint.colorTrans(Conf.buttonCenterColor, trans));
		g.fillRect(x + Conf.buttonBorder, y + Conf.buttonBorder, width - (Conf.buttonBorder << 1), height - (Conf.buttonBorder * 2));
		
		g.setColor(Paint.colorTrans(Conf.buttonOtherDotColor, trans));
		int dotWidth = width / gridWidth + 1;
		int dotHeight = height / gridHeight + 1;
		for (int a = 0; a < grid.length; a++) {
			
			if (grid[a]) g.fillRect(a % gridWidth * width / gridWidth + x, a / gridWidth * height / gridHeight + y, dotWidth, dotHeight);
			
		}
		
		hover = false;
		if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
			hover = true;
			
			if (press) {
				
				g.setColor(Conf.buttonPressColor);
				this.press.register(true);
				
			} else {
				g.setColor(Conf.buttonHoverColor);
				this.press.register(false);
			}
			
			if ((int) trans > 12) g.fillRect(x, y, width, height);
			
		} else {
			this.press.register(false);
		}
		
		g.setColor(Paint.colorTrans(Conf.buttonTextColor, trans));
		g.setFont(new Font(Conf.buttonTextFont.getName(), Conf.buttonTextFont.getStyle(), height - (Conf.buttonBorder * 2)));
		g.drawString(text, x + Conf.buttonBorder, y + (height * 2 / 3));
		
	}
	
	public void genGrid(int gridSize) {
		gridWidth = width / gridSize;
		gridHeight = height / gridSize;
		grid = new boolean[gridWidth * gridHeight];
		
		for (int a = 0; a < grid.length; a++) {
			
			grid[a] = Root.rand.nextInt(4096) < 1024;
			
		}
		
	}
	
	public void genGrid() {
		
		genGrid(Conf.buttonGridSize);
		
	}
	
}
