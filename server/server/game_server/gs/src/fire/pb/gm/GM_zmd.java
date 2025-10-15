package fire.pb.gm;

import java.util.LinkedList;
import java.util.List;

public class GM_zmd extends GMCommand { 
	
//走马灯提示
	
	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return true;

		List<String> pars = new LinkedList<String>();
		String str = "";
		for (String arg : args)
			str = str + arg + " ";
		pars.add(str);
		fire.pb.talk.MessageMgr.broadcastMsgNotify(170027, 0, pars);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
