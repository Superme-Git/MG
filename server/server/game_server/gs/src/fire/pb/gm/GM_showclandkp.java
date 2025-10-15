package fire.pb.gm;

import fire.pb.clan.ClanUtils;

public class GM_showclandkp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int clanindex = Integer.parseInt(args[0]);

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo v = ClanUtils.getClanInfoByIndex(clanindex);
				if(v==null){
					sendToGM("公会不存在");
					return true;
				}
				sendToGM("上周公会dkp  " + v.getLastclandkp());
				sendToGM("本周公会dkp  " + v.getClandkp());
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
