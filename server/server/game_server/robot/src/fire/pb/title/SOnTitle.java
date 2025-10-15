
package fire.pb.title;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOnTitle__ extends mkio.Protocol { }

/** 服务器通知客户端佩戴称谓   同时发送周围的玩家
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOnTitle extends __SOnTitle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 798436;

	public int getType() {
		return 798436;
	}

	public long roleid; // 角色id
	public int titleid; // 称谓id
	public java.lang.String titlename; // 称谓名

	public SOnTitle() {
		titlename = "";
	}

	public SOnTitle(long _roleid_, int _titleid_, java.lang.String _titlename_) {
		this.roleid = _roleid_;
		this.titleid = _titleid_;
		this.titlename = _titlename_;
	}

	public final boolean _validator_() {
		if (roleid < 0) return false;
		if (titleid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(titleid);
		_os_.marshal(titlename, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		titleid = _os_.unmarshal_int();
		titlename = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOnTitle) {
			SOnTitle _o_ = (SOnTitle)_o1_;
			if (roleid != _o_.roleid) return false;
			if (titleid != _o_.titleid) return false;
			if (!titlename.equals(_o_.titlename)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += titleid;
		_h_ += titlename.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(titleid).append(",");
		_sb_.append("T").append(titlename.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

