package deip.game.phy;

// just make class extend this shet and (ObjBehavior) AMyClass when Obj create

import java.awt.*;
import deip.data.Conf;
import deip.lib.*;

public class ObjBehavior implements Cloneable {
	
	public static final ObjBehavior none = new ObjBehavior(); // shouldn't be used if needs to be killable
	
	public void updateTick(Phy game, Obj[] objs, int objNum, int id) {}
	public void damageDealed(Phy game, Obj[] objs, int objNum, int id, int psychopath, String hurtedBy, int healthLost, int armorLost) {} // to this obj, called before potentially being killed
	public void collide(Phy game, Obj[] objs, int objNum, int id, int idw) {}
	public void killed(Phy game, Obj[] objs, int objNum, int id, int objKiller, String killer) {} // called after being killed
	public void disappeared(Phy game, Obj[] objs, int objNum, int id) {} // called before disappearing
	
	public ObjBehavior clone() throws CloneNotSupportedException {
		
		return (ObjBehavior) super.clone();
	}
	
	public static ObjBehavior newDefault() {
		
		return new DefaultObjBehavior();
	}
	
	public static void updateTickHurtAnim(Obj[] objs, int id, DefaultObjBehavior behavior) {
		
		if (behavior.dmgAnim > -1) {
			
			objs[id].bodyColor = Util.colorBetween(behavior.bodyColor, Conf.dmgColor, behavior.dmgAnim * Conf.betweenFuncRange / Conf.dmgAnimTime);
			objs[id].borderColor = Util.colorBetween(behavior.borderColor, Conf.dmgColor, behavior.dmgAnim * Conf.betweenFuncRange / Conf.dmgAnimTime);
			
		}
		
		behavior.dmgAnim--;
		
	}
	
	public static void updateTickDyingAnim(Phy game, Obj[] objs, int id, DefaultObjBehavior behavior) {
		
		if (behavior.dying > 0) {
			int oldSize = objs[id].size;
			
			objs[id].collidable = false;
			objs[id].size = (int) ((double) objs[id].size * Conf.destroyMultiplier);
			objs[id].trans -= 0xff / Conf.destroyTime;
			behavior.dying++;
			
			objs[id].display.changeSize(oldSize, objs[id].size);
			
		}
		
		if (behavior.dying > Conf.destroyTime) {
			
			game.died = true;
			
		}
		
	}
	
	public static class DefaultObjBehavior extends ObjBehavior implements Cloneable { // most default
		
		int dying = 0;
		int dmgAnim = -1;
		Color bodyColor;
		Color borderColor;
		
		public void damageDealed(Phy game, Obj[] objs, int objNum, int id, int psychopath, String hurtedBy, int healthLost, int armorLost) {
			
			if (dmgAnim < 0) {
				
				bodyColor = objs[id].bodyColor;
				borderColor = objs[id].borderColor;
				
			}
			
			dmgAnim = Conf.dmgAnimTime;
			
		}
		
		public void killed(Phy game, Obj[] objs, int objNum, int id, int killerObj, String killer) {
			dying = 1;
		}
		
		public void updateTick(Phy game, Obj[] objs, int objNum, int id) {
			
			updateTickHurtAnim(objs, id, this);
			updateTickDyingAnim(game, objs, id, this);
			
		}
		
		public DefaultObjBehavior clone() throws CloneNotSupportedException {
			
			return (DefaultObjBehavior) super.clone();
		}
		
	}
	
	public static class StarBehavior extends DefaultObjBehavior implements Cloneable {
		
		public StarBehavior() {
			
			super();
			
		}
		
		public void killed(Phy game, Obj[] objs, int objNum, int id, int objKiller, String killer) {
			
			if (killer.equals("obj")) {
				
				objs[objKiller].score = (int) ((double) objs[objKiller].score * Conf.starScoreMultiply);
				
			}
			
			super.killed(game, objs, objNum, id, objKiller, killer);
		}
		
		public StarBehavior clone() throws CloneNotSupportedException {
			
			return (StarBehavior) super.clone();
		}
		
	}
	
	public static class PaneBehavior extends DefaultObjBehavior implements Cloneable {
		
		public PaneBehavior() {
			
			super();
			
		}
		
		public void killed(Phy game, Obj[] objs, int objNum, int id, int objKiller, String killer) {
			int sx = objs[id].x;
			int sy = objs[id].y;
			
			for (int a = 0; a < objNum; a++) {
				Obj.Direction path = new Obj.Direction(sx - objs[a].x, sy - objs[a].y, 0);
				path = path.newScale((Conf.paneKnockbackRange - path.length()) / Conf.paneKnockbackDivide / objs[a].weight).normalize().round();
				
				if (path.scale > 0) {
					
					objs[a].xm -= path.x;
					objs[a].ym -= path.y;
					
				}
				
			}
			
			super.killed(game, objs, objNum, id, objKiller, killer);
			
		}
		
		public PaneBehavior clone() throws CloneNotSupportedException {
			
			return (PaneBehavior) super.clone();
		}
		
	}
	
}
