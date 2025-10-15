package fire.pb.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.log.RemoteLogParam;
import fire.log.beans.ItemBaseBean;
import fire.log.beans.RegBean;
import fire.pb.game.MulDayLoginGiftData;
import fire.pb.game.Smuldayloginreward;
import fire.pb.item.Pack;
import fire.pb.item.SMulDayLogin;
import fire.pb.main.ConfigManager;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.DateValidate;
import fire.pb.util.MessageUtil;

//七日登录送礼处理
public class MulDayLogin {

	private static Map<Integer, Smuldayloginreward> mulDayLoginRewardConf = ConfigManager.getInstance().getConf(Smuldayloginreward.class);
	
	private static Logger logger = Logger.getLogger("AWARD");
	
	//领取七日登录奖励(存储过程中调用)
	public static boolean getMulDayLoginReward(long roleId, int loginDays){
		long now = System.currentTimeMillis();
		xbean.MulDayLoginGift mulDayLoginGiftData = xtable.Rolemuldayloginreward.get(roleId);
		if(mulDayLoginGiftData == null) 
			return false;
		
		//人物形象
		Integer roleShape = xtable.Properties.selectShape(roleId);
		Integer rewardIndex = roleShape - 1;
		
		Integer roleSchool = xtable.Properties.selectSchool(roleId);
		
		Smuldayloginreward cfg = mulDayLoginRewardConf.get(loginDays);
		if(cfg == null) 
			return false;
		
		MulDayLoginGiftData mulDayLoginGift = cfg.getRewardvec().get(rewardIndex);
		
		Pack itemBag = new Pack(roleId, false);
		
		int emptyVolume = itemBag.getFreepos().size();
		if(emptyVolume < mulDayLoginGift.needbagvolume){
			//失败，空间不足
			MessageMgr.sendMsgNotify(roleId, 141095, Arrays.asList(String.valueOf(mulDayLoginGift.needbagvolume)));
			logger.error("MulDayLogin reward error:背包空间不足  roleid:" + roleId + " freePos:" + emptyVolume + " days:" + loginDays);
			return false;
		}
		
		if(mulDayLoginGiftData.getRewardmap().get(loginDays) != null 
				&& mulDayLoginGiftData.getRewardmap().get(loginDays)!=0){
			//已经领取了该奖励
			logger.error("MulDayLogin reward error:已经领取了该奖励  roleid:" + roleId + " days:" + loginDays);
			return false;
		}
		
		final fire.pb.pet.Module petMod=(fire.pb.pet.Module)fire.pb.main.ModuleManager.getInstance().getModuleByName("pet");
		if(null == petMod){
			logger.error("MulDayLogin reward error:petmodule is null roleid:" + roleId);
			return false;
		}
		
		fire.pb.pet.PetManager petMgr = petMod.getPetManager();
		if (null == petMgr){
			logger.error("MulDayLogin reward error:PetManager is null roleid:" + roleId);
			return false;
		}
		
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, false);
		int remainsize = petColumn.getRemainSize();
		if( remainsize < mulDayLoginGift.needpetbagvolume){
			MessageMgr.sendMsgNotify(roleId, 143417, null);
			logger.error("MulDayLogin reward error:petcol is full! roleid:" + roleId + " remainsize:" + remainsize);
			return false;//宠物栏已满
		}

		Map<String, Object> rewardParas = new HashMap<String, Object>();
		
		//运营日志
		List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();
		
		boolean res1 = addItem(roleId, roleSchool, mulDayLoginGift.getItem1id(), mulDayLoginGift.getItem1num(), 
				mulDayLoginGift.getItem1bind(), items, rewardParas);
		
		if (!res1) {
			// 添加物品失败
			logger.error("MulDayLogin reward error:添加物品失败1  roleid:" + roleId + " days:" + loginDays);
		}
		
		boolean res2 = addItem(roleId, roleSchool, mulDayLoginGift.getItem2id(), mulDayLoginGift.getItem2num(), 
				mulDayLoginGift.getItem2bind(), items, rewardParas);
		
		if (!res2) {
			// 添加物品失败
			logger.error("MulDayLogin reward error:添加物品失败2  roleid:" + roleId + " days:" + loginDays);
		}
		
		boolean res3 = addItem(roleId, roleSchool, mulDayLoginGift.getItem3id(), mulDayLoginGift.getItem3num(), 
				mulDayLoginGift.getItem3bind(), items, rewardParas);
		
		if (!res3) {
			// 添加物品失败
			logger.error("MulDayLogin reward error:添加物品失败3  roleid:" + roleId + " days:" + loginDays);
		}
		
		if (mulDayLoginGift.getPet1id()>0) {
    		fire.pb.pet.PetAttr petAttr = petMgr.getAttr(mulDayLoginGift.getPet1id());
    		if (null == petAttr){
    			logger.error("MulDayLogin reward error:增加宠物1失败, roleid:" + roleId + "宠物配置表中 并没有此ID对应的宠物:" + mulDayLoginGift.getPet1id());
    			return false;
    		}
    		int addRes = -1;
    		if(mulDayLoginGift.getPet1bind() == 0){
    			addRes = petColumn.addpet(mulDayLoginGift.getPet1id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, false, null);
    		}else{
    			addRes = petColumn.addpet(mulDayLoginGift.getPet1id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, true, null);
    		}
    		if(addRes <= 0){
    			logger.error("MulDayLogin reward error:增加宠物1失败, roleid:" + roleId + "加宠物失败.");
    			return false;
    		}
    		MessageMgr.psendMsgNotifyWhileCommit(roleId, 142378, MessageMgr.getStringList(petAttr.getName()));
		}
		
		if (mulDayLoginGift.getPet2id()>0) {
    		fire.pb.pet.PetAttr petAttr = petMgr.getAttr(mulDayLoginGift.getPet2id());
    		if (null == petAttr){
    			logger.error("MulDayLogin reward error:增加宠物2失败, roleid:" + roleId + "宠物配置表中 并没有此ID对应的宠物:" + mulDayLoginGift.getPet2id());
    			return false;
    		}
    		int addRes = -1;
    		if(mulDayLoginGift.getPet2bind() == 0){
    			addRes = petColumn.addpet(mulDayLoginGift.getPet2id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, false, null);
    		}else{
    			addRes = petColumn.addpet(mulDayLoginGift.getPet2id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, true, null);
    		}
    		if(addRes <= 0){
    			logger.error("MulDayLogin reward error:增加宠物2失败, roleid:" + roleId + "加宠物失败.");
    			return false;
    		}
    		MessageMgr.psendMsgNotifyWhileCommit(roleId, 142378, MessageMgr.getStringList(petAttr.getName()));
		}
		
		if (mulDayLoginGift.getPet3id()>0) {
    		fire.pb.pet.PetAttr petAttr = petMgr.getAttr(mulDayLoginGift.getPet3id());
    		if (null == petAttr){
    			logger.error("MulDayLogin reward error:增加宠物3失败, roleid:" + roleId + "宠物配置表中 并没有此ID对应的宠物:" + mulDayLoginGift.getPet3id());
    			return false;
    		}
    		int addRes = -1;
    		if(mulDayLoginGift.getPet3bind() == 0){
    			addRes = petColumn.addpet(mulDayLoginGift.getPet3id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, false, null);
    		}else{
    			addRes = petColumn.addpet(mulDayLoginGift.getPet3id(), petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_LEIJILOGIN, PetColour.WHITE, true, null);
    		}
    		if(addRes <= 0){
    			logger.error("MulDayLogin reward error:增加宠物3失败, roleid:" + roleId + "加宠物失败.");
    			return false;
    		}
    		MessageMgr.psendMsgNotifyWhileCommit(roleId, 142378, MessageMgr.getStringList(petAttr.getName()));
		}
		
		mulDayLoginGiftData.getRewardmap().put(loginDays, now);
		
		SMulDayLogin msg = new SMulDayLogin();
		msg.logindays = mulDayLoginGiftData.getLogindays();
		msg.rewardmap.putAll(mulDayLoginGiftData.getRewardmap());
		mkdb.Procedure.psendWhileCommit(roleId, msg);
		
		logger.info("MulDayLogin reward 七日登录奖励领取成功  roleid:" + roleId + " days:" + loginDays);
		//运营日志
		RegBean sibean=new RegBean(fire.log.beans.RegBean.REG_SERVEN,loginDays,items);
		fire.log.YYLogger.signingLog(roleId,sibean);
		
		return true;
	}
	


	//跨天处理（存储过程中调用）
	public static void dayCross(long roleId){
		//累计登录送礼处理
		long now = System.currentTimeMillis();
		xbean.MulDayLoginGift mulDayLoginGiftData = xtable.Rolemuldayloginreward.get(roleId);
		if(mulDayLoginGiftData==null){
			mulDayLoginGiftData = xbean.Pod.newMulDayLoginGift();
			xtable.Rolemuldayloginreward.insert(roleId, mulDayLoginGiftData);
		}
		
		int confSize = mulDayLoginRewardConf.values().size();

		if(!DateValidate.inTheSameDay(mulDayLoginGiftData.getLogintime(), now)){
			mulDayLoginGiftData.setLogindays(mulDayLoginGiftData.getLogindays()+1);
		}

		for(int rewardIdx=1; rewardIdx<=confSize && rewardIdx<=mulDayLoginGiftData.getLogindays(); rewardIdx++){
			if(!mulDayLoginGiftData.getRewardmap().containsKey(rewardIdx)){
				mulDayLoginGiftData.getRewardmap().put(rewardIdx, 0L);
			}
		}

		boolean isAvailable = false;
		if(mulDayLoginGiftData.getRewardmap().size()<confSize){
			isAvailable = true;
		}else{
			for(int idx=1;idx<=confSize;idx++){
				Long rewardVal = mulDayLoginGiftData.getRewardmap().get(idx);
				if(rewardVal!=null && rewardVal==0){
					isAvailable = true;
					break;
				}
			}
		}
		mulDayLoginGiftData.setLogintime(now);
		if(isAvailable){
			//领完了，不再发送协议
			SMulDayLogin snd = new SMulDayLogin();
			snd.logindays = mulDayLoginGiftData.getLogindays();
			snd.rewardmap.putAll(mulDayLoginGiftData.getRewardmap());
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		}
	}
	
	//离线处理（存储过程中调用）
	public static void offline(long roleId, long lastOfflineTime){
		offline(roleId, lastOfflineTime, System.currentTimeMillis());
	}
	

	public static void offline(long roleId, long lastOfflineTime, long now){
		xbean.MulDayLoginGift mulDayLoginGiftTbl = xtable.Rolemuldayloginreward.get(roleId);
		if(mulDayLoginGiftTbl==null){
			mulDayLoginGiftTbl = xbean.Pod.newMulDayLoginGift();
			mulDayLoginGiftTbl.setLogindays(1);
			mulDayLoginGiftTbl.getRewardmap().put(1, 0L);
			xtable.Rolemuldayloginreward.insert(roleId, mulDayLoginGiftTbl);
		}
		
		//下线与现在同一天时，不算入累计天数
		if(now>lastOfflineTime && now>mulDayLoginGiftTbl.getLogintime() 
				&& !DateValidate.inTheSameDay(mulDayLoginGiftTbl.getLogintime(), now)){
			int dayOfBetweenTwo = DateValidate.getDaysBetween(now, mulDayLoginGiftTbl.getLogintime());
			mulDayLoginGiftTbl.setLogindays(mulDayLoginGiftTbl.getLogindays()+dayOfBetweenTwo);
		}
		
		mulDayLoginGiftTbl.setLogintime(now);
	}
	
	//上线处理(存储过程中调用)
	public static void online(long roleId){
		//累计登录送礼处理
		long now = System.currentTimeMillis();
		xbean.MulDayLoginGift mulDayLoginGiftTbl = xtable.Rolemuldayloginreward.get(roleId);
		if(mulDayLoginGiftTbl==null){
			mulDayLoginGiftTbl = xbean.Pod.newMulDayLoginGift();
			xtable.Rolemuldayloginreward.insert(roleId, mulDayLoginGiftTbl);
		}
		
		int confSize = mulDayLoginRewardConf.values().size();

		if(!DateValidate.inTheSameDay(mulDayLoginGiftTbl.getLogintime(), now)){
			mulDayLoginGiftTbl.setLogindays(mulDayLoginGiftTbl.getLogindays()+1);
		}

		for(int rewardIdx=1; rewardIdx<=confSize && rewardIdx<=mulDayLoginGiftTbl.getLogindays(); rewardIdx++){
			if(!mulDayLoginGiftTbl.getRewardmap().containsKey(rewardIdx)){
				mulDayLoginGiftTbl.getRewardmap().put(rewardIdx, 0L);
			}
		}

		boolean isAvailable = false;
		if(mulDayLoginGiftTbl.getRewardmap().size()<confSize){
			isAvailable = true;
		}else{
			for(int idx=1;idx<=confSize;idx++){
				Long rewardVal = mulDayLoginGiftTbl.getRewardmap().get(idx);
				if(rewardVal!=null && rewardVal==0){
					isAvailable = true;
					break;
				}
			}
		}
		mulDayLoginGiftTbl.setLogintime(now);
		if(isAvailable){
			//领完了，不再发送协议
			SMulDayLogin msg = new SMulDayLogin();
			msg.logindays = mulDayLoginGiftTbl.getLogindays();
			msg.rewardmap.putAll(mulDayLoginGiftTbl.getRewardmap());
			mkdb.Procedure.psendWhileCommit(roleId, msg);
		}
	}
	
	public static boolean addItem(long roleId, int roleSchool, String itemIdStr, int itemNum, 
			int itemBind, List<ItemBaseBean> items, Map<String, Object> rewardParas){
		
		if(itemIdStr == null){
			return false;
		}
		
		int itemId = 0;
		int prof = 0;
		String [] itemProfStrs = itemIdStr.split(";");
		if(itemProfStrs.length == 1){
			String [] itemStrs = itemProfStrs[0].split(",");
			prof = Integer.valueOf(itemStrs[0]);
			if(prof == 0){
				itemId = Integer.valueOf(itemStrs[1]);
			}
		}else{
			for(String itemProfStr : itemProfStrs){
				String [] itemStrs = itemProfStr.split(",");
				prof = Integer.valueOf(itemStrs[0]);
				if(prof == roleSchool){
					itemId = Integer.valueOf(itemStrs[1]);
					break;
				}
			}
		}
		
		if (itemId != 0 && itemNum>0) {
			// 添加物品
			if(itemBind == 0){
				if (itemNum != BagUtil.addItem(roleId, itemId, itemNum, "MulDayLogin", 
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_lianxudenglu, itemId)){
						MessageMgr.sendMsgNotify(roleId, 144965 ,null);
						//logger.error("MulDayLogin reward error:添加物品失败1  roleid:" + roleId + " days:" + loginDays);
						return false;
					}
			}else{
				if (itemNum != BagUtil.addBindItem(roleId, itemId, itemNum, "MulDayLogin", 
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_lianxudenglu, itemId,true)){
					MessageMgr.sendMsgNotify(roleId, 144965 ,null);
					//logger.error("MulDayLogin reward error:添加物品失败1  roleid:" + roleId + " days:" + loginDays);
					return false;
				}
			}
			MessageUtil.psendAddItemWhileCommit(roleId, itemId, itemNum);
			rewardParas.put(RemoteLogParam.ITEM,itemId);
			rewardParas.put(RemoteLogParam.ITEMNUM,itemNum );
			//运营日志
			ItemBaseBean logItemBaseBean = new ItemBaseBean(itemId,itemNum);
			items.add(logItemBaseBean);
		}
		
		return true;
	}
	
}
