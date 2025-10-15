package fire.pb.gm;

import mkdb.Procedure;

public class GM_debugoff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleId = getGmroleid();
		final int debugtype;
		if (args[0].equals("all")) {
			debugtype = 0;
		} else {
			debugtype = Integer.valueOf(args[0]);
		}
		Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (debugtype == 0) {
					xtable.Debugflag.remove(roleId);
					sendToGM("清除所有调试状态");
				}

				xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
				if (debugflag != null)
					debugflag.getDebugs().remove((Integer) debugtype);
				sendToGM("清除调试状态：" + debugtype);
				return true;
			}
		};
		procedure.submit();

		return true;
	}

	@Override
	String usage() {
		return "1 = 战斗脚本； 2 = 战斗动作时间 ; all = 关闭所有";
	}
}
