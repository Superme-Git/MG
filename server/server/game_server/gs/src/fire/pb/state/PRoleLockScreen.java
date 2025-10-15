package fire.pb.state;

import fire.log.Logger;
import fire.pb.hook.RoleHookManager;
import mkdb.Procedure;

public class PRoleLockScreen extends Procedure {
	final private long roleId;
	final private Boolean type;
	final private static Logger logger = Logger.getLogger("SYSTEM");
	
	public PRoleLockScreen(long roleId,Boolean type)
	{
		this.roleId = roleId;
		this.type = type;
	}
	
	@Override
	protected boolean process()
	{
		RoleHookManager.getInstance().setLockScreenStateByRoleId(roleId, type);
		logger.info("PRoleLockScreen roleid:" + roleId );
		return true;
	}
}
