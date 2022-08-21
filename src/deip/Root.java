package deip;

import deip.g.*;
import deip.game.phy.*;
import deip.lib.*;
import deip.run.*;

import java.util.Random;

public class Root {
	
	public static Random rand = new Random(System.currentTimeMillis());
	public static Phy game = new Phy(false, 0);
	
	public static void main(String[] args) {
		
		try {
			
			Paint.game = game;
			game.init();
			Window.init();
			Threads.init(game);
			
		} catch (Throwable e) {
			
			Log.err("exception thrown in main thread, stack trace in stderr");
			e.printStackTrace();
			
		}
		
	}
	
	public static void shutdown(int code) {
		
		Threads.paint.stop();
		Threads.game.stop();
		System.exit(code);
		
	}
	
}
