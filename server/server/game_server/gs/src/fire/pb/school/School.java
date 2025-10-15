package fire.pb.school;


public enum School {
	WARRIOR(SchoolConst.WARRIOR,SchoolConst.WARRIOR_SHOUXI),
	PRIEST(SchoolConst.PRIEST,SchoolConst.PRIEST_SHOUXI),
	PALADIN(SchoolConst.PALADIN,SchoolConst.PALADIN_SHOUXI),
	 HUNTER(SchoolConst.HUNTER,SchoolConst.HUNTER_SHOUXI),
	WARLOCK(SchoolConst.WARLOCK,SchoolConst.WARLOCK_SHOUXI),
	SAMAN(SchoolConst.SAMAN,SchoolConst.SAMAN_SHOUXI),
	MAGIC(SchoolConst.MAGIC,SchoolConst.MAGIC_SHOUXI),
	ROGUE(SchoolConst.ROGUE,SchoolConst.ROGUE_SHOUXI),
	 DRUID(SchoolConst.DRUID,SchoolConst.DRUID_SHOUXI);
	
	private int value;
	private int npcid;

	private School(int value,int npcid) {
		this.value = value;
		this.npcid=npcid;
	}
	public int getValue(){
		return value;
	}
	public int getNpcid(){
		return npcid;
	}
	public static School getSchoolByNpcid(int npcID){
		for (School school : School.values()) {
			if(school.npcid==npcID)
				return school;
		}
		return null;
	}
	public static School getSchoolBySchoolid(int schoolid){
		for (School school : School.values()) {
			if(school.value==schoolid)
				return school;
		}
		return null;
	}
	public static int getShouxiTitleid(School school){
		
		switch (school) {
		case WARRIOR:
			return SchoolConst.WARRIOR_SHOUXI_TITLE;
		case PRIEST:
		    return SchoolConst.PRIEST_SHOUXI_TITLE;
		case PALADIN:
		    return SchoolConst.PALADIN_SHOUXI_TITLE;
		case HUNTER:
			return SchoolConst.HUNTER_SHOUXI_TITLE;
		case WARLOCK:
			return SchoolConst.WARLOCK_SHOUXI_TITLE;
		case SAMAN:
			return SchoolConst.SAMAN_SHOUXI_TITLE;
		case MAGIC:
			return SchoolConst.MAGIC_SHOUXI_TITLE;
		case ROGUE:
			return SchoolConst.ROGUE_SHOUXI_TITLE;
		case DRUID:
			return SchoolConst.DRUID_SHOUXI_TITLE;
		
		default:
			return 0;
		}
	}
	public static int getBattleNpc(School school){

		switch (school) {
		case WARRIOR:
			return SchoolConst.WARRIOR_SHOUXI_BATTLE_NPC;
		case PRIEST:
		    return SchoolConst.PRIEST_SHOUXI_BATTLE_NPC;
		case PALADIN:
		    return SchoolConst.PALADIN_SHOUXI_BATTLE_NPC;
		case HUNTER:
			return SchoolConst.HUNTER_SHOUXI_BATTLE_NPC;
		case WARLOCK:
			return SchoolConst.WARLOCK_SHOUXI_BATTLE_NPC;
		case SAMAN:
			return SchoolConst.SAMAN_SHOUXI_BATTLE_NPC;
		case MAGIC:
			return SchoolConst.MAGIC_SHOUXI_BATTLE_NPC;
		case ROGUE:
			return SchoolConst.ROGUE_SHOUXI_BATTLE_NPC;
		case DRUID:
			return SchoolConst.DRUID_SHOUXI_BATTLE_NPC;
		
		default:
			return 0;
		}
	}
	public static School getSchoolByBattleNpc(int battleNpcid){

		switch (battleNpcid) {
		case SchoolConst.WARRIOR_SHOUXI_BATTLE_NPC:
			return WARRIOR;
		case SchoolConst.PRIEST_SHOUXI_BATTLE_NPC:
		    return PRIEST;
		case SchoolConst.PALADIN_SHOUXI_BATTLE_NPC:
		    return PALADIN;
		case SchoolConst.HUNTER_SHOUXI_BATTLE_NPC:
			return HUNTER;
		case SchoolConst.WARLOCK_SHOUXI_BATTLE_NPC:
			return WARLOCK;
		case SchoolConst.SAMAN_SHOUXI_BATTLE_NPC:
			return SAMAN;
		case SchoolConst.MAGIC_SHOUXI_BATTLE_NPC:
			return MAGIC;
		case SchoolConst.ROGUE_SHOUXI_BATTLE_NPC:
			return ROGUE;
		case SchoolConst.DRUID_SHOUXI_BATTLE_NPC:
			return DRUID;
		
		default:
			return null;
		}
	}
	
	public static School getSchoolByFighterWinner(int battleid){

		switch (battleid) {
		case 161803:
			return WARRIOR;
		case 161902:
		    return PRIEST;
		case 162102:
		    return PALADIN;
		case 162002:
			return HUNTER;
		case 169202:
			return WARLOCK;
		case 169102:
			return SAMAN;
		case 161702:
			return MAGIC;
		case 169002:
			return ROGUE;
		case 162202:
			return DRUID;
		
		default:
			return null;
		}
	}
	
	public static School getSchoolByFighterConfigWinner(int configid){
		switch (configid) {
		case 161803:
		case 3094:
			return WARRIOR;
		case 161902:
		case 3095:
			return PRIEST;
		case 162102:
		case 3097:
			return PALADIN;
		case 162002:
		case 3096:
			return HUNTER;
		case 169202:
		case 3101:
			return WARLOCK;
		case 169102:
		case 3100:
			return SAMAN;
		case 161702:
		case 3093:
			return MAGIC;
		case 169002:
		case 3099:
			return ROGUE;
		case 162202:
		case 3098:
			return DRUID;
			
		default:
			return null;
		}
	}
	
	
	
	//z == 0 lian meng by changhao
	public static int getBattleIdBySchool(School school){

		switch (school) {
		case MAGIC:
			return 3105;
		case WARRIOR:
		    return 3106;
		case PRIEST:
		    return 3107;
		case HUNTER:
			return 3108;
		case PALADIN:
			return 3109;
		case DRUID:
			return 3110;
		case ROGUE:
			return 3111;
		case SAMAN:
			return 3112;
		case WARLOCK:
			return 3113;
		
		default:
			return 0;
		}
	}
	
	/***
	 * 得到一二三级称谓 title by changhao
	 * @param level
	 * @param school
	 * @return
	 */
	public static int getTitleIdBySchool(int level, int sex, School school){

		int malebaseid = 25;
		int femalebaseid = 49;
		switch (school)
		{
		case WARRIOR:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 0;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 0;
			}	
		}
		case PRIEST:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 6;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 6;
			}
		}
		case PALADIN:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 1;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 1;
			}
		}
		case HUNTER:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 2;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 2;
			}
		}
		case WARLOCK:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 7;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 7;
			}
		}
		case SAMAN:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 1;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 1;
			}
		}
		case MAGIC:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 5;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 5;
			}
		}
		case ROGUE:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 4;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 4;
			}
		}
		case DRUID:
		{
			if (sex == 1) //男  by changhao
			{
				return malebaseid + 8 * (level - 1) + 3;
			}
			else
			{
				return femalebaseid + 8 * (level - 1) + 3;
			}
		}
		
		default:
			return 0;
		}
	}
}
