package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.master.SNotifyMaster;
import fire.pb.title.Title;
import fire.pb.util.DateValidate;

/**
 * 弟子罢免师傅
 * @author nobody
 */
public class PDisMissMasterProc extends mkdb.Procedure {
	
	private long prenticeRoleId;
	
	public PDisMissMasterProc(long  rid){
		this.prenticeRoleId = rid;
	}

	@Override
	protected boolean process() throws Exception {
		
		xbean.MenstorAndApprent temp = xtable.Menstor.select(prenticeRoleId);
		if(temp == null || temp.getFlag() != 1 || temp.getMenstorid() <= 0){
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146403 , null);
			return false;
		}
		long masterId = temp.getMenstorid();
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			return false;
		}
		List<Long> members = new ArrayList<Long>();
		members.add(masterId);
		members.add(prenticeRoleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, members));
		
		//检查师傅 和徒弟的状态
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null || master.getFlag() != 2){
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146403 , null);
			return false;
		}
		xbean.MenstorAndApprent apprent = xtable.Menstor.get(prenticeRoleId);
		
		xbean.CurrApprent currApprent = null;
		int index = -1;
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent curr = menstor.getCurrapprentices().get(i);
			if(curr.getApprentinfo().getRoleid() == prenticeRoleId){
				currApprent = curr;
				index = i;
				break;
			}
			
		}
		if(currApprent == null){
			return false;
		}
		
		boolean force = false;
		xbean.Properties prop =  xtable.Properties.select(masterId);
		if(prop == null){
			return false;
		}
		long currTime = System.currentTimeMillis();
		long lastOffLineTime = prop.getOfflinetime();
		long lastOnlineTime = prop.getOnlinetime();
		if(lastOffLineTime > lastOnlineTime){
			int offLineDays = DateValidate.getDaysBetween1(currTime,lastOffLineTime);
			if(offLineDays <7){
				force = true;
			}
		}else{
			force = true;
		}
		
		//1.处理徒弟相关数据
		if(currApprent.getTitleid() > 0){
			Title title = new Title(prenticeRoleId, false);
			title.removeTitle(currApprent.getTitleid());
		}
		apprent.getAchievement().clear();
		apprent.setEqipmentscore(0);
		apprent.setMenstorid(0);
		apprent.setFlag(0);
		if(force){
			apprent.setBashiflag(2);
			apprent.setBashitime(currTime);
		}else{
			apprent.setBashiflag(1);
		}
		
		//2.处理师傅相关数据
		menstor.getCurrapprentices().remove(index);
		if(menstor.getCurrapprentices().size() > 0){
			//先注释废弃称谓，过后删除
			//xdb.Procedure.pexecuteWhileCommit(new PExchangeAllApprentsTitleProc(masterId, prop.getRolename()));
		}else{
			//如果师傅只有这一个徒弟 还罢免了， 那么这个人就要收回师傅的称谓 和资格
			if(menstor.getApprentices().size() == 0){
				//先注释废弃称谓，过后删除
				/*if(master.getMenstortitleid() > 0){
					Title title = new Title(masterId, false);
					title.removeTitle(master.getMenstortitleid());
				}*/
				
				master.setFlag(0);
				master.setMenstortitleid(0);
				mkdb.Procedure.psendWhileCommit(masterId, new SNotifyMaster());
			}
		}
		
		List<String> parms = new ArrayList<String>();
		parms.add(prop.getRolename());
		fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146405 , parms);
		
		
		xbean.Properties prop1 =  xtable.Properties.select(prenticeRoleId);
		if(prop1 != null){
			List<String> parms1 = new ArrayList<String>();
			parms1.add(prop1.getRolename());
			fire.pb.talk.MessageMgr.sendSystemMessageToRole(masterId, 146442, parms1);		
		}
		
		mkdb.Procedure.psendWhileCommit(prenticeRoleId, new SNotifyMaster());
		return true;
	}
}
