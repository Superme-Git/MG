package fire.pb.gm;

import java.util.HashSet;
import java.util.Set;

import fire.pb.SBindTel;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.TelBindUtils;

public class GM_cancelforbiddeal extends GMCommand {

	@Override
	boolean exec(String[] args) {
		// 取消禁止部分玩家进行交易
		Set<Long> roleIdSet = new HashSet<>();
		if (args.length >= 1) {
			for (int i = 0; i < args.length; i++)
				roleIdSet.add(Long.parseLong(args[i].trim()));
			setForUnBindDeal(roleIdSet);
		} else {
			// 取消所有玩家进行交易
			TelBindUtils.setUnBindTelAgain();
		}
		return true;
	}

	/**
	 * @author yangtao
	 * @dateTime 2016年8月30日 下午4:13:33
	 * @version 1.0
	 * @param roleid
	 */
	private void setForUnBindDeal(Set<Long> roleidList) {
		mkdb.Procedure unForbiddeal = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				for (Long roleid : roleidList) {
					xbean.Properties prop = xtable.Properties.get(roleid);
					if (prop != null) {
						prop.setIsbindtelagain(0);
						SBindTel sGetBindTel = new SBindTel();
						sGetBindTel.status = 1;
						sGetBindTel.bindteltime=prop.getBindteltime();
						gnet.link.Onlines.getInstance().send(roleid, sGetBindTel);
						MessageMgr.psendSystemMessageToRole(roleid, 191033, null);
					}
				}
				return true;
			}
		};
		unForbiddeal.submit();
	}

	@Override
	String usage() {
		return "取消封禁交易必须绑定手机//cancelforbiddeal $userid$";
	}
}
