package deip.data;

import deip.graphics.*;
import deip.game.phy.*;

import java.awt.*;

public class Conf {
	// Some constants in the game
	
	// graphics
	public static final Font debugFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	public static final Font bottomBarFont = new Font(Font.DIALOG, Font.BOLD, 753664);
	public static final Font buttonTextFont = new Font(Font.DIALOG, Font.BOLD, -0);
	public static final Font dsStatFont = new Font(Font.DIALOG, Font.BOLD, 22);
	public static final Font dsNickFont = new Font(Font.DIALOG, Font.BOLD, 40);
	public static final Color bgColor = new Color(0xBED5BC);
	public static final Color gridColor = new Color(0x6B8D61);
	public static final Color tankFrendlyBodyColor = new Color(0x0099FF);
	public static final Color tankFrendlyBorderColor = new Color(0x009A96);
	public static final Color sqBodyColor = new Color(0xFFFF00);
	public static final Color sqBorderColor = new Color(0xB3B300);
	public static final Color sqGreenBodyColor = new Color(0x2EE82E);
	public static final Color sqGreenBorderColor = new Color(0x1CB41C);
	public static final Color triBodyColor = new Color(0xD4597D);
	public static final Color triBorderColor = new Color(0xDE366C);
	public static final Color pentaBodyColor = new Color(0x0C649C);
	public static final Color pentaBorderColor = new Color(0x0B527F);
	public static final Color starBodyColor = new Color(0xF1F139);
	public static final Color starBorderColor = new Color(0xBDBD14);
	public static final Color paneBodyColor = new Color(0x14E3AA);
	public static final Color paneBorderColor = new Color(0x26B18B);
	public static final Color movingPolygonBody = new Color(0xDC880A);
	public static final Color movingPolygonBorder = new Color(0xB4770A);
	public static final Color barrelBodyColor = new Color(0x505050);
	public static final Color barrelBorderColor = new Color(0x404040);
	public static final Color debugColor = new Color(0xFF0000);
	public static final Color progressBgColor = new Color(0x535353);
	public static final Color progressSecondColor = new Color(0x727272);
	public static final Color healthColor = new Color(0x4AFF3C);
	public static final Color armorColor = new Color(0x97C1C1);
	public static final Color scoreBarColor = new Color(0xE8E80C);
	public static final Color scoreBarTextColor = new Color(0xA8A80B);
	public static final Color levelProgressBarColor = new Color(0x17E5E5);
	public static final Color tankClassListBoxCenter = new Color(0x27DB27);
	public static final Color tankClassListBoxCenterHover = new Color(0x21B321);
	public static final Color tankClassListBoxCenterPress = new Color(0x1C951C);
	public static final Color tankClassListBoxOutline = new Color(0x262626);
	public static final Color upgradeButtonSymbolColor = new Color(0x0C0C0C);
	public static final Color[] upgradeButtonDefaultColors = new Color[]{ // order is: regen, max hp, body dmg, bullet speed, penetration, bullet dmg, reload, speed
		new Color(0xE48BEE),
		new Color(0xD129EF),
		new Color(0x7B05E3),
		new Color(0x2B25D5),
		new Color(0xCEBC27),
		new Color(0xCE2825),
		new Color(0x21D029),
		new Color(0x23CDB3),
	};
	public static final Color[] upgradeButtonHoverColors = new Color[]{
		new Color(0xBC76C4),
		new Color(0xB826D2),
		new Color(0x6C07C6),
		new Color(0x231EA0),
		new Color(0xAFA022),
		new Color(0x9E2120),
		new Color(0x1BAD21),
		new Color(0x1EA18D),
	};
	public static final Color[] upgradeButtonPressColors = new Color[]{
		new Color(0xA267A8),
		new Color(0x9B20B1),
		new Color(0x5807A1),
		new Color(0x1D197D),
		new Color(0x92851E),
		new Color(0x7D1C1A),
		new Color(0x17901C),
		new Color(0x178472),
	};
	public static final Color[] upgradeButtonDisabledColors = new Color[]{ // order is: regen, max hp, body dmg, bullet speed, penetration, bullet dmg, reload, speed
		new Color(0x826282),
		new Color(0x7F6E80),
		new Color(0x815F97),
		new Color(0x5E5E97),
		new Color(0x7D7A5C),
		new Color(0x715555),
		new Color(0x517452),
		new Color(0x547874),
	};
	public static final Color[] upgradeBarColors = new Color[]{
		new Color(0xD37EDC),
		new Color(0xB723D5),
		new Color(0x6A07C2),
		new Color(0x2923BF),
		new Color(0xBDAD26),
		new Color(0xC62523),
		new Color(0x1FC227),
		new Color(0x21C4AC),
	};
	public static final Color[] upgradeBarSecondColors = new Color[]{
		new Color(0xCD9AE0),
		new Color(0xDD4EFA),
		new Color(0x8819EE),
		new Color(0x3732E8),
		new Color(0xE7CE44),
		new Color(0xE2413D),
		new Color(0x48E853),
		new Color(0x56D9C3),
	};
	public static final Color dmgColor = new Color(0xCE4242);
	public static final Color bulletPartColor = new Color(0x000000);
	public static final Color dsBgColor = new Color(0x150F0F);
	public static final Color dsTextColor = new Color(0xD0D0E5);
	public static final Color buttonCenterColor = new Color(0x5e5e6A);
	public static final Color buttonOutlineColor = new Color(0x6a6a6f);
	public static final Color buttonOtherDotColor = new Color(0x66666c); // of course
	public static final Color buttonTextColor = new Color(0xC5C8D9);
	public static final Color buttonHoverColor = new Color(0x1D000000, true);
	public static final Color buttonPressColor = new Color(0x32000000, true);
	public static final Color bgMapCenterAreaColor = new Color(0x2524B5EE, true);
	public static final Color bgMapPlayerColor = new Color(0x651E92F1, true);
	public static final Color[] bgNoiseColorPallete = new Color[]{
		new Color(0x120CBDE2, true),
		new Color(0x1277E20C, true),
		new Color(0x14E2370C, true),
		new Color(0x14171717, true),
	};
	public static final Color noC = new Color(0); // use when you must use Color but don't need to specify it
	public static final byte mapBorderTransStart = 127;
	public static final int mapBorderTransStep = 0x4ff;
	public static final int progressBarSpaceMultiply = 1;
	public static final int progressBarSpaceDivide = 2;
	public static final int gridBetween = 4000; // distance between two lines in grid
	public static final int displaySizeX = 185000; // 1600 default screen size
	public static final int displaySizeY = 105000; // 900 default screen size
	public static final int defaultObjBorder = 650;
	//public static final int mapBorderDensityStart = displaySizeX;
	public static final int defBarrelShootOffset = 500;
	public static final int classMenuBoxDistance = 20515;
	public static final int classMenuBoxSize = 15416;
	public static final int classMenuBoxborder = 1092;
	public static final int classMenuBoxXOffset = 43000;
	public static final int classMenuXOffsetDiv = 12;
	public static final int upgradeMenuCornerDistance = 1750;
	public static final int upgradeMenuHeight = 3500;
	public static final int upgradeMenuNormalWidth = 35000;
	public static final int upgradeMenuBarDistance = 1000;
	public static final int upgradeMenuButtonWidth = 3000;
	public static final int upgradeMenuBarStepDistance = 50;
	public static final int upgradeMenuForEachDistanceAdd = upgradeMenuBarDistance + upgradeMenuHeight;
	public static final int upgradeMenuDefaultLevels = 7;
	public static final int upgradeNumber = 8;
	public static final int umXOffset = 40000;
	public static final int umXOffsetDiv = 12;
	public static final int dsTransDiv = 12;
	public static final int dsTransScale = 0xffff;
	public static final int dsBgTransMax = 96; // 254 max value it can be
	public static final int destroyTime = 20;
	public static final int dmgAnimTime = 15;
	public static final int bulletColorSwap = 512;
	public static final int buttonGridSize = 15;
	public static final int buttonBorder = 15;
	public static final int bgMapPlayerSize = 45;
	public static final int bgMapPlayerHalfSize = bgMapPlayerSize / 2;
	public static final int bgNoiseDensity = 100;
	public static final int bgNoiseMinSize = 5;
	public static final int bgNoiseDifSize = 10;
	public static final int cameraFollowDiv = 5;
	public static final long maxProgress = 4096;
	public static final double dsButtonsYMul = 0.65; // height of window multiplied by this number
	public static final double dsButtonRetryXMul = 0.4;
	public static final double dsButtonSRecXMul = 0.6;
	public static final double dsButtonsHeightMul = 0.08;
	public static final double dsButtonRetryWidthMul = 2.8; // this is height to width scale
	public static final double dsButtonSRecWidthMul = 6.65; // same as above
	public static final double destroyMultiplier = 1.06; // multiplies object size by this value when lifetime is at end (and when object is destroyed)
	
	// game
	public static final int mapSize = 1500000;
	public static final int mapOutSpace = 0x8fffff;
	public static final int mapPositiveBorder = mapOutSpace + mapSize;
	public static final int mapCenter = mapOutSpace + (mapSize / 2);
	public static final int mapBorderSlowStep = 0x7ff;
	public static final int maxObjects = 4096;
	public static final int groupsPerSegment = 8;
	public static final int segmentsPerRow = 6;
	public static final int allSegments = segmentsPerRow * segmentsPerRow;
	public static final int segmentSize = mapSize / segmentsPerRow;
	public static final int defaultRegenWait = 60 * 40;
	public static final int defaultRegen = 175;
	public static final int basicTankSize = 6000;
	public static final int objSlowMul = 32;
	public static final int objSlowDiv = 33;
	public static final int bodyDmg = 15000;
	//public static final int collisionAngle = Obj.Direction.defScale;
	//public static final int collisionUnderAngleCheck = 30; // speed below collision angle isn't checked
	public static final int baseCollisionSpeed = 10;
	public static final int randSpinBase = 0x4f;
	public static final int randSpinBound = 0xff;
	public static final int defaultSpin = randSpinBase + (randSpinBound / 2);
	public static final int maxUpgrade = 14;
	public static final int upgradeStep = 7;
	public static final int separationLevel = 30;
	public static final int basicTankSpeed = 12;
	public static final int basicTankHealth = 150000;
	public static final int basicBulletSpread = Obj.Direction.defScale / 16;
	public static final int basicBulletLifetime = 270; // 4.5 second
	public static final int basicBulletSpeed = 200;
	public static final int basicBulletHealth = 5000;
	public static final int basicBulletRecoil = 60;
	public static final int basicWeight = 7;
	public static final int basicBulletWeight = 13;
	public static final double starScoreMultiply = 1.05;
	public static final int paneKnockbackRange = 45000;
	public static final int paneKnockbackDivide = 4;
	
	public static final Obj sqObj = new Obj(0, 0, 2500, basicWeight, 0, 10000, 0, 6, 0, ObjBehavior.newDefault(), new ObjDisplay.RPolygon(4), Obj.ObjType.Res, sqBodyColor, sqBorderColor, new Obj.Barrel[0]);
	public static final Obj sqGreenObj = new Obj(0, 0, 2500, basicWeight, 0, 60000, 0, 60, 0, ObjBehavior.newDefault(), new ObjDisplay.RPolygon(4), Obj.ObjType.Res, sqGreenBodyColor, sqGreenBorderColor, new Obj.Barrel[0]);
	public static final Obj triObj = new Obj(0, 0, 3000, 8, 0, 30000, 0, 20, 0, ObjBehavior.newDefault(), new ObjDisplay.RPolygon(3, 800), Obj.ObjType.Res, triBodyColor, triBorderColor, new Obj.Barrel[0]);
	public static final Obj pentaObj = new Obj(0, 0, 5000, 16, 0, 100000, 0, 80, 0, ObjBehavior.newDefault(), new ObjDisplay.RPolygon(5), Obj.ObjType.Res, pentaBodyColor, pentaBorderColor, new Obj.Barrel[0]);
	public static final Obj bPentaObj = new Obj(0, 0, 15000, 450, 0, 3500000, 0, 8000, 1000, ObjBehavior.newDefault(), new ObjDisplay.RPolygon(5), Obj.ObjType.Res, pentaBodyColor, pentaBorderColor, new Obj.Barrel[0]);
	public static final Obj starObj = new Obj(0, 0, 9000, 5, 0, 550000, 0, 0, 0, new ObjBehavior.StarBehavior(), new ObjDisplay.CursedStar(10, 4500, 9000, 2000), Obj.ObjType.Res, starBodyColor, starBorderColor, new Obj.Barrel[0]);
	public static final Obj paneObj = new Obj(0, 0, 5000, 45, 0, 1150000, 0, 1300, 0, new ObjBehavior.PaneBehavior(), new ObjDisplay.Star(4, 3500, 6500, 800), Obj.ObjType.Res, paneBodyColor, paneBorderColor, new Obj.Barrel[0]);
	public static final Obj movingPolygon = new Obj(0, -0, 11000, Integer.MAX_VALUE, 0, 1650000, 0, 2130, 0, ObjBehavior.newDefault(), new ObjDisplay.MovingPolygon(950, 7), Obj.ObjType.Res, movingPolygonBody, movingPolygonBorder, new Obj.Barrel[]{});
	public static final Obj.GroupSpawn[][] regularAreaObjs = new Obj.GroupSpawn[][]{
		new Obj.GroupSpawn[]{
			new Obj.GroupSpawn(sqObj, 196, 5, 25, 25000, segmentSize - 2 - 25000),
			new Obj.GroupSpawn(triObj, 64, 2, 8, 15000, segmentSize / 2),
			new Obj.GroupSpawn(pentaObj, 24, 1, 5, 10000, segmentSize / 3),
			new Obj.GroupSpawn(sqGreenObj, 1, 1, 0, 0, 1),
			new Obj.GroupSpawn(starObj, 3, 1, 0, 0, 1),
			new Obj.GroupSpawn(paneObj, 24, 1, 2, 6000, 10000),
		},
		new Obj.GroupSpawn[]{
			new Obj.GroupSpawn(triObj, 164, 4, 7, 20000, segmentSize / 2),
			new Obj.GroupSpawn(pentaObj, 128, 2, 6, 15000, segmentSize / 3),
			new Obj.GroupSpawn(sqGreenObj, 1, 1, 0, 0, 1),
			new Obj.GroupSpawn(starObj, 12, 1, 0, 0, 1),
			new Obj.GroupSpawn(movingPolygon, 28, 1, 0, 0, 1),
		},
	};
	public static final Obj.GroupSpawn[][] centerAreaObjs = new Obj.GroupSpawn[][]{
		new Obj.GroupSpawn[]{
			new Obj.GroupSpawn(pentaObj, 255, 8, 22, segmentSize - 3, 1),
			new Obj.GroupSpawn(bPentaObj, 196, 1, 3, segmentSize - 3, 1),
			new Obj.GroupSpawn(starObj, 48, 1, 1, starObj.size, segmentSize - starObj.size - 1),
		},
		new Obj.GroupSpawn[]{
			new Obj.GroupSpawn(pentaObj, 96, 3, 5, 24000, 36000),
			new Obj.GroupSpawn(bPentaObj, 148, 1, 3, segmentSize - 3, 1),
			new Obj.GroupSpawn(starObj, 64, 1, 3, starObj.size, segmentSize / 2),
			new Obj.GroupSpawn(movingPolygon, 48, 1, 0, 0, 1),
		},
	};
	public static final int[] levels = new int[]{
		3, 4, 6, 10, 13, // 5
		15, 17, 20, 23, 25, // 10
		35, 45, 55, 60, 65, // 15
		75, 83, 95, 107, 120, // 20
		135, 150, 167, 185, 208, // 25
		230, 260, 290, 320, 355, // 30
		390, 425, 550, 580, 610, // 35
		650, 690, 740, 800, 860, // 40
		930, 1000, 1070, 1150, 1240, // 45
	};
	public static final int[] upgradesSpeed = new int[]{ // for each 2 values you multiply value by first value and divide by second;
		1, 1, // 0 (no upgrade)
		12, 10, // 1
		13, 10, // 2
		14, 10, // 3
		15, 10, // 4
		16, 10, // 5
		17, 10, // 6
		18, 10, // 7
		19, 10, // 8
		20, 10, // 9
		21, 10, // 10
		22, 10, // 11
		23, 10, // 12
		24, 10, // 13
		25, 10, // 14
	};
	public static final int[] upgradesRegenerate = new int[]{
		1, 1, // 0 (no upgrade)
		13, 10, // 1
		16, 10, // 2
		19, 10, // 3
		21, 10, // 4
		23, 10, // 5
		25, 10, // 6
		27, 10, // 7
		29, 10, // 8
		31, 10, // 9
		32, 10, // 10
		33, 10, // 11
		34, 10, // 12
		35, 10, // 13
		36, 10, // 14
	};
	public static final int[] upgradesRegenWait = new int[]{ // this upgrade makes value smaller not larger so it's reverse
		1, 1, // 0 (no upgrade)
		10, 19, // 1
		10, 29, // 2
		10, 35, // 3
		10, 59, // 4
		10, 71, // 5
		10, 88, // 6
		10, 100, // 7 (4.5 second (if default regenWait is 60 * 40 or 2400))
		10, 121, // 8
		10, 149, // 9
		10, 181, // 10
		10, 222, // 11
		10, 255, // 12
		10, 288, // 13
		10, 300, // 14 (1 second)
	};
	public static final int[] upgradesReload = new int[]{
		1, 1, // 0 (no upgrade)
		10, 12, // 1
		10, 14, // 2
		10, 15, // 3
		10, 16, // 4
		10, 17, // 5
		100, 175, // 6
		100, 180, // 7
		100, 185, // 8
		100, 190, // 9
		100, 195, // 10
		100, 200, // 11
		100, 205, // 12
		100, 210, // 13
		100, 215, // 14
	};
	public static final int[] upgradesBSpeed = new int[]{
		1, 1, // 0 (no upgrade)
		13, 10, // 1
		17, 10, // 2
		23, 10, // 3
		27, 10, // 4
		33, 10, // 5
		37, 10, // 6
		40, 10, // 7
		43, 10, // 8
		46, 10, // 9
		49, 10, // 10
		52, 10, // 11
		55, 10, // 12
		58, 10, // 13
		60, 10, // 14
	};
	public static final int[] upgradesBDmg = new int[]{
		1, 1, // 0 (no upgrade)
		20, 10, // 1
		26, 10, // 2
		31, 10, // 3
		35, 10, // 4
		38, 10, // 5
		40, 10, // 6
		42, 10, // 7
		44, 10, // 8
		46, 10, // 9
		48, 10, // 10
		50, 10, // 11
		52, 10, // 12
		54, 10, // 13
		55, 10, // 14
	};
	public static final int[] upgradesBHp = new int[]{
		1, 1, // 0 (no upgrade)
		20, 10, // 1
		29, 10, // 2
		38, 10, // 3
		46, 10, // 4
		54, 10, // 5
		60, 10, // 6
		66, 10, // 7
		72, 10, // 8
		77, 10, // 9
		81, 10, // 10
		85, 10, // 11
		89, 10, // 12
		93, 10, // 13
		97, 10, // 14
	};
	public static final int[] upgradesHp = new int[]{
		1, 1, // 0 (no upgrade)
		20, 10, // 1
		30, 10, // 2
		37, 10, // 3
		43, 10, // 4
		47, 10, // 5
		50, 10, // 6
		53, 10, // 7
		56, 10, // 8
		59, 10, // 9
		62, 10, // 10
		65, 10, // 11
		68, 10, // 12
		71, 10, // 13
		74, 10, // 14
	};
	public static final Obj basicTankBullet = new Obj(0, 0, 0, 0, 2000, basicBulletWeight, basicBulletLifetime, basicBulletHealth, 0, 0, 7499 /* this guy saying prices in ads */, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(300), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{});
	public static final Obj defaultBullet = new Obj(0, 0, 0, 0, 2000, basicBulletWeight, basicBulletLifetime, basicBulletHealth, 0, 0, 6500, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(325), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{});
	public static final Obj biggerDefaultBullet = new Obj(0, 0, 0, 0, 2500, basicBulletWeight, basicBulletLifetime, basicBulletHealth, 0, 0, 6500, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(350), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{});
	public static final Obj minigunBullet = new Obj(0, 0, 0, 0, 1350, 10, basicBulletLifetime, basicBulletHealth, 0, 0, 2000, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(150), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{});
	public static final Obj destroyerBullet = new Obj(0, 0, 0, 0, 5000, 40, 360 /* 6 seconds */, basicBulletHealth, 0, 0, 15000, 1, ObjBehavior.newDefault(), new ObjDisplay.Circle(defaultObjBorder), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{});
	
	public static final Obj basicTank = new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, Conf.tankFrendlyBodyColor, Conf.tankFrendlyBorderColor, new Obj.Barrel[]{
		new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, basicBulletRecoil, 0, new BarrelDisplay.Generic(5000, 0, 2000, 2000), basicTankBullet),
	});
	// 0-7 bit: class level (0 for 15, 1 for 30, etc…)
	// 8-24 bit: avaiable after class id (0xffff for basic class)
	public static final int[] builtinClasses = new int[]{
		0 | (0xffff << 8), // gunner 0
		0 | (0xffff << 8), // twin 1
		0 | (0xffff << 8), // flank guard 2
		0 | (0xffff << 8), // sniper 3
		1 | (0 << 8), // minigun 4
		1 | (0 << 8), // destroyer 5
		1 | (1 << 8), // triple shot 6
		1 | (1 << 8), // quad tank 7
		1 | (1 << 8), // twin flank 8
	};
	public static final Obj[] builtinClassTanks = new Obj[]{
		// gunner
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 20, 0, Obj.Direction.defScale / 3, basicBulletRecoil, 0, new BarrelDisplay.Generic(5000, 0, 2000, -2000, 2000, -3000, 3000), defaultBullet),
		}),
		// twin
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, basicBulletRecoil, 0, new BarrelDisplay.Generic(5000, -1650, 2000, 2500), defaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 23, basicBulletSpread, basicBulletRecoil, 0, new BarrelDisplay.Generic(5000, 1650, 2000, 2500), defaultBullet),
		}),
		// flank guard
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, 0, new BarrelDisplay.Generic(5000, 0, 2000, 2000), basicTankBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale * 2, new BarrelDisplay.Generic(4000, 0, 2000, 2000), basicTankBullet),
		}),
		// sniper
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(300, 55, 0, basicBulletSpread / 2, 90, 0, new BarrelDisplay.Generic(6500, 0, 2000, 2000),
				new Obj(0, 0, 0, 0, 2000, 17, basicBulletLifetime, basicBulletHealth, 0, 0, 9500, 0, ObjBehavior.newDefault(), new ObjDisplay.Circle(defaultObjBorder), (byte) 0xff, Obj.ObjType.Bullet, noC, noC, new Obj.Barrel[]{})),
		}),
		// minigun
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 40, 0, basicBulletSpread, 20, 0, new BarrelDisplay.Generic(4500, 900, 2000, 1350), minigunBullet),
			new Obj.Barrel(basicBulletSpeed, 40, 20, basicBulletSpread, 20, 0, new BarrelDisplay.Generic(4500, -900, 2000, 1350), minigunBullet),
			new Obj.Barrel(basicBulletSpeed, 40, 30, basicBulletSpread, 20, 0, new BarrelDisplay.Generic(3500, 2550, 1500, 1350), minigunBullet),
			new Obj.Barrel(basicBulletSpeed, 40, 10, basicBulletSpread, 20, 0, new BarrelDisplay.Generic(3500, -2550, 1500, 1350), minigunBullet),
		}),
		// destroyer
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0,  0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 115, 0, basicBulletSpread, 500, 0, new BarrelDisplay.Generic(5000, 0, 1500, 5000), destroyerBullet),
		}),
		// triple shot
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, -Obj.Direction.defScale / 2, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 90, 0, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale / 2, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
		}),
		// quad tank
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, 0, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale * 2, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale * 3, new BarrelDisplay.Generic(5000, 0, 2000, 2500), biggerDefaultBullet),
		}),
		// twin flank
		new Obj(0, 0, basicTankSize, basicWeight, 0, basicTankHealth, 0, 0, basicTankSpeed, new ObjBehavior.PlayerBehavior(), new ObjDisplay.Circle(), Obj.ObjType.Tank, tankFrendlyBodyColor, tankFrendlyBorderColor, new Obj.Barrel[]{
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, 0, new BarrelDisplay.Generic(5000, -1650, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 23, basicBulletSpread, 0, 0, new BarrelDisplay.Generic(5000, 1650, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 0, basicBulletSpread, 0, Obj.Direction.defScale * 2, new BarrelDisplay.Generic(5000, -1650, 2000, 2500), biggerDefaultBullet),
			new Obj.Barrel(basicBulletSpeed, 45, 23, basicBulletSpread, 0, Obj.Direction.defScale * 2, new BarrelDisplay.Generic(5000, -1650, 2000, 2500), biggerDefaultBullet),
		}),
	};
	
	// misc
	public static final String version = "1.1";
	public static final int calcTimeCycles = 30; // number of cycles to refresh avg calc time and from these calc this value
	public static final int betweenFuncRange = 4096;
	public static final int versionId = 0;
	
	// window
	public static final String winTitle = "deip.oi v" + version;
	
}
