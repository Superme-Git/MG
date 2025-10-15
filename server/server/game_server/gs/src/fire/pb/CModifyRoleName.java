package fire.pb;

import gnet.link.Onlines;
import fire.pb.role.PModifyRoleName;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CModifyRoleName__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CModifyRoleName extends __CModifyRoleName__ {
	@Override
	protected void process() {
		final long roleid = Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new PModifyRoleName(roleid, newname, itemkey, false).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786506;

	public int getType() {
		return 786506;
	}

	public java.lang.String newname;
	public int itemkey; // 使用的改名符itemkey

	public CModifyRoleName() {
		newname = "";
	}

	public CModifyRoleName(java.lang.String _newname_, int _itemkey_) {
		this.newname = _newname_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(newname, "UTF-16LE");
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		newname = _os_.unmarshal_String("UTF-16LE");
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CModifyRoleName) {
			CModifyRoleName _o_ = (CModifyRoleName)_o1_;
			if (!newname.equals(_o_.newname)) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += newname.hashCode();
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(newname.length()).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
