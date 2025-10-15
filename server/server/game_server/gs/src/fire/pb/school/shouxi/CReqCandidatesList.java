
package fire.pb.school.shouxi;

import fire.pb.map.SceneNpcManager;
import fire.pb.school.School;
import fire.pb.school.ShouXiMsgID;

public class CReqCandidatesList {

	private final long roleid;

	private final long npckey;

	public CReqCandidatesList(long roleid, long npckey) {

		super();
		this.roleid = roleid;
		this.npckey = npckey;
	}

	public void process() {

		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		fire.pb.PropRole propRole = new fire.pb.PropRole(roleid, true);
		int school = propRole.getSchool();
		School npcSchool = School.getSchoolByNpcid(npcid);
		//门派不对,直接返回
		if (npcSchool == null || npcSchool.getValue() != school)
			return;
		if (!fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().checkCanVote(System.currentTimeMillis())) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ShouXiMsgID.Cantvote,npcid, null);
			return;
		}

		// 发送最新候选人消息给客户端
		fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().refreshCandidateswithSP(roleid, school,npckey);

	}
}
