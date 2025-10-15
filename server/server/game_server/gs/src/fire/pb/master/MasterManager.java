package fire.pb.master;


import java.util.HashMap;
import java.util.Map;

import fire.log.Logger;
import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import fire.pb.master.proc.PDisMissApprentBeforProc;
import fire.pb.master.proc.PFinishApprentProc;
import fire.pb.master.proc.PGiveMasterFuShiProc;
import fire.pb.master.proc.PNotifyApprentProc;
import fire.pb.master.proc.PReqDisMissMasterProc;
import fire.pb.master.proc.PTakeMasterWineProc;
import fire.pb.master.proc.PUpdateSTLXProc;
import fire.pb.message.SStringRes;
import mkdb.Transaction;

/**
 * 师徒管理器
 * @author nobody
 *
 */
public class MasterManager {
	
	static class SingletonHolder {
		static MasterManager singleton = new MasterManager();
	}
	
	public static MasterManager getInstance() {
		return SingletonHolder.singleton;
	}
	
	public static final Logger logger = Logger.getLogger(MasterManager.class);
	
	/* 能做师傅的最低等级   */
	public static final int MASTER_MIN_LEVEL =  80;
	
	/* 可以拜师的最低等级   */
	public static final int APPRENT_MIN_LEVEL = 25;
	
	/* 徒弟的等级必须小于这个等级，大于等于这个等级就可以出师了   */
	public static final int APPRENT_MAX_LEVEL = 60;
	
	/* 师傅罢免徒弟所需符石   */
	public static final int DISMISS_APPENTCE_FUSHI = 200;
	
	/* 结拜酒道具id   */
	public static final int JIE_BAI_JIU_ITEEM_ID = 50311;
	
	//先注释废弃称谓，过后删除
	//四个弟子的称谓id
	/*public static final int APPENTICE_TITLE_1 = 251; // 的大弟子
	public static final int APPENTICE_TITLE_2 = 252;// 的二弟子
	public static final int APPENTICE_TITLE_3 = 253;// 的三弟子
	public static final int APPENTICE_TITLE_4 = 254;// 的四弟子
	
	//弟子出徒以后获得的称谓
	public static final int APPENTICE_TITLE_5 = 255;// 的弟子
	
	//师傅的六个级别称谓
	public static final int MASTER_TITLE_LEVEL_0 = 245; //能者为师   这个称谓是第一次收徒的时候给的
	public static final int MASTER_TITLE_LEVEL_1 = 246;//良师益友
	public static final int MASTER_TITLE_LEVEL_2 = 247; //诲人不倦*/
	
	//1.连续登录
	public static final int CONTINUE_LOGIN_KEY = 1;
	public static final int CONTINUE_LOGIN_VALUE = 3;
	
	//2.出入江湖
	public static final int TASKMAJOR_30_KEY = 2;
	public static final int TASKMAJOR_30_VALUE = 1;
	
	//3.小有名气
	public static final int TASKMAJOR_40_KEY = 3;
	public static final int TASKMAJOR_40_VALUE = 1;
	
	//4.大展拳脚
	public static final int TASKMAJOR_50_KEY = 4;
	public static final int TASKMAJOR_50_VALUE = 1;
	
	//5.功成名就
	public static final int TASKMAJOR_60_KEY = 5;
	public static final int TASKMAJOR_60_VALUE = 1;
	
	//6.神宠相随
	public static final int PET_WITH_CLOR_4 = 6;
	public static final int PET_WITH_CLOR_4_VALUE = 1;
	
	//徒弟充值
	public static final int CASH_KEY = 12;
	public static final int CASH_VALUE = 1;
	
	//徒弟VIP
	public static final int VIP_KEY = 13;
	public static final int VIP_VALUE = 1;
	
	//徒弟等级是60级的时候显示
	public static final int LEVEL_60_KEY = 14;
	public static final int LEVEL_60_VALUE = 1;
	
	public Map<Integer, Integer> lianxinMap = new HashMap<Integer, Integer>();
	
	
	private MasterManager(){
		lianxinMap.put(CONTINUE_LOGIN_KEY, CONTINUE_LOGIN_VALUE);
		lianxinMap.put(TASKMAJOR_30_KEY, TASKMAJOR_30_VALUE);
		lianxinMap.put(TASKMAJOR_40_KEY, TASKMAJOR_40_VALUE);
		lianxinMap.put(TASKMAJOR_50_KEY, TASKMAJOR_50_VALUE);
		lianxinMap.put(TASKMAJOR_60_KEY, TASKMAJOR_60_VALUE);
		lianxinMap.put(PET_WITH_CLOR_4, PET_WITH_CLOR_4_VALUE);
		lianxinMap.put(CASH_KEY, CASH_VALUE);
		lianxinMap.put(VIP_KEY, VIP_VALUE);
		lianxinMap.put(LEVEL_60_KEY, LEVEL_60_VALUE);
	}
	
	public boolean isMasterApprenticeship(long roleid, long otherRid){
		
		
		return false;
	}
	
	public static int  getMaxApprentice(final int masterlevel) {
		if(masterlevel < MASTER_MIN_LEVEL){
			return 0;
		}else if(masterlevel >= MASTER_MIN_LEVEL && masterlevel < 85){
			return 2;
		}else{
			return 4;
		}
	}
	
	/**
	 * 每个徒弟应当获得的称谓
	 * @param index
	 * @return
	 */
	public static int getAppentceTitleIdByIndex(int index){
		//先注释废弃称谓，过后删除
		switch (index) {
		/*case 0:
			return APPENTICE_TITLE_1;
		case 1:
			return APPENTICE_TITLE_2;
		case 2:
			return APPENTICE_TITLE_3;
		case 3:
			return APPENTICE_TITLE_4;*/
		default:
			return 0;
		}
	}
	
	
	public String getTitleName(int titleId){
		Map<Integer, SStringRes> stringCfgs = ConfigManager.getInstance().getConf(fire.pb.message.SStringRes.class);
		if(stringCfgs == null){
			return null;
		}
		
		SStringRes config  = stringCfgs.get(titleId);
		if(config != null){
			return config.getMsg();
		}
		
		return null;
	}
	
	
	
	
	/**
	 * 根据出徒数量获得的称谓
	 * @param index
	 * @return
	 */
	public int getTitleIdByAppentceNum(int number){
		//先注释废弃称谓，过后删除
		/*if(number >= 1 && number<= 4){
			return MASTER_TITLE_LEVEL_1;
		}
		
		if(number >= 5){
			return MASTER_TITLE_LEVEL_2;
		}*/
		return 0;
	}
	
	public boolean initAchieve(int key, xbean.ApprentceChieve aChieve){
		Integer value = lianxinMap.get(key);
		if(value != null){
			aChieve.setTotal(value);
			return true;
		}
		return false;
	}
	
	/**
	 * 师徒相关npc服务
	 * @param roleId
	 * @param serviceId
	 * @return
	 */
	public static boolean masterService(long roleId, int serviceId){
		
		switch (serviceId) {
		case 2457://领取拜师酒
			new PTakeMasterWineProc(roleId).submit();
			return true;
			
		case 2458 ://结成师徒
			new PNotifyApprentProc(roleId).submit();
			return true;
			
		case 2459://出师
			new PFinishApprentProc(roleId).submit();
			return true;
			
		case 2460 ://罢徒
			new PDisMissApprentBeforProc(roleId).submit();
			return true;
			
		case 2461://罢师
			new PReqDisMissMasterProc(roleId).submit();
			return true;
		}
		return false;
	}
	
	
	public void roleLogin(long roleId){
		xbean.MenstorInfo menstor = xtable.Apprents.select(roleId);
		xbean.MenstorAndApprent master = xtable.Menstor.select(roleId);
		SNotifyMaster send = new SNotifyMaster();
		if(menstor == null || master == null){
			mkdb.Procedure.psendWhileCommit(roleId, send);
			
		}else{
			send.flag = master.getFlag();
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}
	
	public void updateStlx(long roleId, int key, int value){
		updateStlx(roleId, key, value, 0);
	}
	
	public void updateStlx(long roleId, int key, int value, int extValue){
		
		xbean.MenstorAndApprent apprentce = xtable.Menstor.select(roleId);
		if(apprentce == null){
			return;
		}
		
		if(apprentce.getFlag() != 1 && apprentce.getMenstorid() > 0){
			PropRole prole = new PropRole(roleId, true);
			if (Transaction.current() != null){
				mkdb.Procedure.pexecuteWhileCommit(new PGiveMasterFuShiProc(roleId, prole.getName(), extValue));
			}else{
				new PGiveMasterFuShiProc(roleId, prole.getName(), extValue).submit();
			}
			return;
		}
		
		if(apprentce.getAchievement().get(key) != null){
			return;
		}
		
		if (Transaction.current() != null){
			mkdb.Procedure.pexecuteWhileCommit(new PUpdateSTLXProc(roleId, key, value, extValue));
		}else{
			new PUpdateSTLXProc(roleId, key, value, extValue).submit();
		}
	}
	
	
	public static int getTaskKeyBuyTaskId(int taskId){
		
		if(102152 == taskId){
			return TASKMAJOR_30_KEY;
		}
		
		if(102138 == taskId){
			return TASKMAJOR_40_KEY;
		}
		
		if(102396 == taskId){
			return TASKMAJOR_50_KEY;
		}
		return 0;
	}
	
	
	public double getReturnRatio(){
		return  0.5d;
	}
	
	public int getTitleIdByRankIndex(int index){
		if(index == 0){
			return 250;
		}else if(index == 1 || index == 2){
			return 249;
		}else if(index >= 3 && index <= 9){
			return 248;
		}
		return 0;
	}
	
	public boolean isFriendEatch(long masterId, long prenticeRoleId){
		
		xbean.FriendGroups groups = xtable.Friends.select(masterId);		
		xbean.FriendGroups groups1 = xtable.Friends.select(prenticeRoleId);		
		if(groups == null || groups1 == null){
			return false;
		}
		if(groups.getFriendmap().get(prenticeRoleId) == null || 
				groups1.getFriendmap().get(masterId) == null 
				){
			return false;
		}
		
		return true;
	}
	
}