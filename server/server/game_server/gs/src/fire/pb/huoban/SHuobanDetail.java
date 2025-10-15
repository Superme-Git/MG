
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SHuobanDetail__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SHuobanDetail extends __SHuobanDetail__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818835;

	public int getType() {
		return 818835;
	}

	public fire.pb.huoban.HuoBanDetailInfo huoban; // 我的一个伙伴信息

	public SHuobanDetail() {
		huoban = new fire.pb.huoban.HuoBanDetailInfo();
	}

	public SHuobanDetail(fire.pb.huoban.HuoBanDetailInfo _huoban_) {
		this.huoban = _huoban_;
	}

	public final boolean _validator_() {
		if (!huoban._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(huoban);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huoban.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SHuobanDetail) {
			SHuobanDetail _o_ = (SHuobanDetail)_o1_;
			if (!huoban.equals(_o_.huoban)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huoban.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huoban).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

