package deip.g;

import java.awt.*;
import javax.swing.*;
import deip.Root;
import deip.data.Conf;
import deip.game.phy.*;
import deip.lib.Log;
import deip.lib.Util;

public class Paint {

	public static class CFrame extends JPanel {
		
		public void paintComponent(Graphics g) {
			
			try {
				long paintStart = System.currentTimeMillis();
				boolean cursorHoveringMenu = false;
				showcaseRot += Conf.defaultSpin;
				Obj.Direction showcaseSpinDir = Obj.Direction.fromDegrees(showcaseRot, Obj.Direction.defScale);
				Paint.g = g;
				objs = game.objects.clone();
				objNum = game.objectNumber;
				
				scX = objs[game.player].x - (Conf.displaySizeX / 2);
				scY = objs[game.player].y - (Conf.displaySizeY >> 1 /* something very different */);
				
				// class select menu (not painting but throwing these shitting objects that are class menu)
				int playerLevel = Util.currentLevel(objs[game.player].score, Conf.levels);
				int playerClass = playerLevel / 15;
				int playerClassChosed = game.players[0].selectedClass;
				int classesBaseAdd = Conf.classMenuBoxDistance - (Conf.classMenuBoxSize / 2);
				Point classesBaseXY = new Point(scX + classesBaseAdd + tankClassesX, scY + classesBaseAdd);
				//System.out.println(Window.mousePos.x);
				Point inGameMousePos = posInGame(Window.mousePos.x, Window.mousePos.y);
				
				if (playerClassChosed == playerClass - 1) {
					playerClassChosed--;
				}
				
				if (playerClassChosed == playerClass) {
					tankClassesX -= (tankClassesX + Conf.classMenuBoxXOffset) / Conf.classMenuXOffsetDiv;
				} else {
					tankClassesX -= tankClassesX / Conf.classMenuXOffsetDiv;
				}
				
				classNumber = 0;
				for (int a = 0; a < Conf.classes.length; a++) {
					
					if ((Conf.classes[a] >> 8 & 0xffff) == game.players[0].classId) {
						
						classes[classNumber] = a;
						classNumber++;
						
					}
					
				}
				
				//classNumber = 1;
				for (int a = 0; a < classNumber; a += 2) {
					
					for (int b = -0; b < 2; b++) {
						
						if (a + b == classNumber) {
							break;
						}
						
						Color boxCenterColor = Conf.tankClassListBoxCenter;
						int x = classesBaseXY.x + (b * Conf.classMenuBoxDistance);
						int y = classesBaseXY.y + (a / 2 * Conf.classMenuBoxDistance);
						int hoverCheckSize = Conf.classMenuBoxSize / 2 + Conf.classMenuBoxborder;
						//System.out.println("mouse: " + inGameMousePos.x + ", obj: " + x);
						if (inGameMousePos.x > x - hoverCheckSize && inGameMousePos.x < x + hoverCheckSize && inGameMousePos.y > y - hoverCheckSize && inGameMousePos.y < y + hoverCheckSize) {
							
							boxCenterColor = Conf.tankClassListBoxCenterHover;
							cursorHoveringMenu = true;
							
							if (Window.mouseClicked) {
								
								boxCenterColor = Conf.tankClassListBoxCenterPress;
								
							} else if (lastClicked) {
								
								game.classChosed(0, classes[a + b]);
								
							}
							
						}
						
						objs[objNum] = new Obj(x, y, Conf.classMenuBoxSize, 0, 0, 0, 0, 0, 0, ObjBehavior.none, new ObjDisplay.RPolygon(4, Conf.classMenuBoxborder), Obj.ObjType.Tank, boxCenterColor, Conf.tankClassListBoxOutline, new Obj.Barrel[]{});
						objNum++;
						objs[objNum] = Conf.classTanks[classes[a + b]].clone();
						objs[objNum].x = x;
						objs[objNum].y = y;
						Util.changeDirection(objs[objNum], showcaseSpinDir, showcaseRot);
						//System.out.println("yo: " + ((BarrelDisplay.Generic) objs[objNum].barrels[0].display).yoffset + ", objNum: " + objNum + ", class: " + (a + b));
						objNum++;
						
					}
					
				}
				oldPlayerClass = playerClass;
				oldPlayerClassChosed = playerClassChosed;
				
				// background
				g.setColor(Conf.bgColor);
				g.fillRect(0, 0, Window.w.getWidth(), Window.w.getHeight());
				g.setColor(Conf.gridColor);
				grid(-(objs[game.player].x % Conf.gridBetween), -(objs[game.player].y % Conf.gridBetween));
				if (Conf.debug) {
					g.setColor(Conf.debugColor);
					segmentBorders(objs[game.player]);
				}
				// not background
				drawObjs();
				
				// upgrade menu
				int umCornerDistanceX = selectX(Conf.upgradeMenuCornerDistance + umX);
				int umStartHere = wheight - selectY(Conf.upgradeMenuCornerDistance + (Conf.upgradeMenuForEachDistanceAdd * Conf.upgradeNumber) - Conf.upgradeMenuBarDistance);
				int umWidth = selectX(Conf.upgradeMenuNormalWidth);
				int umHeight = selectY(Conf.upgradeMenuHeight);
				int umButtonWidth = selectX(Conf.upgradeMenuButtonWidth);
				int umForEachDistanceAdd = selectY(Conf.upgradeMenuForEachDistanceAdd);
				int umButtonOvalX = umCornerDistanceX + umWidth - umHeight;
				int umButtonSymbolAdd0 = umHeight / 2 - (umHeight / 12); // really didn't had any better name for this thing
				boolean umCanDoAnything = false;
				
				for (int a = 0; a < Conf.upgradeNumber; a++) {
					int y = umStartHere + (umForEachDistanceAdd * a);
					int umProgressStepWidth = (int) ((long) (umWidth - umButtonWidth - umHeight) * Conf.maxProgress / (long) umWidth / (long) Conf.upgradeMenuDefaultLevels);
					int umLevel = 0;
					int ubX = umButtonOvalX + (umHeight / 2) - umButtonWidth;
					long[] barValues = new long[Conf.upgradeMenuDefaultLevels * 2];
					Color[] barColors = new Color[barValues.length];
					
					if (a == 0)
						umLevel = game.players[0].regenLevel;
					else if (a == 1)
						umLevel = game.players[0].maxhpLevel;
					else if (a == 2)
						umLevel = game.players[0].bodyDmgLevel;
					else if (a == 3)
						umLevel = game.players[0].bSpeedLevel;
					else if (a == 4)
						umLevel = game.players[0].bHealthLevel;
					else if (a == 5)
						umLevel = game.players[0].bDamageLevel;
					else if (a == 6)
						umLevel = game.players[0].reloadLevel;
					else // a == 7
						umLevel = game.players[0].speedLevel;
					
					for (int b = -0; b < Conf.upgradeMenuDefaultLevels; b++) {
						int ib = Conf.upgradeMenuDefaultLevels - b;
						
						barValues[b * 2] = umProgressStepWidth * ib;
						barValues[b * 2 + 1] = umProgressStepWidth * ib - Conf.upgradeMenuBarStepDistance;
						barColors[b * 2] = Conf.progressSecondColor;
						
						if (ib <= umLevel) {
							
							barColors[b * 2 + 1] = Conf.upgradeBarColors[a];
							
							if (ib + Conf.upgradeMenuDefaultLevels <= umLevel) {
								barColors[b * 2 + 1] = Conf.upgradeBarSecondColors[a];
							}
							
						} else {
							barColors[b * 2 + 1] = Conf.progressBgColor;
						}
						
					}
					
					paintProgressBar(umCornerDistanceX, y, umWidth, umHeight, barValues, barColors);
					// upgrade button
					if (game.upgradeUpgradable(0, umLevel)) {
						if (Window.mousePos.x >= ubX && Window.mousePos.x <= ubX + umButtonWidth + (umHeight / 2) && Window.mousePos.y >= y && Window.mousePos.y <= y + umHeight) {
							
							g.setColor(Conf.upgradeButtonHoverColors[a]);
							if (Window.mouseClicked) {
								g.setColor(Conf.upgradeButtonPressColors[a]);
							} else if (lastClicked) {
								game.upgradeUpgraded(0, a);
							}
							cursorHoveringMenu = true;
							
						} else {
							g.setColor(Conf.upgradeButtonDefaultColors[a]);
						}
						umCanDoAnything = true;
					} else {
						
						g.setColor(Conf.upgradeButtonDisabledColors[a]);
						
					}
					g.fillOval(umButtonOvalX, y, umHeight, umHeight);
					g.fillRect(ubX, y, umButtonWidth, umHeight);
					g.setColor(Conf.upgradeButtonSymbolColor);
					g.fillRect(umButtonOvalX + (umHeight / 4), y + umButtonSymbolAdd0, umHeight / 2, umHeight / 6);
					g.fillRect(umButtonOvalX + umButtonSymbolAdd0, y + (umHeight / 4), umHeight / 6, umHeight / 2);
					
					if (Window.mousePos.x >= umCornerDistanceX && Window.mousePos.x <= umCornerDistanceX + umWidth && Window.mousePos.y >= y && Window.mousePos.y <= y + umHeight) {
						
						cursorHoveringMenu = true;
						
					}
					
				}
				
				if (umCanDoAnything) {
					umX -= umX / Conf.umXOffsetDiv;
				} else {
					umX -= (umX + Conf.umXOffset) / Conf.umXOffsetDiv;
				}
				
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
				
				// bottom bar
				int highestScore = 1;
				for (int a = 0; a < objNum; a++) {
					
					if (objs[a].type == Obj.ObjType.Tank && objs[a].score > highestScore) {
						highestScore = objs[a].score;
					}
					
				}
				
				int sbby = wheight * 8 / 9;
				int sbbw = wwidth * 4 / 9;
				int sbbx = (wwidth - sbbw) / 2;
				int sbbh = (wheight - sbby) * 2 / 3;
				g.setFont(new Font(Conf.bottomBarFont.getName(), Conf.bottomBarFont.getStyle(), sbbh / 2));
				paintProgressBar(sbbx, sbby, sbbw, sbbh, new long[]{(long) objs[game.player].score * Conf.maxProgress / highestScore}, new Color[]{Conf.scoreBarColor});
				g.setColor(Conf.scoreBarTextColor);
				g.drawString("score " + Util.numWSuffix(objs[game.player].score) + " level " + playerLevel, sbbx + sbbh / 3, sbby + sbbh * 2 / 3);
				
				if (Conf.debug) {
					
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
				
				Log.err("exception thrown in paint thread, stack trace in stderr");
				e.printStackTrace();
				
			}
			
			paintDone = true;
		}
		
	}
	
	public static Phy game;
	public static Obj[] objs;
	public static Graphics g;
	public static long delay = 1000 / 60;
	public static int objNum;
	public static int wwidth;
	public static int wheight;
	public static int scX;
	public static int scY;
	public static int cycleTime = 0;
	public static int cycleTimeBuffer = 0;
	public static int cycleTimeCycles = 0;
	public static int showcaseRot = 0;
	public static int tankClassesX = -Conf.classMenuBoxXOffset;
	public static int umX = -Conf.umXOffset;
	public static int classNumber = 0;
	public static int oldPlayerClass = 0;
	public static int oldPlayerClassChosed = 0;
	public static int[] classes = new int[16];
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
				
				Root.shutdown(1);
				
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
		
		return new Color(color.getRGB() + ((int) obj.trans << 24), true);
	}
	
	private static Point posOnScreen(int x, int y) {
		
		return new Point(xOnScreen(x), yOnScreen(y));
	}
	
	private static int xOnScreen(int x) {
		
		return selectX(x - scX);
	}
	
	private static int yOnScreen(int y) {
		
		return selectY(y - scY);
	}
	
	public static Point selectPos(int x, int y) {
		
		return new Point(selectX(x), selectY(y));
		//return new Point(x, y);
	}
	
	public static int selectX(int x) {
		
		return (int) ((long) x * wwidth / Conf.displaySizeX);
	}
	
	public static int selectY(int y) {
		
		return (int) ((long) y * wheight / Conf.displaySizeY);
	}
	
	private static int invertSelX(int x) {
		
		return (int) ((long) x * Conf.displaySizeX / wwidth);
	}
	
	private static int invertSelY(int y) {
		
		return (int) ((long) y * Conf.displaySizeY / wheight);
	}
	
	private static Point invertSel(int x, int y) {
		
		return new Point(invertSelX(x), invertSelY(y));
	}
	
	private static int posInGameX(int x) {
		
		return invertSelX(x) + scX;
	}
	
	private static int posInGameY(int y) {
		
		return invertSelY(y) + scY;
	}
	
	private static Point posInGame(int x, int y) {
		
		return new Point(posInGameX(x), posInGameY(y));
	}
	
	private static void grid(int offsetX, int offsetY) {
		
		for (int i = offsetX; i < Conf.displaySizeX; i += Conf.gridBetween) {
			int pos = selectX(i);
			
			g.fillRect(pos, 0, 1, wheight);
			
		}
		
		for (int i = offsetY; i < Conf.displaySizeY; i += Conf.gridBetween) {
			int pos = selectY(i);
			
			g.fillRect(0, pos, wwidth, 1);
			
		}
		
	}
	
	private static void segmentBorders(Obj player) {
		
		g.fillRect(xOnScreen((player.x - Conf.mapOutSpace + Conf.displaySizeX / 2) / Conf.segmentSize * Conf.segmentSize + Conf.mapOutSpace), 0, 1, wheight);
		g.fillRect(0, yOnScreen((player.y - Conf.mapOutSpace + Conf.displaySizeY / 2) / Conf.segmentSize * Conf.segmentSize + Conf.mapOutSpace), wwidth, 1);
		
	}
	
	private static Color borderColor(Obj player) {
		
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
	
	private static Color borderColorCreate(byte trans) {
		
		return new Color(trans << 24, true);
	}
	
	private static byte borderColorNormalize(int val) {
		if (val < Conf.mapBorderTransStart) {
			
			return Conf.mapBorderTransStart;
		} else if (val > 255) {
			
			return (byte) 0xff;
		}
		
		return (byte) val;
	}
	
	private static void paintProgressBar(int x, int y, int width, int height, long[] progresses, Color[] progressesColor) {
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
	
	private static void progressBarPart(int x, int y, int width, int height) {
		
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
