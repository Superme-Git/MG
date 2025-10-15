
package fire.pb.title;


import fire.pb.PropRole;
import fire.pb.battle.livedie.LiveDieMange;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COffTitle__ extends mkio.Protocol { }

/** 客户端请求卸下称谓
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COffTitle extends __COffTitle__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || !fire.pb.StateCommon.isOnline(roleid))
			return;
		if (checkCOfftitle(roleid))
			new POnTitleProc(roleid, -1).submit();
	}
	
	public boolean checkCOfftitle(long roleid){
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role != null){
			if(isLiveDieTitle(prole)){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 162120, null);
				return false;
			}
//			if(isFactionTitle(prole)){
//				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144142, null);
//				return false;
//			}
		}
		
		return true;
		
	}
	
	/**
	 * 生死战的称号不能被替下
	 * @param prole
	 * @return
	 */
	public static boolean isLiveDieTitle(PropRole prole){
		if(prole.getTitleID() == LiveDieMange.getLiveDieTitle()){
			return true;
		}
		return false;
	}
	
	
/*	private boolean isFactionTitle(PropRole prole) {
		if(prole.getTitleID() >= fire.pb.clan.Constant.FACTION_MASTER_TITLE && prole.getTitleID() <= fire.pb.clan.Constant.FACTION_MEMBER_TITLE)
			return true;
		return false;
	}*/

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 798437;

	public int getType() {
		return 798437;
	}


	public COffTitle() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COffTitle) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COffTitle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

