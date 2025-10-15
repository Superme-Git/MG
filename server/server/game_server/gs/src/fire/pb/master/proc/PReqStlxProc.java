package fire.pb.master.proc;

import fire.pb.master.Achive;
import fire.pb.master.PBaseInfo;
import fire.pb.master.PInfo;
import fire.pb.master.SPrenticesList;

/**
 * 请求师徒连心信息
 * @author nobody
 */
public class PReqStlxProc extends mkdb.Procedure{
	
	private long masterId;
	
	public PReqStlxProc(long rid){
		this.masterId = rid;
	}

	@Override
	protected boolean process() throws Exception {
		
		SPrenticesList send = new SPrenticesList();
		xbean.MenstorInfo menstor = xtable.Apprents.select(masterId);
		xbean.MenstorAndApprent master = xtable.Menstor.select(masterId);
		if(menstor == null || master == null){
			return false;
		}
		
		if(master.getFlag() != 2){
			return false;
		}
		
		send.shide = master.getShidezhi();
		for(xbean.HasApprent hasApp : menstor.getApprentices()){
			send.chushilist.add(toApprent(hasApp));
		}
		
		for(xbean.CurrApprent curr : menstor.getCurrapprentices()){
			PInfo pin = new PInfo();
			pin.prentice = toApprent(curr.getApprentinfo());
			for(int key : curr.getAchievement().keySet()){
				xbean.ApprentceChieve ac = curr.getAchievement().get(key);
				Achive temp = new Achive();
				temp.currnumber = ac.getCurrnumber();
				temp.totalnum = ac.getTotal();
				temp.flag = ac.getFlag();
				pin.achivemap.put(key, temp);
			}
			send.prenticelist.add(pin);
		}
		
		mkdb.Procedure.psendWhileCommit(masterId, send);
		return true;
	}
	
	
	private PBaseInfo toApprent(xbean.HasApprent hasApp){
		PBaseInfo pb = new PBaseInfo();
		pb.camp =  hasApp.getCamp();
		pb.roleid = hasApp.getRoleid();
		pb.rolename = hasApp.getName();
		pb.level = hasApp.getLevel();
		pb.school = hasApp.getSchool();	
		pb.shap = hasApp.getShap();
		return pb;
	}
	
	
	
}
