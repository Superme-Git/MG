
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshNaiJiu__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshNaiJiu extends __SRefreshNaiJiu__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787474;

	public int getType() {
		return 787474;
	}

	public int packid;
	public java.util.ArrayList<fire.pb.item.EquipNaiJiu> data;

	public SRefreshNaiJiu() {
		data = new java.util.ArrayList<fire.pb.item.EquipNaiJiu>();
	}

	public SRefreshNaiJiu(int _packid_, java.util.ArrayList<fire.pb.item.EquipNaiJiu> _data_) {
		this.packid = _packid_;
		this.data = _data_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		for (fire.pb.item.EquipNaiJiu _v_ : data)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.compact_uint32(data.size());
		for (fire.pb.item.EquipNaiJiu _v_ : data) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.item.EquipNaiJiu _v_ = new fire.pb.item.EquipNaiJiu();
			_v_.unmarshal(_os_);
			data.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshNaiJiu) {
			SRefreshNaiJiu _o_ = (SRefreshNaiJiu)_o1_;
			if (packid != _o_.packid) return false;
			if (!data.equals(_o_.data)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += data.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(data).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

