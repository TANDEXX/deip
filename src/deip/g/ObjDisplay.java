package deip.g;

import deip.data.Conf;
import deip.game.phy.*;
import java.awt.*;

public class ObjDisplay extends Object implements Cloneable {
	
	public void changeSize(int before, int after) {} // always should to be called when size is changed
	public void paint(Graphics g, Obj[] objs, int a, Point csSize, Point ccPos, Point cPos) {}
	
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
			
			g.setColor(Paint.colorTrans(objs[a].borderColor, objs[a]));
			g.fillOval(cPos.x, cPos.y, csSize.x, csSize.y);
			g.setColor(Paint.colorTrans(objs[a].bodyColor, objs[a]));
			g.fillOval(cPos.x + border.x, cPos.y + border.y, csSize.x - (border.x * 2), csSize.y - (border.y * 2));
			
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
			
			g.setColor(Paint.colorTrans(objs[a].borderColor, objs[a]));
			g.fillPolygon(outline0, outline1, vertexes);
			g.setColor(Paint.colorTrans(objs[a].bodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, vertexes);
			//System.out.println("ID: " + a + ", X: " + outline0[0] + ", Y: " + outline1[0]);
			
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
			
			g.setColor(Paint.colorTrans(objs[a].borderColor, objs[a]));
			g.fillPolygon(outline0, outline1, vertexes);
			g.setColor(Paint.colorTrans(objs[a].bodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, vertexes);
			
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
				
				g.setColor(Paint.colorTrans(objs[a].borderColor, objs[a]));
				g.fillPolygon(outline0, outline1, vertexes);
				g.setColor(Paint.colorTrans(objs[a].bodyColor, objs[a]));
				g.fillPolygon(inline0, inline1, vertexes);
				
			} catch (CloneNotSupportedException e) {}
		}
		
		public Star clone() throws CloneNotSupportedException {
			
			return (Star) super.clone();
		}
		
	}
	
}
