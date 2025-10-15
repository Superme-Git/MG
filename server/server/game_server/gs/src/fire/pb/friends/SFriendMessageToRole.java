
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SFriendMessageToRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SFriendMessageToRole extends __SFriendMessageToRole__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806436;

	public int getType() {
		return 806436;
	}

	public long roleid;
	public java.lang.String content;
	public short rolelevel;
	public java.util.ArrayList<com.locojoy.base.Octets> details; // 展示品信息
	public java.util.ArrayList<fire.pb.talk.DisplayInfo> displayinfo; // 展示的物品key与type

	public SFriendMessageToRole() {
		content = "";
		details = new java.util.ArrayList<com.locojoy.base.Octets>();
		displayinfo = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
	}

	public SFriendMessageToRole(long _roleid_, java.lang.String _content_, short _rolelevel_, java.util.ArrayList<com.locojoy.base.Octets> _details_, java.util.ArrayList<fire.pb.talk.DisplayInfo> _displayinfo_) {
		this.roleid = _roleid_;
		this.content = _content_;
		this.rolelevel = _rolelevel_;
		this.details = _details_;
		this.displayinfo = _displayinfo_;
	}

	public final boolean _validator_() {
		if (roleid < 0) return false;
		for (fire.pb.talk.DisplayInfo _v_ : displayinfo)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(content, "UTF-16LE");
		_os_.marshal(rolelevel);
		_os_.compact_uint32(details.size());
		for (com.locojoy.base.Octets _v_ : details) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(displayinfo.size());
		for (fire.pb.talk.DisplayInfo _v_ : displayinfo) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		content = _os_.unmarshal_String("UTF-16LE");
		rolelevel = _os_.unmarshal_short();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			details.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.talk.DisplayInfo _v_ = new fire.pb.talk.DisplayInfo();
			_v_.unmarshal(_os_);
			displayinfo.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SFriendMessageToRole) {
			SFriendMessageToRole _o_ = (SFriendMessageToRole)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!content.equals(_o_.content)) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (!details.equals(_o_.details)) return false;
			if (!displayinfo.equals(_o_.displayinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += content.hashCode();
		_h_ += rolelevel;
		_h_ += details.hashCode();
		_h_ += displayinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(content.length()).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(details).append(",");
		_sb_.append(displayinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

