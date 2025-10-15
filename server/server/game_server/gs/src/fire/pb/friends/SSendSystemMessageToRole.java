
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendSystemMessageToRole__ extends mkio.Protocol { }

/** 系统好友消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendSystemMessageToRole extends __SSendSystemMessageToRole__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806555;

	public int getType() {
		return 806555;
	}

	public long systemroleid; // 如果是0 表示是系统消息 大于0的一定是好友发的消息
	public int contentid;
	public java.util.ArrayList<com.locojoy.base.Octets> contentparam;
	public java.lang.String time;

	public SSendSystemMessageToRole() {
		contentparam = new java.util.ArrayList<com.locojoy.base.Octets>();
		time = "";
	}

	public SSendSystemMessageToRole(long _systemroleid_, int _contentid_, java.util.ArrayList<com.locojoy.base.Octets> _contentparam_, java.lang.String _time_) {
		this.systemroleid = _systemroleid_;
		this.contentid = _contentid_;
		this.contentparam = _contentparam_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(systemroleid);
		_os_.marshal(contentid);
		_os_.compact_uint32(contentparam.size());
		for (com.locojoy.base.Octets _v_ : contentparam) {
			_os_.marshal(_v_);
		}
		_os_.marshal(time, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		systemroleid = _os_.unmarshal_long();
		contentid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			contentparam.add(_v_);
		}
		time = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendSystemMessageToRole) {
			SSendSystemMessageToRole _o_ = (SSendSystemMessageToRole)_o1_;
			if (systemroleid != _o_.systemroleid) return false;
			if (contentid != _o_.contentid) return false;
			if (!contentparam.equals(_o_.contentparam)) return false;
			if (!time.equals(_o_.time)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)systemroleid;
		_h_ += contentid;
		_h_ += contentparam.hashCode();
		_h_ += time.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(systemroleid).append(",");
		_sb_.append(contentid).append(",");
		_sb_.append(contentparam).append(",");
		_sb_.append("T").append(time.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

