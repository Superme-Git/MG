package fire.pb.master.proc;

import fire.pb.master.MasterManager;


/**
 * 更新师徒连心任务
 * @author nobody
 */
public class PUpdateSTLXProc extends mkdb.Procedure {
	
	private int key;
	private int contentValue;
	private long roleId;
	private int extValue;
	
	public PUpdateSTLXProc(long roleId, int key, int value, int extValue){
		this.roleId = roleId;
		this.key = key;
		this.contentValue = value;
		this.extValue = extValue;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.MenstorAndApprent temp1 = xtable.Menstor.select(roleId);
		if(temp1 == null || temp1.getFlag() != 1){
			return false;
		}
		
		long masterId = temp1.getMenstorid();
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			return false;
		}
		
		xbean.CurrApprent currApp = null;
		for(xbean.CurrApprent temp : menstor.getCurrapprentices()){
			if(temp.getApprentinfo().getRoleid() == roleId){
				currApp = temp;
				break;
			}
		}
		if(currApp == null){
			return false;
		}
		
		xbean.ApprentceChieve aChieve = currApp.getAchievement().get(key);
		if(aChieve == null){
			aChieve = xbean.Pod.newApprentceChieve();
			if(!MasterManager.getInstance().initAchieve(key, aChieve)){
				return false;
			}
			currApp.getAchievement().put(key, aChieve);
		}
		
		if(aChieve.getCurrnumber() >= aChieve.getTotal()){
			return false;
		}
		
		aChieve.setCurrnumber(aChieve.getCurrnumber() + contentValue);
		aChieve.setContent(extValue);//这个数值主要是为记录 徒弟充值 返还给师傅的符石数目
		
		if(aChieve.getCurrnumber() >= aChieve.getTotal()){
			aChieve.setFlag(1);
			xbean.MenstorAndApprent apprence = xtable.Menstor.get(roleId);
			apprence.getAchievement().put(key, 1);
		}
		
		return true;
	}
}
