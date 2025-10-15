package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.SceneManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.mission.treasuremap.PEnterEventNpcBattle;
import fire.pb.npc.SNpcShare;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PCreateTimerNpcByData extends Procedure {

	private final EventTimerGroupData timerData;
	private final int noticeId;
	private final long roleid;
	
	public PCreateTimerNpcByData(EventTimerGroupData timerData, int noticeId, long roleid) {
		this.timerData = timerData;
		this.noticeId = noticeId;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		PropRole pRole = new PropRole(roleid, true);
		Map<Integer, List<Integer>> npcMap = timerData.getNpcMap();
		List<Integer> mapIdList = new ArrayList<Integer>();
		
		for (Map.Entry<Integer, List<Integer>> entry : npcMap.entrySet()) {
			List<Integer> npcList = entry.getValue();
			int mapId = entry.getKey();
			mapIdList.add(mapId);
			
			StringBuilder sb = new StringBuilder();
			sb.append("事件Id:").append(timerData.id);
			sb.append(",活动类型Id:").append(timerData.actid);
			sb.append(",在地图:").append(mapId).append(",共刷出npc数量为:").append(npcList.size());
			TimerNpcService.logger.info(sb.toString());
			
			xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(timerData.id);
			if (npcInfo != null) {
				Integer npcCnt = npcInfo.getNpcinfo().size();
				if (npcCnt != null) {
					if (timerData.npcAllCount <= npcCnt)
						break;
				}
			}
			else {
				npcInfo = xbean.Pod.neweventNpcInfoCol();
				xtable.Eventnpcinfotable.insert(timerData.id, npcInfo);
			}
			
			
			for (Integer npcId : npcList) {
				Integer npcCnt2 = npcInfo.getNpcinfo().size();
				if (npcCnt2 != null) {
					if (timerData.npcAllCount <= npcCnt2)
						break;
				}
				else
					npcCnt2 = 0;
				
				SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
				String npcname = fire.pb.npc.NpcManager.getInstance().getNpcName(npc);
				if (npcname.length() == 0) {
					npcname = npc.getName();
					TimerNpcService.logger.error("大萌萌没有名字");
				}
				
				long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
				
				xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npcKey);
				if (eInfo == null) {
					eInfo = xbean.Pod.neweventNpcInfo();
				}
				eInfo.setNpcid(npcId);
				eInfo.setNpcstatus(0);
				eInfo.setBattleendtime(timerData.battletime);
				eInfo.setBattletime(timerData.battletime);
				eInfo.setCreatetime(System.currentTimeMillis());
				
				npcInfo.getNpcinfo().put(npcKey, eInfo);
				
				fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapId, 3, timerData.npcLastTime*1000);
				
				if (timerData.match == 1) {
					//创建定时器,匹配进入战斗的角色
					mkdb.Executor.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							new PEnterEventNpcBattle(npcId, npcKey).submit();
						}
					}, timerData.matchsec, TimeUnit.SECONDS);
				}
				
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append("生成事件触发NPC:").append(npcname).append(" 地图:").append(mapId);
				
				TimerNpcService.logger.info(strBuilder.toString());
			}
		}
		
		TimerNpcService.getInstance().npc2eventId.putAll(timerData.npc2eventId);
		
		List<String> param = new ArrayList<String>(2);
		//角色名
		param.add(pRole.getName());
		
		//地图名
		StringBuffer strParam = new StringBuffer();
		int nIndex = 0;
		for (Integer mapId : mapIdList) {
			MapConfig conf =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(mapId);
			if (conf != null) {
				if (nIndex != (mapIdList.size() - 1)) {
					strParam.append(conf.getMapName()).append(",");
				}
				else {
					strParam.append(conf.getMapName());
				}
				nIndex ++;
			}
		}
		param.add(strParam.toString());
		SceneManager.sendAll(MessageMgr.getMsgNotify(noticeId, 0, param));
		
		return true;
	}
	
	
}
