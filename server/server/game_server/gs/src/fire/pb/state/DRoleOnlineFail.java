package fire.pb.state;

import fire.pb.KickErrConst;


public class DRoleOnlineFail implements mkdb.Procedure.Done<PRoleOnline>
{

	@Override
	public void doDone(PRoleOnline p)
	{
		if(p.isSuccess()) return;
		long roleId = p.getRoleId();
		gnet.link.Onlines.getInstance().kick(roleId,KickErrConst.ERR_GM_KICKOUT);//踢下线,TODO kick error code
		StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 角色上线失败。 "));
		//TODO 上线失败要返回客户端信息
	}

}
