package fire.pb.gm;

import fire.pb.clan.srv.ClanManage;

public class GM_addfactionmoney extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final long factionMoney = Long.parseLong(args[0]);
		final long roleid = getGmroleid();

		mkdb.Procedure addFactionMoney = new mkdb.Procedure() {
			@Override
			public boolean process() {
				Long factionid = xtable.Roleidclan.select(roleid);
				if (factionid == null) {
					return false;
				}
				xbean.ClanInfo clan = xtable.Clans.get(factionid);
				if (clan == null) {
					return false;
				}

				ClanManage.AddClanMoney((int) factionMoney, clan);

				return true;
			}
		};

		addFactionMoney.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
