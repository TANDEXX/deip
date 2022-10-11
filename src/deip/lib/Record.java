package deip.lib;

import deip.*;
import deip.data.*;
import deip.game.phy.*;
import deip.generalApi.*;
import deip.graphics.*;
import java.io.*;
import java.util.*;
import java.awt.*;

// I have been working on it for long time but I give up :(

public class Record {
	
	public static final int magicValue = 0x76543061;
	
	public int realLength = 0;
	public byte frameRate;
	public byte frameRateTimer = 1;
	public String filepath;
	public FileInputStream fileReader;
	public ArrayList<RObj[]> frames = new ArrayList();
	
	private Record() {}
	
	public static Record newEmpty(byte frameRate) {
		Record result = new Record();
		result.filepath = Files.path + "records" + Root.pathSlash + Util.formatedDatetime() + ".deiprecord";
		result.frameRate = frameRate;
		
		return result;
	}
	
	public static Record load(String filepath) throws IncompatibleException, IOException {
		Record result = new Record();
		result.filepath = filepath;
		result.fileReader = new FileInputStream(filepath);
		byte[] readBuffer;
		
		readBuffer = new byte[4];
		result.fileReader.read(readBuffer);
		if (magicValue != Util.getIntFromByteArray(readBuffer, 0)) throw new IncompatibleException("wrong record file format");
		result.fileReader.read(readBuffer);
		if (Conf.versionId < Util.getIntFromByteArray(readBuffer, 0)) throw new IncompatibleException("wrong record file format");
		result.fileReader.read(readBuffer);
		result.realLength = Util.getIntFromByteArray(readBuffer, 0);
		readBuffer = new byte[1];
		result.fileReader.read(readBuffer);
		result.frameRate = readBuffer[0];
		
		return result;
	}
	
	public void save() throws IOException {
		Log.info("saving record, preparing file data");
		ArrayList<Byte> bytes = new ArrayList();
		Util.putIntToByteArrayList(bytes, magicValue);
		Util.putIntToByteArrayList(bytes, Conf.versionId);
		Util.putIntToByteArrayList(bytes, frames.size());
		bytes.add(frameRate);
		
		for (int a = 0; a < frames.size(); a++) {
			ArrayList<Byte> frame = frameBytes(frames.get(a));
			
			Util.putIntToByteArrayList(bytes, frame.size());
			Util.putByteArrayListToByteArrayList(bytes, frame);
			
		}
		
		byte[] finalBytes = new byte[bytes.size()];
		for (int a = 0; a < finalBytes.length; a++) {
			finalBytes[a] = bytes.get(a);
		}
		Log.info("saving record, writing down to file. " + Util.numWSuffix(finalBytes.length) + "B file size");
		FileOutputStream out = new FileOutputStream(filepath);
		out.write(finalBytes);
		out.close();
		
		Log.info("record saved successfully to " + filepath);
	}
	
	// use after load(String) function
	public void loadAllFrames() throws IOException {
		byte[] bytes = new byte[fileReader.available()];
		fileReader.read(bytes);
		int currentByte = 0;
		
		while (currentByte <= bytes.length) {
			ArrayList<RObj> frame = readFrame(bytes, currentByte);
			RObj[] frameOutput = new RObj[frame.size()];
			
			for (int a = 0; a < frameOutput.length; a++) {
				
				frameOutput[a] = frame.get(a);
				
			}
			
			frames.add(frameOutput);
			currentByte += Util.getIntFromByteArray(bytes, currentByte); // 1663431306491.deiprecord
			
		}
		
	}
	
	// yes, I started to write a function which I already wrote some time ago…
	/*public ArrayList<RObj> readFrame() throws IOException {
		byte[] readBuffer = new byte[4];
		fileReader.read(readBuffer);
		readBuffer = new byte[Util.getIntFromByteArray(readBuffer, 0)];
		fileReader.read(readBuffer);
		int currentByte = 0;
		ArrayList<RObj> objs = new ArrayList();
		
		while (currentByte <= readBuffer.length) {
			Util.Pair<RObj, Integer> byteCastOutput = RObj.fromBytes(readBuffer, currentByte);
			
			objs.add(byteCastOutput.a);
			currentByte += byteCastOutput.b;
		}
		
		return objs;
	}
	*/
	private static ArrayList<Byte> frameBytes(RObj[] frame) {
		ArrayList<Byte> bytes = new ArrayList();
		
		for (int a = 0; a < frame.length; a++) {
			byte[] obj = frame[a].toBytes();
			
			Util.putByteArrayToByteArrayList(bytes, obj);
			
		}
		
		return bytes;
	}
	
	public void recordFrame(Obj[] objs, int objNum) {
		
		if (frameRateTimer == 0) {
			frameRateTimer = frameRate;
			
			frames.add(RObj.fromObj(objs, objNum));
			realLength++;
			
		}
		
		frameRateTimer--;
	}
	
	// how far it readed is in 4 first bytes of frame (length of frame in bytes) (add this value to currentByte not replace it)
	public ArrayList<RObj> readFrame(byte[] bytes, int offset) {
		ArrayList<RObj> objs = new ArrayList();
		int limit = Util.getIntFromByteArray(bytes, offset);
		int currentByte = offset + 4;
		
		while (currentByte < limit + offset) {
			Util.Pair<RObj, Integer> returnValue = RObj.fromBytes(bytes, currentByte);
			objs.add(returnValue.a);
			currentByte += returnValue.b;
		}
		
		return objs;
	}
	
	public static class RObj {
		
		public int x;
		public int y;
		public int degress;
		public int size;
		public int score;
		public int maxhp;
		public int health;
		public int armor;
		public byte trans;
		public Color bodyColor;
		public Color borderColor;
		public ObjDisplay display;
		public RBarrel[] barrels;
		
		public RObj(int x, int y, int degress, int size, int score, int maxhp, int health, int armor, byte trans, Color bodyColor, Color borderColor, ObjDisplay display, RBarrel[] rBarrels) {
			
			this.x = x;
			this.y = y;
			this.degress = degress;
			this.size = size;
			this.score = score;
			this.maxhp = maxhp;
			this.health = health;
			this.armor = armor;
			this.trans = trans;
			this.bodyColor = bodyColor;
			this.borderColor = borderColor;
			this.display = display;
			barrels = rBarrels;
			
		}
		
		public static RObj fromObj(Obj obj) {
			
			return new RObj(obj.x, obj.y, obj.dir.degrees(), obj.size, obj.score, obj.maxhp, obj.health, obj.armor, obj.trans, obj.currentDisplayBodyColor, obj.currentDisplayBorderColor, obj.display, RBarrel.fromBarrel(obj.barrels));
		}
		
		public static Obj toObj(RObj obj) {
			Obj result = new Obj(obj.x, obj.y, 0, 0, obj.size, 0, 0, obj.maxhp, obj.armor, obj.score, 0, 0, ObjBehavior.none, obj.display, obj.trans, Obj.ObjType.Tank, obj.bodyColor, obj.borderColor, RBarrel.toBarrel(obj.barrels));
			result.health = obj.health;
			result.dir = Obj.Direction.fromDegrees(obj.degress, Obj.Direction.defScale);
			
			return result;
		}
		
		public static RObj[] fromObj(Obj[] obj, int objNum) {
			RObj[] result = new RObj[objNum];
			
			for (int a = 0; a < objNum; a++) {
				result[a] = fromObj(obj[a]);
			}
			
			return result;
		}
		
		public static Obj[] toObj(RObj[] obj) {
			Obj[] result = new Obj[obj.length];
			
			for (int a = 0; a < obj.length; a++) {
				result[a] = obj[a].toObj();
			}
			
			return result;
		}
		
		public static Util.Pair<RObj, Integer> /* RObj, readed byte length */ fromBytes(byte[] bytes, int offset) {
			RObj result = new RObj(
				Util.getIntFromByteArray(bytes, offset), Util.getIntFromByteArray(bytes, offset + 4), Util.getIntFromByteArray(bytes, offset + 48), Util.getIntFromByteArray(bytes, offset + 8),
				Util.getIntFromByteArray(bytes, offset + 12), Util.getIntFromByteArray(bytes, offset + 16), Util.getIntFromByteArray(bytes, offset + 20), Util.getIntFromByteArray(bytes, offset + 24),
				bytes[offset + 52], new Color(Util.getIntFromByteArray(bytes, offset + 28)), new Color(Util.getIntFromByteArray(bytes, offset + 32)), null, new RBarrel[Util.getIntFromByteArray(bytes, offset + 44)]
			);
			byte[] displayBytes = new byte[Util.getIntFromByteArray(bytes, offset + 36)];
			byte[] displayIdBytes = new byte[Util.getIntFromByteArray(bytes, offset + 40)];
			int currentByte = 53;
			
			for (int a = 0; a < displayBytes.length; a++) {
				
				displayBytes[a] = bytes[offset + currentByte];
				currentByte++;
				
			}
			
			for (int a = 0; a < displayIdBytes.length; a++) {
				
				displayIdBytes[a] = bytes[offset + currentByte];
				currentByte++;
				
			}
			
			for (int a = 0; a < result.barrels.length; a++) {
				Util.Pair<RBarrel, Integer> readOutput = RBarrel.fromBytes(bytes, offset + currentByte);
				result.barrels[a] = readOutput.a;
				currentByte += readOutput.b;
				
			}
			
			result.display = Manager.findObjDisplay(new String(displayIdBytes), displayBytes);
			return new Util.Pair(result, currentByte);
		}
		
		public byte[] toBytes() {
			byte[] displayBytes = display.save();
			byte[] displayIdBytes = display.id().getBytes();
			byte[][] barrelBytes = new byte[barrels.length][];
			int finalBytesLength = 4 * (8 + 2 + 3) + 1 + displayBytes.length + displayIdBytes.length;
			
			for (int a = 0; a < barrelBytes.length; a++) {
				
				barrelBytes[a] = barrels[a].toBytes();
				finalBytesLength += barrelBytes[a].length;
				finalBytesLength += 4;
				
			}
			
			byte[] finalBytes = new byte[finalBytesLength];
			Util.putIntToByteArray(finalBytes, x, 0);
			Util.putIntToByteArray(finalBytes, y, 4);
			Util.putIntToByteArray(finalBytes, size, 8);
			Util.putIntToByteArray(finalBytes, score, 12);
			Util.putIntToByteArray(finalBytes, maxhp, 16);
			Util.putIntToByteArray(finalBytes, health, 20);
			Util.putIntToByteArray(finalBytes, armor, 24);
			Util.putIntToByteArray(finalBytes, bodyColor.getRGB(), 28);
			Util.putIntToByteArray(finalBytes, borderColor.getRGB(), 32);
			Util.putIntToByteArray(finalBytes, displayBytes.length, 36);
			Util.putIntToByteArray(finalBytes, displayIdBytes.length, 40);
			Util.putIntToByteArray(finalBytes, barrelBytes.length, 44);
			Util.putIntToByteArray(finalBytes, degress, 48);
			finalBytes[52] = trans;
			int currentIndex = 53;
			Util.putByteArrayToByteArray(finalBytes, displayBytes, currentIndex);
			currentIndex += displayBytes.length;
			Util.putByteArrayToByteArray(finalBytes, displayIdBytes, currentIndex);
			currentIndex += displayIdBytes.length;
			
			for (int a = 0; a < barrelBytes.length; a++) {
				
				Util.putIntToByteArray(finalBytes, barrelBytes[a].length, currentIndex);
				currentIndex += 4;
				Util.putByteArrayToByteArray(finalBytes, barrelBytes[a], currentIndex);
				currentIndex += barrelBytes[a].length;
				
			}
			
			return finalBytes;
		}
		
		public Obj toObj() {
			
			return toObj(this);
		}
		
	}
	
	public static class RBarrel {
		
		public int degress;
		public int shotOffset;
		public BarrelDisplay display;
		
		public RBarrel(int degress, int shotOffset, BarrelDisplay display) {
			
			this.degress = degress;
			this.shotOffset = shotOffset;
			this.display = display;
			
		}
		
		private RBarrel(int degress, int shotOffset) {
			
			this(degress, shotOffset, null);
			
		}
		
		public static RBarrel fromBarrel(Obj.Barrel barrel) {
			
			return new RBarrel(barrel.currentDegrees, barrel.shotOffset, barrel.display);
		}
		
		public static Obj.Barrel toBarrel(RBarrel rBarrel) {
			Obj.Barrel result = new Obj.Barrel(0, 0, 0, 0, 0, 0, rBarrel.display, Conf.defaultBullet);
			
			result.currentDegrees = rBarrel.degress;
			result.shotOffset = rBarrel.shotOffset;
			
			return result;
		}
		
		public static RBarrel[] fromBarrel(Obj.Barrel[] barrels) {
			RBarrel[] result = new RBarrel[barrels.length];
			
			for (int a = 0; a < barrels.length; a++) {
				result[a] = fromBarrel(barrels[a]);
			}
			
			return result;
		}
		
		public static Obj.Barrel[] toBarrel(RBarrel[] barrels) {
			Obj.Barrel[] result = new Obj.Barrel[barrels.length];
			
			for (int a = 0; a < barrels.length; a++) {
				result[a] = barrels[a].toBarrel();
			}
			
			return result;
		}
		
		public static Util.Pair<RBarrel, Integer> /* RBarrel, readed length in bytes */ fromBytes(byte[] bytes, int offset) {
			RBarrel result = new RBarrel(Util.getIntFromByteArray(bytes, offset), Util.getIntFromByteArray(bytes, offset + 4));
			int displayLength = Util.getIntFromByteArray(bytes, offset + 8);
			int displayIdLength = Util.getIntFromByteArray(bytes, offset + 12);
			int currentByte = offset + 16;
			byte[] displayBytes = new byte[displayLength];
			byte[] displayIdBytes = new byte[displayIdLength];
			
			for (int a = 0; a < displayLength; a++) {
				
				displayBytes[a] = bytes[currentByte];
				currentByte++;
				
			}
			
			for (int a = 0; a < displayIdLength; a++) {
				
				displayIdBytes[a] = bytes[currentByte];
				currentByte++;
				
			}
			
			result.display = Manager.findBarrelDisplay(new String(displayIdBytes), displayBytes);
			return new Util.Pair(result, currentByte - offset);
		}
		
		public byte[] toBytes() {
			byte[] displayIdBytes = display.id().getBytes();
			byte[] displayBytes = display.save();
			byte[] finalBytes = new byte[16 + displayBytes.length + displayIdBytes.length];
			Util.putIntToByteArray(finalBytes, degress, 0);
			Util.putIntToByteArray(finalBytes, shotOffset, 4);
			Util.putIntToByteArray(finalBytes, displayBytes.length, 8);
			Util.putIntToByteArray(finalBytes, displayIdBytes.length, 12);
			Util.putByteArrayToByteArray(finalBytes, displayBytes, 16);
			Util.putByteArrayToByteArray(finalBytes, displayIdBytes, 16 + displayBytes.length);
			
			return finalBytes;
		}
		
		public Obj.Barrel toBarrel() {
			
			return toBarrel(this);
		}
		
	}
	
	public static class PhyRecordViewer extends Phy {
		
		int currentRecordFrame;
		
		public PhyRecordViewer(Record record) {
			
			super.record = record;
			
		}
		
		public void init() {}
		public void calc() {
			
			objects = new Obj[record.frames.get(currentRecordFrame).length];
			for (int a = 0; a < objects.length; a++) {
				
				objects[a] = record.frames.get(currentRecordFrame)[a].toObj();
				
			}
			
			objectNumber = objects.length;
			currentRecordFrame++;
			
		}
		
	}
	
}
