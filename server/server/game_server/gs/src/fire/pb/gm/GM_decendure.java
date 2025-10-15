package fire.pb.gm;

import fire.pb.item.Equip;

public class GM_decendure extends GMCommand {
	@Override
	boolean exec(String[] args) {
		float fPct;
		if (args.length >= 1) {
			fPct = (float) (Integer.parseInt(args[0]) / 100.0f);
		} else {
			fPct = 0.5f;
		}

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Equip equip = new Equip(getGmroleid(), false);
				equip.GMDecEndure(fPct);
				return true;
			}
		}.submit();

		sendToGM("減少全身耐久度百分之" + (int)(100 - fPct * 100) + "成功");
		return true;
	}

	@Override
	String usage() {
		return "//decendure 50(百分比)";
	}

}
