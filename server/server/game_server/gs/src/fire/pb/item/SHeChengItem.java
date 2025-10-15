
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SHeChengItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SHeChengItem extends __SHeChengItem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787690;

	public int getType() {
		return 787690;
	}

	public int itemnum;
	public int getitemid;

	public SHeChengItem() {
	}

	public SHeChengItem(int _itemnum_, int _getitemid_) {
		this.itemnum = _itemnum_;
		this.getitemid = _getitemid_;
	}

	public final boolean _validator_() {
		if (itemnum < 0) return false;
		if (getitemid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemnum);
		_os_.marshal(getitemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemnum = _os_.unmarshal_int();
		getitemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SHeChengItem) {
			SHeChengItem _o_ = (SHeChengItem)_o1_;
			if (itemnum != _o_.itemnum) return false;
			if (getitemid != _o_.getitemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemnum;
		_h_ += getitemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemnum).append(",");
		_sb_.append(getitemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SHeChengItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemnum - _o_.itemnum;
		if (0 != _c_) return _c_;
		_c_ = getitemid - _o_.getitemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

