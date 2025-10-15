package fire.pb.battle.specialevent;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.PNewBattle;
import fire.pb.map.Npc;
import fire.pb.map.SMineArea;
import fire.pb.map.SceneNpcManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PFortyThievesBattle extends mkdb.Procedure
{
	
	public final static int FORTY_THIEVES_NPC_KEY = 10254;

	final private long roleId;
	
	final private long npckey;
	
	final private int areaconf;
	
	public PFortyThievesBattle(long roleId, long npckey,int areaconf)
	{
		this.roleId = roleId;
		this.npckey = npckey;
		this.areaconf = areaconf;
	}
	
	@Override
	protected boolean process()
	{
		if(areaconf == 0)
			return true;//没有暗雷区域配置
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int npcId = npc.getNpcID();
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId == null)
		{
			//没有队伍
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,141485, npcId, null);
			return true;
		}
		Team team = TeamManager.getTeamByTeamID(teamId);
		if(team.getNormalMemberIds().size()<3)
		{
			//队伍人数小于3人
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 141485, npcId, null);
			return true;
		}
		
		SMineArea areaInfo = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.SMineArea.class).get(areaconf);
		if(null == areaInfo)
			return true;//暗雷区域配置

		int battleConfId = 9101;
		battleConfId = battleConfId + (areaInfo.level - 1)/10 ;
		
		for(long tmRoleId : team.getNormalMemberIds())
		{
			int rolelevel = xtable.Properties.selectLevel(tmRoleId);
			if(rolelevel < areaInfo.level)
			{
				List<String> strs = new ArrayList<String>();
				strs.add(String.valueOf(areaInfo.level));
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 141486,FORTY_THIEVES_NPC_KEY, strs);
				return true;
			}
		}
		
		PNewBattle newbattle = new PNewBattle(roleId,battleConfId,new FortyThievesBattleHandler(npckey));
		newbattle.setAreaConf(areaconf);
		newbattle.setAreaType(xbean.BattleInfo.AREA_BATTLEHIDE);
		pexecuteWhileCommit(newbattle);
		
		return true;
	}
}
