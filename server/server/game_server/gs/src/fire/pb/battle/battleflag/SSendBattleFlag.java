
package fire.pb.battle.battleflag;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendBattleFlag__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendBattleFlag extends __SSendBattleFlag__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793884;

	public int getType() {
		return 793884;
	}

	public java.util.LinkedList<java.lang.String> friendflags; // 友方标志
	public java.util.LinkedList<java.lang.String> enemyflags; // 敌方标志

	public SSendBattleFlag() {
		friendflags = new java.util.LinkedList<java.lang.String>();
		enemyflags = new java.util.LinkedList<java.lang.String>();
	}

	public SSendBattleFlag(java.util.LinkedList<java.lang.String> _friendflags_, java.util.LinkedList<java.lang.String> _enemyflags_) {
		this.friendflags = _friendflags_;
		this.enemyflags = _enemyflags_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(friendflags.size());
		for (java.lang.String _v_ : friendflags) {
			_os_.marshal(_v_, "UTF-16LE");
		}
		_os_.compact_uint32(enemyflags.size());
		for (java.lang.String _v_ : enemyflags) {
			_os_.marshal(_v_, "UTF-16LE");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			friendflags.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			enemyflags.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendBattleFlag) {
			SSendBattleFlag _o_ = (SSendBattleFlag)_o1_;
			if (!friendflags.equals(_o_.friendflags)) return false;
			if (!enemyflags.equals(_o_.enemyflags)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendflags.hashCode();
		_h_ += enemyflags.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendflags).append(",");
		_sb_.append(enemyflags).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

