package fire.pb.gm;

import java.util.ArrayList;

import fire.pb.huoban.HuoBan;
import fire.pb.huoban.HuoBanColumn;
import fire.pb.huoban.PUpdateHuoBanZhenRong;

public class GM_addjclienthb extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(
						getGmroleid(), false);
				java.util.ArrayList<Integer> members = new ArrayList<Integer>();

				for (HuoBan huobanInfo : huobancol.getAllHuoBans().values()) {
					if (members.size() >= 4) {
						break;
					}

					if (huobanInfo.getHuoBanState() > 0) {
						members.add(huobanInfo.getId());
					}
				}

				if (members.size() > 4)
					return false;

				boolean suc = huobancol.setFightHuoBans(members);
				if (suc) {
					mkdb.Procedure.pexecute(new PUpdateHuoBanZhenRong(
							getGmroleid(), 0, 0, members,
							PUpdateHuoBanZhenRong.HUOBAN));
				}
				return suc;
			};
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
