package deip.run;

import deip.g.*;
import deip.game.phy.*;

public class Threads {
	
	public static Thread paint;
	public static Thread game;
	
	public static void init(Phy game) throws Throwable {
		
		Threads.game = new Thread(game.calcLoop);
		Threads.game.setName("DEIP calculation loop");
		paint = new Thread(Paint.paintLoop);
		paint.setName("DEIP paint loop");
		Threads.game.start();
		paint.start();
		
	}
	
}
