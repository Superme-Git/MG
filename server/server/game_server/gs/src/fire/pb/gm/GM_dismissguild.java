package fire.pb.gm;

import fire.pb.clan.srv.ClanBaseManager;

public class GM_dismissguild extends GMCommand {
	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.select(getGmroleid());
				if (prop == null || prop.getClankey() <= 0) {
					sendToGM("解散公会失败,原因:没有加入任何公会");
					return true;
				}

				xbean.ClanInfo clanInfo = xtable.Clans.get(prop.getClankey());
				if (clanInfo == null) {
					sendToGM("解散公会失败,原因:没有加入任何公会");
					return true;
				}
				ClanBaseManager.getInstance().dismissClan(clanInfo);
				return true;
			}

		}.submit();
		sendToGM("解散公会成功");
		return true;
	}

	@Override
	String usage() {
		return "//dismissguild";
	}

}
