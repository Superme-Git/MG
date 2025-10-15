package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CHeChengItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CHeChengItem extends __CHeChengItem__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		new PCombineItem(roleId, keyinpack, isall == 1 ? true : false,
				(byte) 1, 
				hammer == 1 ? true : false).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787689;

	public int getType() {
		return 787689;
	}

	public byte money;
	public byte isall;
	public byte hammer;
	public int keyinpack;

	public CHeChengItem() {
	}

	public CHeChengItem(byte _money_, byte _isall_, byte _hammer_, int _keyinpack_) {
		this.money = _money_;
		this.isall = _isall_;
		this.hammer = _hammer_;
		this.keyinpack = _keyinpack_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(money);
		_os_.marshal(isall);
		_os_.marshal(hammer);
		_os_.marshal(keyinpack);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		money = _os_.unmarshal_byte();
		isall = _os_.unmarshal_byte();
		hammer = _os_.unmarshal_byte();
		keyinpack = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CHeChengItem) {
			CHeChengItem _o_ = (CHeChengItem)_o1_;
			if (money != _o_.money) return false;
			if (isall != _o_.isall) return false;
			if (hammer != _o_.hammer) return false;
			if (keyinpack != _o_.keyinpack) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)money;
		_h_ += (int)isall;
		_h_ += (int)hammer;
		_h_ += keyinpack;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(money).append(",");
		_sb_.append(isall).append(",");
		_sb_.append(hammer).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CHeChengItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = money - _o_.money;
		if (0 != _c_) return _c_;
		_c_ = isall - _o_.isall;
		if (0 != _c_) return _c_;
		_c_ = hammer - _o_.hammer;
		if (0 != _c_) return _c_;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
