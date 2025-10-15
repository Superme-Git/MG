package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.FushiConst;
import fire.pb.item.Pack;
import fire.pb.master.MasterManager;
import fire.pb.master.STakeAchiveFresh;
import fire.pb.talk.MessageMgr;

/**
 * 请求领取某项奖励
 * @author nobody
 */
public class PReqTakeAwardProc extends mkdb.Procedure{
	
	private long masterId;
	private long apprenceId;
	private int key;
	
	public PReqTakeAwardProc(long masterId, long appId, int key){
		this.masterId = masterId;
		this.apprenceId = appId;
		this.key = key;
	}

	@Override
	protected boolean process() throws Exception {
		PropRole prole = new PropRole(masterId, true);
		if(key == MasterManager.LEVEL_60_KEY){
			return false;
		}
		
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146403 , null);
			return false;
		}
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null || master.getFlag() != 2){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146403 , null);
			return false;
		}
		
		xbean.CurrApprent currApprent = null;
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent curr = menstor.getCurrapprentices().get(i);
			if(curr.getApprentinfo().getRoleid() == apprenceId){
				currApprent = curr;
				break;
			}
			
		}
		if(currApprent == null){
			return false;
		}
		
		//成就尚未完成 或者已经领取奖励了
		xbean.ApprentceChieve achive = currApprent.getAchievement().get(key);
		if(achive == null || achive.getCurrnumber() < achive.getTotal()){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146403 , null);
			return false;
		}
		if( achive.getFlag() == 2){
			MessageMgr.sendMsgNotify(masterId, 144865 , null);
			return false;
		}
		
		int shidezhi = 1;
		if(shidezhi <= 0){
			return false;
		}
		
		master.setShidezhi(shidezhi+ master.getShidezhi());
		MasterManager.logger.info("key="+key + "   masterId="+masterId + "   apprenceId="+apprenceId+ "    shidezhi="+shidezhi +" content="+achive.getContent());
		
		if(key == MasterManager.VIP_KEY && achive.getContent() == 3){
			Pack bag = new Pack(masterId, false);
//			给师傅一个vip3的特殊月卡
			if(bag.doAddItem(37580, 1, 0, 0, "累积充值活动奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, 37580) != 1){
				MessageMgr.sendMsgNotify(masterId, 140655, null);
				return false;
			}
		}
				
		if(key == MasterManager.CASH_KEY && achive.getContent() > 0){
			double ratio = MasterManager.getInstance().getReturnRatio();
			int realReturn = (int)ratio*achive.getContent();
			if(realReturn > 0){
				if(!new PAddFuShi(prole.getUserid(), masterId, realReturn, FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_chongzhifanli).call()){
					MasterManager.logger.info("Exception : key="+key + "   masterId="+masterId + "   apprenceId="+apprenceId+ "    shidezhi="+shidezhi +" realReturn="+realReturn);
					return false;
				}
			}
		}
		achive.setFlag(2);
		STakeAchiveFresh send = new STakeAchiveFresh();
		send.roleid = apprenceId;
		send.key = key;
		send.flag = 2;
		mkdb.Procedure.psendWhileCommit(masterId, send);
		
		List<String> parms = new ArrayList<String>();
		parms.add(shidezhi+"");
		MessageMgr.sendMsgNotify(masterId, 146401 , parms);
		return true;
	}
	
	
	
}
