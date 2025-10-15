package fire.pb.gm;

import fire.pb.clan.ClanUtils;

public class GM_setfb extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 3) {
			sendToGM("参数错误：" + usage());
			return false;
		}
		
		final int clanIndex;
		final int fubenid;
		final int state;

		clanIndex = Integer.valueOf(args[0]);
		fubenid = Integer.valueOf(args[1]);
		state = Integer.valueOf(args[2]);
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoByIndex(clanIndex);
				if (null == clanInfo)
					return false;
				
				xbean.ClanInfo clanInfoEdit = xtable.Clans.get(clanInfo.getKey());
				if (null == clanInfoEdit)
					return false;

				xbean.ClanInstances clanInst = xtable.Claninstances.get(clanInfo.getKey());
				if (clanInst == null)
					return false;
				
				clanInfoEdit.setClaninstservice(0);
				clanInst.getInststate().put(fubenid, state);
				sendToGM("公会:" + clanInfo.getClanname() + ",公会id:" + clanInfo.getIndex() +
						",公会副本:" + fubenid + ",重置状态为:" + state);
				
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//setfb [clanIndex] [fubenid] [state]";
	}

}
