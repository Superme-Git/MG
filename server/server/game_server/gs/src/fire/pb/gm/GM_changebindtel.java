package fire.pb.gm;

import fire.pb.tel.utils.TelBindUtils;

/**
 * 更换绑定手机号绑定
 * 
 * @author yangtao
 * @dateTime 2016年8月18日 下午1:46:48
 * @version 1.0
 */
public class GM_changebindtel extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
		}
		final long roleId = Long.parseLong(args[0]);
		final long tel = Long.parseLong(args[1]);
		TelBindUtils.changeBindTelByGM(roleId,tel);
		return true;
	}

	@Override
	String usage() {
		return "//changebindtel $roleId$ $tel$";
	}
}
