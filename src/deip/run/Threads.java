package deip.run;

import deip.graphics.*;
import deip.game.phy.*;
import deip.lib.*;

import java.io.IOException;

public class Threads {
	
	public static Thread paint;
	public static Thread game;
	public static Thread saveRecord;
	public static Thread loadRecord;
	
	public static void init(Phy game) throws Throwable {
		
		Threads.game = new Thread(game.calcLoop);
		Threads.game.setName("DEIP physics calculation loop");
		paint = new Thread(Paint.paintLoop);
		paint.setName("DEIP paint loop");
		Threads.game.start();
		paint.start();
		
	}
	
	public static boolean /* do it started new thread */ saveRecordInBackground(final Record record) {
		
		if (saveRecordThreadIsAlive()) {
			return false;
		} else {
			
			saveRecord = new Thread(new Runnable() {
				public void run() {
					try {record.save();} catch (Throwable e) {Log.err("failed to save record");}
				}
			});
			saveRecord.setName("DEIP record saver");
			saveRecord.start();
			return true;
		}
		
	}
	
	public static boolean /**/ loadRecordInBackground(final Record record) {
		
		if (loadRecordThreadIsAlive()) {
			return false;
		} else {
			
			loadRecord = new Thread(new Runnable() {
				public void run() {
					
					try {record.loadAllFrames();} catch (IOException e) {
						Log.err("failed at loading record");
					}
					
				}
			});
			loadRecord.setName("DEIP record loader");
			loadRecord.start();
			return true;
		}
		
	}
	
	public static boolean loadRecordThreadIsAlive() {
		boolean isAlive;
		try {isAlive = loadRecord.isAlive();} catch (NullPointerException e) {isAlive = false;}
		
		return isAlive;
	}
	
	public static boolean saveRecordThreadIsAlive() {
		boolean isAlive;
		try {isAlive = saveRecord.isAlive();} catch (NullPointerException e) {isAlive = false;}
		
		return isAlive;
	}
	
}
