
package fire.msp.role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRoleModifyName__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRoleModifyName extends __GRoleModifyName__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role role = 
				fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if(role == null) return;
		if(null == role.getScene()) return;
		role.setName(newname);
		role.marshal();
		mkio.Protocol send = new fire.pb.SModifyRoleName();
		((fire.pb.SModifyRoleName)send).roleid = roleid;
		((fire.pb.SModifyRoleName)send).newname = newname;
		
		if(role.checkVisible())
		{
			role.sendAround(send);
		}

		if (!teammembers.isEmpty())
			gnet.link.Onlines.getInstance().send(teammembers, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730917;

	public int getType() {
		return 730917;
	}

	public long roleid;
	public java.lang.String newname;
	public java.util.HashSet<Long> teammembers;

	public GRoleModifyName() {
		newname = "";
		teammembers = new java.util.HashSet<Long>();
	}

	public GRoleModifyName(long _roleid_, java.lang.String _newname_, java.util.HashSet<Long> _teammembers_) {
		this.roleid = _roleid_;
		this.newname = _newname_;
		this.teammembers = _teammembers_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(newname, "UTF-16LE");
		_os_.compact_uint32(teammembers.size());
		for (Long _v_ : teammembers) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		newname = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			teammembers.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRoleModifyName) {
			GRoleModifyName _o_ = (GRoleModifyName)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!newname.equals(_o_.newname)) return false;
			if (!teammembers.equals(_o_.teammembers)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += newname.hashCode();
		_h_ += teammembers.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(newname.length()).append(",");
		_sb_.append(teammembers).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

