package fire.pb.gm;

import fire.pb.cross.Constant;
import fire.pb.cross.CrossManager;

public class GM_cross extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleid = getGmroleid();
		if (args[0].equals("bc")) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					//TODO:
					CrossManager.getInstance().beginCross(roleid);
					return true;
				}
			}.submit();
			return true;
		} else if (args[0].equals("senddata")) {
			//TODO:
			dosomething(roleid,0);
			return true;
		}
		return true;
	}

	@Override
	String usage() {
		return "跨服相关操作测试";
	}
	
	private void dosomething(final long roleid, int actid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				//TODO:
				return true;
			}
		}.submit();
	}

}
