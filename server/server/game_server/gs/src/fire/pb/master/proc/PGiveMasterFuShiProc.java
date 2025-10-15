package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.FushiConst;
import fire.pb.master.MasterManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;

/**
 * 出师的徒弟充值，师傅获得符石
 * @author nobody
 */
public class PGiveMasterFuShiProc extends mkdb.Procedure{
	
	private final long masterId;
	private String apprentIdName;
	private final long fushi;
	
	public PGiveMasterFuShiProc(long masterId, String name, long fushi){
		this.masterId = masterId;
		this.apprentIdName = name;
		this.fushi = fushi;
	}

	@Override
	protected boolean process() throws Exception {
		PropRole prole = new PropRole(masterId, true);
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null){
			return false;
		}
		if(master.getFlag() != 2){
			return false;
		}
		
		long curr = System.currentTimeMillis();
		if(!DateValidate.inTheSameDay(curr, master.getLastgivefushitime())){
			master.setFushi(0);
		}
		long currFuShi = Math.min(200, master.getFushi());
		
		if(currFuShi >= 200){
			MessageMgr.psendSystemMessageToRole(masterId, 146421 , null);
			return false;
		}
		
		long maxGive = 200 - currFuShi;
		long ext = (long)(fushi * 0.5);
		long add = 0;
		
		if(ext >maxGive){
			add = maxGive;
		}else{
			add = ext;
		}
		
		if(!new PAddFuShi(prole.getUserid(), masterId, (int)add, FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_shifu).call()){
			MasterManager.logger.info("PGiveMasterFuShiProc:Exception:" + "   masterId="+masterId + "   add="+add);
			return false;
		}
		
		master.setFushi(master.getFushi() + add);
		master.setLastgivefushitime(curr);
		
		List<String> parms = new ArrayList<String>();
		parms.add(apprentIdName);
		parms.add(add+"");
		
		MessageMgr.psendSystemMessageToRole(masterId, 146420, parms);
		
		return true;
	}
}
