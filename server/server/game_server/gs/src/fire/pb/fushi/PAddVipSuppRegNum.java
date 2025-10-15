package fire.pb.fushi;

import fire.pb.activity.reg.PAddSuppRegNumProc;
import mkdb.Procedure;

public class PAddVipSuppRegNum extends Procedure {
	long roleid;
	
	public PAddVipSuppRegNum(long roleid) {
		this.roleid = roleid;
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
		
		int suppRegNum = fire.pb.fushi.Module.getVipTableRight(roleid,SSendVipInfo.RIGHT_SUPPREG_COUNT);
		
		pexecuteWhileCommit(new PAddSuppRegNumProc(roleid,suppRegNum));

		return true;
	}
}

