package fire.pb.school.shouxi;

import java.util.List;

import fire.pb.PropRole;
import fire.pb.game.MoneyType;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 投票某个职业领袖 by changhao
 * @author Administrator
 *
 */
public class PVoteCandidate extends Procedure {
	private final long roleid;
	private final long candidateid;
	private final long npckey;

	public PVoteCandidate(long roleid, long candidateid,long npckey) {
		this.roleid = roleid;
		this.candidateid = candidateid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception
	{
		PropRole role = new PropRole(roleid, false);
		PropRole candidateRole = new PropRole(candidateid, true);
		
		//只能投本职业的 by changhao
		if (role.getSchool() != candidateRole.getSchool())
		{
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 190073, null);
			return false;
		}
		
		//扣除职业贡献度20 by changhao
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		long ret = bag.subCurrency(-20, MoneyType.MoneyType_ProfContribute, "elector", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_campaign, 0);
		if (ret == 0)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 170008, null);
			return false;
		}
		
		//记录投票时间 by changhao
		xbean.ProfessionLeaderVoteInfo voteInfo = xtable.Professionleadervotechallenge.get(roleid);
		if (voteInfo == null)
		{
			voteInfo = xbean.Pod.newProfessionLeaderVoteInfo();
			xtable.Professionleadervotechallenge.insert(roleid, voteInfo);
		}
		
		voteInfo.setVotetime(System.currentTimeMillis());
		int ticket = 1;
		
		 xbean.ProfessionLeaderCand candidates = xtable.Professionleadercandidates.select(candidateRole.getSchool());
		 List<Long> candIDs = candidates.getCandidatelist();
	     xbean.ProfessionLeaderTicketInfo candInfo = null;
	     for (long element : candIDs)
	     {
	    	if(element == candidateid)
			{
	    		candInfo = xtable.Roleid2professionleadertickets.get(element);
				break;
			}
		 }
	        
		if (candInfo == null)
			return false;
		
		int iniTicket = candInfo.getTickets();
		candInfo.setTickets(iniTicket + ticket);

		fire.pb.school.SVoteCandidate ssc = new fire.pb.school.SVoteCandidate();
		fire.pb.school.CandidateInfo info = new fire.pb.school.CandidateInfo();
		info.roleid = candInfo.getRoleid();
		info.tickets = candInfo.getTickets();
		info.words = candInfo.getWords();
		String name = xtable.Properties.selectRolename(info.roleid);
		info.rolename = name;
		ssc.candidatelist.add(info);
		ssc.alreadyvote = 1;
		ssc.shouxikey = npckey;
		psendWhileCommit(roleid, ssc);
		MessageMgr.psendMsgNotifyWhileCommit(roleid, 190079, null);
		return true;
	}	
}
