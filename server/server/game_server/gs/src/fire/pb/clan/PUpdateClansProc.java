package fire.pb.clan;

import java.util.Collection;

import fire.pb.clan.srv.ClanManage;

public class PUpdateClansProc extends mkdb.Procedure{
	
	public boolean process() {
		Collection<xbean.ClanInfo> clanInfoList =  Module.clanListMap.values();
		for (xbean.ClanInfo clan : clanInfoList) {
			pexecute(new PUpdateWeekClansProc(clan.getKey()));//更新处理公会周逻辑（其他处理公会周逻辑都在这个方法处理）
			pexecute(new PClanMaintain(clan.getKey()));//每日0点多维护公会 
		}
		return true;
	}
}
