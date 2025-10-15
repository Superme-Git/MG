package fire.pb.school.shouxi;

import fire.pb.PropRole;
import fire.pb.battle.Monster;
import fire.pb.game.MoneyType;
import fire.pb.school.School;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 处理竞选宣言 by changhao
 */
public class PSendElectorWords extends Procedure {
	private final long roleid;
	private final String electorwords;
	private final boolean modifyelectwords;

	public PSendElectorWords(long roleid, String electorwords, boolean modify)
	{
		this.roleid = roleid;
		this.electorwords = electorwords;
		this.modifyelectwords = modify;
	}

	@Override
	protected boolean process() throws Exception
	{
		//屏蔽字处理 处理 by changhao
		if (!fire.pb.util.StringValidateUtil.checkIllegalWord(electorwords))
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142261, null);
			return false;
		}

		if (modifyelectwords == false)
		{
			//扣除职业贡献度100 by changhao
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.subCurrency(-100, MoneyType.MoneyType_ProfContribute, "elector", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_campaign, 0);
			if (ret == 0)
			{
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 170006, null);
				return false;
			}
		}

		PropRole propRole = new PropRole(roleid, true);

		int school = propRole.getSchool();
		xbean.ProfessionLeaderCand candidates = xtable.Professionleadercandidates.get(school);
		xbean.ProfessionLeaderTicketInfo candInfo = xtable.Roleid2professionleadertickets.get(roleid);
		if (candidates == null)
		{
			candidates = xbean.Pod.newProfessionLeaderCand();
			xtable.Professionleadercandidates.insert(school, candidates);
		}

		if (candInfo == null)
		{
			candInfo = xbean.Pod.newProfessionLeaderTicketInfo();
			xtable.Roleid2professionleadertickets.insert(roleid, candInfo);
		}
		
		if (modifyelectwords == false)
		{
			candInfo.setTickets(0);
		}
		
		candInfo.setWords(electorwords);
		candInfo.setRoleid(roleid);
		
		if (modifyelectwords == false)
		{
			candidates.getCandidatelist().add(roleid);
			//把竞选者的能力存下来 by changhao
			xbean.Monster monsterBean = null;
			monsterBean= Monster.copyMonsterFromRoleOrPet(roleid, School.getBattleNpc(School.getSchoolBySchoolid(school)), true, 0);
			if (xtable.Professionleaderfightdata.get(roleid) != null)
			{
				xtable.Professionleaderfightdata.remove(roleid);
			}
		
			xtable.Professionleaderfightdata.insert(roleid, monsterBean);
			
			//竞选成功 by changhao
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 190078, null);
		}
		else
		{
			//修改宣言成功 by changhao
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 190077, null);
		}
		
		return true;
	}

}
