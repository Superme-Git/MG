package fire.pb.clan;

import mkdb.Procedure;

/**
 * 更新玩家是否有公会信息
 *
 */
public class PRefreshRoleClanKey extends Procedure {
	private final long roleid;
	
	public PRefreshRoleClanKey(final long roleid){
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		// TODO Auto-generated method stub
		long factionkey=-1;
		Long factionId = xtable.Roleidclan.select(roleid);
		if (factionId != null && factionId > 0) {
			factionkey=factionId;
		}
		String clanname = xtable.Clans.selectClanname(factionkey);
		if (null == clanname) {
			clanname="";
		}
		SRefreshRoleClan sRefreshRoleClan=new SRefreshRoleClan();
		sRefreshRoleClan.clankey=factionkey;
		sRefreshRoleClan.clanname=clanname;
		Procedure.psendWhileCommit(roleid, sRefreshRoleClan);
		return true;
	}
}
