package fire.pb.gm;

import fire.pb.activity.impexam.ImpExamManager;

public class GM_keju extends GMCommand {

	@Override
	boolean exec(String[] args) {
		ImpExamManager.test = true;
		sendToGM("智力试练设置成测试模式");
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
