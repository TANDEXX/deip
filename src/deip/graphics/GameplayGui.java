package deip.graphics;

import deip.Root;
import deip.data.*;
import deip.game.phy.*;
import deip.lib.*;

import java.awt.*;

public interface GameplayGui {
	
	PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player /* not player as obj id but in `game.players` */, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) throws Throwable;
	
	class PaintOutput {
		
		public boolean blockMousePress;
		public boolean setObjNum;
		public int objNum;
		
		public PaintOutput(boolean blockMousePress) {
			
			this(blockMousePress, 0);
			setObjNum = false;
			
		}
		
		public PaintOutput(boolean blockMousePress, int objNum) {
			
			this.blockMousePress = blockMousePress;
			this.objNum = objNum;
			setObjNum = true;
			
		}
		
	}
	
	public class Defaults {
		
		public static ClassMenu classMenu = new ClassMenu();
		public static UpgradeMenu upgradeMenu = new UpgradeMenu();
		public static LevelBar levelBar = new LevelBar();
		public static DeathScreen deathScreen = new DeathScreen();
		public static Background background = new Background();
		
		public static class ClassMenu implements GameplayGui {
			
			private int showcaseRot = 0;
			private int tankClassesX = -Conf.classMenuBoxXOffset;
			//private int oldPlayerClass = 0;
			//private int oldPlayerClassChosed = 0;
			
			public PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) throws CloneNotSupportedException {
				int classNumber = 0;
				int[] classes = new int[16];
				boolean cursorHoveringMenu = false;
				showcaseRot += Conf.defaultSpin;
				Obj.Direction showcaseSpinDir = Obj.Direction.fromDegrees(showcaseRot, Obj.Direction.defScale);
				
				// class select menu (not painting but throwing these shitting objects to array that are class menu)
				int playerLevel = Util.currentLevel(objs[game.player].score, Conf.levels);
				int playerClass = playerLevel / 15;
				int playerClassChosed = game.players[0].selectedClass;
				int classesBaseAdd = Conf.classMenuBoxDistance - (Conf.classMenuBoxSize / 2);
				Point classesBaseXY = new Point(scX + classesBaseAdd + tankClassesX, scY + classesBaseAdd);
				Point inGameMousePos = Paint.posInGame(mx, my);
				
				if (playerClassChosed == playerClass - 1) {
					playerClassChosed--;
				}
				
				if (playerClassChosed == playerClass) {
					tankClassesX -= (tankClassesX + Conf.classMenuBoxXOffset) / Conf.classMenuXOffsetDiv;
				} else {
					tankClassesX -= tankClassesX / Conf.classMenuXOffsetDiv;
				}
				
				classNumber = 0;
				for (int a = 0; a < Data.classes.length; a++) {
					
					if ((Data.classes[a] >> 8 & 0xffff) == game.players[0].classId) {
						
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
							
							if (mclicked) {
								
								boxCenterColor = Conf.tankClassListBoxCenterPress;
								
							} else if (mlastClicked) {
								
								game.classChosed(0, classes[a + b]);
								
							}
							
						}
						
						objs[objNum] = new Obj(x, y, Conf.classMenuBoxSize, 0, 0, 0, 0, 0, 0, ObjBehavior.none, new ObjDisplay.RPolygon(4, Conf.classMenuBoxborder), Obj.ObjType.Tank, boxCenterColor, Conf.tankClassListBoxOutline, new Obj.Barrel[]{});
						objNum++;
						objs[objNum] = Data.classTanks[classes[a + b]].clone();
						objs[objNum].x = x;
						objs[objNum].y = y;
						Util.changeDirection(objs[objNum], showcaseSpinDir, showcaseRot);
						//System.out.println("yo: " + ((BarrelDisplay.Generic) objs[objNum].barrels[0].display).yoffset + ", objNum: " + objNum + ", class: " + (a + b));
						objNum++;
						
					}
					
				}
				//oldPlayerClass = playerClass;
				//oldPlayerClassChosed = playerClassChosed;
				
				return new PaintOutput(cursorHoveringMenu, objNum);
			}
			
		}
		
		public static class UpgradeMenu implements GameplayGui {
			
			int umX = -Conf.umXOffset;
			
			public PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) {
				boolean cursorHoveringMenu = false;
				
				int umCornerDistanceX = Paint.selectX(Conf.upgradeMenuCornerDistance + umX);
				int umStartHere = wheight - Paint.selectY(Conf.upgradeMenuCornerDistance + (Conf.upgradeMenuForEachDistanceAdd * Conf.upgradeNumber) - Conf.upgradeMenuBarDistance);
				int umWidth = Paint.selectX(Conf.upgradeMenuNormalWidth);
				int umHeight = Paint.selectY(Conf.upgradeMenuHeight);
				int umButtonWidth = Paint.selectX(Conf.upgradeMenuButtonWidth);
				int umForEachDistanceAdd = Paint.selectY(Conf.upgradeMenuForEachDistanceAdd);
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
					
					Paint.paintProgressBar(umCornerDistanceX, y, umWidth, umHeight, barValues, barColors);
					// upgrade button
					if (game.upgradeUpgradable(0, umLevel)) {
						if (Window.mousePos.x >= ubX && Window.mousePos.x <= ubX + umButtonWidth + (umHeight / 2) && Window.mousePos.y >= y && Window.mousePos.y <= y + umHeight) {
							
							g.setColor(Conf.upgradeButtonHoverColors[a]);
							if (Window.mouseClicked) {
								g.setColor(Conf.upgradeButtonPressColors[a]);
							} else if (mlastClicked) {
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
				
				return new PaintOutput(cursorHoveringMenu);
			}
			
		}
		
		public static class LevelBar implements GameplayGui {
			
			public PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) {
				Util.Pair plevel = Util.currentLevelAndRest(objs[game.player].score, Conf.levels);
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
				Paint.paintProgressBar(sbbx, sbby, sbbw, sbbh, new long[]{(long) objs[game.player].score * Conf.maxProgress / highestScore}, new Color[]{Conf.scoreBarColor});
				g.setColor(Conf.scoreBarTextColor);
				g.drawString("score " + Util.numWSuffix(objs[game.player].score) + " level " + Util.currentLevel(objs[game.player].score, Conf.levels), sbbx + sbbh / 3, sbby + sbbh * 2 / 3);
				sbbw = wwidth * 4 / 14;
				sbbx = (wwidth - sbbw) / 2;
				sbbh = wheight / 25;
				sbby -= sbbh * 4 / 3;
				Paint.paintProgressBar(sbbx, sbby, sbbw, sbbh, new long[]{(long) (Integer) plevel.b * Conf.maxProgress / Conf.levels[(Integer) plevel.a]}, new Color[]{Conf.levelProgressBarColor});
				
				return new PaintOutput(false);
			}
			
		}
		
		public static class DeathScreen implements GameplayGui {
			
			public int transLevel = 1;
			public int retryBTransLevel = 1;
			public int playerScore = 0;
			public int playerLifetime = 0;
			public int playerLevel = 0;
			public boolean firstTime = true;
			public boolean getDeadInformation = true;
			public boolean sRecBClicked = true;
			public String formatedPlayerScore = "";
			public String formatedPlayTime = "";
			public Util.Press playerDeathPress = new Util.Press();
			public Button retryButton = new Button("retry");
			public Button saveRecordButton = new Button("save record");
			
			public PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) throws Throwable {
				playerDeathPress.register(game.mainPlayerdied);
				int buttonsY = (int) ((double) wheight * Conf.dsButtonsYMul);
				int buttonsHeight = (int) ((double) wheight * Conf.dsButtonsHeightMul);
				int noBordButtonsHeight = buttonsHeight - (Conf.buttonBorder * 2);
				int buttonRetryX = (int) ((double) wwidth * Conf.dsButtonRetryXMul);
				int buttonSRecX = (int) ((double) wwidth * Conf.dsButtonSRecXMul);
				int buttonRetryWidth = (int) ((double) noBordButtonsHeight * Conf.dsButtonRetryWidthMul + (Conf.buttonBorder * 2));
				int buttonSRecWidth = (int) ((double) noBordButtonsHeight * Conf.dsButtonSRecWidthMul + (Conf.buttonBorder * 2));
				byte fullTrans = (byte) (transLevel * 0xfe / Conf.dsTransScale + 1);
				byte fullRetryBTrans = (byte) (retryBTransLevel * 0xfe / Conf.dsTransScale + 1);
				
				if (playerDeathPress.released) sRecBClicked = true;
				else if (playerDeathPress.pressed) sRecBClicked = false;
				
				retryButton.x = buttonRetryX;
				retryButton.y = buttonsY;
				retryButton.width = buttonRetryWidth;
				retryButton.height = buttonsHeight;
				
				saveRecordButton.x = buttonSRecX;
				saveRecordButton.y = buttonsY;
				saveRecordButton.width = buttonSRecWidth;
				saveRecordButton.height = buttonsHeight;
				
				if (firstTime) {
					retryButton.genGrid();
					saveRecordButton.genGrid();
					
					firstTime = false;
				}
				
				if (getDeadInformation && game.mainPlayerdied) {
					
					playerScore = objs[game.players[player].objId].score;
					playerLifetime = objs[game.players[player].objId].lifeTime;
					formatedPlayerScore = Util.numWSuffix(playerScore);
					playerLevel = Util.currentLevel(playerScore, Conf.levels);
					formatedPlayTime = Util.formatPlayTime(-playerLifetime);
					
				}
				
				getDeadInformation = !game.mainPlayerdied;
				if (game.mainPlayerdied) transLevel -= (transLevel - Conf.dsTransScale) / Conf.dsTransDiv;
				else transLevel -= transLevel / Conf.dsTransDiv;
				if (game.mainPlayerdied && !sRecBClicked) retryBTransLevel -= (retryBTransLevel - Conf.dsTransScale) / Conf.dsTransDiv;
				else retryBTransLevel -= retryBTransLevel / Conf.dsTransDiv;
				
				g.setColor(Paint.colorTrans(Conf.dsBgColor, transLevel * Conf.dsBgTransMax / Conf.dsTransScale + 1));
				g.fillRect(0, 0, wwidth, wheight);
				
				retryButton.trans = fullTrans;
				saveRecordButton.trans = fullRetryBTrans;
				retryButton.paintCycle(g, mx, my, mclicked);
				//saveRecordButton.paintCycle(g, mx, my, mclicked);
				
				g.setFont(Conf.dsStatFont);
				g.setColor(Paint.colorTrans(Conf.dsTextColor, fullTrans));
				g.drawString("score: " + formatedPlayerScore, buttonRetryX, buttonsY - (Conf.dsStatFont.getSize() * 2));
				g.drawString("level: " + playerLevel, buttonRetryX, buttonsY - (Conf.dsStatFont.getSize() * 3));
				g.drawString("time survived: " + formatedPlayTime, buttonRetryX, buttonsY - (Conf.dsStatFont.getSize() * 4));
				g.setFont(Conf.dsNickFont);
				g.drawString(Data.nick, buttonRetryX, buttonsY - (Conf.dsStatFont.getSize() * 4) - Conf.dsNickFont.getSize());
				
				if (retryButton.press.released && game.mainPlayerdied) {
					game.atEnd = Phy.AtEnd.ReInit;
				}
				
				if (saveRecordButton.press.released && game.mainPlayerdied && !sRecBClicked) {
					//sRecBClicked = Threads.saveRecordInBackground(game.record);
				}
				
				return new PaintOutput(game.mainPlayerdied);
			}
		}
		
		public static class Background implements GameplayGui {
			
			public PaintOutput paint(Graphics g, Paint.CFrame component, Phy game, Obj[] objs, int objNum, int player, int scX, int scY, int mx, int my, boolean mclicked, boolean mlastClicked, int wwidth, int wheight) throws Throwable {
				
				// noise
				/*
				for (int a = 0; a < Conf.bgNoiseDensity; a++) {
					
					g.setColor(Conf.bgNoiseColorPallete[Root.rand.nextInt(Conf.bgNoiseColorPallete.length)]);
					int size = Conf.bgNoiseMinSize + Root.rand.nextInt(Conf.bgNoiseDifSize);
					g.fillRect(Root.rand.nextInt(wwidth), Root.rand.nextInt(wheight), size, size);
					
				}
				*/
				
				// map, center area
				g.setColor(Conf.bgMapCenterAreaColor);
				g.fillRect(wwidth / 3, wheight / 3, wwidth / 3, wheight / 3);
				
				// map, player pointer
				g.setColor(Conf.bgMapPlayerColor);
				g.fillOval((objs[player].x - Conf.mapOutSpace) * wwidth / Conf.mapSize - Conf.bgMapPlayerHalfSize, (objs[player].y - Conf.mapOutSpace) * wheight / Conf.mapSize - Conf.bgMapPlayerHalfSize, Conf.bgMapPlayerSize, Conf.bgMapPlayerSize);
				
				// grid
				g.setColor(Conf.gridColor);
				Paint.grid(-(scX % Conf.gridBetween), -(scY % Conf.gridBetween));
				if (Data.debugMode) { // segment borders
					g.setColor(Conf.debugColor);
					Paint.segmentBorders(objs[game.player]);
				}
				
				
				return new PaintOutput(false);
			}
		}
		
	}
	
}
