
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STransfromShape__ extends mkio.Protocol { }

/** 改变玩家造型
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STransfromShape extends __STransfromShape__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790465;

	public int getType() {
		return 790465;
	}

	public long playerid; // 玩家id
	public int shape; // 玩家新造型

	public STransfromShape() {
	}

	public STransfromShape(long _playerid_, int _shape_) {
		this.playerid = _playerid_;
		this.shape = _shape_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(playerid);
		_os_.marshal(shape);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		playerid = _os_.unmarshal_long();
		shape = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STransfromShape) {
			STransfromShape _o_ = (STransfromShape)_o1_;
			if (playerid != _o_.playerid) return false;
			if (shape != _o_.shape) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)playerid;
		_h_ += shape;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(playerid).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(STransfromShape _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(playerid - _o_.playerid);
		if (0 != _c_) return _c_;
		_c_ = shape - _o_.shape;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

