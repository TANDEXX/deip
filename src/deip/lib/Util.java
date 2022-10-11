package deip.lib;

import java.awt.*;
//import java.time.*;
//import java.time.format.*;
import java.util.*;

import deip.data.*;
import deip.game.phy.*;

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
	
	public static Pair<Integer, Integer> currentLevelAndRest(int score, int[] levels) {
		int a = 0;
		
		try {
			while (score >= levels[a]) {
				
				score -= levels[a];
				a++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		return new Pair(a, score);
	}
	
	public static int currentLevel(int score, int[] levels) {
		
		return currentLevelAndRest(score, levels).a;
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
	
	public static String formatPlayTime(int invertedLifetime) {
		
		if (invertedLifetime < 60) {
			return invertedLifetime * (1000 / 60) + " milliSeconds";
		} else if (invertedLifetime < 60 * 60) {
			return invertedLifetime / 60 + " seconds";
		} else if (invertedLifetime < 60 * 60 * 60) {
			return invertedLifetime / 60 / 60 + " minutes";
		} else if (invertedLifetime < 60 * 60 * 60 * 24) {
			return invertedLifetime / 60 / 60 / 60 + " hours";
		} else if (invertedLifetime < 60 * 60 * 60 * 24 * 30) {
			return invertedLifetime / 60 / 60 / 60 / 24 + " days";
		} else if (invertedLifetime < 60 * 60 * 60 * 24 * 30 * 12) {
			return invertedLifetime / 60 / 60 / 60 / 24 / 30 + "months";
		} else {
			return invertedLifetime / 60 / 60 / 60 / 24 / 30 / 12 + "years";
		}
		
	}
	
	public static String formatedDatetime() {
		
		//return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss"));
		return Long.toString(System.currentTimeMillis());
	}
	
	public static void putIntToByteArray(byte[] array, int value, int idx) {
		
		array[idx + 3] = (byte) (value & 0xff);
		array[idx + 2] = (byte) (value >> 8 & 0xff);
		array[idx + 1] = (byte) (value >> 16 & 0xff);
		array[idx + 0] = (byte) (value >> 24 & 0xff);
		
	}
	
	public static void putIntToByteArrayList(ArrayList<Byte> al, int value) {
		
		al.add((byte) (value >> 24 & 0xff));
		al.add((byte) (value >> 16 & 0xff));
		al.add((byte) (value >> 8 & 0xff));
		al.add((byte) (value & 0xff));
		
	}
	
	public static void putByteArrayToByteArray(byte[] array, byte[] secArray, int idx) {
		
		for (int a = 0; a < secArray.length; a++) {
			array[idx + a] = secArray[a];
		}
		
	}
	
	public static void putByteArrayToByteArrayList(ArrayList<Byte> al, byte[] ar) {
		
		for (byte b : ar) {
			al.add(b);
		}
		
	}
	
	public static void putByteArrayListToByteArrayList(ArrayList<Byte> a0, ArrayList<Byte> a1) {
		
		for (Byte aByte : a1) {
			a0.add(aByte);
		}
		
	}
	
	public static int getIntFromByteArray(byte[] array, int idx) {
		int result = array[idx + 3];
		result |= (int) array[idx + 2] << 8;
		result |= (int) array[idx + 1] << 16;
		result |= (int) array[idx] << 24;
		
		return result;
	}
	
	public static class Press {
		
		public boolean press = false;
		public boolean lastPress = false;
		public boolean released = false;
		public boolean pressed = false;
		
		public void register(boolean press) {
			
			lastPress = this.press;
			this.press = press;
			
			pressed = false;
			if (press && !lastPress) {
				pressed = true;
			}
			
			released = false;
			if (!press && lastPress) {
				released = true;
			}
			
		}
		
	}
	
	public static class Pair<A, B> {
		
		public A a;
		public B b;
		
		public Pair(A a, B b) {
			
			this.a = a;
			this.b = b;
			
		}
		
	}
	
}
