package fire.pb;

public class PSendLoginIp extends mkdb.Procedure
{
	private final long roleId;
	private final long lastlogintime;
	
	public PSendLoginIp(long roleId, long lastlogintime)
	{
		this.roleId = roleId;
		this.lastlogintime = lastlogintime;
	}
	
	
	@Override
	protected boolean process() throws Exception
	{
		xbean.Properties xprop = xtable.Properties.select(roleId);
		
		xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(xprop.getUserid());
		
		if(xprop.getLastloginip() != 0)
		{
			SSendLoginIp snd = new SSendLoginIp();
			snd.lastip = xprop.getLastloginip();
			snd.lasttime = lastlogintime;
			if (userinfo != null) 
				snd.loginip = userinfo.getLoginip();
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		}
		if(userinfo!= null && xprop.getLastloginip() != userinfo.getLoginip())
		{
			xprop = xtable.Properties.get(roleId);
			xprop.setLastloginip(userinfo.getLoginip());
		}
		return true;
	}
	

}
