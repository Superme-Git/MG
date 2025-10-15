package fire.pb.team;

import fire.pb.PropRole;
import fire.pb.huoban.PUpdateHuoBanZhenRong;
import fire.pb.huoban.SChangeZhenrong;

/***
 * 角色上限
 * @author changhao
 *
 */
public class PRoleOnline extends mkdb.Procedure
{
	
	private long roleId;

	public PRoleOnline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process(){
		
		PropRole prole = new PropRole(roleId, true);
		//发送自己当前默认光环
		SSetMyFormation sSetMyFormation = new SSetMyFormation();
		sSetMyFormation.formation = prole.getDealutFormId();
		sSetMyFormation.entersend = 1;
		mkdb.Procedure.psendWhileCommit(roleId, sSetMyFormation);
		
		//如果有跟随npc，则发送
		int follownpc = fire.pb.mission.UtilHelper.getFollowid(roleId);
		if(follownpc > 0) {
			fire.pb.mission.SNpcFollowStart snpcfollow = new fire.pb.mission.SNpcFollowStart();
			snpcfollow.npcid = follownpc;
			mkdb.Procedure.psendWhileCommit(roleId, snpcfollow);
		}
		
		//伙伴阵容更新 TODO
		xbean.HuoBanZhenrong hbzhenrongs = xtable.Huobanzhenrongs.select(roleId);
		if (hbzhenrongs == null) {
			xbean.HuoBanColumn huobancol = xtable.Huobancolumns.select(roleId);
			if (huobancol != null) {
				mkdb.Procedure.pexecute(
						new PUpdateHuoBanZhenRong(this.roleId, 
								new PropRole(this.roleId, true).getDealutFormId(), 
								1,
								huobancol.getFighthuobans(), 
								PUpdateHuoBanZhenRong.BOTH));
			}
		}
		else {
			SChangeZhenrong snd = new SChangeZhenrong();
			xbean.HuoBanZhenrongInfo info = hbzhenrongs.getZhenrong().get(hbzhenrongs.getCurrent());
			if(info == null){
				info = xbean.Pod.newHuoBanZhenrongInfo();
				info.setZhenfa(new PropRole(this.roleId, true).getDealutFormId());
				hbzhenrongs.getZhenrong().put(hbzhenrongs.getCurrent(), info);
			}
			snd.zhenrong = hbzhenrongs.getCurrent();
			snd.zhenfa = info.getZhenfa();
			snd.huobanlist.addAll(info.getHuoban());
			snd.reason = 1;
			mkdb.Procedure.psendWhileCommit(roleId, snd);
			
		}
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId == null)
			return true;
		try
		{
			Team team = new Team(teamId, false);
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIds()));//锁所有队员
			if (team.isInTeam(roleId))
				team.roleOnline(roleId);
			
			// 捉鬼任务状态的处理
			new fire.pb.circletask.catchit.PCatchItRoleOnlineProc(roleId).call();
		}
		catch(Exception e)
		{
			TeamManager.logger.error(e);
		}
		return true;
	}
}
