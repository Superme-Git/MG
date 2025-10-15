package fire.pb.gm;

import fire.pb.talk.MessageMgr;

public class GM_log extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();

		try {
			MessageMgr.sendMsgToPop(roleId, "手动执行运营分析日志");
			fire.log.Module.analyseLogFromXml();
			int historyUser = fire.log.Module.getLogAnalyser()
					.getHistoryUserNum();
			int historyRole = fire.log.Module.getLogAnalyser()
					.getHistoryRoleNum();
			StringBuffer sb = new StringBuffer();
			sb.append("历史创建账号总数： " + historyUser + "   ");
			sb.append("历史创建角色总数： " + historyRole + "   ");
			MessageMgr.sendMsgToPop(roleId, "运营分析日志解析完成");
			MessageMgr.sendMsgToPop(roleId, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			MessageMgr.sendMsgToPop(roleId, "运营分析日志解析异常" + e.getMessage());
		}

		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
