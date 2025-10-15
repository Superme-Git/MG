
package fire.pb.school.shouxi;

import fire.pb.PropRole;
import fire.pb.map.SceneNpcManager;
import fire.pb.school.School;
import mkdb.Procedure;

public class PMyElector extends Procedure {

	private final long roleid;

	private final long npckey;

	public PMyElector(long roleid, long npckey) {

		this.roleid = roleid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {

		PropRole propRole = new PropRole(roleid, true);
		int school = propRole.getSchool();
		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		School npcSchool = School.getSchoolByNpcid(npcid);
		// 门派不对,直接返回
		if (npcSchool == null || npcSchool.getValue() != school)
			return false;
		
		
		//还没有参加竞选 不能修改宣言 by changhao
		xbean.ProfessionLeaderTicketInfo c = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
		if (c == null)
		{
			return false;
		}

		fire.pb.school.SMyElector ssc=new fire.pb.school.SMyElector();
		xbean.ProfessionLeaderCand shouxiCand=xtable.Professionleadercandidates.select(school);
		
		if(shouxiCand!=null)
		{
			java.util.List<Long> cans=	shouxiCand.getCandidatelist();
			for (long candID : cans)
			{
				xbean.ProfessionLeaderTicketInfo candInfo=xtable.Roleid2professionleadertickets.select(candID);
			
				if (candID == roleid)
				{
					ssc.electorwords = candInfo.getWords();
					break;
				}
			}
		}
		
		mkdb.Procedure.psendWhileCommit(roleid, ssc);
		
		return true;
	}

}
