package fire.pb.team;

import java.util.ArrayList;
import java.util.List;

//把队员拉到队长地图里 by changhao
public class PGotoTeamLeader extends mkdb.Procedure
{
	private final Team team;
	
	private final long memberroleid;
	
	private final int delay;
	
	
	public PGotoTeamLeader(Team team, long memberroleid, int delay)
	{
		this.team = team;
		this.memberroleid = memberroleid;
		this.delay = delay;
	}
		
	@Override
	protected boolean process() throws Exception
	{
		int ret = TeamManager.getInstance().execGotoLeader(memberroleid, team, true, delay);
		if (ret == 0)
		{
			xbean.Properties p = xtable.Properties.select(memberroleid);
			
			List<String> params = new ArrayList<String>();
			params.add(p.getRolename());
			
			fire.pb.talk.MessageMgr.sendMsgNotify(team.getTeamLeaderId(), 160196 ,params);	
		}
		
		return true;
	}	
}
