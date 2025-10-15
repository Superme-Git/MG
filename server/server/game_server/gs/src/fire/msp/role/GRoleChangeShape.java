
package fire.msp.role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRoleChangeShape__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRoleChangeShape extends __GRoleChangeShape__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role  role = 
				fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
			if(role == null) return;
			if(null == role.getScene()) return;
			role.setModeid(shape);
			mkio.Protocol send;
			if (reason == HUANSE) {
				send = new fire.pb.move.SRoleModelChange();
				((fire.pb.move.SRoleModelChange)send).roleid = roleid;
				((fire.pb.move.SRoleModelChange)send).shape = shape;
			} else {
				send = new fire.pb.move.SRoleChangeShape();
				((fire.pb.move.SRoleChangeShape)send).roleid = roleid;
				((fire.pb.move.SRoleChangeShape)send).shape = shape;
			}
		//	role.sendWhoSeeMeAndMe(send);
			if(role.checkVisible())
			{
				role.sendAround(send);
			}
			// 发送给队员,如果有的话
			if (!teammembers.isEmpty())
				gnet.link.Onlines.getInstance().send(teammembers, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730914;

	public int getType() {
		return 730914;
	}

	public final static int TUIBIAN = 0;
	public final static int HUANSE = 1;
	public final static int OTHER = 2;

	public long roleid;
	public int shape;
	public int reason;
	public java.util.HashSet<Long> teammembers;

	public GRoleChangeShape() {
		teammembers = new java.util.HashSet<Long>();
	}

	public GRoleChangeShape(long _roleid_, int _shape_, int _reason_, java.util.HashSet<Long> _teammembers_) {
		this.roleid = _roleid_;
		this.shape = _shape_;
		this.reason = _reason_;
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
		_os_.marshal(shape);
		_os_.marshal(reason);
		_os_.compact_uint32(teammembers.size());
		for (Long _v_ : teammembers) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		shape = _os_.unmarshal_int();
		reason = _os_.unmarshal_int();
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
		if (_o1_ instanceof GRoleChangeShape) {
			GRoleChangeShape _o_ = (GRoleChangeShape)_o1_;
			if (roleid != _o_.roleid) return false;
			if (shape != _o_.shape) return false;
			if (reason != _o_.reason) return false;
			if (!teammembers.equals(_o_.teammembers)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += shape;
		_h_ += reason;
		_h_ += teammembers.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(reason).append(",");
		_sb_.append(teammembers).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

