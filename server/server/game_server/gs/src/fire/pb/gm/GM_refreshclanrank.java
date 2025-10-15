package fire.pb.gm;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import fire.pb.clan.Module;
import fire.pb.clan.srv.ClanRank;

public class GM_refreshclanrank extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Collection<xbean.ClanInfo> clanInfoList =  Module.clanListMap.values();
				int curtimes=100;//初始设置延迟100毫秒处理,每次100毫秒递增
				for (xbean.ClanInfo clan : clanInfoList) {
					if(clan!=null){
						if(clan.getClanlevel()<=5){
							curtimes=curtimes+100;
							mkdb.Executor.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									new ClanRank(clan.toData()).submit();
//									mkdb.Procedure.pexecuteWhileCommit(new ClanRank(clan.toData()));
								}
							}, curtimes, TimeUnit.MILLISECONDS);
						}
					}
				}
				sendToGM("处理合服后公会等级排行榜显示数量bug");
				sendToGM("公会等级排行榜已经更新，只是处理5级以下包括5级");
				return true;
			}
			
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
