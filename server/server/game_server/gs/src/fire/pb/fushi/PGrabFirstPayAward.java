
package fire.pb.fushi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import fire.log.Logger;
import fire.pb.game.ActivityAward;
import fire.pb.game.FirstPayGiftData;
import fire.pb.game.SFirstPayReward;
import fire.pb.game.SPointCardFirstPayReward;
import fire.pb.item.Pack;
import fire.pb.item.SDirectUseItem;
import fire.pb.main.ConfigManager;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;


public class PGrabFirstPayAward extends Procedure {

	public long roleId;

	private static Logger logger = Logger.getLogger("AWARD");
	
	public PGrabFirstPayAward(long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		
		//点卡服屏蔽首充礼包
		//if(fire.pb.fushi.Module.GetPayServiceType() == 1){
		//	return false;
		//}
		
        Integer usrId = xtable.Properties.selectUserid(roleId);
        xbean.FirstCharge firstPayData = xtable.Firstchargenew.get(usrId);
        if (firstPayData == null || firstPayData.getChargestatus() == 2){
        	logger.error("PGrabFirstPayAward failed. firstPayData is null or payStatus error. roleId:" + roleId);
        	return true;
        }
        
		final fire.pb.pet.Module petMod=(fire.pb.pet.Module)fire.pb.main.ModuleManager.getInstance().getModuleByName("pet");
		if(null == petMod){
			logger.error("PGrabFirstPayAward failed. petmodule is null roleid:" + roleId);
			return false;
		}
		
		fire.pb.pet.PetManager petMgr = petMod.getPetManager();
		if (null == petMgr){
			logger.error("PGrabFirstPayAward failed. petMgr is null roleid:" + roleId);
			return false;
		}
		
		Integer roleShape = xtable.Properties.selectShape(roleId);
		Integer rewardIdx = roleShape - 1;
        
		Map<Integer, ? extends SFirstPayReward> sFirstPayConf = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			sFirstPayConf = ConfigManager.getInstance().getConf(fire.pb.game.SFirstPayReward.class);
		} else {
			sFirstPayConf = ConfigManager.getInstance().getConf(fire.pb.game.SPointCardFirstPayReward.class);
		}
		
        int needItemNum = 0;
        int needPetNum = 0;
        //计算需要多少道具和宠物的空位
        for(SFirstPayReward fp : sFirstPayConf.values()){
        	FirstPayGiftData fpgd_ = fp.rewardmap.get(rewardIdx);
	    	if(fpgd_.itemid==0){
	    		needPetNum = needPetNum + fpgd_.petnum;
	    	}else{
	    		needItemNum++;
	    	}
        }
        
		Pack itemPack = new Pack(roleId, false);
		int emptyVolume = itemPack.getFreepos().size();
		if(emptyVolume < needItemNum){
			//道具栏已满
			MessageMgr.sendMsgNotify(roleId, 141095, Arrays.asList(String.valueOf(sFirstPayConf.size())));
			logger.error("PGrabFirstPayAward failed. itemCol is full! roleid:" + roleId + " emptyVolume:"+emptyVolume+" inum:"+needItemNum);
			return false;
		}
		
		PetColumn petPen = new PetColumn(roleId, PetColumnTypes.PET, false);
		int emptySize = petPen.getRemainSize();
		if(emptySize < needPetNum){
			//宠物栏已满
			MessageMgr.sendMsgNotify(roleId, 142377, null);
			logger.error("PGrabFirstPayAward failed. petPen is full! roleid:" + roleId + " remainsize:"+emptySize+" pnum:"+needPetNum);
			return false;
		}
        
	    firstPayData.setChargestatus(2);
	    for (SFirstPayReward fp : sFirstPayConf.values()) {
	    	FirstPayGiftData fpgd_ = fp.rewardmap.get(rewardIdx);
	    	if(fpgd_.itemid == 0){
	    		fire.pb.pet.PetAttr petAttr = petMgr.getAttr(fpgd_.petid);
	    		if (null == petAttr){
	    			logger.error("PGrabFirstPayAward, 增加宠物失败, roleid:" + roleId + "宠物配置表中 并没有此ID对应的宠物:" + fpgd_.petid);
	    			return false;
	    		}
	    		
	    		if(fpgd_.getPetisbind() == 0){
	    			petPen.addpet(fpgd_.petid, petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_CHARGE, PetColour.WHITE, false, null);
	    		}else{
	    			petPen.addpet(fpgd_.petid, petAttr.initlevel, petAttr.getKind(), null, PetColumn.ADD_REASON_CHARGE, PetColour.WHITE, true, null);
	    		}
	    		
	    		MessageMgr.psendMsgNotifyWhileCommit(roleId, 142378, MessageMgr.getStringList(petAttr.getName()));
	    		
	    		continue;
	    	}
	    	else {
	    		int realAdd = 0;
	    		
	    		if(fpgd_.getIsbind() == 0){
	    			realAdd = BagUtil.addItem(roleId, fpgd_.itemid, fpgd_.itemnum, "first charge", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, fpgd_.itemid);
	    		}else{
	    			realAdd = BagUtil.addBindItem(roleId, fpgd_.itemid, fpgd_.itemnum, "first charge", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, fpgd_.itemid,true);
	    		}
	    		
			    if (realAdd != fpgd_.itemnum){
			    	logger.error("PGrabFirstPayAward, roleid:" + roleId + "增加道具失败,itemid:" + fpgd_.itemid + " itemnum:" + fpgd_.itemnum);
			    	fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 142338, 0, null);
			    	return false;
			    }
			    NavigableMap<Integer, SDirectUseItem> sDircUseItem = ConfigManager.getInstance().getConf(SDirectUseItem.class);
				SDirectUseItem sUseItem = sDircUseItem.get(fpgd_.itemid);
				if (sUseItem == null) {
					MessageUtil.psendAddItemWhileCommit(roleId, fpgd_.itemid, fpgd_.itemnum);
				} 
	    	}
	    }
	    
	    xbean.RoleDoubleChareInfo mulPay = xtable.Roledoublecharge.select(roleId);
	    psendWhileCommit(roleId, new SRefreshChargeState(2,FushiManager.getDoubleChareState(mulPay, usrId)));
	    
	    logger.info("PGrabFirstPayAward, roleid:" + roleId + "领取首充礼包成功!");
	    
		return true;
	}
	
}

