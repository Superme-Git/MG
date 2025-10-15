
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SHuobanList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SHuobanList extends __SHuobanList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818833;

	public int getType() {
		return 818833;
	}

	public java.util.ArrayList<fire.pb.huoban.HuoBanInfo> huobans; // 我的伙伴列表

	public SHuobanList() {
		huobans = new java.util.ArrayList<fire.pb.huoban.HuoBanInfo>();
	}

	public SHuobanList(java.util.ArrayList<fire.pb.huoban.HuoBanInfo> _huobans_) {
		this.huobans = _huobans_;
	}

	public final boolean _validator_() {
		for (fire.pb.huoban.HuoBanInfo _v_ : huobans)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(huobans.size());
		for (fire.pb.huoban.HuoBanInfo _v_ : huobans) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.huoban.HuoBanInfo _v_ = new fire.pb.huoban.HuoBanInfo();
			_v_.unmarshal(_os_);
			huobans.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SHuobanList) {
			SHuobanList _o_ = (SHuobanList)_o1_;
			if (!huobans.equals(_o_.huobans)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobans.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobans).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

