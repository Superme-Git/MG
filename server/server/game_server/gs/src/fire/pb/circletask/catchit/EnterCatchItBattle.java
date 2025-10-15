package fire.pb.circletask.catchit;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mkdb.Transaction;
import fire.pb.PropRole;
import fire.pb.battle.BattleConfig;
import fire.pb.battle.BattleField;
import fire.pb.battle.PNewBattle;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;

public class EnterCatchItBattle {
	private final long roleid;
	private final long npckey;
	private final int npcid;
	private final int ctquestid;
	
	public static Logger logger = Logger.getLogger("TASK");
	
	public EnterCatchItBattle(final long roleid, final long npckey, final int npcid, final int ctquestid){
		this.roleid = roleid;
		this.npckey = npckey;
		this.npcid = npcid;
		this.ctquestid = ctquestid;
	}
	
	public void enterBattle(){
		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		
		xbean.CircleTaskMap questMap = xtable.Rolecircletask.select(roleid);
		if (null == questMap) {
			return;
		}
		xbean.CircleTaskInfo questinfo = questMap.getTaskmap().get(ctquestid);
		if (null == questinfo) {
			return;
		}
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(questinfo.getId());
		if(ct == null) {
			return;
		}
		
		if (ct.teamnum > 0) {
			fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
			if (null == team || !team.isTeamLeader(roleid)){
				return;
			}
			
			// 全队正常状态成员 加锁
			final java.util.List<Long> members = team.getNormalMemberIds();
			if (members.size() < ct.teamnum) {
				STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(140277, npcid, null);
				gnet.link.Onlines.getInstance().send(roleid, notify);
				return;
			}
			
			//判断有无等级不足的队员
			List<String> levelApplyNames = new ArrayList<String>(5);
			for (Long mem : team.getNormalMemberIds()) {
				PropRole memberRole = new PropRole(mem, true);
				if(memberRole.getLevel() < ct.levelmin) {
					levelApplyNames.add(memberRole.getName());
				}
			}
			
			//有人等级不足
			if (levelApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelApplyNames) {
					if (nIndex != (levelApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				
				List<String> paras = new ArrayList<String>(2);
				paras.add(strParam.toString());
				paras.add(String.valueOf(ct.levelmin));
				for (Long mem : team.getNormalMemberIds()) {
					STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(150523, 0, paras);
					gnet.link.Onlines.getInstance().send(mem, notify);
				}
				
				return;
			}
		}
		
		int battleCfgId = questinfo.getDstitemid();
		if (battleCfgId <= 0) {
			return;
		}
		
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battleCfgId);
		if (battleConfig == null || battleConfig.monsterRands.size() == 0)
		{
			BattleField.logger.error("无法通过EnterCatchItBattle的战斗配置id获得参与战斗的怪物，BattleConfigId：" + battleCfgId
					+" roleid ="+ roleid +" npckey ="+ npckey+" npcid ="+ npcid+" ctquestid ="+ ctquestid);
			return;
		}
		
		CatchItTaskEndHandler handler = new CatchItTaskEndHandler(npckey, roleid, ctquestid, ct.teamnum, ct.teamshare);
		if (Transaction.current() != null) {
//			new PNewBattle(roleid, battleCfgId, handler).call();
			PNewBattle battle = new PNewBattle(roleid, battleCfgId, BattleField.GUEST_FIRST_ID, questinfo.getNpcname(), handler);
			battle.call();
		}
		else {
//			new PNewBattle(roleid, battleCfgId, handler).submit();
			PNewBattle battle = new PNewBattle(roleid, battleCfgId, BattleField.GUEST_FIRST_ID, questinfo.getNpcname(), handler);
			battle.submit();
		}
			
		logger.debug("玩家   " + prole.getName() + "进入大萌任务");
	}

}
