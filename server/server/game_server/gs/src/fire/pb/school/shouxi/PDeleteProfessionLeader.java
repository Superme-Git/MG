package fire.pb.school.shouxi;


import java.util.List;

import fire.pb.title.Title;
import mkdb.Procedure;


/***
 * 删除职业领袖 by changhao
 *
 */
public class PDeleteProfessionLeader extends Procedure
{
	public long roleid;
	public int oldschool;

	public PDeleteProfessionLeader(long roleid, int oldschool) {
		this.roleid = roleid;
		this.oldschool = oldschool;
	}

	@Override
	protected boolean process() throws Exception
	{
		//删除候选人，领袖 by changhao
		xbean.ProfessionLeaderCand candidates = xtable.Professionleadercandidates.get(oldschool);
		if(candidates != null)
		{	
			List<Long> cans = candidates.getCandidatelist();
			for (long candID : cans)
			{
				if (candID == roleid)
				{
					candidates.getCandidatelist().remove(Long.valueOf(roleid));
				}
			}
		}
		
		xtable.Roleid2professionleadertickets.remove(roleid);
		
		xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.get(oldschool);
		if (sxi != null)
		{
			if (sxi.getRoleid() == roleid)
			{
				xtable.Professionleader.remove(oldschool);
				
				//删除职业领袖称谓 by changhao
				fire.pb.school.School s = fire.pb.school.School.getSchoolBySchoolid(oldschool);
				int titleID = fire.pb.school.School.getShouxiTitleid(s);
				Title title = new Title(roleid, false);
				if (title.roleHaveTitle(titleID))
				{
					Title.removeTitle(roleid, titleID);
				}
			}
		}
		
		return true;
	}
}
