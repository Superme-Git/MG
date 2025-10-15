package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.master.MasterManager;
import fire.pb.master.PrenticeData;
import fire.pb.master.SDismissApprentces;
import fire.pb.master.SNotifyMaster;
import fire.pb.title.Title;
import fire.pb.util.DateValidate;

/**
 * 师傅正式罢免某个徒弟
 * @author nobody
 */
public class PDisMissApprentAfterProc extends mkdb.Procedure{

	private long masterId;
	private long apprenceId;
	
	public PDisMissApprentAfterProc(long mid, long aid){
		this.masterId = mid;
		this.apprenceId = aid;
	}

	@Override
	protected boolean process() throws Exception {
		
		PropRole masterRole = new PropRole(masterId, true);
		
		int useId = masterRole.getUserid();
		List<Integer> userList = new ArrayList<Integer>();
		userList.add(useId);
		lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userList));
		
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146397 , null);
			return false;
		}
		
		List<Long> members = new ArrayList<Long>();
		members.add(masterId);
		members.add(apprenceId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, members));
		
		//检查师傅 和徒弟的状态
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null || master.getFlag() != 2){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146397 , null);
			return false;
		}
		xbean.MenstorAndApprent apprent = xtable.Menstor.get(apprenceId);
		if(apprent == null || apprent.getFlag() != 1 || apprent.getMenstorid() != masterId){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146397 , null);
			return false;
		}
		
		xbean.CurrApprent currApprent = null;
		int index = -1;
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent curr = menstor.getCurrapprentices().get(i);
			if(curr.getApprentinfo().getRoleid() == apprenceId){
				currApprent = curr;
				index = i;
				break;
			}
			
		}
		if(currApprent == null){
			return false;
		}
		
		long currTime = System.currentTimeMillis();
		boolean free = false;
		xbean.Properties prop =  xtable.Properties.select(apprenceId);
		if(prop != null){
			long lastOffLineTime = prop.getOfflinetime();
			long lastOnlineTime = prop.getOnlinetime();
			if(lastOffLineTime > lastOnlineTime){
				int offLineDays = DateValidate.getDaysBetween1(currTime,lastOffLineTime);
				if(offLineDays >=7){
					free = true;
				}
			}
		}
		
		if(!free){
			if(!FushiManager.subFushiFromUser(useId, masterId, MasterManager.DISMISS_APPENTCE_FUSHI,0,0,FushiConst.REASON_SUB_DISMISS_APPRENCE,YYLoggerTuJingEnum.NO_USED, false)){
				fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146456 , null);
				return false;
			}
		}
		
		//1.处理徒弟相关数据
		if(currApprent.getTitleid() > 0){
			Title title = new Title(apprenceId, false);
			title.removeTitle(currApprent.getTitleid());
		}
		apprent.getAchievement().clear();
		apprent.setEqipmentscore(0);
		apprent.setMenstorid(0);
		apprent.setFlag(0);
		
		//2.处理师傅相关数据
		menstor.getCurrapprentices().remove(index);
		if(menstor.getCurrapprentices().size() > 0){
			mkdb.Procedure.pexecuteWhileCommit(new PExchangeAllApprentsTitleProc(masterId, masterRole.getName()));
		}else{
			//如果师傅只有这一个徒弟 还罢免了， 那么这个人就要收回师傅的称谓 和资格
			if(menstor.getApprentices().size() == 0){
				if(master.getMenstortitleid() > 0){
					Title title = new Title(masterId, false);
					title.removeTitle(master.getMenstortitleid());
				}
				
				master.setFlag(0);
				master.setMenstortitleid(0);
				mkdb.Procedure.psendWhileCommit(masterId, new SNotifyMaster());
			}
		}
		
		
		//3.通知师傅当前的徒弟列表信息
		SDismissApprentces send = new SDismissApprentces();
		for(xbean.CurrApprent curr : menstor.getCurrapprentices()){
			long rid = curr.getApprentinfo().getRoleid();
			PrenticeData pd = new PrenticeData();
			pd.roleid = rid;
			pd.rolename = curr.getApprentinfo().getName();
			
			xbean.Properties temp =  xtable.Properties.select(rid);
			if(temp != null){
				long lastOffLineTime = temp.getOfflinetime();
				long lastOnlineTime = temp.getOnlinetime();
				if(lastOffLineTime > lastOnlineTime){
					int offLineDays = DateValidate.getDaysBetween1(currTime,lastOffLineTime);
					if(offLineDays >=7){
						pd.flag = 1;
					}
				}
			}
			send.prenticelist.add(pd);
		}
		mkdb.Procedure.psendWhileCommit(masterId, send);
		
		if(prop != null){
			List<String> parms = new ArrayList<String>();
			parms.add(prop.getRolename());
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146404 , parms);
		}
		xbean.Properties prop1 =  xtable.Properties.select(masterId);
		if(prop1 != null){
			List<String> parms1 = new ArrayList<String>();
			parms1.add(prop1.getRolename());
			fire.pb.talk.MessageMgr.sendSystemMessageToRole(apprenceId, 146443, parms1);
		}
		mkdb.Procedure.psendWhileCommit(apprenceId, new SNotifyMaster());
		return true;
	}
}
