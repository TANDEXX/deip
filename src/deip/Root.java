package deip;

import deip.generalApi.*;
import deip.graphics.*;
import deip.game.phy.*;
import deip.lib.*;
import deip.run.*;

import java.util.Random;

public class Root {
	
	public static OS os;
	public static String osName;
	public static String osVer;
	public static String userHome;
	public static char pathSlash = '/';
	public static Random rand = new Random(System.currentTimeMillis());
	public static Phy game = new Phy(false, 0);
	
	public static void main(String[] args) {
		
		try {
			
			osName = System.getProperty("os.name");
			osVer = System.getProperty("os.version");
			userHome = System.getProperty("user.home");
			if (osName.toLowerCase().contains("windows")) {
				pathSlash = '\\';
				os = OS.Windows;
			} else if (osName.toLowerCase().contains("mac")) {
				os = OS.Mac;
			} else if (osName.toLowerCase().contains("linux")) {
				os = OS.Linux;
			} else if (osName.toLowerCase().contains("dos")) {
				throw new Throwable("SEND ME HOW YOU DID THIS IF YOU DIDN'T CHANGE CODE SO I CAN BE FLAT EARTHER"); // not seriously
			}
			
			Files.init();
			Log.init();
			Manager.init();
			Paint.game = game;
			game.init();
			/*
			Record record = Record.load("/home/jonatan/.config/deip/records/1663431306491.deiprecord");
			record.loadAllFrames();
			game = new Record.PhyRecordViewer(record);
			*/
			Window.init();
			Threads.init(game);
			
			Log.info("game initialized successfully");
			
		} catch (Throwable e) {
			
			Log.err("exception thrown in main thread, stack trace in stderr and logs");
			Log.exception(e);
			Log.close();
			System.exit(2);
			
		}
		
	}
	
	public static void shutdown() {
		
		Log.info("deip shutdown called");
		Threads.paint.stop();
		game.atEnd = Phy.AtEnd.Stop;
		Window.destroy();
		
		if (Threads.saveRecordThreadIsAlive()) {
			Log.info("record saving not done, waiting for it");
			while (Threads.saveRecordThreadIsAlive()) {try {Thread.sleep(1000);} catch (InterruptedException e) {}}
		}
		
		Log.info("last message, saving logs");
		Log.close();
		
		System.exit(0);
		
	}
	
	public enum OS {
		
		Windows,
		Mac,
		Linux, // os on that this game was designed by the way
		Unknown,
		
	}
	
}
