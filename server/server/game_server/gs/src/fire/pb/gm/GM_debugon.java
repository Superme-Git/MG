package fire.pb.gm;

import mkdb.Procedure;

public class GM_debugon extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleId = getGmroleid();
		final int debugtype = Integer.valueOf(args[0]);
		Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
				if (debugflag == null) {
					debugflag = xbean.Pod.newDebugFlag();
					xtable.Debugflag.insert(roleId, debugflag);
				}
				debugflag.getDebugs().add(debugtype);
				sendToGM("进入调试状态：" + debugtype);
				return true;
			}
		};
		procedure.submit();

		return true;
	}

	@Override
	String usage() {
		return "1 = 战斗脚本； 2 = 战斗动作时间";
	}
}
