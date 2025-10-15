
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAppendItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAppendItem extends __CAppendItem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787455;

	public int getType() {
		return 787455;
	}

	public int keyinpack;
	public int idtype;
	public long id;

	public CAppendItem() {
	}

	public CAppendItem(int _keyinpack_, int _idtype_, long _id_) {
		this.keyinpack = _keyinpack_;
		this.idtype = _idtype_;
		this.id = _id_;
	}

	public final boolean _validator_() {
		if (keyinpack < 1) return false;
		if (id < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(keyinpack);
		_os_.marshal(idtype);
		_os_.marshal(id);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		idtype = _os_.unmarshal_int();
		id = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAppendItem) {
			CAppendItem _o_ = (CAppendItem)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (idtype != _o_.idtype) return false;
			if (id != _o_.id) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += idtype;
		_h_ += (int)id;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(idtype).append(",");
		_sb_.append(id).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAppendItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = idtype - _o_.idtype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

