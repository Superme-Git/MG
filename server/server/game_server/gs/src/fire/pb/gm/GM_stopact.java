package fire.pb.gm;

import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.ActivityManager;

public class GM_stopact extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM(usage());
			return false;
		}
		int actId = Integer.parseInt(args[0]);
		int endAct = Integer.parseInt(args[1]);
		boolean cancelFuture = false;
		if (args.length >= 3)
			cancelFuture = args[2].equals("1") ? true : false;
		AbstractScheduledActivity act = ActivityManager.getActivitymap().get(
				actId);
		if (act == null)
			return false;
		try {
			if (endAct == 1)
				act.end(cancelFuture);
			else
				act.stop(cancelFuture);
			ActivityManager.getActivitymap().remove(actId);
			act = null;
		} catch (Exception e) {
			logger.error("Stop activity by GM Command failed.id:" + actId, e);

		}
		return true;
	}

	@Override
	String usage() {

		return "//stopact activityid cancelweekrepeat 1/0 ��һ������ָ�����Ψһid,�ڶ�������Ϊ1��ʱ���ʾ�����,Ϊ0��ʱ���ʾ��ͣ�(�Ժ󻹿��Իָ�),����������Ϊ1��ʾ����ѭ���ĻҲͣ��";
	}

}
