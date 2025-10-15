package fire.pb.circletask;

import java.util.Arrays;

import fire.pb.map.SceneNpcManager;
import fire.pb.mission.Module;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PSubmitCircleTask extends mkdb.Procedure{
	private int questid;
	private long roleid;
	private fire.pb.map.Npc npc = null;
	private java.util.ArrayList<fire.pb.npc.SubmitUnit> things = new java.util.ArrayList<fire.pb.npc.SubmitUnit>();
	
	public PSubmitCircleTask(int questid, long roleid, long npckey, java.util.ArrayList<fire.pb.npc.SubmitUnit> things ){
		this.questid = questid;
		this.roleid = roleid;
		
		if (things != null)
			this.things = things;
		
		npc = SceneNpcManager.selectNpcByKey(npckey);
	}
	
	public boolean process(){
		
		/*
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean single = true;
			Team team = TeamManager.getInstance().selectTeamByRoleId(roleid);
			if (team != null)
			{
				if (team.getTeamInfo().getMembers().size() > 0)
				{
					single = false;
				}
			}
			
			if (single == true) //一个人就不让完成 by changhao
			{
				boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
				if (ok == false)
				{
					Module.logger.info("PSubmitCircleTask:角色[" + roleid + "]提交循环任务失败-nodaypay:" + questid);
					return false;
				}				
			}
		}
		*/	
		
		Long teamid = xtable.Roleid2teamid.select(roleid);
		if(teamid != null) //先上team锁，防止死锁
			lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, Arrays.asList(teamid)));
		final CircleTask sq = new CircleTask(roleid, false);
		if (sq.getQuestMap() == null)
			return false;
		
		if (things.isEmpty()){
			if (sq.submit(questid, npc)){
				return true;
			}
			else
				return false;
		}
		else{
			if (sq.submitItemPet(questid, npc, things)){
				return true;
			}
			else
				return false;
		}
	}
}
