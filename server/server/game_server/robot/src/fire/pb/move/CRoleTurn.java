
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleTurn__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleTurn extends __CRoleTurn__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790439;

	public int getType() {
		return 790439;
	}

	public final static int DIR_UP = 0; // 向上
	public final static int DIR_RIGHTUP = 1; // 右上
	public final static int DIR_RIGHT = 2; // 向右
	public final static int DIR_RIGHTDOWN = 3; // 右下
	public final static int DIR_DOWN = 4; // 向下
	public final static int DIR_LEFTDOWN = 5; // 左下
	public final static int DIR_LEFT = 6; // 向左
	public final static int DIR_LEFTUP = 7; // 左上

	public int direction; // 转向的方向

	public CRoleTurn() {
	}

	public CRoleTurn(int _direction_) {
		this.direction = _direction_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(direction);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		direction = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRoleTurn) {
			CRoleTurn _o_ = (CRoleTurn)_o1_;
			if (direction != _o_.direction) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += direction;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(direction).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRoleTurn _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = direction - _o_.direction;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

