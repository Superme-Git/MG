
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRideUpdate__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRideUpdate extends __SRideUpdate__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787783;

	public int getType() {
		return 787783;
	}

	public int itemkey; // //包里道具key by changhao
	public int itemid; // //道具id by changhao
	public int rideid; // 坐骑id by changhao

	public SRideUpdate() {
	}

	public SRideUpdate(int _itemkey_, int _itemid_, int _rideid_) {
		this.itemkey = _itemkey_;
		this.itemid = _itemid_;
		this.rideid = _rideid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemkey);
		_os_.marshal(itemid);
		_os_.marshal(rideid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemkey = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		rideid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRideUpdate) {
			SRideUpdate _o_ = (SRideUpdate)_o1_;
			if (itemkey != _o_.itemkey) return false;
			if (itemid != _o_.itemid) return false;
			if (rideid != _o_.rideid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemkey;
		_h_ += itemid;
		_h_ += rideid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemkey).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(rideid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRideUpdate _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = rideid - _o_.rideid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

