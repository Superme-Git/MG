package fire.pb.gm;

import mkdb.util.UniqName;

public class GM_super extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				UniqName.release("superaward", "zhongdajiang");
				sendToGM("superaward 释放成功");
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
