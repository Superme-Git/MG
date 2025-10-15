package fire.pb;

import java.util.ArrayList;
import java.util.List;

import fire.log.beans.OpLvlBean;
import fire.pb.event.LevelupEvent;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

/**
 * @author Administrator
 * 用来处理突破任务完成后的经验升级
 */

public class PLevelBreakUpProc extends Procedure {
	private final long roleid;
	
	public PLevelBreakUpProc(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		final xbean.Properties prop = xtable.Properties.get(roleid);
		if (null == prop)
			return false;
		final int rolelv = prop.getLevel();
		
		final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
		
		int BfLvl=prop.getLevel();
		long BfExp=prop.getExp();
		
		// 自动升级并且自动加点儿
		List<LevelupEvent> eventList = new ArrayList<LevelupEvent>();
		boolean islevelup = false;//是否升级标志
		
		fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(roleid,	false);
		while (prop.getExp() >= erole.getNextExp()) {
			if (prop.getLevel() >= PLevelUpProc.getMaxLevel()) {
				break;
			}
			
			if (!new PLevelUpProc(prop.getUserid(), roleid).call()) {
				fire.log.LogManager.logger.error("自动升级出现问题!");
				return false;
			}
			
			eventList.add(new fire.pb.event.LevelupEvent(roleid, prop.getLevel()));
			islevelup=true;
		}
		
		if (eventList.size() > 0) {
			for (LevelupEvent event : eventList) {
				//fire.pb.event.Poster.getPoster().dispatchEvent(event);
				mkdb.Procedure.pexecuteWhileCommit(
						new mkdb.Procedure() {
							@Override
							protected boolean process() {
								TeamManager.getTeamByRoleId(roleid);//强行锁队伍
								fire.pb.event.Poster.getPoster().dispatchEvent(event);
								return true;
							}
					} );
			}
		}
		
		if (islevelup) {
			int AfLvl = prop.getLevel();
			long AfExp = prop.getExp();
			fire.log.YYLogger.OpLvlLog(roleid, new OpLvlBean(
					OpLvlBean.Op_LvlBean_LvlType_Game, BfLvl,
					AfLvl, BfExp, AfExp, PAddExpProc.OTHER_QUEST));
		}
		
		// 通知客户端刷新经验
		mkdb.Procedure.psendWhileCommit(roleid, new SRefreshUserExp(prop.getExp()));
		if (send.datas.size() > 0)
			Procedure.psendWhileCommit(roleid, send);
		
		prop.getTupotips().put(rolelv, 2); //任务已经完成

		return true;
	}
	
}
