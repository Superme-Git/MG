package fire.pb.gm;

import java.util.Calendar;

import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.RoleLivenessManager;

public class GM_addact extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			RoleLivenessManager.logger.debug("角色[" + getGmroleid() + "]服务器推荐度:"
					+ RoleLivenessManager.getCurrentRecommend());
			sendToGM("参数错误：" + usage());
			return false;
		}

		final int activeId = Integer.valueOf(args[0]);
		int setcount = 0;
		if (args.length == 2)
			setcount = Integer.valueOf(args[1]);
		final int count = setcount;
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				RoleLiveness actrole = RoleLiveness
						.getRoleLiveness(getGmroleid());
				if (count == 0) {
					actrole.handleActiveLivenessData(activeId);
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(actrole.getNewPlayActiveDay()
							.getTime());
					RoleLivenessManager.logger.debug("角色[" + getGmroleid()
							+ "]活跃度:" + actrole.getLiveness() + ",记录时间:"
							+ date.get(Calendar.DAY_OF_MONTH));
					RoleLivenessManager.logger.debug("角色[" + getGmroleid()
							+ "]actives:"
							+ actrole.getNewPlayActiveDay().getActives().size()
							+ ",chests:"
							+ actrole.getNewPlayActiveDay().getChests().size());
				} else
					actrole.setActiveNum(activeId, count);
				return true;
			};
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//addactive [activeid] [第二个参数：如果有的话是设置的值]";
	}
}
