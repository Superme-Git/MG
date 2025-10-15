package fire.pb.gm;

public class GM_enterline extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		final long roleid = getGmroleid();
		final int taskid = Integer.parseInt(args[0]);
		new fire.pb.mission.instance.line.PReqLineTask(roleid, taskid)
				.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
