package fire.pb;

import fire.pb.state.State;

/**
 * 刷新角色活力体力
 *
 */
public class PRefreshRoleTimigData extends mkdb.Procedure{
	private long roleid;
	
	public PRefreshRoleTimigData(final long roleid){
		this.roleid = roleid;
	}
	
	public boolean process(){
//		if (fire.pb.state.StateManager.selectStateIdByRoleId(roleid) != State.ENTRY_STATE)
//			return true;
//		
////		fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(roleid);
////		PropRole prole = new PropRole(roleid, false);
//		final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
//		if (!send.datas.isEmpty()){
//			psendWhileCommit(roleid, send);
//		}
		return true;
	}
}
