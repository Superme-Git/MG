package fire.pb.clan;

public class Constant {
	//帮派称谓id
	///////1////////
	public static final int FACTION_MASTER_TITLE = 259;// 会长
	public static final int FACTION_VICE_MASTER_TITLE = 260;// 副会长
	///////2//////
	public static final int FACTION_ONE_ARMY_GROUP_MASTER_TITLE = 261;// 第一军团长
	public static final int FACTION_TWO_ARMY_GROUP_MASTER_TITLE = 262;// 第二军团长
	public static final int FACTION_THREE_ARMY_GROUP_MASTER_TITLE = 263;// 第三军团长
	public static final int FACTION_FOUR_ARMY_GROUP_MASTER_TITLE = 264;// 第四军团长
	/////3///
	public static final int FACTION_ONE_ARMY_GROUP_ELITE_TITLE = 266;// 第一军团精英
	//////3////
	public static final int FACTION_TWO_ARMY_GROUP_ELITE_TITLE = 269;// 第二军团精英
	//////3///
	public static final int FACTION_THREE_ARMY_GROUP_ELITE_TITLE = 272;// 第三军团精英
	/////3///
	public static final int FACTION_FOUR_ARMY_GROUP_ELITE_TITLE = 275;// 第四军团精英
	////4//////
	public static final int FACTION_MEMBER_TITLE = 277;// 成员
	/////5/////
	
	public static final int FACTION_BUILD_CLOSE_DODE = -1;
	
	/**
	 * 判断是否是公会称号
	 * @param titleid
	 * @return
	 */
	public static boolean isClanTitle(int titleid){
		if(titleid==FACTION_MASTER_TITLE
				||titleid==FACTION_VICE_MASTER_TITLE
				||titleid==FACTION_ONE_ARMY_GROUP_MASTER_TITLE
				||titleid==FACTION_TWO_ARMY_GROUP_MASTER_TITLE
				||titleid==FACTION_THREE_ARMY_GROUP_MASTER_TITLE
				||titleid==FACTION_FOUR_ARMY_GROUP_MASTER_TITLE
				||titleid==FACTION_ONE_ARMY_GROUP_ELITE_TITLE
				||titleid==FACTION_TWO_ARMY_GROUP_ELITE_TITLE
				||titleid==FACTION_THREE_ARMY_GROUP_ELITE_TITLE
				||titleid==FACTION_FOUR_ARMY_GROUP_ELITE_TITLE
				||titleid==FACTION_MEMBER_TITLE){
			return true;
		}
		return false;
	}
	
}
