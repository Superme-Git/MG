package fire.pb.hook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.PAddExpProc;
import fire.pb.battle.OperationType;
import fire.pb.event.Event;
import fire.pb.event.EventHandler;
import fire.pb.event.PetSkillChangeEvent;
import fire.pb.event.RoleSkillChangeEvent;
import fire.pb.event.SetFightPetEvent;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SWorldMapConfig;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.role.NextExpAndResMoney;
import fire.pb.skill.SkillRole;
import fire.pb.talk.ChatChannel;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;
import gnet.link.Onlines;
import mkdb.Procedure;
import xbean.HookData;


// 挂机处理类
public class RoleHookManager implements EventHandler{
	
	private static class SingletonHolder {
		private static RoleHookManager singleton = new RoleHookManager();
	}
	
	public static RoleHookManager getInstance() {
		return SingletonHolder.singleton;
	}
	
	public final static int HookSubDPoint = 167;
	
	final private static Logger logger = Logger.getLogger("SYSTEM");
	
	private List<Integer> hookMapList = new ArrayList<Integer>();
	
	private RoleHookManager(){
		Map<Integer,SWorldMapConfig> sworldmap = ConfigManager.getInstance().getConf(SWorldMapConfig.class);
		for(SWorldMapConfig wm :  sworldmap.values()){
			if(wm.getSubUnderGroundMap() == null)
				continue;
			
			String[] strs = wm.getSubUnderGroundMap().split(",");
			for (String mapid : strs) {
				int mapidi = Integer.parseInt(mapid);
				if(mapidi == 0)
					continue;
				hookMapList.add(mapidi);
			}
		}
		
		fire.pb.event.Poster.getPoster().listenEvent( this, PetSkillChangeEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, SetFightPetEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, RoleSkillChangeEvent.class );
	}
	

	//初始化玩家的挂机数据
	public void initHookdata(long roleId){
		HookData hookData = xtable.Rolehook.get(roleId);
		if(hookData == null){
			hookData = xbean.Pod.newHookData();
			hookData.setCangetdpoint((short)200);
			hookData.setLastgettime(System.currentTimeMillis());
			
			xtable.Rolehook.insert(roleId, hookData);
			//logger.info("RoleHookManager initHookdata roleid:" + roleId);
		}
	}
	
	public void onEvent( Event e ){
		if(e instanceof PetSkillChangeEvent){
			PetSkillChangeEvent pscevent = (PetSkillChangeEvent)e;
			final fire.pb.PropRole prop = new fire.pb.PropRole(pscevent.getRoleid(), true);
			if(prop.getFightpetkey() != pscevent.getPetkey()){
				return;
			}
			
			setPetDefaultSkill(pscevent.getRoleid(),pscevent.getPetkey());
			
		}
		else if(e instanceof SetFightPetEvent){
			SetFightPetEvent sfpevent = (SetFightPetEvent)e;
			if(sfpevent.getPetkey() == -1){
				setPetOpType(sfpevent.getRoleid(),(short)0);
				setPetOpId(sfpevent.getRoleid(),0);
				return;
			}
			
			setPetDefaultSkill(sfpevent.getRoleid(),sfpevent.getPetkey());
		}
		else if(e instanceof RoleSkillChangeEvent){
			RoleSkillChangeEvent rscevent = (RoleSkillChangeEvent)e;
			if(rscevent.getSkillid() == -1){
				return;
			}
			
			if(rscevent.getSkillid() > 0){
				setCharOpType(rscevent.getRoleid(),(short)OperationType.ACTION_SKILL);
				setCharOpId(rscevent.getRoleid(),rscevent.getSkillid());
			}
			
			refreshHookBattledata(rscevent.getRoleid(),true);
		}
	}
	
	//刷新玩家的挂机数据
	public void refreshHookdata(long roleId, boolean inProcedure){
		HookData hookData = xtable.Rolehook.get(roleId);
		if(hookData == null){
			hookData = xbean.Pod.newHookData();
			hookData.setCangetdpoint((short)200);
			hookData.setLastgettime(System.currentTimeMillis());
			
			xtable.Rolehook.insert(roleId, hookData);
		}
		
		SRefreshRoleHookData send = new SRefreshRoleHookData();

		send.rolehookdata.cangetdpoint = hookData.getCangetdpoint();
		send.rolehookdata.getdpoint = hookData.getGetdpoint();
		
		if(hookData.getIsautobattle())
			send.rolehookdata.isautobattle = 1;
		else
			send.rolehookdata.isautobattle = 0;
		
		send.rolehookdata.charoptype = hookData.getCharoptype();
		send.rolehookdata.charopid = hookData.getCharopid();
		send.rolehookdata.petoptype = hookData.getPetoptype();
		send.rolehookdata.petopid = hookData.getPetopid();
		send.rolehookdata.offlineexp = hookData.getOfflineexp();
		
		if (inProcedure)
			Procedure.psendWhileCommit(roleId, send);
		else
			Onlines.getInstance().send(roleId, send);

	}
	
	//刷新玩家的挂机相关战斗数据
	public void refreshHookBattledata(long roleId, boolean inProcedure){
		HookData hookData = xtable.Rolehook.get(roleId);
		if(hookData == null){
			hookData = xbean.Pod.newHookData();
			hookData.setCangetdpoint((short)200);
			hookData.setLastgettime(System.currentTimeMillis());
			
			xtable.Rolehook.insert(roleId, hookData);
			//logger.info("RoleHookManager refreshHookBattledata 初始化hookdata roleid:" + roleId);
		}
		
		SRefreshRoleHookBattleData send = new SRefreshRoleHookBattleData();

		if(hookData.getIsautobattle())
			send.rolehookbattledata.isautobattle = 1;
		else
			send.rolehookbattledata.isautobattle = 0;
		
		send.rolehookbattledata.charoptype = hookData.getCharoptype();
		send.rolehookbattledata.charopid = hookData.getCharopid();
		send.rolehookbattledata.petoptype = hookData.getPetoptype();
		send.rolehookbattledata.petopid = hookData.getPetopid();
		
		if (inProcedure)
			Procedure.psendWhileCommit(roleId, send);
		else
			Onlines.getInstance().send(roleId, send);
		//logger.info("RoleHookManager refreshHookBattledata roleid:" + roleId);
	}
	
	//刷新玩家的挂机经验相关数据
	public void refreshHookExpdata(long roleId, boolean inProcedure){
		HookData hookData = xtable.Rolehook.get(roleId);
		if(hookData == null){
			hookData = xbean.Pod.newHookData();
			hookData.setCangetdpoint((short)200);
			hookData.setLastgettime(System.currentTimeMillis());
			
			xtable.Rolehook.insert(roleId, hookData);
			//logger.info("RoleHookManager refreshHookExpdata 初始化hookdata roleid:" + roleId);
		}
		
		SRefreshRoleHookExpData send = new SRefreshRoleHookExpData();

		send.rolehookexpdata.cangetdpoint = hookData.getCangetdpoint();
		send.rolehookexpdata.getdpoint = hookData.getGetdpoint();
		send.rolehookexpdata.offlineexp = hookData.getOfflineexp();
		
		if (inProcedure)
			Procedure.psendWhileCommit(roleId, send);
		else
			Onlines.getInstance().send(roleId, send);
		
		//logger.info("RoleHookManager refreshHookExpdata roleid:" + roleId 
		//		+ " cangetdp:" + hookData.getCangetdpoint() 
		//		+ " getdpoint:" + hookData.getGetdpoint()
		//		+ " offlineexp:" + hookData.getOfflineexp());

	}
	
	//当前地图是否是挂机地图
	public boolean isHookMap(long mapId){
		for(int id : hookMapList){
			if(mapId == id){
				return true;
			}
		}
		return false;
	}
	

	//累积获得经验
	public void refreshOfflineExpByTime(long roleId, long lastOnlineTime){
		
		int offlineexp = 0;
		xbean.Properties prop =  xtable.Properties.select(roleId);
		int roleLevel = prop.getLevel();
		if(roleLevel < 20){
			//小于20级的时候没有离线经验
			//logger.info("RoleHookManager refreshOfflineExpByTime 等级小于20 roleid:" + roleId);
			return;
		}
		
		long lastOffLineTime = prop.getOfflinetime();
		long curr = System.currentTimeMillis();
		long minu = 0;
		if(lastOffLineTime == 0){
			//logger.info("RoleHookManager refreshOfflineExpByTime 上次登录时间是0 roleid:" + roleId);
			return;
		}
		
		//不太好判断,用这个方式，就是当前时间和登录开始缓存的上次登录时间都大于上次离线时间的时候是非法。
		if(lastOnlineTime > lastOffLineTime && curr > lastOffLineTime){
			//logger.info("RoleHookManager refreshOfflineExpByTime 时间非法 roleid:" + roleId);
			return;
		}
		
		minu = (curr - lastOffLineTime)/DateValidate.minuteMills;
		if(minu > 720){
			//最多给720分钟的经验
			minu = 720;
		}
		
		if(minu < 30){
			return ;
		}
		
		NextExpAndResMoney cfg = ConfigManager.getInstance().getConf(NextExpAndResMoney.class).get(roleLevel);
		offlineexp = (int) (minu * cfg.offlexppermin);
		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setOfflineexp(hookData.getOfflineexp()+offlineexp);
		boolean res = new PAddExpProc(roleId, offlineexp, true,  PAddExpProc.OTHER, "离线获得经验").call();
		if(res){
			sendOfflineExpMsg(roleId,minu,offlineexp);
		}
		
		//logger.info("RoleHookManager refreshOfflineExpByTime roleid:" + roleId + " min:" + minu + " offlineexp:" + offlineexp);
		
	}
	
	//发送离线经验消息
	public void sendOfflineExpMsg(long roleId, long offlineTime, long offlineExp){

		//小于30分钟不发离线经验消息
		if(offlineTime < 30)
			return ;
		
		StringBuilder strbuilder = new StringBuilder();
		strbuilder.append("<T t=\"您距离上次离开累计离线");
		strbuilder.append(offlineTime);
		strbuilder.append("分钟，共获得");
		strbuilder.append(offlineExp);
		strbuilder.append("离线经验");
		strbuilder.append(" c=\"FFFFFF00\"></R>");
		 
		StringBuilder sb1 = new StringBuilder();
		sb1.append("挂机");
		
		final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos  = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
		
		ChatChannel.getInstance().process(roleId, fire.pb.talk.ChannelType.CHANNEL_SYSTEM, strbuilder.toString(), sb1.toString(), showinfos, 0);

		//消息也发
		List<String> parameters = new ArrayList<String>();
		parameters.add(Long.toString(offlineTime));
		parameters.add(Long.toString(offlineExp));
		MessageMgr.sendMsgNotify(roleId, HookMsg2ClientEnum.OFFLINEEXP, parameters);
		MessageMgr.psendSystemMessageToRole(roleId, 160116, parameters);
	}
	
	//设置能够获取双倍点数
	public void setCanGetDpoint(long roleId,short canGetDpoint){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setCangetdpoint(canGetDpoint);
		
		//logger.info("RoleHookManager setCanGetDpoint roleid:" + roleId + " canGetDpoint:" + canGetDpoint );

	}
	
	//设置已经领取双倍点数
	public void setGetDpoint(long roleId,short getDpoint){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setGetdpoint(getDpoint);
		
		//logger.info("RoleHookManager setGetDpoint roleid:" + roleId + " getDpoint:" + getDpoint );

	}
	
	//设置自动战斗
	public void setAutoBattle(long roleId,byte isAutoBattle){
		
		HookData hookData =  xtable.Rolehook.get(roleId);

		if(isAutoBattle == 0)
			hookData.setIsautobattle(false);
		else
			hookData.setIsautobattle(true);
		
		//logger.info("RoleHookManager setAutoBattle roleid:" + roleId + " isAutoBattle:" + isAutoBattle );
	}
	
	//设置人物操作类型
	public void setCharOpType(long roleId,short charOpType){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setCharoptype(charOpType);
		
		//logger.info("RoleHookManager setCharOpType roleid:" + roleId + " isAutoBattle:" + charOpType );

	}
	
	//设置人物操作id
	public void setCharOpId(long roleId,int charOpId){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setCharopid(charOpId);
		
		//logger.info("RoleHookManager setCharOpId roleid:" + roleId + " charOpId:" + charOpId );
		
	}
	
	//设置宠物操作类型
	public void setPetOpType(long roleId,short petOpType){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setPetoptype(petOpType);
		
		//logger.info("RoleHookManager setPetOpType roleid:" + roleId + " petOpType:" + petOpType );

	}
	
	//设置宠物操作id
	public void setPetOpId(long roleId,int petOpId){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setPetopid(petOpId);
		
		//logger.info("RoleHookManager setPetOpId roleid:" + roleId + " petOpId:" + petOpId );

	}
	
	//设置离线经验
	public void setOfflineExp(long roleId,long offlineExp){

		HookData hookData =  xtable.Rolehook.get(roleId);
		hookData.setOfflineexp(offlineExp);
		
		//logger.info("RoleHookManager setOfflineExp roleid:" + roleId + " offlineExp:" + offlineExp );

	}
	
	//领取双倍点数
	public int getDpoint(long roleId){

		HookData hookData =  xtable.Rolehook.get(roleId);
		short canGetDpoint = hookData.getCangetdpoint();
		short changeval = 0;
		
		if(canGetDpoint <= 0){
			//logger.info("RoleHookManager getDpoint roleid:" + roleId + " errno:2" );
			return HookGetDpointResEnum.GETDPOINTFAIL;
		}
			
		short getDpoint = hookData.getGetdpoint();
		if(getDpoint >= 1000){
			getDpoint = 1000;
			hookData.setGetdpoint(getDpoint);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, HookMsg2ClientEnum.GETDPOINTLIMIT, null);
			//logger.info("RoleHookManager getDpoint roleid:" + roleId + " errno:1" );
			return HookGetDpointResEnum.GETDPOINTLIMIT;
		}
			
		if((1000 - getDpoint) >= 100){
			//当差值大于100时
			if(canGetDpoint >= 100){
				canGetDpoint -= 100;
				getDpoint += 100;
				changeval = 100;
			}else{
				getDpoint += canGetDpoint;
				changeval = canGetDpoint;
				canGetDpoint = 0;
			}
		}else{
			//当差值小于100时
			short diffval = (short) ((short)1000 - getDpoint);
			if(canGetDpoint >= diffval){
				canGetDpoint -= diffval;
				getDpoint += diffval;
				changeval = diffval;
			}else{
				getDpoint += canGetDpoint;
				changeval = canGetDpoint;
				canGetDpoint = 0;
			}
		}
		
		hookData.setCangetdpoint(canGetDpoint);
		hookData.setGetdpoint(getDpoint);
		
		List<String> parameters = new ArrayList<String>();
		parameters.add(Short.toString(changeval));
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, HookMsg2ClientEnum.GETDPOINTSUCC, parameters);
		
		//logger.info("RoleHookManager getDpoint roleid:" + roleId + " errno:0" );
		return HookGetDpointResEnum.GETDPOINTSUCC;
	}
	
	//冻结双倍点数
	public int freeDpoint(long roleId){
		HookData hookData =  xtable.Rolehook.get(roleId);
		short canGetDpoint = hookData.getCangetdpoint();
		short getDpoint = hookData.getGetdpoint();
		short changeval = 0;
		if(getDpoint <= 0){
			//logger.info("RoleHookManager freeDpoint roleid:" + roleId + " errno:1" );
			return HookFreeDpointResEnum.FREEDPOINTFAIL;
		}

		changeval = getDpoint;
		canGetDpoint += getDpoint;
		getDpoint = 0;
		
		hookData.setCangetdpoint(canGetDpoint);
		hookData.setGetdpoint(getDpoint);
		
		List<String> parameters = new ArrayList<String>();
		parameters.add(Short.toString(changeval));
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, HookMsg2ClientEnum.FREEDPOINTSUCC, parameters);
		//logger.info("RoleHookManager freeDpoint roleid:" + roleId + " errno:0" );
		return HookFreeDpointResEnum.FREEDPOINTSUCC;
	}
	
	//累积获得经验
	public int getDpointExp(long roleId){
		
		int dpointexp = 0;
		xbean.Properties prop =  xtable.Properties.select(roleId);
		int roleLevel = prop.getLevel();
		
		NextExpAndResMoney cfg = ConfigManager.getInstance().getConf(NextExpAndResMoney.class).get(roleLevel);
		dpointexp = cfg.dpointexp;
		
		return dpointexp;

	}
	
	//是否有挂机经验加成
	public boolean isHookExpAdd(long roleId){
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(HookSubDPoint);
		Role RoleObj = RoleManager.getInstance().getRoleByID(roleId);
		if(RoleObj ==null){
			return false;
		}
		long mapId = RoleObj.getMapId();
		boolean ishookmap = isHookMap(mapId);
		if(!ishookmap){
			return false;
		}
		HookData hookData =  xtable.Rolehook.get(roleId);
		short getDpoint = hookData.getGetdpoint();
		if(getDpoint < Integer.parseInt(common.getValue())){
			return false;
		}

		return true;
	}

	//减少一点双倍点数
	public void SubDpoint(long roleId){
		HookData hookData =  xtable.Rolehook.get(roleId);
		short getDpoint = hookData.getGetdpoint();
		if(getDpoint <= 0)
			return;

		getDpoint--;

		hookData.setGetdpoint(getDpoint);
		
		//logger.info("RoleHookManager SubDpoint roleid:" + roleId + " getDpoint:" + getDpoint );
	}
	
	//每日增加可领取双倍点数
	public void addDpointDaily(long roleId){
		HookData hookData =  xtable.Rolehook.get(roleId);
		short canGetDpoint = hookData.getCangetdpoint();
		long lasttime = hookData.getLastgettime();
		long now = System.currentTimeMillis();
		if(fire.pb.util.DateValidate.inTheSameDay(lasttime,now))
			return;

		// 可领取双倍点数大于2000时,不再增加.已超出2000点的部分不重置
		if (canGetDpoint >= 2000) {
			canGetDpoint += 0;
		} else {
			canGetDpoint += 200;
			if (canGetDpoint > 2000)
				canGetDpoint = 2000;
		}

		hookData.setCangetdpoint(canGetDpoint);
		hookData.setLastgettime(now);
		//logger.info("RoleHookManager addDpointDaily roleid:" + roleId + " canGetDpoint:" + canGetDpoint );
	}
	
	//跨天登录增加双倍点数
	public void loginAddDpoint(long roleId, long lastOnlineTime){

		HookData hookData =  xtable.Rolehook.get(roleId);
		long lasttime = hookData.getLastgettime();
		long curr = System.currentTimeMillis();
		
		int diffdays = fire.pb.util.DateValidate.getDaysBetween(lasttime,curr);
		if (diffdays > 0) {
			short canGetDpoint = hookData.getCangetdpoint();

			// 可领取双倍点数大于2000时,不再增加.已超出2000点的部分不重置
			if (canGetDpoint >= 2000) {
				canGetDpoint += 0;
			} else {
				canGetDpoint += (200 * diffdays);
				if (canGetDpoint > 2000)
					canGetDpoint = 2000;
			}

			hookData.setCangetdpoint(canGetDpoint);
			hookData.setLastgettime(curr);
		}
		//logger.info("RoleHookManager loginAddDpoint roleid:" + roleId + " offlinedays:" + diffdays );
	}
	
	//消耗一定数量的双倍点数
	public boolean consumeDpoint(long roleId, int cnt) {
		HookData hookData = xtable.Rolehook.get(roleId);
		
		short getDpoint = hookData.getGetdpoint();
		if (getDpoint >= cnt) {
			getDpoint -= cnt;
			hookData.setGetdpoint(getDpoint);
			//logger.info("RoleHookManager consumeDpoint roleid:" + roleId + " getDpoint:" + getDpoint );
			return true;
		}
		//logger.error("RoleHookManager consumeDpoint roleid:" + roleId + " getDpoint:" + getDpoint + " cnt:" + cnt);
		return false;		
	}
	
	//增加一定数量的双倍点数
//	public boolean addDpoint(long roleId, int cnt) {
//		HookData hookData = xtable.Rolehook.get(roleId);
//		
//		short getDpoint = hookData.getGetdpoint();
//		getDpoint += cnt;
//		if (getDpoint >= 2000) {
//			getDpoint = 2000;
//		}
//		
//		hookData.setGetdpoint(getDpoint);
//		//logger.info("RoleHookManager addDpoint roleid:" + roleId + " cnt:" + cnt);
//		return true;
//	}
	
	//获取剩余双倍点数
	public short getLastDpoint(long roleId) {
		HookData hookData = xtable.Rolehook.select(roleId);
		return hookData.getGetdpoint();
	}

	//获取所有的双倍点数
	public short getAllDpoint(long roleId) {
		HookData hookData = xtable.Rolehook.select(roleId);
		return (short)(hookData.getGetdpoint() + hookData.getCangetdpoint());
	}
	
	//登录时重设宠物缺省技能
	public void loginSetPetDefaultSkill(long roleId){
		
		xbean.Properties prop =  xtable.Properties.select(roleId);
		if(prop.getFightpetkey() <= 0){
			setPetOpType(roleId,(short)OperationType.ACTION_ATTACK);
			setPetOpId(roleId,0);
			return;
		}
		final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, true);
		final xbean.PetInfo xpetinfo = petcol.getPetInfo(prop.getFightpetkey());
		Pet pet = Pet.getPet(xpetinfo);
		int petSkillId = pet.getAutoSkillId();
		if(petSkillId <= 0){
			setPetOpType(roleId,(short)OperationType.ACTION_ATTACK);
			setPetOpId(roleId,0);
			return ;
		}

		setPetOpType(roleId,(short)OperationType.ACTION_SKILL);
		setPetOpId(roleId,petSkillId);
	}
	
	//登录时重设宠物缺省技能
	public void setPetDefaultSkill(long roleId, int petKey){
		
		final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, true);
		final xbean.PetInfo xpetinfo = petcol.getPetInfo(petKey);
		Pet pet = Pet.getPet(xpetinfo);
		int petSkillId = pet.getAutoSkillId();
		if(petSkillId <= 0) {
			setPetOpType(roleId,(short)OperationType.ACTION_ATTACK);
			setPetOpId(roleId,petSkillId);
		} else {
			setPetOpType(roleId,(short)OperationType.ACTION_SKILL);
			setPetOpId(roleId,petSkillId);
		}
		refreshHookBattledata(roleId,true);
	}
	
	//登录时重设人物缺省技能
	public void loginSetRoleDefaultSkill(long roleId){
		
		SkillRole srole = new SkillRole(roleId, true);

		int roleSkillId = srole.getAutoSkillId();
		if(roleSkillId <= 0){
			setCharOpType(roleId,(short)OperationType.ACTION_ATTACK);
			return ;
		}

		setCharOpType(roleId,(short)OperationType.ACTION_SKILL);
		setCharOpId(roleId,roleSkillId);
	}
	
	//得到锁屏状态
	public Boolean getLockScreenStateByRoleId(final long roleId)
	{
		Boolean state =xtable.Rolelockscreen.select(roleId);
		if(state == null){
			state = false;
			xtable.Rolelockscreen.add(roleId, state);
		}

		return state;
	}
	
	//设置锁屏状态
	public void setLockScreenStateByRoleId(final long roleId, Boolean lockScreenState)
	{
		Long battleId = xtable.Roleid2battleid.select(roleId);
		if(battleId!=null){	
			xbean.BattleInfo battle = xtable.Battle.get(battleId);
			if (battle == null){
				xtable.Rolelockscreen.remove(roleId);
				xtable.Rolelockscreen.add(roleId, lockScreenState);
				return;
			}
			// 保存战斗者的当前坐标
			if(lockScreenState){
				if(!battle.getOfflineroleids().contains(roleId))
					battle.getOfflineroleids().add(roleId);
			}else{
				if(battle.getOfflineroleids().contains(roleId))
					battle.getOfflineroleids().remove(roleId);
			}
		}
		xtable.Rolelockscreen.remove(roleId);
		xtable.Rolelockscreen.add(roleId, lockScreenState);
	}
}
