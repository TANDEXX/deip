package deip.graphics;

import java.awt.*;

import deip.data.*;
import deip.game.phy.*;
import deip.lib.*;
import deip.lib.IncompatibleException;

public class BarrelDisplay implements Cloneable {
	
	
	public Point shootPoint() {return new Point(0, 0);}
	public void changeSize(Obj[] objs, int idx, int before, int after) {} // before and after are values from ObjDisplay
	public void paint(Graphics g, Obj[] objs, int objNum, int a, int b, Point csSize, Point ccPos, Point cPos) {}
	public byte[] save() {return new byte[]{};}
	public String id() {return "deip:none";}
	public BarrelDisplay load(byte[] bytes) throws IncompatibleException {return new BarrelDisplay();}
	
	public BarrelDisplay clone() throws CloneNotSupportedException {
		
		return (BarrelDisplay) super.clone();
	}
	
	public static class Generic extends BarrelDisplay {
		
		public int length;
		public int xoffset;
		public int yoffset;
		public int blcx;
		public int brcx;
		public int tlcx;
		public int trcx;
		
		public Generic() {
			this(0, 0, 0, 0);
		}
		
		public Generic(int length, int xoffset, int yoffset, int width) {
			
			this(length, xoffset, yoffset, -width/2, width/2, -width/2, width/2);
			
		}
		
		public Generic(int length, int xoffset, int yoffset, int blcx, int brcx, int tlcx, int trcx) {
			
			this.length = length;
			this.xoffset = xoffset;
			this.yoffset = yoffset;
			this.blcx = blcx;
			this.brcx = brcx;
			this.tlcx = tlcx;
			this.trcx = trcx;
			
		}
		
		public Point shootPoint() {
			
			return new Point(xoffset, yoffset + length);
		}
		
		public void paint(Graphics g, Obj[] objs, int objNum, int a, int b, Point csSize, Point ccPos, Point cPos) {
			int[] outline0 = new int[4];
			int[] outline1 = new int[4];
			int[] inline0 = new int[4];
			int[] inline1 = new int[4];
			
			Obj.Direction bdir = Obj.Direction.rawSinus(objs[a].barrels[b].currentDegrees, Obj.Direction.defScale);
			Obj.Direction bcdir = bdir.newScale(yoffset - objs[a].barrels[b].shotOffset).normalize();
			Obj.Direction brdir = bcdir.xOffset(brcx + xoffset);
			Obj.Direction bldir = bcdir.xOffset(blcx + xoffset);
			
			outline0[0] = Paint.selectX(brdir.x) + ccPos.x;
			outline1[0] = Paint.selectY(brdir.y) + ccPos.y;
			outline0[1] = Paint.selectX(bldir.x) + ccPos.x;
			outline1[1] = Paint.selectY(bldir.y) + ccPos.y;
			
			bcdir = bdir.newScale(bcdir.scale + objs[a].barrelBorder - objs[a].barrels[b].shotOffset).normalize();
			brdir = bcdir.xOffset(brcx - objs[a].barrelBorder + xoffset);
			bldir = bcdir.xOffset(blcx + objs[a].barrelBorder + xoffset);
			
			inline0[0] = Paint.selectX(brdir.x) + ccPos.x;
			inline1[0] = Paint.selectY(brdir.y) + ccPos.y;
			inline0[1] = Paint.selectX(bldir.x) + ccPos.x;
			inline1[1] = Paint.selectY(bldir.y) + ccPos.y;
			
			bcdir = bdir.newScale(bcdir.scale + length - objs[a].barrelBorder - objs[a].barrels[b].shotOffset).normalize();
			brdir = bcdir.xOffset(trcx + xoffset);
			bldir = bcdir.xOffset(tlcx + xoffset);
			
			outline0[2] = Paint.selectX(bldir.x) + ccPos.x;
			outline1[2] = Paint.selectY(bldir.y) + ccPos.y;
			outline0[3] = Paint.selectX(brdir.x) + ccPos.x;
			outline1[3] = Paint.selectY(brdir.y) + ccPos.y;
			
			bcdir = bdir.newScale(bcdir.scale - objs[a].barrelBorder - objs[a].barrels[b].shotOffset).normalize();
			brdir = bcdir.xOffset(trcx - objs[a].barrelBorder + xoffset);
			bldir = bcdir.xOffset(tlcx + objs[a].barrelBorder + xoffset);
			
			inline0[2] = Paint.selectX(bldir.x) + ccPos.x;
			inline1[2] = Paint.selectY(bldir.y) + ccPos.y;
			inline0[3] = Paint.selectX(brdir.x) + ccPos.x;
			inline1[3] = Paint.selectY(brdir.y) + ccPos.y;
			
			g.setColor(Paint.colorTrans(Conf.barrelBorderColor, objs[a]));
			g.fillPolygon(outline0, outline1, 4);
			g.setColor(Paint.colorTrans(Conf.barrelBodyColor, objs[a]));
			g.fillPolygon(inline0, inline1, 4);
			
			super.paint(g, objs, objNum, a, b, csSize, ccPos, cPos);
		}
		
		public String id() {return "deip:generic";}
		
		public byte[] save() {
			byte[] result = new byte[7 * 4];
			Util.putIntToByteArray(result, length, 0);
			Util.putIntToByteArray(result, xoffset, 4);
			Util.putIntToByteArray(result, yoffset, 8);
			Util.putIntToByteArray(result, blcx, 12);
			Util.putIntToByteArray(result, brcx, 16);
			Util.putIntToByteArray(result, tlcx, 20);
			Util.putIntToByteArray(result, trcx, 24);
			
			return result;
		}
		
		public Generic load(byte[] bytes) {
			
			return new Generic(
				Util.getIntFromByteArray(bytes, 0), Util.getIntFromByteArray(bytes, 4),
				Util.getIntFromByteArray(bytes, 8), Util.getIntFromByteArray(bytes, 12),
				Util.getIntFromByteArray(bytes, 16), Util.getIntFromByteArray(bytes, 20),
				Util.getIntFromByteArray(bytes, 24));
		}
		
		public Generic clone() throws CloneNotSupportedException {
			
			return (Generic) super.clone();
		}
		
	}
	
}
