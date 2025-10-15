package fire.pb.gm;

import java.util.Map;

import fire.pb.buff.BuffRoleImpl;
import fire.pb.effect.Module;

public class GM_outputattr extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long dstroleid = 0; 
		if (args.length < 1)
			dstroleid = getGmroleid();
		else
			dstroleid = Long.parseLong(args[0]);
		
		final xbean.Properties prop = xtable.Properties.select(dstroleid);
		if (prop == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}
		
		GMInteface.outputattr(dstroleid);
		
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return "//outputattr  $Player$";
	}

}
