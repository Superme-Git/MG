package fire.pb;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;


public class PropConf{
	static Properties FuShiProp = ConfigManager.getInstance().getPropConf("fushi");
	public static class FuShi{
		public static final int GOLD_BOX_ID=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.goldboxid");
		public static final int GOLD_BOX_NUM=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.goldboxnum");		
		public static final int SILVER_BOX_ID=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.silverboxid");	
		public static final int SILVER_BOX_NUM=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.silverboxnum");	
		public static final int SHOP_GOLD_BOX_ID=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.shopgoldboxid");	
		public static final int HYD_GOLD_BOX_ID=FireProp.getIntValue(FuShiProp, "mtgserv.fushi.hydgoldboxid");	
	}

	static Properties SysProp = ConfigManager.getInstance().getPropConf("sys");
	public static class Sys{
		public static final int HTTP_PORT=FireProp.getIntValue(SysProp, "sys.http.port");
		public static final int MAX_CONNECTED_NUMBER = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.queue.onlinenum.max");
		public static final int ZERO_QUEUE_TICKET_TIME = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.queue.zeroticket.time");
		public static final int BROADCAST_QUEUE_PERIOD = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.queue.broadcast.time");
		public static final int TRADE_DISTANCE = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.trade.distance");
	}
	
	//public static final java.util.Properties sqprop = fire.pb.main.ConfigManager.getInstance().getPropConf("specialquest");
	static Properties sqprop = ConfigManager.getInstance().getPropConf("specialquest");
	public static class Specialquest{
		public static final int PATROL_MAXSTEP=FireProp.getIntValue(sqprop, "fire.pb.specialquest.patrolmaxstep");
		public static final int PATROL_MINSTEP=FireProp.getIntValue(sqprop, "fire.pb.specialquest.patrolminstep");
		public static final int PATROL_BATTL=FireProp.getIntValue(sqprop, "fire.pb.specialquest.patrolbattle");
	}

	static Properties taskProp = ConfigManager.getInstance().getPropConf("task");
	public static class Mission{
		public static final int BOSI_MINlEVEL=FireProp.getIntValue(taskProp, "mtgserv.qihua.minLevel");
		public static final int BOSI_TASKID=FireProp.getIntValue(taskProp, "mtgserv.qihua.taskid");;
		public static final int BOSI_TIMES=FireProp.getIntValue(taskProp, "mtgserv.qihua.times");
		public static final int BOSI_MSG=FireProp.getIntValue(taskProp, "mtgserv.qihua.awardMsg");
	    public static final int QIHUA_ID=FireProp.getIntValue(taskProp, "mtgserv.qihua.id");
	    public static final int YICAO_ID=FireProp.getIntValue(taskProp, "mtgserv.yicao.id");
	    public static final int CHAYE_ID=FireProp.getIntValue(taskProp, "mtgserv.chaye.id");
	    public static final int HIDEME_ID=FireProp.getIntValue(taskProp, "mtgserv.hideme.buffid");
	}
	static Properties battleProp = ConfigManager.getInstance().getPropConf("battle");
	public static class Battle{
		// 公式调试模式 0为非debug模式 1为debug模式。在debug模式下支持公式修改后直接启动服务器。否则需要重新生成服务器程序 gs.jar文件
		public static final int DEBUG_FORMULA_MODULE = FireProp.getIntValue(battleProp, "mtgserv.debug_module"); 		
		public static final int STEPLEN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_battle"); // 遇怪的最大步数
		public static final int STEPLEN_NOBATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_nobattle"); // 战斗结束后不遇怪的路程
		public static final int NOBATTLE_BUFFID  = FireProp.getIntValue(battleProp, "mtgserv.nobattleBuffid");
		public static final int DEATH_PUNISH = FireProp.getIntValue(battleProp, "mtgserv.msg.deathpunish");
		public static final int MAX_CONDUCTIONFO_LEN = FireProp.getIntValue(battleProp, "mtgserv.maxConductInfoLen");
		public static final float AUTO_PROTECT_RATE= FireProp.getFloatValue(battleProp, "mtgserv.auto_protect_rate"); //默认自动保护率	0.01
		public static final int WATCHERS_IN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.watcherInBattle");
		public static final int WATCHERS_TOTOAL_NUMS = FireProp.getIntValue(battleProp, "mtgserv.watcherTotalNum");	
		public static final int PROP_SP_INITIAL = FireProp.getIntValue(battleProp, "mtgserv.prop_sp_initial"); //初始怒气
		public static final int PROP_SP_MAX = FireProp.getIntValue(battleProp, "mtgserv.prop_sp_max"); //怒气上限
		public static final float PROP_SP_DISCOUNT = FireProp.getFloatValue(battleProp, "mtgserv.prop_sp_discount"); //死亡后怒气折扣
		public static final String PROP_SP_FORMULA = FireProp.getStringValue(battleProp, "mtgserv.prop_sp_formula"); //愤怒公式
		public static final int BATTLEENTER_SP_MAX = FireProp.getIntValue(battleProp, "mtgserv.battleEnter_sp_max"); //进战斗怒气上限
		public static final float RAND_SPEED_VALUE = FireProp.getFloatValue(battleProp, "mtgserv.rand_speed_value"); //乱速浮动值	0.05
		public static final float DAMAGE_DEFEND_PARAM = FireProp.getFloatValue(battleProp, "mtgserv.damage_defend_param"); //防御减伤系数	0.5
		public static final float MIX_PHYSIC_DAMAGE_VALUE = FireProp.getFloatValue(battleProp, "mtgserv.mix_physic_damage_value"); //最低物理伤害系数	0.1
		public static final float DAMAGE_PROTECT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.damage_protect_rate"); //保护分担伤害系数	0.5
		public static final float MIX_PHYSIC_CRIT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.mix_physic_crit_rate"); //最低物理暴击率	0.05
		public static final float MIX_MAGIC_CRIT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.mix_magic_crit_rate"); //最低法术暴击率	0.05
		public static final float MIX_HEAL_CRIT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.mix_heal_crit_rate"); //最低治疗暴击率	0.05
		public static final float DAMAGE_FLOAT_VALUE = 0.0f;
		public static final float POISON_INFECT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.poison_infect_rate"); //毒传染率	0.15
		public static final float MYSTERY_FORBID_RATE = FireProp.getFloatValue(battleProp, "mtgserv.mystery_forbid_rate"); //奥术禁锢率	0.15
		public static final float PANDORA_BOX_RATE = FireProp.getFloatValue(battleProp, "mtgserv.pandora_box_rate"); //超度邪恶率	0.5
		public static final int PROP_EP_MAX = FireProp.getIntValue(battleProp, "mtgserv.prop_ep_max"); //效果点上限	
		public static final float ROLE_HIDE_EXT_CRIT_RATE = FireProp.getFloatValue(battleProp, "mtgserv.role_hide_ext_crit_rate"); //破隐附加暴击率	
		public static final int ROLE_HIDE_EXT_CRIT_EFFECT = FireProp.getIntValue(battleProp, "mtgserv.role_hide_ext_crit_effect"); //破隐附加暴击伤害
		public static final String CAMERA_FILE_SEVER_ULR = FireProp.getStringValue(battleProp, "mtgserv.camera_file_server_url"); //录像文件url头	
		public static final String CAMERA_FILE_ULR_HEAD = FireProp.getStringValue(battleProp, "mtgserv.camera_file_url_head"); //录像文件url头	
		public static final String CAMERA_FILE_ULR_TAIL = FireProp.getStringValue(battleProp, "mtgserv.camera_file_url_tail"); //录像文件url尾	
	/*	public static final int STEPLEN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_battle"); //控制基础命中率	0.6
		public static final int STEPLEN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_battle"); //控制命中率上限	0.98
		public static final int STEPLEN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_battle"); //控制衰减系数	0.9
		public static final int STEPLEN_BATTLE = FireProp.getIntValue(battleProp, "mtgserv.steplen_battle"); //控制增长系数	0.95
	*/
		
	}
	static Properties crossProp = ConfigManager.getInstance().getPropConf("cross");
	public static class Cross {
		public static final int PVP = FireProp.getIntValue(crossProp, "cross.pvp"); // 是不是跨服PVP
	}
	static Properties serverIdProp = ConfigManager.getInstance().getPropConf("serverid");
	public static class ServerId {
		public static final int ServerId = FireProp.getIntValue(serverIdProp, "server.id"); // 预设读取配置时使用的服务器ID,因为初始化配置的时候还没有得到服务器ID
	}
}
