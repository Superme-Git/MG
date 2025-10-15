
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleJumpStop__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleJumpStop extends __CRoleJumpStop__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790479;

	public int getType() {
		return 790479;
	}

	public fire.pb.move.Pos destpos; // 飞越停止点
	public byte destz; // 飞越停止z坐标

	public CRoleJumpStop() {
		destpos = new fire.pb.move.Pos();
	}

	public CRoleJumpStop(fire.pb.move.Pos _destpos_, byte _destz_) {
		this.destpos = _destpos_;
		this.destz = _destz_;
	}

	public final boolean _validator_() {
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(destpos);
		_os_.marshal(destz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		destpos.unmarshal(_os_);
		destz = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRoleJumpStop) {
			CRoleJumpStop _o_ = (CRoleJumpStop)_o1_;
			if (!destpos.equals(_o_.destpos)) return false;
			if (destz != _o_.destz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += destpos.hashCode();
		_h_ += (int)destz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(destpos).append(",");
		_sb_.append(destz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

