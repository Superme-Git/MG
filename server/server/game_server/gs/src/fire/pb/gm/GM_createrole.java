package fire.pb.gm;

import fire.pb.main.ConfigManager;

public class GM_createrole extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length > 1) {
			sendToGM(usage());
			return false;
		}

		boolean isOpen = true;
		if (args.length == 1) {
			isOpen = args[0].equals("0") ? false : true;
		}

		if(isOpen != ConfigManager.isCanCreateRole) {
			ConfigManager.isCanCreateRole = isOpen;
			sendToGM((isOpen ? "开启" : "关闭") + "创建角色成功");
		}else{
			sendToGM("当前创建角色状态已经是" + (isOpen ? "开启" : "关闭") + "请不要重复设置!");
		}
		return true;
	}

	@Override
	String usage() {
		return "//createrole [0,1]";
	}
}
