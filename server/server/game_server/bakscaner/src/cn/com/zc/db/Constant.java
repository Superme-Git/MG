package cn.com.zc.db;

public class Constant {
	
	//活跃角色得到数据库表名
	public static final String TABLE_ROLE_PROPERTY = "activeroletable";
	
	
	
	
	//各种活跃账号类型的id
	public static final int WEEK_INCREASE_ACTIVE_USER = 101;
	public static final int WEEK_BACK_ACTIVE_USER = 102;
	public static final int WEEK_KEEP__ACTIVE = 103;
	public static final int WEEK_INCREASE_ACTIVE_LOSE_USER = 104;
	public static final int WEEK_BACK_ACTIVE_LOSE_USER = 105;
	public static final int WEEK_KEEP_ACTIVE_LOSE = 106;
	
	public static final int DAY_INCREASE_ACTIVE_USER = 107;
	public static final int DAY_BACK_ACTIVE_USER = 108;
	public static final int DAY_KEEP__ACTIVE = 109;
	public static final int DAY_INCREASE_ACTIVE_LOSE_USER = 110;
	public static final int DAY_BACK_ACTIVE_LOSE_USER = 111;
	public static final int DAY_KEEP_ACTIVE_LOSE = 112;
	
	//各种付费账号类型的id
	public static final int WEEK_INCREASE_CHARGE_USER = 201;
	public static final int WEEK_BACK_CHARGE_USER = 202;
	public static final int WEEK_KEEP__CHARGE = 203;
	public static final int WEEK_INCREASE_CHARGE_LOSE_USER = 204;
	public static final int WEEK_BACK_CHARGE_LOSE_USER = 205;
	public static final int WEEK_KEEP_CHARGE_LOSE = 206;
	
	public static final int DAY_INCREASE_CHARGE_USER = 207;
	public static final int DAY_BACK_CHARGE_USER = 208;
	public static final int DAY_KEEP__CHARGE = 209;
	public static final int DAY_INCREASE_CHARGE_LOSE_USER = 210;
	public static final int DAY_BACK_CHARGE_LOSE_USER = 211;
	public static final int DAY_KEEP_CHARGE_LOSE = 212;
	
	//各种活跃角色类型的id
	public static final int WEEK_INCREASE_ACTIVE_ROLE = 301;
	public static final int WEEK_BACK_ACTIVE_ROLE = 302;
	public static final int WEEK_KEEP__ACTIVE_ROLE = 303;
	public static final int WEEK_INCREASE_ACTIVE_LOSE_ROLE = 304;
	public static final int WEEK_BACK_ACTIVE_LOSE_ROLE = 305;
	public static final int WEEK_KEEP_ACTIVE_LOSE_ROLE = 306;
	
	public static final int DAY_INCREASE_ACTIVE_ROLE = 307;
	public static final int DAY_BACK_ACTIVE_ROLE = 308;
	public static final int DAY_KEEP_ACTIVE_ROLE = 309;
	public static final int DAY_INCREASE_ACTIVE_LOSE_ROLE = 310;
	public static final int DAY_BACK_ACTIVE_LOSE_ROLE = 311;
	public static final int DAY_KEEP_ACTIVE_LOSE_ROLE = 312;
	
	//各种消费角色的id
	public static final int WEEK_INCREASE_CONSUME_ROLE = 401;
	public static final int WEEK_BACK_CONSUME_ROLE = 402;
	public static final int WEEK_KEEP_CONSUME_ROLE = 403;
	public static final int WEEK_INCREASE_CONSUME_LOSE_ROLE = 404;
	public static final int WEEK_BACK_CONSUME_LOSE_ROLE = 405;
	public static final int WEEK_KEEP_CONSUME_LOSE_ROLE = 406;
	
	public static final int DAY_INCREASE_CONSUME_ROLE = 407;
	public static final int DAY_BACK_CONSUME_ROLE = 408;
	public static final int DAY_KEEP_CONSUME_ROLE = 409;
	public static final int DAY_INCREASE_CONSUME_LOSE_ROLE = 410;
	public static final int DAY_BACK_CONSUME_LOSE_ROLE = 411;
	public static final int DAY_KEEP_CONSUME_LOSE_ROLE = 412;
	
	//人物等级区间
	public static final int[] ROLE_LEVEL_INTERAL = new int[31];
	public static final int ROLE_LEVEL_SPACE = 5;
	static{
		int vIndex = 1;
		for(int i=0; i<ROLE_LEVEL_INTERAL.length; i++){
			ROLE_LEVEL_INTERAL[i] = vIndex;
			vIndex += ROLE_LEVEL_SPACE;
		}
	}
	
	//充值和消费额度区间
	public static final int[] TRADE_LIMIT = {0, 5, 10, 20, 30, 50, 100, 200, 300};
	
	//在线时长区间
	public static final int[] ONLINE_TIME_SPACE = {0, 15, 30, 60, 120, 180, 240, 360};
	
	
	//天
	public static final int DAY = 0;
	//周
	public static final int WEEK = 1;
	
	//遍历的类型
	public static final int ALL_TYPE = 0;
	public static final int QUICK_PHOTO = 1;
	public static final int SORT_ROLE_LEVEL = 2;
	public static final int SORT_MONEY = 4;
	public static final int STATE_LOG = 8;
	public static final int SORT_ROSE_NUM = 16;
	public static final int PET_SCORE = 32;
	
	//符石类型
	public static final int UNBIND_FUSHI_BUY = 2;
	public static final int BIND_FUSHI_BUY = 1;
	
	//快照类型
	public static final int ROLE_PROPERTY = 1;
	public static final String ROLE_PROPERTY_FIRST_LINE = "serverid,userid,platformid,roleid,shapeid,school,lev,bagmoney,storagemoney,exp,smoney,ybgdmoney,ybzcmoney,create_time,total_online_minute,today_online_minute,last_login_time,rolename,factionid,camp";
	public static final int FAMILY_DETAIL = 2;
	public static final String FAMILY_DETAIL_FIRST_LINE = "familyid,factionid,leaderid,famlevel,fammoney,famspecialcount,fammembercount,fambuildingdegree,famdrugcount,familyname,familycreater";
	public static final int FACTION_DETAIL = 3;
	public static final String FACTION_DETAIL_FIRST_LINE = "factionid,leaderid,faclevel,facbuildingderee,facname,factioncreater";
	public static final int SHOP_DETAIL = 4;
	public static final String SHOP_DETAIL_FIRST_LINE = "shopid,shopmaster,shopmoney,shopdesknum,shopdesknumclose";
	public static final int SKILL_DETAIL = 5;
	public static final String SKILL_DETAIL_FIRST_LINE = "userid,roleid,druglevel,cooklevel,ironlevel,sewinglevel,craftslevel";
	public static final int ITEM_DETAIL = 6;
	public static final String ITEM_DETAIL_FIRST_LINE = "userid,roleid,lastlogintime,flag360,items";
	public static final int FU_SHI = 7;
	public static final String FU_SHI_FIRST_LINE = "serverid,userid,lastlogintime,flag360,addtotal,buytotal,selltotal,usedtotal,delta,usedtotal2,fushi1,fushi2,fushi3,fushi4";
	public static final int ROLE_RELATION_PROPERTY = 8;
	public static final String ROLE_RELATION_PROPERTY_FIRST_LINE = "userid,roleid,familyid,factionid,shopid1,shopid2";
	public static final int ROLE_XIULIAN_PROPERTY = 9;
	public static final String ROLE_XIULIAN_PROPERTY_FIRST_LINE = "userid,roleid,xlv1,xexp1,xlv2,xexp2,xlv3,xexp3,xlv4,xexp4,xlv5,xexp5,xlv6,xexp6";
	public static final int ROLE_EQUIP_DETAIL = 10;
	public static final String ROLE_EQUIP_DETAIL_FIRST_LINE = "uesrid,account,roleid,rolename,bagtype,equipcode";
}
