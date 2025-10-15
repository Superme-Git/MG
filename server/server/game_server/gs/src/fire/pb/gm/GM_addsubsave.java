package fire.pb.gm;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;

public class GM_addsubsave extends GMCommand {
	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数错误：" + usage());
			return false;
		}
		final int saveId = Integer.valueOf(args[0]);
		final int subsaveId = Integer.valueOf(args[1]);
		int count = 1;
		if (args.length == 3)
			count = Integer.valueOf(args[2]);
		final int subsavecount = count;
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				InstanceZone instzone = InstanceZoneFactory
						.getRoleCurInstanceZone(getGmroleid(), false);
				instzone.updateSubSave(saveId, subsaveId, subsavecount);
				return true;
			};
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "//addsubsave [saveid] [subsaveid]";
	}
}
