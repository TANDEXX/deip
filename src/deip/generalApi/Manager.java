package deip.generalApi;

import deip.graphics.*;
import deip.lib.*;
import deip.data.*;

public class Manager {
	
	public static void init() {
		
		Data.classes = Conf.builtinClasses.clone();
		Data.classTanks = Conf.builtinClassTanks.clone();
		
	}
	
	public static ObjDisplay findObjDisplay(String id, byte[] objDisplay) {
		
		for (int a = 0; a < Data.objectDisplays.length; a++) {
			
			if (id.equals(Data.objectDisplays[a].id())) {
				
				try {return Data.objectDisplays[a].load(objDisplay);} catch (IncompatibleException e) {
					return new ObjDisplay();
				}
			}
			
		}
		
		return new ObjDisplay(); // none if didn't found any
	}
	
	// same as findObjDisplay but for barrels
	public static BarrelDisplay findBarrelDisplay(String id, byte[] barrelDisplay) {
		
		for (int a = 0; a < Data.barrelDisplays.length; a++) {
			
			if (id.equals(Data.barrelDisplays[a].id())) {
				
				try {return Data.barrelDisplays[a].load(barrelDisplay);} catch (IncompatibleException e) {
					return new BarrelDisplay();
				}
			}
			
		}
		
		return new BarrelDisplay();
	}
	
}
