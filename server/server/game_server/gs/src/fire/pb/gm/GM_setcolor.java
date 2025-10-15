package fire.pb.gm;

import fire.msp.move.GRoleEquipChange;
import fire.pb.GsClient;
import fire.pb.item.EquipItemType;

public class GM_setcolor extends GMCommand {
	@Override
	boolean exec(String[] args) {
		try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					xbean.Properties prop = xtable.Properties
							.get(getGmroleid());
					if (prop == null) {
						return false;
					}
					prop.setRolecolor1(102);
					prop.setRolecolor2(202);
					GRoleEquipChange notifymap = new GRoleEquipChange();
					notifymap.roleid = getGmroleid();
					notifymap.pos = EquipItemType.ROLE_COLOR1;
					notifymap.itemid = 102;
					GsClient.pSendWhileCommit(notifymap);
					return true;
				}
			}.submit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
