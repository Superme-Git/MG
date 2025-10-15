package fire.pb.npc;

import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.game.Snpcjianglifafang;
import fire.pb.main.ConfigManager;
import fire.pb.util.DateValidate;

public class PNpcAwardProc extends mkdb.Procedure{
	
	private long roleId;
	private int serviceId;
	
	public PNpcAwardProc(long roleId, int serviceId){
		this.roleId = roleId;
		this.serviceId = serviceId;
	}
	
	public boolean process(){
		
		xbean.Properties prop = xtable.Properties.get(this.roleId);
		if(prop == null) return false;
		
		Map<Integer, Snpcjianglifafang> npcawards = ConfigManager.getInstance().getConf(Snpcjianglifafang.class);
		if(npcawards == null) return false;
		Snpcjianglifafang cfg = npcawards.get(this.serviceId);
		if(cfg == null) return false;
		if(prop.getLevel() < cfg.lvxianzhi) return false;
		
		xbean.npcawardrecords records = xtable.Role2npcawards.get(this.roleId);
		if(records == null){
			records = xbean.Pod.newnpcawardrecords();
			xtable.Role2npcawards.insert(this.roleId, records);
		}
		xbean.npcaward award = records.getRecords().get(cfg.jiangliid);
		long now = System.currentTimeMillis();
		if(award == null){
			award = xbean.Pod.newnpcaward();
			award.setLasttime(now);
			records.getRecords().put(cfg.jiangliid, award);
		}
		boolean theSameDay = DateValidate.inTheSameDay(now, award.getLasttime());
		if(!theSameDay){
			//不是同一天次数清0
			award.setCount(0);
		}
		if(award.getCount()<cfg.jianglicishu){
			award.setCount(award.getCount()+1);
			award.setLasttime(now);
		}else{
			//WUPANLIANG 提示消息
			return false;
		}
		award.setTotalcount(award.getCount()+1);
		RewardMgr.getInstance().distributeAllAward(roleId, cfg.jiangliid, null, 
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_npcjiangli, 0, PAddExpProc.OTHER, "NPC奖励",true);
		
		return true;
	}
	
	
	public static boolean containedByNpcAward(int serviceId){
		Map<Integer, Snpcjianglifafang> npcawards = ConfigManager.getInstance().getConf(Snpcjianglifafang.class);
		if(npcawards == null) return false;
		return npcawards.containsKey(serviceId);
	}
}
