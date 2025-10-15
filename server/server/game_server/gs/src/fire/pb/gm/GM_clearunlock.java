package fire.pb.gm;

public class GM_clearunlock extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final Long roleid = getGmroleid();
				fire.pb.lock.service.LockService.getInstance()
						.clearUnlockTime(roleid);
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "clearunlock";
	}

}
