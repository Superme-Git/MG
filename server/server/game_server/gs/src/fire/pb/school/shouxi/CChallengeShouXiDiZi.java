package fire.pb.school.shouxi;

import fire.pb.map.SceneNpcManager;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.school.Shouxi;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/***
 * 挑战职业领袖 by changhao
 *
 */
public class CChallengeShouXiDiZi {
  private final long roleid;
  private final long npckey;
	public CChallengeShouXiDiZi(long roleid, long npckey) {
		this.roleid=roleid;
		this.npckey=npckey;
	}
	public void process()
	{
		xbean.Properties prop=xtable.Properties.select(roleid);
		int npcid=SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		if(!ProfessionLeaderManager.getInstance().isInChallengePeriod(System.currentTimeMillis()))
		{
			MessageMgr.sendMsgNotify(roleid, 170010,npcid, null);
			return;
		}
		
		if (prop.getLevel()<SchoolConst.CHALLENGE_MINLEVEL)
		{
			MessageMgr.sendMsgNotify(roleid, 190080,npcid, null);
			return;
		}
		
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if(team!=null&&(!team.isAbsentMember(roleid)))
		{
			MessageMgr.sendMsgNotify(roleid, 190081, npcid,null);
			return;
		}
		
	    Shouxi sx = Shouxi.getShouxiByNpcid(npcid);
	    School school=School.getSchoolByNpcid(npcid);
	    if(sx==null||school==null||school.getValue()!=prop.getSchool())
	    {
	    	MessageMgr.sendMsgNotify(roleid, 190083 ,null);
	    	return;
	    }

		//不能挑战自己 by changhao
		xbean.ProfessionLeaderInfo shouxiInfo=xtable.Professionleader.select(school.getValue());
		if(shouxiInfo!=null&&shouxiInfo.getRoleid()==roleid)
		{
	    	MessageMgr.sendMsgNotify(roleid, 190075 ,null);
			return;
		}
		
		new PChallengeShouxi(roleid,npckey,npcid).submit();
	}

}
