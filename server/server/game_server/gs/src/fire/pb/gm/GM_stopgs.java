package fire.pb.gm;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;

public class GM_stopgs extends GMCommand {

	@Override
	boolean exec(String[] args) {
		try {
			long time = 30;
			if (args.length >= 1)
				time = Long.valueOf(args[0]) * 60;

			ManagementFactory.getPlatformMBeanServer().setAttribute(
					new javax.management.ObjectName("bean:name=stopper"),
					new javax.management.Attribute("StopTime", time));
			sendToGM("Stop GS in " + time + " s.");
			List<String> pars = new LinkedList<String>();
			pars.add(String.valueOf(time / 60));
			fire.pb.talk.MessageMgr.broadcastMsgNotify(141390, 0, pars);
			fire.pb.talk.MessageMgr.broadcastMsgNotify(141391, 0, pars);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
