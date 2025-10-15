package robot.clan;

/**
 * 公会相关
 * 
 * @author yt
 */
public class Clan {

	public long masterid; // 帮主ID

	public long vicemasterid = -1; // 副帮主ID

	public int nextSaveNum = 1;

	public long nextClearClanNum = 0l;

	public long resetTime = 0l;

	public long timeDelay = 5000l;

	public boolean isFirst =false;
}
