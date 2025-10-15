package fire.pb.school.shouxi;

import fire.pb.map.SceneNpcManager;
import mkdb.Procedure;

/***
 * 发送候选人数据 by changhao
 */
public class PSendCandidateList extends Procedure {
	private final long roleid;
	private final long npckey;

	public PSendCandidateList(long roleid, long npckey) {
		this.roleid = roleid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {
		
		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		fire.pb.PropRole propRole = new fire.pb.PropRole(roleid, true);
		int school = propRole.getSchool();
		fire.pb.school.School npcSchool = fire.pb.school.School.getSchoolByNpcid(npcid);

		if (npcSchool == null || npcSchool.getValue() != school)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190073, npcid, null);
			
			return false;
		}
		
		if (false)//!fire.pb.school.shouxi.ShouxiManager.getInstance().checkCanVote(System.currentTimeMillis()))
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190076, npcid, null);
			return false;
		}

		fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().refreshCandidateswithSP(roleid, school,npckey);
		
		return true;
	}

}
