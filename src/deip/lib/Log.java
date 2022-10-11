package deip.lib;

import deip.Root;

import java.io.*;

public class Log {
	
	//public static PrintWriter fout;
	
	public static void init() throws IOException {
		//fout = new PrintWriter(Files.path + "logs" + Root.pathSlash + Util.formatedDatetime() + ".txt");
		
		info("started logging into a file");
		
	}
	
	public static void close() {
		
		//fout.close();
	}
	
	public static void info(String msg) {
		
		log("[deip] info: " + msg);
	}
	
	public static void warn(String msg) {
		
		log("[deip] warn" + msg);
	}
	
	public static void err(String msg) {
		
		log("[deip] err: " + msg);
	}
	
	public static void log(String msg) {
		
		System.out.println(msg);
		//fout.println(msg);
		
	}
	
	public static void exception(Throwable e) {
		
		e.printStackTrace();
		//e.printStackTrace(fout);
		
	}
	
}
