package deip.lib;

import java.awt.*;
import deip.data.Conf;
import deip.game.phy.Obj;

public class Util {
	
	public static String intArrayToString(int[] array) {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(array[0]);
		for (int a = 1; a < array.length; a++) {
			
			buffer.append(", ");
			buffer.append(array[a]);
			
		}
		
		return buffer.toString();
	}
	
	public static void removeFromArray(Object[] array, int num, int idx) {
		
		for (int a = idx; a < num; a++) {
			
			array[a] = array[a + 1];
			
		}
		
	}
	
	public static int sum2d(int x, int y) {
		
		return positive(x) + positive(y);
	}
	
	public static int positive(int a) {
		int ret = a;
		
		if (a < 0) {
			ret = -ret;
		}
		
		return ret;
	}
	
	public static String numWSuffix(int x) {
		
		if (x >= 1000000000) {
			return (float) (x / 10000000) / 100.0 + "G";
		} else if (x >= 1000000) {
			return (float) (x / 10000) / 100.0 + "M";
		} else if (x >= 1000) {
			return (float) (x / 10) / 100.0 + "K";
		} else if (x > -1) {
			return x + "";
		} else {
			return x + " noob :)";
		}
		
	}
	
	public static int currentLevel(int score, int[] levels) {
		int b = 0;
		
		try {
			for (int a = score; a >= levels[b]; a -= levels[b]) {
				
				b++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return levels.length;
		}
		
		return b;
	}
	
	// use outside Phy class (in simplification)
	public static void changeDirection(Obj obj, Obj.Direction newDir, int newDegress) {
		obj.dir = newDir;
		
		for (int a = 0; a < obj.barrels.length; a++) {
			
			obj.barrels[a].currentDegrees = Obj.Direction.normalizeDegrees(obj.barrels[a].degrees + newDegress, Obj.Direction.defScale);
			
		}
		
	}
	
	public static Obj.Barrel[] cloneBarrelArray(Obj.Barrel[] src) throws CloneNotSupportedException {
		Obj.Barrel[] result = new Obj.Barrel[src.length];
		
		for (int a = 0; a < src.length; a++) {
			
			result[a] = src[a].clone();
			
		}
		
		return result;
	}
	
	public static int between(int a, int b, int at) {
		
		return a + ((b - a) * at / Conf.betweenFuncRange);
	}
	
	public static Color colorBetween(Color a, Color b, int at) {
		
		return new Color(between(a.getBlue(), b.getBlue(), at) | (between(a.getGreen(), b.getGreen(), at) << 8) | (between(a.getRed(), b.getRed(), at) << 16));
	}
	
}
