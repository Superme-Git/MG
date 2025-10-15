package fire.pb.circletask;

import java.util.HashMap;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.SchoolMaster;
import mkdb.Procedure;

public class PAutoAddCircleTask extends Procedure {
	private final long roleid;
	private final int circletype;
	private final int questid;
	private static Map<Integer, Long> schoolMasterMap = new HashMap<Integer, Long>();
	private final boolean isCheckGiveup;
	private final boolean isForce;
	public PAutoAddCircleTask(long roleid) {
		super();
		this.roleid = roleid;
		this.circletype = 1010000; //暂时默认自动加师门循环类型
		this.questid = 0;
		this.isCheckGiveup = false;
		this.isForce = false;
	}
	
	public PAutoAddCircleTask(long roleid, int circletype) {
		super();
		this.roleid = roleid;
		this.circletype = circletype;
		this.questid = 0;
		this.isCheckGiveup = false;
		this.isForce = false;
	}
	
	public PAutoAddCircleTask(long roleid, int circletype, int questid) {
		super();
		this.roleid = roleid;
		this.circletype = circletype;
		this.questid = questid;
		this.isCheckGiveup = false;
		this.isForce = false;
	}
	
	public PAutoAddCircleTask(long roleid, int circletype, int questid, boolean isCheckGiveup) {
		super();
		this.roleid = roleid;
		this.circletype = circletype;
		this.questid = questid;
		this.isCheckGiveup = isCheckGiveup;
		this.isForce = false;
	}
	
	public PAutoAddCircleTask(long roleid, int circletype, int questid, boolean isCheckGiveup, boolean isforce) {
		super();
		this.roleid = roleid;
		this.circletype = circletype;
		this.questid = questid;
		this.isCheckGiveup = isCheckGiveup;
		this.isForce = isforce;
	}

	@Override
	protected boolean process() throws Exception {
		CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + circletype + ",强制:" + isForce + ",AutoAddCircTask!");
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
		if (quest_map != null){
			xbean.CircleTaskInfo schoolquestinfo = quest_map.getTaskmap().get(circletype);
			if (schoolquestinfo != null){
				if(isForce){
					schoolquestinfo.setQueststate(0);
				}
				if (schoolquestinfo.getQueststate() == SpecialQuestState.DONE || schoolquestinfo.getQueststate() == SpecialQuestState.UNDONE){
					CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + schoolquestinfo.getQuestid() + "状态是DONE或者UNDONE ！");
					return true;
				}
				else {
					if(!isForce){
						long now = System.currentTimeMillis();
						if (fire.pb.util.DateValidate.inTheSameDay(schoolquestinfo.getTakequesttime(),now)) {
							int dayMax = CircleTaskManager.getInstance().getCTCircleMax(roleid, circletype);
							if (schoolquestinfo.getSumnum() >= dayMax) {
								if (circletype == 1060000) {
									fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
								}
								CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + schoolquestinfo.getQuestid() + "达到最大次数！");
								return true;
							}
						} else {
						}
					}
				}
				
				if(!isForce){
					if(isCheckGiveup && schoolquestinfo.getLastgiveuptime() > 0) {
						CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + schoolquestinfo.getQuestid() + "上次放弃了，不自动接取了！");
						return true;
					}
				}
			}
		}
		final PropRole prole = new PropRole(roleid, true);
	    Long npckey = getRoleMastaerKey(roleid, prole.getSchool());
	    if (npckey == null){
	    	CircleTaskManager.logger.error("角色[" + roleid + "]任务" + circletype + "获取师门师傅npckey出错！");
	    	return false;
	    }
	    SchoolMaster sm = CircleTaskManager.getInstance().getMasterMap().get(prole.getSchool());
	    if (sm == null){
	    	CircleTaskManager.logger.error("角色[" + roleid + "]任务" + circletype + "获取师门师傅配置结构出错！!");
	    	return false;
	    }
	    CircleTaskManager.logger.info("角色[" + roleid + "]任务" + circletype + ",准备接去循环任务!questid:" + questid);
	    if(questid > 0) {
	    	if(!isForce)
	    		pexecuteWhileCommit(new PAcceptCircTask(roleid, npckey, sm.getMasterid(), circletype, questid));
	    	else
	    		pexecuteWhileCommit(new PAcceptCircTask(roleid, npckey, sm.getMasterid(), circletype, questid, true));
	    } else {
	    	if(!isForce)
	    		pexecuteWhileCommit(new PAcceptCircTask(roleid, npckey, sm.getMasterid(), circletype, false));
	    	else
	    		pexecuteWhileCommit(new PAcceptCircTask(roleid, npckey, sm.getMasterid(), circletype, questid, true));
	    }
	    return true;
	}
	
	private Long getRoleMastaerKey(long roleid,int school) {
		SchoolMaster sm = CircleTaskManager.getInstance().getMasterMap().get(school);
		Long npckey = schoolMasterMap.get(school);
		if (npckey != null)
			return npckey;
		if (sm == null)
			return null;
		final int npcid = sm.getMasterid();	
		for(fire.pb.map.Npc npc : SceneNpcManager.getInstance().getNpcs().values()) {
			if(npc.getNpcID() == npcid) {
				npckey = npc.getNpcKey();
				schoolMasterMap.put(school, npckey);
				break;
			}			
		}
		return npckey;
	}
	
}

