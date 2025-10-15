package fire.pb.gm;

import mkdb.Procedure;
import fire.pb.SServerLevel;

public class GM_setdays extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数错误：" + usage());
			return false;
		}

		int days = 0;
		if (args.length == 1)
			days = Integer.valueOf(args[0]);

		final int cnt = days;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.get(1);
				if (serviceInfo == null) {
					serviceInfo = xbean.Pod.newServiceInfo();
					xtable.Serviceinfos.insert(1, serviceInfo);
				}
				
				serviceInfo.setDays(cnt);
				serviceInfo.setUpdatetime(System.currentTimeMillis());
				
				sendToGM("服务器开服天数设置为:" + cnt);
				
				SServerLevel sLevel = fire.pb.timer.Module.getServerLvData();
				Procedure.psendWhileCommit(getGmroleid(), sLevel);
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}

}
