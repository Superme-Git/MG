package fire.pb.main;

import java.util.ArrayList;
import java.util.List;

import fire.script.ScriptEngineBindThreadMgr;

public class Main {
	private static List<String> xmlData = new ArrayList<String>();
	private static String curPath;
	private static String curAbsolutePath;
	
	public static String getCurAbsolutePath() {
		return curAbsolutePath;
	}

	public static void setCurAbsolutePath(String curAbsolutePath) {
		Main.curAbsolutePath = curAbsolutePath;
	}

	public static List<String> getXmlData() {
		return xmlData;
	}

	public static String getCurPath() {
		return curPath;
	}

	public static void setCurPath(String curPath) {
		Main.curPath = curPath;
	}

	private static long mainThreadid = -1;

	public static String[] args;

	public static void docleanup() {
		ModuleMgr.getInstance().exit();
	}

	public static void init() throws Exception {
		mainThreadid = Thread.currentThread().getId();
		initCurPath();
		initXmlData();
		ConfigMgr.init(getCurPath() + "gamedata/xml/auto", new XStreamUnmarshaller(), getCurPath() + "properties");
		ModuleMgr.init();
	}

	public static void initCurPath() {
		String path = System.getProperty("user.dir");
		setCurAbsolutePath(path + "\\");
		if (path.indexOf("jsconvert") != -1) {
			setCurPath("");
		} else {
			setCurPath(path + "/gs/");
		}
	}

	public static void initXmlData() {
		xmlData.add("fire.pb.battle.SBattleInfo.xml");
		xmlData.add("fire.pb.battle.SCondition.xml");
		xmlData.add("fire.pb.battle.STargetFilter.xml");
		xmlData.add("fire.pb.buff.SIBuffConfig.xml");
		xmlData.add("fire.pb.buff.SCBuffConfig.xml");
		xmlData.add("fire.pb.effect.SAttrEffectID2Name.xml");
		xmlData.add("fire.pb.game.SActivityAward.xml");
		xmlData.add("fire.pb.main.ModuleInfo.xml");
		xmlData.add("fire.pb.skill.SSceneSkillConfig.xml");
		xmlData.add("fire.pb.skill.SSkillConfig.xml");
		xmlData.add("fire.pb.skill.SSubSkillConfig.xml");
		xmlData.add("fire.pb.game.SPointCardActivityAward.xml");
		xmlData.add("fire.pb.npc.SRefreshTimerNpc.xml");
		xmlData.add("fire.pb.npc.SRefreshTimerNpcDianKa.xml");
	}

	public static boolean isInMainThread() {
		return Thread.currentThread().getId() == mainThreadid;
	}

	public static boolean isInXmlData(String xmlFile) {
		return getXmlData().contains(xmlFile);
	}

	public static void main(String[] args) throws Exception {
		long lasting = System.currentTimeMillis();
		Main.args = args;
		init();
		try {
			ScriptEngineBindThreadMgr.initialization();
			onShutdown();
			System.out.println("CREATED FINISHED");
			System.out.print("It takes " + ((System.currentTimeMillis() - lasting) / 1000.0) + "s to finished");
		} catch (Exception e) {
			System.out.println("CREATED FAILED");
			e.printStackTrace();
		}
	}

	private static void onShutdown() throws Exception {
		docleanup();
	}
}
