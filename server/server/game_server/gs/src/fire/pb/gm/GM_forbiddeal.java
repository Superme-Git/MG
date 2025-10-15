package fire.pb.gm;

import java.util.HashSet;
import java.util.Set;

import fire.pb.SBindTelAgain;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.TelBindUtils;

public class GM_forbiddeal extends GMCommand {

	@Override
	boolean exec(String[] args) {
		// 禁止部分玩家进行交易
		Set<Long> roleIdSet = new HashSet<>();
		if (args.length >= 1) {
			for (int i = 0; i < args.length; i++)
				roleIdSet.add(Long.parseLong(args[i].trim()));
			setForBindDeal(roleIdSet);
		} else {
			// 禁止所有玩家进行交易
			TelBindUtils.setBindTelAgain();
		}
		return true;
	}

	/**
	 * @author yangtao
	 * @dateTime 2016年8月30日 下午4:13:33
	 * @version 1.0
	 * @param roleid
	 */
	private void setForBindDeal(Set<Long> roleidList) {
		mkdb.Procedure forbiddeal = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				for (Long roleid : roleidList) {
					xbean.Properties prop = xtable.Properties.get(roleid);
					if (prop != null) {
						prop.setIsbindtelagain(1);
						SBindTelAgain sBindTelAgain = new SBindTelAgain();
						gnet.link.Onlines.getInstance().send(roleid, sBindTelAgain);
						MessageMgr.psendSystemMessageToRole(roleid, 191021,null);
					}
				}
				return true;
			}
		};
		forbiddeal.submit();
	}

	@Override
	String usage() {
		return "封禁交易必须绑定手机//forbiddeal $userid$";
	}
}
