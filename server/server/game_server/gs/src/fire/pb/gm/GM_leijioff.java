package fire.pb.gm;

import java.util.Calendar;

import fire.pb.state.MulDayLogin;

public class GM_leijioff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleId = getGmroleid();
		final int days = Integer.valueOf(args[0]);
		new mkdb.Procedure() {
			public boolean process() {
				xbean.Properties prop = xtable.Properties.select(roleId);
				long lastOfflineTime = prop.getOfflinetime();

				long now = System.currentTimeMillis();
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, days);
				now = cal.getTimeInMillis();
				MulDayLogin.offline(roleId, lastOfflineTime, now);
				return true;
			}
		}.submit();
		
		sendToGM("累计登陆奖励增加" + days + "天成功");
		return true;
	}

	@Override
	String usage() {
		return "//leijioff 1(天数)";
	}

}
