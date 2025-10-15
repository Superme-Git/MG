
package fire.pb.school.shouxi;

import fire.pb.map.SceneNpcManager;
import fire.pb.school.SCanElect;
import fire.pb.school.School;
import gnet.link.Onlines;

public class CCheckCanElect {

	private final long roleid;

	private final long npckey;

	public CCheckCanElect(long roleid, long npckey) {

		this.roleid = roleid;
		this.npckey = npckey;
	}

	public void process() {

		fire.pb.PropRole propRole = new fire.pb.PropRole(roleid, true);
		final int school = propRole.getSchool();
		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		School npcSchool = School.getSchoolByNpcid(npcid);
		// 门派不同,直接返回 by changhao
		if (npcSchool == null || school != npcSchool.getValue())
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 170010, npcid, null);
			return;
		}
		
		if (!ProfessionLeaderManager.getInstance().isInChallengePeriod(System.currentTimeMillis()))
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 141983, npcid, null);
			return;
		}
		xbean.ProfessionLeaderVoteInfo voteInfo = xtable.Professionleadervotechallenge.select(roleid);
		//还没挑战成功 by changhao
		
		boolean b = false;
		if (voteInfo != null)
		{
			b = ProfessionLeaderManager.getInstance().isInChallengePeriod(voteInfo.getChallengetime());
		}
		
		xbean.ProfessionLeaderInfo shouxiInfo=xtable.Professionleader.select(school);
		if(shouxiInfo != null && shouxiInfo.getRoleid() == roleid)
		{
			b = true;
		}
		
		if (b == false)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 170009, npcid, null);
			return;
		}
		
		if (!ProfessionLeaderManager.getInstance().isInChallengePeriod(voteInfo.getChallengetime()))
		{
			//return;
		}
		// 如果已参加竞选
		xbean.ProfessionLeaderTicketInfo candInfo = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
		if (candInfo != null)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190082,npcid, null);
			return;
		}
		
		Onlines.getInstance().send(roleid, new SCanElect(npckey));
	}

}
