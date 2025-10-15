package fire.pb.gm;

import java.util.concurrent.ExecutionException;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;

public class GM_delbuff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		final long roleid = (args.length > 1) ? Integer.valueOf(args[0])
				: getGmroleid();
		final int buffid = (args.length > 1) ? Integer.valueOf(args[1])
				: Integer.valueOf(args[0]);
		try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					BuffAgent agent = new BuffRoleImpl(roleid);
					boolean success = agent.removeCBuffWithSP(buffid);
					if (success)
						sendToGM("删除角色：" + roleid + " 的buff ：" + buffid);
					return true;
				}
			}.submit().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}