
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleJump__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleJump extends __SRoleJump__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790478;

	public int getType() {
		return 790478;
	}

	public long roleid; // 角色ID
	public fire.pb.move.Pos srcpos; // 出发点
	public fire.pb.move.Pos destpos; // 到达点
	public byte jumptype; // 0为普通跳，1为2段跳

	public SRoleJump() {
		srcpos = new fire.pb.move.Pos();
		destpos = new fire.pb.move.Pos();
	}

	public SRoleJump(long _roleid_, fire.pb.move.Pos _srcpos_, fire.pb.move.Pos _destpos_, byte _jumptype_) {
		this.roleid = _roleid_;
		this.srcpos = _srcpos_;
		this.destpos = _destpos_;
		this.jumptype = _jumptype_;
	}

	public final boolean _validator_() {
		if (!srcpos._validator_()) return false;
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(srcpos);
		_os_.marshal(destpos);
		_os_.marshal(jumptype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		srcpos.unmarshal(_os_);
		destpos.unmarshal(_os_);
		jumptype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleJump) {
			SRoleJump _o_ = (SRoleJump)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!srcpos.equals(_o_.srcpos)) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			if (jumptype != _o_.jumptype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += srcpos.hashCode();
		_h_ += destpos.hashCode();
		_h_ += (int)jumptype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(srcpos).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(jumptype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

