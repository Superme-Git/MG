package fire.pb.gm;

public class GM_opentnshinfo extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "打开天女散花提示信息";
	}

}
