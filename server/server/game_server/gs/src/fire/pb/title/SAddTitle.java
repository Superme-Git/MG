
package fire.pb.title;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddTitle__ extends mkio.Protocol { }

/** 服务器通知客户端添加一个称谓
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddTitle extends __SAddTitle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 798433;

	public int getType() {
		return 798433;
	}

	public fire.pb.title.TitleInfo info;

	public SAddTitle() {
		info = new fire.pb.title.TitleInfo();
	}

	public SAddTitle(fire.pb.title.TitleInfo _info_) {
		this.info = _info_;
	}

	public final boolean _validator_() {
		if (!info._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(info);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		info.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddTitle) {
			SAddTitle _o_ = (SAddTitle)_o1_;
			if (!info.equals(_o_.info)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += info.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(info).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

