package deip.lib;

public class Log {
	
	public static void info(String msg) {
		
		System.out.print("[deip] info: ");
		System.out.println(msg);
		
	}
	
	public static void warn(String msg) {
		
		System.out.print("[deip] warn: ");
		System.out.println(msg);
		
	}
	
	public static void err(String msg) {
		
		System.out.print("[deip] err: ");
		System.out.println(msg);
		
	}
	
}
