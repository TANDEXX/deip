package deip.game.phy;

import deip.data.Conf;
import deip.lib.Util;
import deip.g.*;
import java.awt.*;

public class Obj implements Cloneable {
	
	public static class Direction implements Cloneable {
		
		public static final Direction def = new Direction(1, 0, 1);
		public static final int defScale = 0xffff;
		
		public int x;
		public int y;
		public int scale;
		
		public Direction(int x, int y, int scale) {
			
			this.x = x;
			this.y = y;
			this.scale = scale;
			
		}
		
		public static Direction negative(Direction dir) {
			
			return new Direction(-dir.x, -dir.y, dir.scale);
		}
		
		public static Direction sub(Direction origin, Direction op) {
			
			return new Direction(origin.x - op.x, origin.y - op.y, origin.scale);
		}
		
		public static Direction add(Direction origin, Direction op) {
			
			return new Direction(origin.x + op.x, origin.y + op.y, origin.scale);
		}
		
		public static Direction round(Direction dir) {
			long x = dir.x;
			long y = dir.y;
			double fountLen = Math.sqrt((x * x) + (y * y));
			
			return normalize(dir.x, dir.y, dir.scale - (int) (fountLen * 1.144) + dir.scale);
		}
		
		// both dirs must have same scale
		public static Direction xOffset(Direction ndir, int x) {
			Direction rdir = ndir.round();
			int xs = x;
			if (x < 0) {
				xs = -x;
			}
			Direction relative = fromDegrees(normalizeDegrees(ndir.degrees() + ndir.scale, ndir.scale), ndir.scale).newScale(xs).normalize().round();
			
			if (x < 0) {
				return rdir.sub(relative);
			} else {
				return rdir.add(relative);
			}
		}
		
		public static Direction tanges(Direction dir) {
			double fScale = dir.scale;
			double deg = dir.degrees() / 2.0;
			double tv = deg / fScale * Math.PI;
			double ox = Math.tan(tv) * fScale;
			double oy = Math.tan(tv + (Math.PI / 2.0)) * fScale;
			if (dir.x > 0) {
				oy = -oy;
			}
			if (dir.y < 0) {
				ox = -ox;
			}
			
			return new Direction((int) ox, (int) oy, dir.scale);
		}
		
		public static Direction sinus(Direction dir) {
			
			return rawSinus(dir.degrees(), dir.scale);
		}
		
		public static Direction rawSinus(int degrees, int scale) {
			double fScale = scale;
			double deg = degrees / 2.0;
			double sv = deg / fScale * Math.PI;
			double ox = Math.sin(sv) * fScale;
			double oy = Math.cos(sv) * fScale;
			
			return new Direction((int) ox, (int) oy, scale);
		}
		
		public static int normalizeDegrees(int deg, int scale) {
			int fourScale = scale * 4;
			
			return (deg + fourScale) % fourScale;
		}
		
		// dir must be normalized (not rounded or something)
		public static int degrees(Direction dir) {
			
			if (dir.x < 0) {
				if (dir.y < 0) {
					return -dir.x + (dir.scale * 2);
				} else {
					return dir.y + (dir.scale * 3);
				}
			} else {
				if (dir.y < 0) {
					return -dir.y + dir.scale;
				} else {
					return dir.x;
				}
			}
			
			//return dir.scale * 4 - (int) (Math.atan2(dir.y, dir.x) / Math.PI * dir.scale * 2);
		}
		
		public static Direction fromDegrees(int deg, int scale) {
			int degIter = deg;
			int x = 0;
			int y = 0;
			boolean uncompleted = true;
			
			if (degIter < scale) {
				x = degIter;
				y = scale - degIter;
				uncompleted = false;
			}
			
			if (uncompleted) {
				degIter -= scale;
				
				if (degIter < scale) {
					x = scale - degIter;
					y = -degIter;
					uncompleted = false;
				}
			}
			
			if (uncompleted) {
				degIter -= scale;
				
				if (degIter < scale) {
					x = -degIter;
					y = degIter - scale;
					uncompleted = false;
				}
			}
			
			if (uncompleted) {
				degIter -= scale;
				
				if (degIter < scale) {
					x = degIter - scale;
					y = degIter;
				}
			}
			
			return new Direction(x, y, scale);
		}
		
		public static Direction normalize(int x, int y, int scale) {
			try {
				boolean fx;
				boolean fy;
				long px;
				long py;
				
				if (x < 0) {
					fx = true;
					px = -x;
				} else {
					fx = false;
					px = x;
				}
				
				if (y < 0) {
					fy = true;
					py = -y;
				} else {
					fy = false;
					py = y;
				}
				
				long sum = (px + py);
				int ox = (int) (px * (long) scale / sum);
				int oy = (int) (py * (long) scale / sum);
				int newScale = ox + oy;
				
				if (fx) ox = -ox;
				if (fy) oy = -oy;
				
				return new Direction(ox, oy, newScale);
			} catch (ArithmeticException e) {
				
				return new Direction(scale, 0, scale);
			}
		}
		
		public int length(Direction dir) {
			
			return length(dir.x, dir.y);
		}
		
		public int length(int x, int y) {
			
			return (int) Math.sqrt((x * x) + (y * y));
		}
		
		public Direction newScale(int scale) {
			
			return new Direction(x, y, scale);
		}
		
		public static Direction normalize(Direction dir) {
			
			return normalize(dir.x, dir.y, dir.scale);
		}
		
		public Direction negative() {
			
			return negative(this);
		}
		
		public Direction add(Direction adder) {
			
			return add(this, adder);
		}
		
		public Direction sub(Direction subtracer /* ? */) {
			
			return sub(this, subtracer);
		}
		
		// use when normlized
		public Direction xOffset(int x) {
			
			return xOffset(this, x);
		}
		
		public Direction tanges() {
			
			return tanges(this);
		}
		
		public Direction sinus() {
			
			return sinus(this);
		}
		
		public Direction round() {
			
			return round(this);
		}
		
		public int degrees() {
			
			return degrees(this);
		}
		
		public Direction normalize() {
			
			return normalize(x, y, scale);
		}
		
		public int length() {
			
			return length(this);
		}
		
		public Direction clone() throws CloneNotSupportedException {
			
			return new Direction(x, y, scale);
		}
		
	}
	
	public static class MapSegment {
		
		public boolean active = false;
		public int groups = 0;
		public int[] objGroupsX = new int[Conf.groupsPerSegment];
		public int[] objGroupsY = new int[Conf.groupsPerSegment];
		public int[] objGroupsSize = new int[Conf.groupsPerSegment];
		public int[] objGroupsCount = new int[Conf.groupsPerSegment];
		public Obj[] objGroupsObj = new Obj[Conf.groupsPerSegment];
		
		public static Point segmentPos(int segment) {
			int row = segment / Conf.segmentsPerRow;
			int column = segment % Conf.segmentsPerRow;
			
			return new Point(Conf.segmentSize * column + Conf.mapOutSpace, Conf.segmentSize * row + Conf.mapOutSpace);
		}
		
		public static int segment(int x, int y) {
			
			try {
				return (x - Conf.mapOutSpace) / Conf.segmentSize + ((y - Conf.mapOutSpace) / Conf.segmentSize * Conf.segmentsPerRow);
			} catch (ArithmeticException e) {
				return 0;
			}
		}
		
		public static int[] seenSegments(int x, int y, int viewFieldX, int viewFieldY) {
			int nx = x - viewFieldX;
			int px = x + viewFieldX;
			int ny = y - viewFieldY;
			int py = y + viewFieldY;
			int[] buffer = new int[4];
			
			buffer[0] = segment(nx, ny);
			buffer[1] = segment(px, ny);
			buffer[2] = segment(nx, py);
			buffer[3] = segment(px, py);
			
			return buffer;
		}
		
	}
	
	public static class Barrel implements Cloneable {
		
		// Cycles are executed 60 times a second (60hz), this game always goes by this frame rate
		// these values are counted in cycles:
		// reload, delay
		// bspeed is how long it will travel in one cycle
		// for distance comparison default player tank size is 6000 and default view field is 185000x105000
		
		public Obj bullet;
		public BarrelDisplay display;
		public int bspeed;
		public int spread;
		public int reload;
		public int delay;
		public int recoil;
		public int degrees;
		// currents
		public int currentDegrees;
		public int shootCountDown = 0;
		public int shootOffset = 0;
		public boolean reachedZero = true;
		
		public Barrel(int bSpeed, int reload, int delay, int spread, int recoil, int degrees, BarrelDisplay display, Obj bullet) {
			
			this.bspeed = bSpeed;
			this.spread = spread;
			this.recoil = recoil;
			this.reload = reload;
			this.delay = delay;
			this.degrees = degrees;
			currentDegrees = degrees;
			this.display = display;
			this.bullet = bullet;
			
		}
		
		public Barrel clone() throws CloneNotSupportedException {
			Barrel result = new Barrel(bspeed, reload, delay, spread, recoil, degrees, display.clone(), bullet.clone());
			result.currentDegrees = currentDegrees;
			result.shootCountDown = shootCountDown;
			result.shootOffset = shootOffset;
			result.reachedZero = reachedZero;
			
			return result;
		}
		
	}
	
	public static class GroupSpawn {
		
		public Obj obj;
		public int chance;
		public int minObjs;
		public int objsDif; // maxObjs - minObjs
		public int minSize;
		public int sizeDif; // maxSize - minSize
		
		public GroupSpawn(Obj obj, int chance, int minObjs, int objsDif, int minSize, int sizeDif) {
			
			this.obj = obj;
			this.chance = chance;
			this.minObjs = minObjs;
			this.objsDif = objsDif;
			this.minSize = minSize;
			this.sizeDif = sizeDif;
			
		}
		
	}
	
	public static class Player {
		
		public int objId;
		public int classId = 0xffff;
		public int selectedClass = 0;
		
		public int regenLevel = 0;
		public int maxhpLevel = 0;
		public int bodyDmgLevel = 0;
		public int bSpeedLevel = 0;
		public int bHealthLevel = 0;
		public int bDamageLevel = 0;
		public int reloadLevel = 0;
		public int speedLevel = 0;
		
		public boolean toSet = false;
		public boolean classUpdated = false;
		
		public Player(int objId) {
			
			this.objId = objId;
			
		}
		
	}
	
	public enum ObjType {
		
		Tank,
		Bullet,
		Res,
		
	}
	
	// if you want "infinite" lifetime then give 0
	
	public boolean collidable = true;
	public byte trans;
	public int seg = Integer.MAX_VALUE;
	public int group = Integer.MAX_VALUE;
	public int owner = Integer.MAX_VALUE;
	public int x;
	public int y;
	public int xm;
	public int ym;
	public int spin = 0;
	public int size;
	public int lifeTime;
	public int score;
	public int health;
	public int maxhp;
	public int armor;
	public int regenerate = Conf.defaultRegen; // health per cycle (60hz)
	public int regenWaitCounter = 0;
	public int regenWait = Conf.defaultRegenWait;
	public int bodyDmg;
	public int speed;
	public int weight;
	public int barrelBorder = Conf.defaultObjBorder;
	public Direction dir = new Direction(0, Direction.defScale, Direction.defScale);
	public Color bodyColor;
	public Color borderColor;
	public ObjBehavior behavior;
	public ObjDisplay display;
	public ObjType type;
	public Barrel[] barrels;
	
	public Obj(int x, int y, int size, int weight, int lifeTime, int maxhp, int armor, int score, int speed, ObjBehavior behavior, ObjDisplay display, ObjType type, Color bodyColor, Color borderColor, Barrel[] barrels) {
		
		this(x, y, 0, 0, size, weight, lifeTime, maxhp, armor, score, Conf.bodyDmg, speed, behavior, display, (byte) 0xff, type, bodyColor, borderColor, barrels);
		
	}
	
	public Obj(int x, int y, int xm, int ym, int size, int weight, int lifeTime, int maxhp, int armor, int score, int bodyDmg, int speed, ObjBehavior behavior, ObjDisplay display, byte trans, ObjType type, Color bodyColor, Color borderColor, Barrel[] barrels) {
		
		this.behavior = behavior;
		this.display = display;
		this.x = x;
		this.y = y;
		this.xm = xm;
		this.ym = ym;
		this.size = size;
		this.maxhp = maxhp;
		this.health = maxhp;
		this.armor = armor;
		this.score = score;
		this.bodyDmg = bodyDmg;
		this.speed = speed;
		this.weight = weight;
		this.lifeTime = lifeTime;
		this.trans = trans;
		this.type = type;
		this.bodyColor = bodyColor;
		this.borderColor = borderColor;
		this.barrels = barrels;
		
	}
	
	public Obj clone() throws CloneNotSupportedException {
		Obj o = new Obj(x, y, xm, ym, size, weight, lifeTime, maxhp, armor, score, bodyDmg, speed, behavior.clone(), display.clone(), trans, type, bodyColor, borderColor, Util.cloneBarrelArray(barrels));
		o.seg = seg;
		o.group = group;
		o.owner = owner;
		o.spin = spin;
		o.health = health;
		o.regenerate = regenerate;
		o.regenWaitCounter = regenWaitCounter;
		o.regenWait = regenWait;
		o.dir = dir.clone();
		
		return o;
	}
	
}
