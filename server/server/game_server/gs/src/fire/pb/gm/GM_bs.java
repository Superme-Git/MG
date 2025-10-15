package fire.pb.gm;

public class GM_bs extends GMCommand {

	@Override
	boolean exec(String[] args) {

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {

				long roleId = getGmroleid();
				xbean.MenstorAndApprent master = xtable.Menstor.get(roleId);
				if (master != null) {
					master.setBashiflag(1);
				}
				sendToGM("清除拜师24小时限制");
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
