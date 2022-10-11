package deip.graphics;

import java.awt.*;
import javax.swing.*;
import deip.*;
import deip.data.*;
import deip.game.phy.*;
import deip.lib.*;

public class Paint {

	public static class CFrame extends JPanel {
		
		private boolean cursorHoveringMenu;
		
		public void paintComponent(Graphics g) {
			
			try {
				long paintStart = System.currentTimeMillis();
				cursorHoveringMenu = false;
				Paint.g = g;
				objs = game.objects.clone();
				objNum = game.objectNumber;
				
				cX -= (cX - objs[game.player].x) / Conf.cameraFollowDiv;
				cY -= (cY - objs[game.player].y) / Conf.cameraFollowDiv;
				//cX = objs[game.player].x;
				//cY = objs[game.player].y;
				fovX = objs[game.player].fovX;
				fovY = objs[game.player].fovY;
				scX = cX - (fovX / 2);
				scY = cY - (fovY >> 1 /* something very different */);
				
				// background
				g.setColor(Conf.bgColor);
				g.fillRect(0, 0, Window.w.getWidth(), Window.w.getHeight());
				callGameplayGuiLayer(Data.gameplayGuiL0);
				
				// not background
				drawObjs();
				callGameplayGuiLayer(Data.gameplayGuiL1);
				
				// map border
				int topMapBorder = yOnScreen(Conf.mapOutSpace);
				int bottomMapBorder = yOnScreen(Conf.mapPositiveBorder);
				int leftMapBorder = xOnScreen(Conf.mapOutSpace);
				int rightMapBorder = xOnScreen(Conf.mapPositiveBorder);
				int mapBorderSize = bottomMapBorder - topMapBorder;
				g.setColor(borderColor(objs[game.player]));
				g.fillRect(0, 0, Window.w.getWidth(), topMapBorder);
				g.fillRect(0, bottomMapBorder, Window.w.getWidth(), Window.w.getHeight() - bottomMapBorder);
				g.fillRect(0, topMapBorder, leftMapBorder, mapBorderSize);
				g.fillRect(rightMapBorder, topMapBorder, Window.w.getWidth() - rightMapBorder, mapBorderSize);
				
				callGameplayGuiLayer(Data.gameplayGuiL2);
				
				if (Data.debugMode) {
					
					g.setFont(Conf.debugFont);
					g.setColor(Conf.debugColor);
					g.drawString("real x: " + objs[game.player].x, 0, Conf.debugFont.getSize());
					g.drawString("real y: " + objs[game.player].y, 0, Conf.debugFont.getSize() * 2);
					g.drawString("from center x: " + (objs[game.player].x - Conf.mapCenter), 0, Conf.debugFont.getSize() * 3);
					g.drawString("from center y: " + (objs[game.player].y - Conf.mapCenter), 0, Conf.debugFont.getSize() * 4);
					g.drawString("xm: " + objs[game.player].xm, 0, Conf.debugFont.getSize() * 5);
					g.drawString("ym: " + objs[game.player].ym, 0, Conf.debugFont.getSize() * 6);
					g.drawString("objs: " + objNum, 0, Conf.debugFont.getSize() * 7);
					g.drawString("avg physics calc time: " + ((float) game.calcTime / 10.0) + " / 16.6 ms", 0, Conf.debugFont.getSize() * 8);
					g.drawString("avg paint time: " + ((float) cycleTime / 10.0) + " / 16.6 ms", 0, Conf.debugFont.getSize() * 9);
					
					for (int a = 0; a < objNum; a++) {
						
						g.drawString(a + "", xOnScreen(objs[a].x), yOnScreen(objs[a].y));
						
					}
					
				}
				
				lastClicked = Window.mouseClicked;
				long paintEnd = System.currentTimeMillis();
				int paintTime = (int) (paintEnd - paintStart);
				cycleTimeBuffer += paintTime * 10;
				
				if (cycleTimeCycles == Conf.calcTimeCycles) {
					
					cycleTime = cycleTimeBuffer / Conf.calcTimeCycles;
					cycleTimeCycles = 0;
					cycleTimeBuffer = 0;
					
				}
				
				cycleTimeCycles++;
				Window.cursorHoveringMenu = cursorHoveringMenu;
				
			} catch (Throwable e) {
				
				Log.err("exception thrown in paint thread, stack trace in stderr and logs");
				Log.exception(e);
				
			}
			
			paintDone = true;
		}
		
		private void callGameplayGuiLayer(GameplayGui[] gui) throws Throwable {
			
			for (int a = 0; a < gui.length; a++) {
				GameplayGui.PaintOutput out = gui[a].paint(g, this, game, objs, objNum, 0, scX, scY, Window.mousePos.x, Window.mousePos.y, Window.mouseClicked, lastClicked, wwidth, wheight);
				
				if (out.blockMousePress) cursorHoveringMenu = true;
				if (out.setObjNum) objNum = out.objNum;
			}
			
		}
		
	}
	
	public static Phy game;
	public static Obj[] objs;
	public static Graphics g;
	public static long delay = 1000 / 60;
	public static int objNum;
	public static int wwidth;
	public static int wheight;
	public static int cX;
	public static int cY;
	public static int scX;
	public static int scY;
	public static int fovX;
	public static int fovY;
	public static int cycleTime = 0;
	public static int cycleTimeBuffer = 0;
	public static int cycleTimeCycles = 0;
	public static int showcaseRot = 0;
	public static boolean lastClicked = false;
	public static boolean paintDone = true;
	public static final Runnable paintLoop = new Runnable() {
		public void run() {
			
			try {
				
				while (true) {
					
					try {
						Thread.sleep(delay);
						
						while (!paintDone) {
							
							Thread.sleep(1);
							
						}
						
					} catch (InterruptedException e) {}
					
					showcaseRot += Conf.defaultSpin;
					paintDone = false;
					wwidth = Window.p.getWidth();
					wheight = Window.p.getHeight();
					
					Window.w.repaint(delay);
					
				}
				
			} catch (Throwable e) {
				
				Root.shutdown();
				
			}
			
		}
	};
	
	private static void drawObjs() {
		
		for (int a = 0; a < objNum; a++) {
			
			switch (objs[a].type) {
				
				case Tank:
				case Bullet:
				case Res:
					
					Point csSize = selectPos(objs[a].size, objs[a].size);
					Point ccPos = posOnScreen(objs[a].x, objs[a].y);
					Point cPos = new Point(ccPos.x - (csSize.x / 2), ccPos.y - (csSize.y / 2));
					
					for (int b = 0; b < objs[a].barrels.length; b++) {
						/*int[] outline0 = new int[4];
						int[] outline1 = new int[4];
						int[] inline0 = new int[4];
						int[] inline1 = new int[4];
						int brcx = objs[a].barrels[b].brcx;
						int blcx = objs[a].barrels[b].blcx;
						int trcx = objs[a].barrels[b].trcx;
						int tlcx = objs[a].barrels[b].tlcx;
						
						Obj.Direction bdir = Obj.Direction.rawSinus(objs[a].barrels[b].currentDegrees, Obj.Direction.defScale);
						Obj.Direction bcdir = bdir.newScale(objs[a].barrels[b].yoffset - objs[a].barrels[b].shootOffset).normalize();
						Obj.Direction brdir = bcdir.xOffset(brcx + objs[a].barrels[b].xoffset);
						Obj.Direction bldir = bcdir.xOffset(blcx + objs[a].barrels[b].xoffset);
						
						outline0[0] = selectX(brdir.x) + ccPos.x;
						outline1[0] = selectY(brdir.y) + ccPos.y;
						outline0[1] = selectX(bldir.x) + ccPos.x;
						outline1[1] = selectY(bldir.y) + ccPos.y;
						
						bcdir = bdir.newScale(bcdir.scale + objs[a].barrelBorder - objs[a].barrels[b].shootOffset).normalize();
						brdir = bcdir.xOffset(brcx - objs[a].barrelBorder + objs[a].barrels[b].xoffset);
						bldir = bcdir.xOffset(blcx + objs[a].barrelBorder + objs[a].barrels[b].xoffset);
						
						inline0[0] = selectX(brdir.x) + ccPos.x;
						inline1[0] = selectY(brdir.y) + ccPos.y;
						inline0[1] = selectX(bldir.x) + ccPos.x;
						inline1[1] = selectY(bldir.y) + ccPos.y;
						
						bcdir = bdir.newScale(bcdir.scale + objs[a].barrels[b].length - objs[a].barrelBorder - objs[a].barrels[b].shootOffset).normalize();
						brdir = bcdir.xOffset(trcx + objs[a].barrels[b].xoffset);
						bldir = bcdir.xOffset(tlcx + objs[a].barrels[b].xoffset);
						
						outline0[2] = selectX(bldir.x) + ccPos.x;
						outline1[2] = selectY(bldir.y) + ccPos.y;
						outline0[3] = selectX(brdir.x) + ccPos.x;
						outline1[3] = selectY(brdir.y) + ccPos.y;
						
						bcdir = bdir.newScale(bcdir.scale - objs[a].barrelBorder - objs[a].barrels[b].shootOffset).normalize();
						brdir = bcdir.xOffset(trcx - objs[a].barrelBorder + objs[a].barrels[b].xoffset);
						bldir = bcdir.xOffset(tlcx + objs[a].barrelBorder + objs[a].barrels[b].xoffset);
						
						inline0[2] = selectX(bldir.x) + ccPos.x;
						inline1[2] = selectY(bldir.y) + ccPos.y;
						inline0[3] = selectX(brdir.x) + ccPos.x;
						inline1[3] = selectY(brdir.y) + ccPos.y;
						
						g.setColor(colorTrans(Conf.barrelBorderColor, objs[a]));
						g.fillPolygon(outline0, outline1, 4);
						g.setColor(colorTrans(Conf.barrelBodyColor, objs[a]));
						g.fillPolygon(inline0, inline1, 4);
						*/
						
						objs[a].barrels[b].display.paint(g, objs, objNum, a, b, csSize, ccPos, cPos);
						
					}
					
					objs[a].display.paint(g, objs, a, csSize, ccPos, cPos);
					
					/*if (objs[a].shape == Obj.ObjShape.Circle) {
						
						g.setColor(colorTrans(objs[a].borderColor, objs[a]));
						g.fillOval(cPos.x, cPos.y, csSize.x, csSize.y);
						g.setColor(colorTrans(objs[a].bodyColor, objs[a]));
						g.fillOval(cPos.x + border.x, cPos.y + border.y, csSize.x - (border.x * 2), csSize.y - (border.y * 2));
						
					} else {
						int degAdd = Obj.Direction.defScale * 4 / objs[a].vertexNum;
						int deg = Obj.Direction.normalizeDegrees(objs[a].dir.degrees() + (degAdd / 2), Obj.Direction.defScale);
						int centerSize = objs[a].size - objs[a].border;
						int[] outline0 = new int[objs[a].vertexNum];
						int[] outline1 = new int[objs[a].vertexNum];
						int[] inline0 = new int[objs[a].vertexNum];
						int[] inline1 = new int[objs[a].vertexNum];
						//Obj.Direction current = Obj.Direction.fromDegrees(deg, Obj.Direction.defScale);
						
						for (int b = 0; b < objs[a].vertexNum; b++) {
							Obj.Direction line = Obj.Direction.rawSinus(deg, Obj.Direction.defScale).newScale(objs[a].size).normalize().round();
							outline0[b] = selectX(line.x) + ccPos.x;
							outline1[b] = selectY(line.y) + ccPos.y;
							
							line = line.newScale(centerSize).normalize().round();
							inline0[b] = selectX(line.x) + ccPos.x;
							inline1[b] = selectY(line.y) + ccPos.y;
							
							deg += degAdd;
							deg = Obj.Direction.normalizeDegrees(deg, Obj.Direction.defScale);
							
						}
						
						g.setColor(colorTrans(objs[a].borderColor, objs[a]));
						g.fillPolygon(outline0, outline1, objs[a].vertexNum);
						g.setColor(colorTrans(objs[a].bodyColor, objs[a]));
						g.fillPolygon(inline0, inline1, objs[a].vertexNum);
						//System.out.println("ID: " + a + ", X: " + outline0[0] + ", Y: " + outline1[0]);
						
					}*/
					
					if (objs[a].health != objs[a].maxhp && objs[a].health >= 0 && objs[a].type != Obj.ObjType.Bullet) {
						
						try {
							paintProgressBar(ccPos.x - (csSize.x / 2), ccPos.y - csSize.y, csSize.x, csSize.y / 4, new long[] {(long) objs[a].health * Conf.maxProgress / (long) objs[a].maxhp}, new Color[] {Conf.healthColor});
						} catch (ArithmeticException e) {}
						
					}
					
					break;
					
			}
			
		}
		
	}
	
	public static Color colorTrans(Color color, Obj obj) {
		
		return colorTrans(color, obj.trans);
	}
	
	public static Color colorTrans(Color color, int trans) {
		
		return new Color(color.getRGB() + (trans << 24), true);
	}
	
	public static Point posOnScreen(int x, int y) {
		
		return new Point(xOnScreen(x), yOnScreen(y));
	}
	
	public static int xOnScreen(int x) {
		
		return selectX(x - scX);
	}
	
	public static int yOnScreen(int y) {
		
		return selectY(y - scY);
	}
	
	public static Point selectPos(int x, int y) {
		
		return new Point(selectX(x), selectY(y));
		//return new Point(x, y);
	}
	
	public static int selectX(int x) {
		
		return (int) ((long) x * wwidth / fovX);
	}
	
	public static int selectY(int y) {
		
		return (int) ((long) y * wheight / fovY);
	}
	
	public static int invertSelX(int x) {
		
		return (int) ((long) x * fovX / wwidth);
	}
	
	public static int invertSelY(int y) {
		
		return (int) ((long) y * fovY / wheight);
	}
	
	public static Point invertSel(int x, int y) {
		
		return new Point(invertSelX(x), invertSelY(y));
	}
	
	public static int posInGameX(int x) {
		
		return invertSelX(x) + scX;
	}
	
	public static int posInGameY(int y) {
		
		return invertSelY(y) + scY;
	}
	
	public static Point posInGame(int x, int y) {
		
		return new Point(posInGameX(x), posInGameY(y));
	}
	
	public static void grid(int offsetX, int offsetY) {
		
		for (int i = offsetX; i < fovX; i += Conf.gridBetween) {
			int pos = selectX(i);
			
			g.fillRect(pos, 0, 1, wheight);
			
		}
		
		for (int i = offsetY; i < fovY; i += Conf.gridBetween) {
			int pos = selectY(i);
			
			g.fillRect(0, pos, wwidth, 1);
			
		}
		
	}
	
	public static void segmentBorders(Obj player) {
		
		g.fillRect(xOnScreen((player.x - Conf.mapOutSpace + fovX / 2) / Conf.segmentSize * Conf.segmentSize + Conf.mapOutSpace), 0, 1, wheight);
		g.fillRect(0, yOnScreen((player.y - Conf.mapOutSpace + fovY / 2) / Conf.segmentSize * Conf.segmentSize + Conf.mapOutSpace), wwidth, 1);
		
	}
	
	public static Color borderColor(Obj player) {
		
		try {
			int select0;
			int select1;
			int xn = -(player.x - Conf.mapOutSpace);
			int xp = player.x - Conf.mapOutSpace - Conf.mapSize;
			int yn = -(player.y - Conf.mapOutSpace);
			int yp = player.y - Conf.mapOutSpace - Conf.mapSize;
			
			select0 = xn;
			if (xn < xp) {
				
				select0 = xp;
				
			}
			
			select1 = yn;
			if (yn < yp) {
				
				select1 = yp;
				
			}
			
			if (select0 < select1) {
				
				select0 = select1;
				
			}
			
			return borderColorCreate(borderColorNormalize(select0 / Conf.mapBorderTransStep));
		} catch (ArithmeticException e) {
			
			return borderColorCreate(Conf.mapBorderTransStart);
		}
		
	}
	
	public static Color borderColorCreate(byte trans) {
		
		return new Color(trans << 24, true);
	}
	
	public static byte borderColorNormalize(int val) {
		if (val < Conf.mapBorderTransStart) {
			
			return Conf.mapBorderTransStart;
		} else if (val > 255) {
			
			return (byte) 0xff;
		}
		
		return (byte) val;
	}
	
	public static void paintProgressBar(int x, int y, int width, int height, long[] progresses, Color[] progressesColor) {
		int doubleBgHeight = height * Conf.progressBarSpaceMultiply / Conf.progressBarSpaceDivide;
		int bgHeight = doubleBgHeight / 2;
		int progressWidth = width - doubleBgHeight;
		
		g.setColor(Conf.progressBgColor);
		progressBarPart(x, y, width, height);
		
		for (int a = 0; a < progresses.length; a++) {
			
			g.setColor(progressesColor[a]);
			progressBarPart(x + bgHeight, y + bgHeight, (int) ((progresses[a] * progressWidth / Conf.maxProgress * progressWidth / width) + doubleBgHeight), height - doubleBgHeight);
			
		}
		
	}
	
	public static void progressBarPart(int x, int y, int width, int height) {
		
		g.fillOval(x, y, height, height);
		g.fillOval(x + width - height, y, height, height);
		g.fillRect(x + (height / 2), y, width - height, height);
		
	}
	
	// site: true = vertical, false = horizontal
	/*private static void straightGradient(Graphics g, Color top, Color bottom, boolean site, int x, int y, int width, int height) {
		int end = y + height - 1;
		int len;
		
		if (site) {
			len = width;
		} else {
			len = height;
		}
		
		for (int i = x; i < end; i++) {
			Color current = calcGradient(top, bottom, i, len);
			
			g.setColor(current);
			g.fillRect(x, i, width, 1);
			
		}
		
	}
	private static void slantGradient() {}
	
	private static Color calcGradient(Color from, Color to, int at, int len) {
		int buffer;
		
		buffer = calcGradientSingleColor((byte) from.getBlue(), (byte) to.getBlue(), at, len);
		buffer |= calcGradientSingleColor((byte) (from.getGreen() << 8), (byte) ((byte) to.getGreen() << 8), at, len);
		buffer |= calcGradientSingleColor((byte) (from.getRed() << 16), (byte) (to.getRed() << 16), at, len);
		buffer |= calcGradientSingleColor((byte) (from.getAlpha() << 24), (byte) (to.getAlpha() << 24), at, len);
		
		return new Color(buffer, true);
	}
	
	private static byte calcGradientSingleColor(byte from, byte to, int at, int len) {
		int difference = to - from;
		byte result;
		
		if (difference < 0) { // from bigger
			
			difference = from - to;
			int a = difference / len;
			result = (byte) (from + a);
			
		} else { // to bigger or equal
			
			int a = difference / len;
			result = from + a;
			
		}
		
		return result;
	}
	*/
}
