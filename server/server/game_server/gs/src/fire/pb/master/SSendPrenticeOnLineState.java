
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendPrenticeOnLineState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendPrenticeOnLineState extends __SSendPrenticeOnLineState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816472;

	public int getType() {
		return 816472;
	}

	public long roleid;
	public java.lang.String rolename; // 徒弟的名字
	public long lastofflinetime; // 上次下线时间
	public byte onlinestate; // 在线状态

	public SSendPrenticeOnLineState() {
		rolename = "";
	}

	public SSendPrenticeOnLineState(long _roleid_, java.lang.String _rolename_, long _lastofflinetime_, byte _onlinestate_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.lastofflinetime = _lastofflinetime_;
		this.onlinestate = _onlinestate_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(lastofflinetime);
		_os_.marshal(onlinestate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		lastofflinetime = _os_.unmarshal_long();
		onlinestate = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendPrenticeOnLineState) {
			SSendPrenticeOnLineState _o_ = (SSendPrenticeOnLineState)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (lastofflinetime != _o_.lastofflinetime) return false;
			if (onlinestate != _o_.onlinestate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += (int)lastofflinetime;
		_h_ += (int)onlinestate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(lastofflinetime).append(",");
		_sb_.append(onlinestate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

