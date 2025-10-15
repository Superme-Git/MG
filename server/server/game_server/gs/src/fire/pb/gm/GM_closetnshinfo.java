package fire.pb.gm;

public class GM_closetnshinfo extends GMCommand {

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
		return "关闭天女散花提示信息";
	}

}
