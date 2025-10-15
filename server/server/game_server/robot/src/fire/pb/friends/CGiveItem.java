
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGiveItem__ extends mkio.Protocol { }

/** 赠送道具
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGiveItem extends __CGiveItem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806635;

	public int getType() {
		return 806635;
	}

	public long roleid; // 赠送目标的角色id
	public java.util.HashMap<Integer,Integer> itemmap; // 赠送的道具列表 key=itemKey value=itemNum

	public CGiveItem() {
		itemmap = new java.util.HashMap<Integer,Integer>();
	}

	public CGiveItem(long _roleid_, java.util.HashMap<Integer,Integer> _itemmap_) {
		this.roleid = _roleid_;
		this.itemmap = _itemmap_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.compact_uint32(itemmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : itemmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			itemmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGiveItem) {
			CGiveItem _o_ = (CGiveItem)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!itemmap.equals(_o_.itemmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += itemmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(itemmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

