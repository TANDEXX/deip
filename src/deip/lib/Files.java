package deip.lib;

import deip.*;
import deip.data.*;
import java.io.*;
import java.util.ArrayList;

public class Files {
	
	public static final String readme = "here are deip.oi v" + Conf.version + " game's files. If you are here to modify some files, then good luck :)\n";
	public static final String generalConfigName = "general.deipconf";
	public static final int configMagicValue = 0x76543060;
	public static String path;
	
	public static void init() throws Throwable {
		path = Root.userHome + Root.pathSlash;
		if (Root.os == Root.OS.Windows) path += "AppData\\Roaming\\deip\\";
		else path += ".config/deip/";
		
		new File(path + "logs").mkdirs();
		new File(path + "records").mkdir();
		
		FileOutputStream readmeOutputStream = new FileOutputStream(path + "README.TXT");
		readmeOutputStream.write(readme.getBytes());
		readmeOutputStream.close();
		if (!new File(path + generalConfigName).exists()) GeneralConfig.def.save();
		
	}
	
	public static class GeneralConfig {
		
		public static final GeneralConfig def = new GeneralConfig(Data.nick, Data.recordFrameRate);
		
		public String nick;
		public byte recordFrameRate;
		
		public GeneralConfig(String nick, byte recordFrameRate) {
			
			this.nick = nick;
			this.recordFrameRate = recordFrameRate;
			
		}
		
		public void save() throws IOException {
			File file = new File(path + generalConfigName);
			FileOutputStream out = new FileOutputStream(file);
			out.write(getArray());
			out.close();
			
		}
		
		public byte[] getArray() {
			byte[] binaryNick = nick.getBytes();
			byte[] result = new byte[14 + binaryNick.length];
			makeConfigHeader(result);
			Util.putIntToByteArray(result, binaryNick.length, 8);
			result[12] = recordFrameRate;
			if (Data.debugMode) result[13] |= 1;
			System.arraycopy(binaryNick, 0, result, 14, binaryNick.length);
			
			return result;
		}
		
	}
	
	public static void makeConfigHeader(byte[] array) {
		
		Util.putIntToByteArray(array, configMagicValue, 0);
		Util.putIntToByteArray(array, Conf.versionId, 4);
		
	}
	
}
