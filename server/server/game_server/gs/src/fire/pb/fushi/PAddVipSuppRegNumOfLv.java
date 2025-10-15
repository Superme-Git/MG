package fire.pb.fushi;

import java.util.Calendar;

import fire.pb.activity.reg.PAddSuppRegNumProc;
import fire.pb.activity.reg.RegAssistant;
import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;

public class PAddVipSuppRegNumOfLv extends Procedure {
	long roleid;
	int oldLevel;
	private RegAssistant regAssistant;
	
	public PAddVipSuppRegNumOfLv(long roleid, int oldLevel, RegAssistant assistant) {
		this.roleid = roleid;
		this.oldLevel = oldLevel;
		this.regAssistant = assistant;
	}
	
	public PAddVipSuppRegNumOfLv(long roleid, int oldLevel) {
		this(roleid, oldLevel, (RegAssistant) SingleContainer
				.getInstance(RegAssistant.class));
	}
	
	@Override
	protected boolean process() throws Exception {
		
		if (roleid<=0)
			return false;
		
		xbean.Vipinfo vipinfo = xtable.Vipinfo.get(roleid);
		if (null == vipinfo) {
			vipinfo = xbean.Pod.newVipinfo();
			xtable.Vipinfo.insert(roleid, vipinfo);
		}

		int vipLevel = vipinfo.getViplevel();
		fire.pb.fushi.SVipInfoConfig vipInfoCfg = Module.getVipInfoConfig(vipLevel);
		if(vipInfoCfg == null){
			return false;
		}
		
		xbean.RegRec record = regAssistant.getRecord(roleid);
		Calendar calendar = Calendar.getInstance();
		int month = regAssistant.getMonthOfYear(calendar);
		
		int curSuppSignNum = 0;
		// 没有该月的记录
		if (!record.getMonthmap().containsKey(month)) {
			//logger.error("PAddVipSuppRegNum error:没有该月的记录  roleid:" + roleId + " month:" + month);
			curSuppSignNum = 0;
		}else{
			curSuppSignNum = record.getMonthmap().get(month).getSuppregnum();
			if(curSuppSignNum < 0){
				//logger.error("PAddVipSuppRegNum error:增加补签次数异常  roleid:" + roleid + " suppSignNum:" + suppSignNum);
				curSuppSignNum = 0;
			}
		}
		int oldSuppRegNum = fire.pb.fushi.Module.getVipTableRightOfLevel(SSendVipInfo.RIGHT_SUPPREG_COUNT,oldLevel);
		int adjustSuppRegNum = oldSuppRegNum - curSuppSignNum;
		if(adjustSuppRegNum < 0){
			//logger.error("PAddVipSuppRegNum error:增加补签次数异常  roleid:" + roleid + " suppSignNum:" + suppSignNum);
			adjustSuppRegNum = 0;
		}
		int newSuppRegNum = fire.pb.fushi.Module.getVipTableRight(roleid,SSendVipInfo.RIGHT_SUPPREG_COUNT);
		
		pexecuteWhileCommit(new PAddSuppRegNumProc(roleid,newSuppRegNum - adjustSuppRegNum));

		return true;
	}
}

