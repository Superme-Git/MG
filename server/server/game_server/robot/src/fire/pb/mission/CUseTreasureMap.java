
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUseTreasureMap__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUseTreasureMap extends __CUseTreasureMap__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805532;

	public int getType() {
		return 805532;
	}

	public int itemkey;
	public int maptype; // 0 普通藏宝图; 1 高级藏宝图

	public CUseTreasureMap() {
	}

	public CUseTreasureMap(int _itemkey_, int _maptype_) {
		this.itemkey = _itemkey_;
		this.maptype = _maptype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemkey);
		_os_.marshal(maptype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemkey = _os_.unmarshal_int();
		maptype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUseTreasureMap) {
			CUseTreasureMap _o_ = (CUseTreasureMap)_o1_;
			if (itemkey != _o_.itemkey) return false;
			if (maptype != _o_.maptype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemkey;
		_h_ += maptype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemkey).append(",");
		_sb_.append(maptype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CUseTreasureMap _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		_c_ = maptype - _o_.maptype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

