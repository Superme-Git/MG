
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleJumpDrawback__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleJumpDrawback extends __SRoleJumpDrawback__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790480;

	public int getType() {
		return 790480;
	}

	public long roleid; // 角色id
	public fire.pb.move.Pos srcpos; // 被拉回的点
	public byte srcz; // 被拉回的z坐标

	public SRoleJumpDrawback() {
		srcpos = new fire.pb.move.Pos();
	}

	public SRoleJumpDrawback(long _roleid_, fire.pb.move.Pos _srcpos_, byte _srcz_) {
		this.roleid = _roleid_;
		this.srcpos = _srcpos_;
		this.srcz = _srcz_;
	}

	public final boolean _validator_() {
		if (!srcpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(srcpos);
		_os_.marshal(srcz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		srcpos.unmarshal(_os_);
		srcz = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleJumpDrawback) {
			SRoleJumpDrawback _o_ = (SRoleJumpDrawback)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!srcpos.equals(_o_.srcpos)) return false;
			if (srcz != _o_.srcz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += srcpos.hashCode();
		_h_ += (int)srcz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(srcpos).append(",");
		_sb_.append(srcz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRoleJumpDrawback _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = srcpos.compareTo(_o_.srcpos);
		if (0 != _c_) return _c_;
		_c_ = srcz - _o_.srcz;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

