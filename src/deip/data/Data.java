package deip.data;

import deip.graphics.*;
import deip.game.phy.*;
import deip.generalApi.*;

public class Data {
	
	// settings
	public static byte recordFrameRate = 40; // (60 / this value) times a second is taken game state shot
	public static boolean debugMode = true;
	
	// module additions (including builtin)
	public static Mod[] modules = new Mod[]{new Mod("deip", "deip.oi", Conf.version, Conf.versionId)};
	public static GameplayGui[] gameplayGuiL0 = new GameplayGui[]{GameplayGui.Defaults.background, GameplayGui.Defaults.classMenu}; // before object painting, (put background additions here)
	public static GameplayGui[] gameplayGuiL1 = new GameplayGui[]{GameplayGui.Defaults.upgradeMenu, GameplayGui.Defaults.levelBar}; // before map border painting
	public static GameplayGui[] gameplayGuiL2 = new GameplayGui[]{GameplayGui.Defaults.deathScreen}; // before debug info, last if debug is disabled
	public static BarrelDisplay[] barrelDisplays = new BarrelDisplay[]{new BarrelDisplay(), new BarrelDisplay.Generic()};
	public static ObjDisplay[] objectDisplays = new ObjDisplay[]{new ObjDisplay(), new ObjDisplay.Circle(), new ObjDisplay.RPolygon(0), new ObjDisplay.CursedStar(0, 0, 0), new ObjDisplay.Star(0, 0, 0), new ObjDisplay.MovingPolygon()};
	public static int[] classes = new int[]{};
	public static Obj[] classTanks = new Obj[]{};
	
	// misc
	public static String nick = "unknown";
	
}
