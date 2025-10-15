package fire.pb.gm;

import java.text.SimpleDateFormat;

import fire.pb.talk.MessageMgr;

public class GM_systime extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long systemTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日    HH:mm:ss");
		String timeStr = format.format(systemTime);

		long roleid = getGmroleid();
		MessageMgr.sendMsgToPop(roleid, "当前系统时间为：  " + timeStr + "毫秒数:"
				+ systemTime);

		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
