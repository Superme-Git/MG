package fire.pb.mission;

import java.util.Properties;

import fire.pb.main.ConfigManager;

public class MissionConst {
	static Properties prop = ConfigManager.getInstance().getPropConf("mission");
	public static final int SUBMIT_TEN_TIMES = 140048;
	public static final int LEVEL_NOT_RIGHT = 140049;
	public static final int SUBMIT_MORE_ITEMS = 140050;
	public static final int NO_ITEMS = 140055;
	public static final int SUBMIT_INCORRECT_ITEMS = 141691;

	// 梦境任务的after battle buff
	public static final int AFTER_BATTLE_BUFF = 500146;

	// 行动力
	public static final int ACTIVITY_NOT_ENOUGH = 142305;

	public static final int ACTIVITY_CONSUME = 142304;

	public static final int ACTIVITY_GOT = 142303;
	// 竞技场引导
	public static final int JJC_GUILD = 210204;
}
