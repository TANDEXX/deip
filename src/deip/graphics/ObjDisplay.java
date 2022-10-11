package deip.graphics;

import deip.Root;
import deip.data.Conf;
import deip.game.phy.*;
import deip.lib.IncompatibleException;
import deip.lib.Util;

import java.awt.*;

public class ObjDisplay extends Object implements Cloneable {
	
	public void changeSize(int before, int after) {} // always should to be called when size is changed
	public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {}
	public byte[] save() {return new byte[]{};}
	public String id() {return "deip:none";}
	public ObjDisplay load(byte[] bytes) throws IncompatibleException {return new ObjDisplay();}
	
	public ObjDisplay clone() throws CloneNotSupportedException {
		
		return (ObjDisplay) super.clone();
	}
	
	public static class Circle extends ObjDisplay {
		
		public int border;
		
		public Circle() {
			
			this(Conf.defaultObjBorder);
			
		}
		
		public Circle(int border) {
			
			this.border = border;
			
		}
		
		public void changeSize(int before, int after) {
			
			border = border * after / before;
			super.changeSize(before, after);
		}
		
		public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {
			Point border = Paint.selectPos(this.border, this.border);
			
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBorderColor, objs[a]));
			g.fillOval(cPos.x, cPos.y, csSize.x, csSize.y);
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBodyColor, objs[a]));
			g.fillOval(cPos.x + border.x, cPos.y + border.y, csSize.x - (border.x * 2), csSize.y - (border.y * 2));
			
		}
		
		public Circle load(byte[] bytes) throws IncompatibleException {
			
			if (bytes.length != 4) {
				throw new IncompatibleException("ObjDisplay is 4 byte value, not " + bytes.length);
			}
			
			return new Circle(Util.getIntFromByteArray(bytes, 0));
		}
		
		public String id() {return "deip:circle";}
		
		public byte[] save() {
			byte[] result = new byte[4];
			Util.putIntToByteArray(result, border, 0);
			
			return result;
		}
		
		public Circle clone() throws CloneNotSupportedException {
			
			return (Circle) super.clone();
		}
		
	}
	
	public static class RPolygon extends ObjDisplay {
		
		public int border;
		public int vertexes;
		
		public RPolygon(int vertexes) {
			
			this(vertexes, Conf.defaultObjBorder);
			
		}
		
		public RPolygon(int vertexes, int border) {
			
			this.vertexes = vertexes;
			this.border = border;
			
		}
		
		public void changeSize(int before, int after) {
			
			border = border * after / before;
			super.changeSize(before, after);
		}
		
		public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {
			int degAdd = Obj.Direction.defScale * 4 / vertexes;
			int deg = Obj.Direction.normalizeDegrees(objs[a].dir.degrees() + (degAdd / 2), Obj.Direction.defScale);
			int centerSize = objs[a].size - border;
			int[] outline0 = new int[vertexes];
			int[] outline1 = new int[vertexes];
			int[] inline0 = new int[vertexes];
			int[] inline1 = new int[vertexes];
			//Obj.Direction current = Obj.Direction.fromDegrees(deg, Obj.Direction.defScale);
			
			for (int b = 0; b < vertexes; b++) {
				Obj.Direction line = Obj.Direction.rawSinus(deg, Obj.Direction.defScale).newScale(objs[a].size).normalize().round();
				outline0[b] = Paint.selectX(line.x) + ccPos.x;
				outline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				line = line.newScale(centerSize).normalize().round();
				inline0[b] = Paint.selectX(line.x) + ccPos.x;
				inline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				deg += degAdd;
				deg = Obj.Direction.normalizeDegrees(deg, Obj.Direction.defScale);
				
			}
			
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBorderColor, objs[a]));
			g.fillPolygon(outline0, outline1, vertexes);
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, vertexes);
			//System.out.println("ID: " + a + ", X: " + outline0[0] + ", Y: " + outline1[0]);
			
		}
		
		public String id() {return "deip:rpolygon";}
		public byte[] save() {
			byte[] result = new byte[8];
			Util.putIntToByteArray(result, border, 0);
			Util.putIntToByteArray(result, vertexes, 4);
			
			return result;
		}
		
		public RPolygon clone() throws CloneNotSupportedException {
			
			return (RPolygon) super.clone();
		}
		
	}
	
	public static class CursedStar extends ObjDisplay { // so when making Star it looked weird but my dad said it looks good
		
		public int vertexes;
		public int closerVertexHeight;
		public int awaierVertexHeight;
		public int border;
		
		public CursedStar(int vertexes, int closerVertexHeight, int awaierVertexHeight) {
			
			this(vertexes, closerVertexHeight, awaierVertexHeight, Conf.defaultObjBorder);
			
		}
		
		public CursedStar(int vertexes, int closerVertexHeight, int awaierVertexHeight, int border) {
			
			this.vertexes = vertexes;
			this.closerVertexHeight = closerVertexHeight;
			this.awaierVertexHeight = awaierVertexHeight;
			this.border = border;
			
		}
		
		public void changeSize(int before, int after) {
			
			closerVertexHeight = closerVertexHeight * after / before;
			awaierVertexHeight = awaierVertexHeight * after / before;
			border = border * after / before;
			super.changeSize(before, after);
		}
		
		public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {
			int degAdd = Obj.Direction.defScale * 4 / vertexes;
			int deg = Obj.Direction.normalizeDegrees(objs[a].dir.degrees(), Obj.Direction.defScale);
			int[] outline0 = new int[vertexes];
			int[] outline1 = new int[vertexes];
			int[] inline0 = new int[vertexes];
			int[] inline1 = new int[vertexes];
			
			for (int b = 0; b < vertexes; b++) {
				Obj.Direction line = Obj.Direction.rawSinus(deg, Obj.Direction.defScale);
				
				if (b % 2 == 0) {
					line = line.newScale(awaierVertexHeight);
				} else {
					line = line.newScale(closerVertexHeight);
				}
				line = line.normalize().round();
				outline0[b] = Paint.selectX(line.x) + ccPos.x;
				outline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				line = line.newScale(line.scale - border).normalize().round();
				inline0[b] = Paint.selectX(line.x) + ccPos.x;
				inline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				deg += degAdd;
				deg = Obj.Direction.normalizeDegrees(deg, Obj.Direction.defScale);
			}
			
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBorderColor, objs[a]));
			g.fillPolygon(outline0, outline1, vertexes);
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, vertexes);
			
		}
		
		public String id() {return "deip:cursedStar";}
		
		public byte[] save() {
			byte[] result = new byte[16];
			Util.putIntToByteArray(result, border, 0);
			Util.putIntToByteArray(result, vertexes, 4);
			Util.putIntToByteArray(result, closerVertexHeight, 8);
			Util.putIntToByteArray(result, awaierVertexHeight, 12);
			
			return result;
		}
		
		public CursedStar clone() throws CloneNotSupportedException {
			
			return (CursedStar) super.clone();
		}
		
	}
	
	public static class Star extends ObjDisplay {
		
		public int vertexes;
		public int closerVertexHeight;
		public int awaierVertexHeight;
		public int border;
		
		public Star(int vertexes, int closerVertexHeight, int awaierVertexHeight) {
			
			this(vertexes, closerVertexHeight, awaierVertexHeight, Conf.defaultObjBorder);
			
		}
		
		public Star(int vertexes, int closerVertexHeight, int awaierVertexHeight, int border) {
			
			this.vertexes = vertexes;
			this.closerVertexHeight = closerVertexHeight;
			this.awaierVertexHeight = awaierVertexHeight;
			this.border = border;
			
		}
		
		public void changeSize(int before, int after) {
			
			closerVertexHeight = closerVertexHeight * after / before;
			awaierVertexHeight = awaierVertexHeight * after / before;
			border = border * after / before;
			super.changeSize(before, after);
		}
		
		public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {
			try {
				int degAdd = Obj.Direction.defScale * 4 / vertexes;
				int deg = Obj.Direction.normalizeDegrees(objs[a].dir.degrees(), Obj.Direction.defScale);
				int[] outline0 = new int[vertexes];
				int[] outline1 = new int[vertexes];
				int[] inline0 = new int[vertexes];
				int[] inline1 = new int[vertexes];
				
				for (int b = 0; b < vertexes; b++) {
					Obj.Direction outline = Obj.Direction.rawSinus(deg, Obj.Direction.defScale);
					Obj.Direction inline = outline.clone();
					
					if (b % 2 == 0) {
						outline = outline.newScale(awaierVertexHeight);
						inline = inline.newScale(awaierVertexHeight - border);
					} else {
						outline = outline.newScale(closerVertexHeight);
						inline = inline.newScale(closerVertexHeight - border);
					}
					outline = outline.normalize().round();
					inline = inline.normalize().round();
					outline0[b] = Paint.selectX(outline.x) + ccPos.x;
					outline1[b] = Paint.selectY(outline.y) + ccPos.y;
					
					inline0[b] = Paint.selectX(inline.x) + ccPos.x;
					inline1[b] = Paint.selectY(inline.y) + ccPos.y;
					
					deg += degAdd;
					deg = Obj.Direction.normalizeDegrees(deg, Obj.Direction.defScale);
				}
				
				g.setColor(Paint.colorTrans(objs[a].currentDisplayBorderColor, objs[a]));
				g.fillPolygon(outline0, outline1, vertexes);
				g.setColor(Paint.colorTrans(objs[a].currentDisplayBodyColor, objs[a]));
				g.fillPolygon(inline0, inline1, vertexes);
				
			} catch (CloneNotSupportedException e) {}
		}
		
		public String id() {return "deip:star";}
		public byte[] save() {
			byte[] result = new byte[16];
			Util.putIntToByteArray(result, border, 0);
			Util.putIntToByteArray(result, vertexes, 4);
			Util.putIntToByteArray(result, closerVertexHeight, 8);
			Util.putIntToByteArray(result, awaierVertexHeight, 12);
			
			return result;
		}
		
		public Star clone() throws CloneNotSupportedException {
			
			return (Star) super.clone();
		}
		
	}
	
	public static class MovingPolygon extends ObjDisplay implements Cloneable {
		
		int border;
		int vertexMaxPos;
		int[] vertexDegress;
		int[] vertexMotion;
		
		public MovingPolygon() {}
		public MovingPolygon(int vertexes) {
			
			this(Conf.defaultObjBorder, vertexes);
			
		}
		
		public MovingPolygon(int border, int vertexes) {
			this.border = border;
			vertexDegress = new int[vertexes];
			vertexMotion = new int[vertexes];
			vertexMaxPos = Obj.Direction.defScale * 4 / vertexes;
			regenVertexes();
			
		}
		
		public void changeSize(int before, int after) {
			
			border = border * after / before;
			
		}
		
		public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {
			int vertexes = vertexDegress.length;
			
			for (int b = 0; b < vertexes; b++) {
				int subtractor = 6;
				if (vertexDegress[b] < 0) subtractor = -subtractor;
				
				vertexMotion[b] -= subtractor;
				vertexDegress[b] += vertexMotion[b];
				
			}
			
			int degAdd = Obj.Direction.defScale * 4 / vertexes;
			int deg = Obj.Direction.normalizeDegrees(objs[a].dir.degrees() + (degAdd / 2), Obj.Direction.defScale);
			int centerSize = objs[a].size - border;
			int[] outline0 = new int[vertexes];
			int[] outline1 = new int[vertexes];
			int[] inline0 = new int[vertexes];
			int[] inline1 = new int[vertexes];
			//Obj.Direction current = Obj.Direction.fromDegrees(deg, Obj.Direction.defScale);
			
			for (int b = 0; b < vertexes; b++) {
				Obj.Direction line = Obj.Direction.rawSinus(deg + vertexDegress[b], Obj.Direction.defScale).newScale(objs[a].size).normalize().round();
				outline0[b] = Paint.selectX(line.x) + ccPos.x;
				outline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				line = line.newScale(centerSize).normalize().round();
				inline0[b] = Paint.selectX(line.x) + ccPos.x;
				inline1[b] = Paint.selectY(line.y) + ccPos.y;
				
				deg += degAdd;
				deg = Obj.Direction.normalizeDegrees(deg, Obj.Direction.defScale);
				
			}
			
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBorderColor, objs[a]));
			g.fillPolygon(outline0, outline1, vertexes);
			g.setColor(Paint.colorTrans(objs[a].currentDisplayBodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, vertexes);
			
		}
		
		public byte[] save() {
			byte[] result = new byte[8];
			Util.putIntToByteArray(result, border, 0);
			Util.putIntToByteArray(result, vertexDegress.length, 4);
			
			return result;
		}
		
		public String id() {
			return "deip:movingPolygon";
		}
		
		public void regenVertexes() {
			
			for (int a = 0; a < vertexDegress.length; a++) {
				
				vertexDegress[a] = Root.rand.nextInt(vertexMaxPos) - (vertexMaxPos / 2);
				vertexMotion[a] = 0;
				
			}
			
		}
		
		public MovingPolygon clone() throws CloneNotSupportedException {
			MovingPolygon result = (MovingPolygon) super.clone();
			result.regenVertexes();
			
			return result;
		}
		
	}
	
}
