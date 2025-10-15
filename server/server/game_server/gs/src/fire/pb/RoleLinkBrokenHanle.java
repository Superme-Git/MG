package fire.pb;

import java.util.Collection;

import fire.pb.state.PRoleOffline;
import fire.pb.state.StateManager;
import gnet.link.Role;
import gnet.link.Onlines.Handle;

public class RoleLinkBrokenHanle implements Handle {

	@Override
	public void onLinkBroken(Role role, int reason) {
		StateManager.logger.error(new StringBuilder("RoleLinkBrokenHanle.onLinkBroken,角色[").append(role.getRoleid()).
				append("]意外断开,原因=").append(reason));
		new fire.pb.state.PRoleOffline(role.getRoleid(), PRoleOffline.TYPE_LINK_BROKEN).submit();
	}

	@Override
	public void onManagerBroken(Collection<Role> roles) {
		// TODO Auto-generated method stub
	}

}
