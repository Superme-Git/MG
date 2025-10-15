package fire.pb.clan;

import java.util.ArrayList;
import java.util.List;

import fire.pb.clan.srv.ClanManage;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PAddClanMoney extends Procedure {
	private final long roleid;
	private final int money;
	private final boolean showMsg;
	private final String des;
	
	public PAddClanMoney(final long roleid, final int money, final boolean showMsg, final String des) {
		this.roleid = roleid;
		this.money = money;
		this.showMsg = showMsg;
		this.des = des;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
		if (clanInfo == null) {
			return false;
		}
		
		if (money < 0) {
			if (clanInfo.getMoney() < -money) {
				return false;
			}
		}
		
		boolean ok = ClanManage.AddClanMoney(money, clanInfo); //调用统一接口 by changhao
		if (!ok)
		{
			return false;
		}
		if (showMsg) {
			final List<String> para = new ArrayList<String>();
			para.add(String.valueOf(money));
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 160157, para);
		}
		
		ClanManage.logger.info("roleId "+roleid+"\t获得公会资金\t"+des+"\t"+money);
		return true;
	}
}
