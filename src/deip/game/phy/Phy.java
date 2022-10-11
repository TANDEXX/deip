package deip.game.phy;

import java.awt.Point;

import deip.Root;
import deip.data.*;
import deip.graphics.*;
import deip.lib.*;

public class Phy {
	
	private static class MouseAction {
		
		int x;
		int y;
		boolean clicked;
		boolean active = false;
		
		MouseAction() {}
		MouseAction(int x, int y, boolean clicked) {
			
			this.x = x;
			this.y = y;
			this.clicked = clicked;
			active = true;
			
		}
		
	}
	
	public Obj.MapSegment[] mapSegments;
	public Obj.GroupSpawn[] regularAreaObjs;
	public Obj.GroupSpawn[] centerAreaObjs;
	public Obj[] objects;
	public MouseAction mouseAction;
	public AtEnd atEnd = AtEnd.Continue;
	public boolean died = false; // temporary value saying do object should disappear or not (use in object behavior to remove current object)
	private boolean serverMode;
	public boolean mainPlayerdied;
	public int mainPlayer = 0;
	public int mainPlayerLastScore = 0;
	public int objectNumber = 1;
	public int player = 0;
	public int rareCycleCounter = 0;
	public int calcTime = 0;
	public int[] lastSeenSegments;
	public Obj.Player[] players;
	public Record record;
	
	public long delay = 1000 / 60;
	public Runnable calcLoop = new Runnable() {
		
		public void run() {
			int calcTimeCycle = 0;
			int calcTimeAverage = 0;
			Log.info("physics engine started");
			
			while (true) {
				long startTime = System.currentTimeMillis(); // do the work here:
				
				try {
					
					calc();
					
					if (atEnd == AtEnd.ReInit) {
						init();
					} else if (atEnd == AtEnd.Stop) {
						atEnd = AtEnd.Continue;
						Log.info("physics engine stopped");
						break;
					}
					atEnd = AtEnd.Continue;
					
				} catch (Throwable e) {
					
					System.out.println("exception thrown in phy thread, stack trace in stderr and logs");
					Log.exception(e);
					
				}
				
				long endTime = System.currentTimeMillis();
				int calcTime = (int) (endTime - startTime);
				calcTimeAverage += calcTime * 10;
				calcTimeCycle++;
				
				if (calcTimeCycle == Conf.calcTimeCycles) {
					
					calcTime = calcTimeAverage / Conf.calcTimeCycles;
					calcTimeCycle = 0;
					calcTimeAverage = 0;
					
				}
				
				try {
					
					Thread.sleep(delay - calcTime);
					
				} catch (InterruptedException e) {} catch (IllegalArgumentException e) {/* when provided argument is negative value */}
			}
			
		}
	};
	
	// not intented to use it, some other class throws ugly error when you delete it or make private
	public Phy() {}
	public Phy(boolean serverMode, int separationLevel) {
		
		this.serverMode = serverMode;
		regularAreaObjs = Conf.regularAreaObjs[separationLevel];
		centerAreaObjs = Conf.centerAreaObjs[separationLevel];
		
	}
	
	public void init() throws CloneNotSupportedException {
		Log.info("physics engine reinitialized");
		//record = Record.newEmpty(Data.recordFrameRate);
		mapSegments = new Obj.MapSegment[Conf.allSegments];
		mainPlayerdied = false;
		objects = new Obj[Conf.maxObjects];
		mouseAction = new MouseAction();
		objectNumber = 1;
		players = new Obj.Player[]{new Obj.Player(player)};
		lastSeenSegments = new int[4];
		int playerSegment = Root.rand.nextInt(Conf.allSegments);
		Point playerPos = Obj.MapSegment.segmentPos(playerSegment);
		playerPos = new Point(playerPos.x + Conf.segmentSize / 2, playerPos.y + Conf.segmentSize / 2);
		for (int a = 0; a < 4; a++) {
			lastSeenSegments[a] = playerSegment;
		}
		
		objects[player] = new Obj(playerPos.x, playerPos.y, Conf.basicTankSize, Conf.basicWeight, 0, 750000, 0, 0, Conf.basicTankSpeed, ObjBehavior.newDefault(), new ObjDisplay.Circle(), Obj.ObjType.Tank, Conf.tankFrendlyBodyColor, Conf.tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(300, 15, 0, 1, 5, 0, new BarrelDisplay.Generic(9000, 0, 6000, 2000), new Obj(0, 0, 0, 0, 2000, Conf.basicWeight, 60, 5000, 0, 0, 7500, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(Conf.defaultObjBorder), (byte) 0xff, Obj.ObjType.Bullet, Conf.noC, Conf.noC, new Obj.Barrel[]{})),
			new Obj.Barrel(800, 40, 100, 1, 6, Obj.Direction.defScale, new BarrelDisplay.Generic(9000, 0, 1000, 6000), new Obj(0, 0, 0, 0, 6000, Conf.basicWeight, 60, 5000, 0, 0, 10000, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(Conf.defaultObjBorder), (byte) 0xff, Obj.ObjType.Bullet, Conf.noC, Conf.noC, new Obj.Barrel[]{})),
			new Obj.Barrel(9000, 180, 50, 1, 11000, 0x28000, new BarrelDisplay.Generic(11000, 10000, 10000, -4000, 4000, -8000, 8000), new Obj(0, 0, 0, 0, 20000, Conf.basicWeight, 0, 25000000, 0, 0, 1000000, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(Conf.defaultObjBorder), (byte) 0xff, Obj.ObjType.Bullet, Conf.noC, Conf.noC, new Obj.Barrel[]{})),
		});
		objects[player] = Conf.basicTank.clone();
		objects[player].x = playerPos.x;
		objects[player].y = playerPos.y;
		//objects[player].score = 1000000000;
		resetMapSegments(mapSegments);
		
		/*for (int a = 0; a < mapSegments.length; a++) {
			
			mapSegments[a] = new Obj.MapSegment();
			
			mapSegments[a].groups = 1;
			mapSegments[a].objGroupsCount[0] = 60;
			mapSegments[a].objGroupsX[0] = Conf.segmentSize / 2;
			mapSegments[a].objGroupsY[0] = Conf.segmentSize / 2;
			mapSegments[a].objGroupsSize[0] = 100000;
			mapSegments[a].objGroupsObj[0] = new Obj(0, 0, 30000, 0, 750, 0, 0, 0, 4, false, Obj.ObjAi.Any, Obj.ObjType.Tank, Conf.healthColor, Conf.armorColor, new Obj.Barrel[0]);
			
		}*/
		
	}
	
	public void calc() throws Throwable {
		Obj[] objCp = objects.clone();
		int objNumCp = objectNumber;
		int tmp0;
		
		if (!mainPlayerdied) {
			if (Input.up) objCp[player].ym -= objCp[player].speed;
			if (Input.down) objCp[player].ym += objCp[player].speed;
			if (Input.left) objCp[player].xm -= objCp[player].speed;
			if (Input.right) objCp[player].xm += objCp[player].speed;
		}
		
		Obj.Direction pdir = new Obj.Direction(mouseAction.x, mouseAction.y, Obj.Direction.defScale).normalize();
		objCp[player].dir = pdir;
		int pdeg = pdir.degrees();
		
		// player class setting
		for (int a = 0; a < players.length; a++) {
			
			Obj classTank;
			
			if (players[a].classId == 0xffff) {
				classTank = Conf.basicTank;
			} else {
				classTank = Data.classTanks[players[a].classId];
			}
			
			if (players[a].toSet) {
				int oldMaxhp = objCp[players[a].objId].maxhp;
				
				if (players[a].classUpdated) {
					
					players[a].selectedClass++;
					objCp[players[a].objId].barrels = Util.cloneBarrelArray(classTank.barrels);
					
				}
				
				int oldRegenWait = objCp[players[a].objId].regenWait;
				objCp[players[a].objId].regenerate = classTank.regenerate * (players[a].maxhpLevel + 1) * Conf.upgradesRegenerate[players[a].regenLevel * 2] / Conf.upgradesRegenerate[players[a].regenLevel * 2 + 1]; // yes, this should bee better made but I want to end this game fastly
				objCp[players[a].objId].regenWait = classTank.regenWait * Conf.upgradesRegenWait[players[a].regenLevel * 2] / Conf.upgradesRegenWait[players[a].regenLevel * 2 + 1];
				objCp[players[a].objId].regenWaitCounter = objCp[players[a].objId].regenWaitCounter * objCp[players[a].objId].regenWait / oldRegenWait;
				objCp[players[a].objId].maxhp = classTank.maxhp * Conf.upgradesHp[players[a].maxhpLevel * 2] / Conf.upgradesHp[players[a].maxhpLevel * 2 + 1];
				objCp[players[a].objId].health = (int) ((long) objCp[players[a].objId].health * (long) objCp[players[a].objId].maxhp / (long) oldMaxhp);
				for (int b = 0; b < objCp[players[a].objId].barrels.length; b++) {
					objCp[players[a].objId].barrels[b].reload = classTank.barrels[b].reload * Conf.upgradesReload[players[a].reloadLevel * 2] / Conf.upgradesReload[players[a].reloadLevel * 2 + 1];
					objCp[players[a].objId].barrels[b].delay = classTank.barrels[b].delay * Conf.upgradesReload[players[a].reloadLevel * 2] / Conf.upgradesReload[players[a].reloadLevel * 2 + 1];
					objCp[players[a].objId].barrels[b].bullet.bodyDmg = classTank.barrels[b].bullet.bodyDmg * Conf.upgradesBDmg[players[a].bDamageLevel * 2] / Conf.upgradesBDmg[players[a].bDamageLevel * 2 + 1];
					objCp[players[a].objId].barrels[b].bullet.maxhp = classTank.barrels[b].bullet.maxhp * Conf.upgradesBHp[players[a].bHealthLevel * 2] / Conf.upgradesBHp[players[a].bHealthLevel * 2 + 1];
					objCp[players[a].objId].barrels[b].bullet.health = objCp[players[a].objId].barrels[b].bullet.maxhp;
					objCp[players[a].objId].barrels[b].bspeed = classTank.barrels[b].bspeed * Conf.upgradesBSpeed[players[a].bSpeedLevel * 2] / Conf.upgradesBSpeed[players[a].bSpeedLevel * 2 + 1];
				}
				objCp[players[a].objId].speed = classTank.speed * Conf.upgradesSpeed[players[a].speedLevel * 2] / Conf.upgradesSpeed[players[a].speedLevel * 2 + 1];
				
				
			}
			
			players[a].toSet = false;
			players[a].classUpdated = false;
			
		}
		
		if (mouseAction.active) {
			
			Util.changeDirection(objCp[player], pdir, pdeg);
			
		}
		
		for (int a = 0; a != objCp[player].barrels.length; a++) {
			
			objCp[player].barrels[a].shotCountDown--;
			
			if (mouseAction.clicked) {
				
				objCp[player].barrels[a].reachedZero = false;
				
				if (objCp[player].barrels[a].shotCountDown < 0) {
					Point shootPoint = objCp[player].barrels[a].display.shootPoint();
					int spread = Root.rand.nextInt(objCp[player].barrels[a].spread) - (objCp[player].barrels[a].spread / 2);
					Obj.Direction shootAngle = Obj.Direction.rawSinus(objCp[player].barrels[a].currentDegrees + spread, Obj.Direction.defScale).normalize().newScale(shootPoint.y).normalize().round().xOffset(shootPoint.x);
					Obj.Direction shootDir = Obj.Direction.fromDegrees(Obj.Direction.normalizeDegrees(objCp[player].barrels[a].currentDegrees + spread, Obj.Direction.defScale), Obj.Direction.defScale).newScale(objCp[player].barrels[a].bspeed).normalize().round();
					Obj.Direction recoil = shootDir.newScale(objCp[player].barrels[a].recoil).normalize().negative();
					
					objCp[objNumCp] = objCp[player].barrels[a].bullet.clone();
					objCp[objNumCp].x = objCp[player].x + shootAngle.x;
					objCp[objNumCp].y = objCp[player].y + shootAngle.y;
					objCp[objNumCp].xm = shootDir.x + objCp[player].xm;
					objCp[objNumCp].ym = shootDir.y + objCp[player].ym;
					objCp[objNumCp].generalBodyColor = Util.colorBetween(objCp[player].generalBodyColor, Conf.bulletPartColor, Conf.bulletColorSwap);
					objCp[objNumCp].generalBorderColor = Util.colorBetween(objCp[player].generalBorderColor, Conf.bulletPartColor, Conf.bulletColorSwap);
					objCp[objNumCp].currentDisplayBodyColor = objCp[objNumCp].generalBodyColor;
					objCp[objNumCp].currentDisplayBorderColor = objCp[objNumCp].generalBorderColor;
					//objCp[objNumCp] = new Obj(objCp[player].x + shootAngle.x, objCp[player].y + shootAngle.y, shootDir.x + objCp[player].xm, shootDir.y + objCp[player].ym, objCp[player].barrels[a].bsize, objCp[player].barrels[a].bWeight, objCp[player].barrels[a].blifetime, objCp[player].barrels[a].bhealth, 0, 0, objCp[player].barrels[a].bdamage, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(), (byte) 0xff, Obj.ObjType.Bullet, , , new Obj.Barrel[0]);
					objCp[objNumCp].owner = player;
					objCp[player].barrels[a].shotCountDown = objCp[player].barrels[a].reload;
					objCp[player].barrels[a].shotOffset = Conf.defBarrelShootOffset;
					objCp[player].xm += recoil.x;
					objCp[player].ym += recoil.y;
					objNumCp++;
					
				}
				
			} else {
				
				if (objCp[player].barrels[a].shotCountDown < 0) {
					
					objCp[player].barrels[a].reachedZero = true;
					
				}
				
				if (objCp[player].barrels[a].reachedZero) {
					
					objCp[player].barrels[a].shotCountDown = objCp[player].barrels[a].delay;
					objCp[player].barrels[a].reachedZero = true;
					
				}
				
			}
			
		}
		
		if (rareCycleCounter == 20) {
			
			rareCycleCounter = 0;
			
			int[] seenSegments = Obj.MapSegment.seenSegments(objCp[player].x, objCp[player].y, objCp[player].fovX / 2, objCp[player].fovY / 2);
			//System.out.println("seenSegments: " + Util.intArrayToString(seenSegments) + ", lastSeenSegments: " + Util.intArrayToString(lastSeenSegments));
			
			for (int sca = 0; sca < 4; sca++) {
				
				boolean left = true;
				boolean join = true;
				
				for (int scb = 0; scb < 4; scb++) {
					
					if (seenSegments[sca] == lastSeenSegments[scb]) {
						
						join = false;
						
					}
					
					if (lastSeenSegments[sca] == seenSegments[scb]) {
						
						left = false;
						
					}
					
				}
				
				
				if (left) {
					
					int seg = lastSeenSegments[sca];
					//int o = 0;
					
					for (int a = 0; a < objNumCp; a++) {
						
						if (Obj.MapSegment.segment(objCp[a].x, objCp[a].y) == seg) {
							
							objCp[a].behavior.disappearing(this, objCp, objNumCp, a);
							remove(objCp, objNumCp, a);
							objNumCp--;
							//System.out.println("REMOVED: " + (a + o));
							//o++;
							a--;
							
						}
						
					}
					
				}
				for (int a = 0; a < sca; a++) {
					
					if (seenSegments[sca] == seenSegments[a]) {
						
						join = false;
						
					}
					
				}
				
				try {
					if (join) {
						int seg = seenSegments[sca];
						Point segmentPos = Obj.MapSegment.segmentPos(seg);
						
						if (mapSegments[seg].groups == 0) {
							Obj.GroupSpawn[] groupSpawn;
							
							if (seg == 14 || seg == 15 || seg == 20 || seg == 21) {
								groupSpawn = centerAreaObjs;
							} else {
								groupSpawn = regularAreaObjs;
							}
							
							spawnGroups(mapSegments[seg], groupSpawn);
							
						}
						
						for (int a = 0; a < mapSegments[seg].groups; a++) {
							
							for (int b = 0; b < mapSegments[seg].objGroupsCount[a]; b++) {
								
								objCp[objNumCp] = mapSegments[seg].objGroupsObj[a].clone();
								objCp[objNumCp].seg = seg;
								objCp[objNumCp].group = a;
								objCp[objNumCp].x = segmentPos.x + mapSegments[seg].objGroupsX[a] + Root.rand.nextInt(mapSegments[seg].objGroupsSize[a] + 1) - (mapSegments[seg].objGroupsSize[a] / 2);
								objCp[objNumCp].y = segmentPos.y + mapSegments[seg].objGroupsY[a] + Root.rand.nextInt(mapSegments[seg].objGroupsSize[a] + 1) - (mapSegments[seg].objGroupsSize[a] / 2);
								//objCp[objNumCp].dir = Obj.Direction.fromDegrees(Root.rand.nextInt(Obj.Direction.defScale), Obj.Direction.defScale);
								//System.out.println("X: " + objCp[objNumCp].x + ", Y: " + objCp[objNumCp].y);
								
								if (mapSegments[seg].objGroupsObj[a].type == Obj.ObjType.Res) {
									
									objCp[objNumCp].spin = Root.rand.nextInt(Conf.randSpinBound) + Conf.randSpinBase;
									
									if (Root.rand.nextBoolean()) {
										objCp[objNumCp].spin = -objCp[objNumCp].spin;
									}
									
								}
								
								for (int c = 0; c < objNumCp; c++) {
									if (colliding(objCp[c].x, objCp[c].y, objCp[objNumCp].x, objCp[objNumCp].y, mapSegments[seg].objGroupsObj[a].size)) {
										objNumCp--;
									}
								}
								
								objNumCp++;
								
								/*for (int z = 0; z < objNumCp; z++) {
									System.out.println("ID: " + z + ", X: " + objCp[z].x + ", Y: " + objCp[z].y);
								}
								System.out.println();*/
								
							}
							
						}
						
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {}
				
			}
			
			lastSeenSegments = seenSegments;
			
		} else {
			
			rareCycleCounter++;
			
		}
		
		//System.out.println("player rot: " + objCp[player].dir.degrees());
		try {
			//System.out.println("4 rot: " + objCp[4].dir.degrees());
		} catch (Throwable e) {}
		
		boolean[] collidable = new boolean[objNumCp];
		
		for (int a = 0; a < objNumCp; a++) {
			
			for (int b = 0; b < objNumCp; b++) {
				collidable[b] = objCp[b].collidable;
			}
			
			objCp[a].behavior.updateTick(this, objCp, objNumCp, a);
			objCp[a].lifeTime--;
			
			if (objCp[a].lifeTime == 0) {
				
				objCp[a].behavior.killed(this, objCp, objNumCp, a, 0, "lifetime");
				
			}
			if (objCp[a].spin != 0) {
				objCp[a].dir = Obj.Direction.fromDegrees(Obj.Direction.normalizeDegrees(objCp[a].dir.degrees() + objCp[a].spin, Obj.Direction.defScale), Obj.Direction.defScale);
			}
			
			if (objCp[a].type != Obj.ObjType.Bullet) {
				
				objCp[a].xm = objCp[a].xm * Conf.objSlowMul / Conf.objSlowDiv;
				objCp[a].ym = objCp[a].ym * Conf.objSlowMul / Conf.objSlowDiv;
				
			}
			
			objCp[a].regenWaitCounter--;
			//if (a == 0) {Log.info("player has " + objCp[a].regenWaitCounter + " regenWaitCounter");}
			
			if (objCp[a].regenWaitCounter < 0) {
				
				objCp[a].health += objCp[a].regenerate;
				
				if (objCp[a].health > objCp[a].maxhp) {
					
					objCp[a].health = objCp[a].maxhp;
					
				}
				
			}
			
			if (objCp[a].collidable) {
				
				tmp0 = Conf.mapOutSpace - objCp[a].x;
				if (tmp0 > 0) objCp[a].xm += tmp0 / Conf.mapBorderSlowStep;
				tmp0 = Conf.mapPositiveBorder - objCp[a].x;
				if (tmp0 < 0) objCp[a].xm += tmp0 / Conf.mapBorderSlowStep;
				tmp0 = Conf.mapOutSpace - objCp[a].y;
				if (tmp0 > 0) objCp[a].ym += tmp0 / Conf.mapBorderSlowStep;
				tmp0 = Conf.mapPositiveBorder - objCp[a].y;
				if (tmp0 < 0) objCp[a].ym += tmp0 / Conf.mapBorderSlowStep;
				
			}
			
			for (int b = 0; b != objNumCp; b++) {
				if (a != b && collidable[a] && collidable[b] && (objCp[a].type != objCp[b].type || objCp[a].type == Obj.ObjType.Tank) && !(objCp[a].owner == b || objCp[b].owner == a)) {
					boolean checkCollision = true;
					
					/*if (Util.sum2d(objCp[b].xm, objCp[b].ym) > Conf.collisionUnderAngleCheck) {
						//Obj.Direction playerM = Obj.Direction.normalize(objCp[a].xm, objCp[a].ym, Obj.Direction.defScale);
						Obj.Direction relativeBM = Obj.Direction.normalize(objCp[b].xm - objCp[a].xm, objCp[b].ym - objCp[a].ym, Obj.Direction.defScale);
						Obj.Direction relativeBPos = Obj.Direction.normalize(objCp[b].x - objCp[a].x, objCp[b].y - objCp[a].y, Obj.Direction.defScale);
						int rbmDegress = relativeBM.degrees();
						int rbpDegress = relativeBPos.degrees();
						
						if (Obj.Direction.normalizeDegrees(rbmDegress - Conf.collisionAngle, Obj.Direction.defScale) > rbpDegress) {
							if (Obj.Direction.normalizeDegrees(rbmDegress + Conf.collisionAngle, Obj.Direction.defScale) < rbpDegress) {
								
								checkCollision = false;
								
							}
						}
						
					}*/
					
					if (checkCollision) {
						//System.out.println((objCp[a].size + objCp[b].size) / 2);
						int size = (objCp[a].size + objCp[b].size) / 2;
						
						if (colliding(objCp[a].x, objCp[a].y, objCp[b].x, objCp[b].y, size)) {
							objCp[a].behavior.collide(this, objCp, objNumCp, a, b);
							int speedDif = Util.sum2d(objCp[a].xm - objCp[b].xm, objCp[a].ym - objCp[b].ym);
							Obj.Direction throwDir = Obj.Direction.normalize(objCp[a].x - objCp[b].x, objCp[a].y - objCp[b].y, speedDif / objCp[b].weight + Conf.baseCollisionSpeed).negative();
							
							objCp[b].xm += throwDir.x;
							objCp[b].ym += throwDir.y;
							
							//try {throw new Throwable();} catch (Throwable e) {e.printStackTrace();}
							
							takeDamage(objCp, objNumCp, b, objCp[a].bodyDmg, a, "obj");
							
							//System.out.println("collide A: " + a + ", B: " + b);
						}
						
					}
					
				}
			}
			
			for (int b = 0; b != objCp[a].barrels.length; b++) {
				
				objCp[a].barrels[b].shotOffset = (int) ((double) objCp[a].barrels[b].shotOffset / 1.1);
				
			}
			
			//System.out.println("id: " + a + ", X: " + objCp[a].x + ", Y: " + objCp[a].y);
			
			if (died) {
				if (objCp[a].behavior.disappearing(this, objCp, objNumCp, a)) {
					
					died = false;
					remove(objCp, objNumCp, a);
					objNumCp--;
					a--;
					
				}
			}
			
		}
		
		for (int a = 0; a < objNumCp; a++) {
			
			objCp[a].x += objCp[a].xm;
			objCp[a].y += objCp[a].ym;
			
		}
		
		if (!serverMode) {
			int lastLevel = Util.currentLevel(mainPlayerLastScore, Conf.levels);
			int currentLevel = Util.currentLevel(objCp[player].score, Conf.levels);
			int currentSeparationLevel = currentLevel / Conf.separationLevel;
			
			if (lastLevel % Conf.separationLevel > currentLevel % Conf.separationLevel) {
				
				for (int a = 0; a < objNumCp; a++) {
					
					if (a != player) {
						objCp[a].behavior.killed(this, objCp, objNumCp, a, 0, "unknown");
					}
					
				}
				
				regularAreaObjs = Conf.regularAreaObjs[currentSeparationLevel];
				centerAreaObjs = Conf.centerAreaObjs[currentSeparationLevel];
				resetMapSegments(mapSegments);
				
			}
			
		}
		mainPlayerLastScore = objCp[player].score;
		
		objects = objCp;
		objectNumber = objNumCp;
		//record.recordFrame(objCp, objNumCp);
	}
	
	public void resetMapSegments(Obj.MapSegment[] ms) {
		
		for (int a = 0; a < ms.length; a++) {
			
			ms[a] = new Obj.MapSegment();
			
		}
		
	}
	
	private void spawnGroups(Obj.MapSegment segment, Obj.GroupSpawn[] groupSpawn) {
		
		for (int a = 0; a < groupSpawn.length; a++) {
			
			spawnGroup(segment, groupSpawn[a]);
			
		}
		
	}
	
	private void spawnGroup(Obj.MapSegment segment, Obj.GroupSpawn group) {
		
		if (Root.rand.nextInt(255) <= group.chance) {
			
			int objNum;
			
			try {
				objNum = Root.rand.nextInt(group.objsDif);
			} catch (IllegalArgumentException e) {
				objNum = 0;
			}
			
			int size = objNum * (group.minSize + group.sizeDif) / (group.minObjs + group.objsDif);
			
			segment.objGroupsObj[segment.groups] = group.obj;
			segment.objGroupsCount[segment.groups] = objNum + group.minObjs;
			segment.objGroupsSize[segment.groups] = size;
			segment.objGroupsX[segment.groups] = Root.rand.nextInt(Conf.segmentSize - size) + (size / 2);
			segment.objGroupsY[segment.groups] = Root.rand.nextInt(Conf.segmentSize - size) + (size / 2);
			segment.groups++;
			
		}
		
	}
	
	private boolean colliding(int ax, int ay, int bx, int by, int size /* average size of both */) {
		
		
		if (ax - size < bx) {
			if (ax + size > bx) {
				if (ay - size < by) {
					if (ay + size > by) {
						
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void remove(Obj[] objs, int num, int idx) {
		
		/*for (int a = idx; a < num; a++) {
			
			objs[a] = objs[a + 1];
			
		}*/
		
		Util.removeFromArray(objs, num, idx);
		
	}
	
	public void takeDamage(Obj[] objs, int objNum, int idx, int dmg, int psychopath, String hurtedBy) {
		int healthBefore = objs[idx].health;
		int armorBefore = objs[idx].armor;
		int realPsychopath = psychopath;
		
		objs[idx].armor -= dmg;
		objs[idx].regenWaitCounter = objs[idx].regenWait;
		
		if (objs[idx].armor < 0) {
			
			objs[idx].health += objs[idx].armor;
			objs[idx].armor = 0;
			
		}
		
		while (objs[realPsychopath].owner != Integer.MAX_VALUE) {
			
			realPsychopath = objs[realPsychopath].owner;
			
		}
		
		objs[idx].behavior.damageDealed(this, objs, objNum, idx, realPsychopath, hurtedBy, healthBefore - objs[idx].health, armorBefore - objs[idx].armor);
		
		if (objs[idx].health < 0) {
			kill(objs, objNum, idx, realPsychopath, hurtedBy);
		}
		
	}
	
	public void kill(Obj[] obj, int objNum, int idx, int objKiller, String killer) {
		int seg = obj[idx].seg;
		int group = obj[idx].group;
		int realKiller = objKiller;
		
		while (obj[realKiller].owner != Integer.MAX_VALUE) {
			
			realKiller = obj[realKiller].owner;
			
		}
		
		if (killer.equals("obj")) {
			
			obj[realKiller].score += obj[idx].score / 2;
			
		}
		
		if (obj[idx].seg != Integer.MAX_VALUE) {
			
			mapSegments[seg].objGroupsCount[group]--;
			
			if (mapSegments[seg].objGroupsCount[group] == 0) {
				
				for (int a = group; a < mapSegments[seg].groups; a++) {
					
					mapSegments[seg].objGroupsCount[a] = mapSegments[seg].objGroupsCount[a + 1];
					mapSegments[seg].objGroupsSize[a] = mapSegments[seg].objGroupsSize[a + 1];
					mapSegments[seg].objGroupsX[a] = mapSegments[seg].objGroupsX[a + 1];
					mapSegments[seg].objGroupsY[a] = mapSegments[seg].objGroupsY[a + 1];
					mapSegments[seg].objGroupsObj[a] = mapSegments[seg].objGroupsObj[a + 1];
					
				}
				
				mapSegments[seg].groups--;
				
			}
			
		}
		
		obj[idx].behavior.killed(this, obj, objNum, idx, realKiller, killer);
	}
	
	//private static boolean resAdd(Obj[] objs, int num) {}
	
	public void classChosed(int player, int id) {
		
		players[player].classId = id;
		players[player].toSet = true;
		players[player].classUpdated = true;
		
	}
	
	public void upgradeUpgraded(int player, int id) {
		
		if (id == 0)
			players[player].regenLevel++;
		else if (id == 1)
			players[player].maxhpLevel++;
		else if (id == 2)
			players[player].bodyDmgLevel++;
		else if (id == 3)
			players[player].bSpeedLevel++;
		else if (id == 4)
			players[player].bHealthLevel++;
		else if (id == 5)
			players[player].bDamageLevel++;
		else if (id == 6)
			players[player].reloadLevel++;
		else
			players[player].speedLevel++;
		
		players[player].toSet = true;
		
	}
	
	public static boolean upgradeUpgradable(int player, int upgradeLevel) {
		
		return upgradeLevel < Conf.maxUpgrade;
	}
	
	public void mouseAction(int x, int y, boolean clicked) {
		boolean clickFlag = clicked;
		
		mouseAction = new MouseAction(x, y, clickFlag);
		
	}
	
	public enum AtEnd {
		
		Continue,
		ReInit, // restart game
		Stop,
		
	}
	
}
