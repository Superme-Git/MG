
package fire.pb.talk;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STransChatMessage2Client__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STransChatMessage2Client extends __STransChatMessage2Client__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 792434;

	public int getType() {
		return 792434;
	}

	public long roleid; // 发言角色ID
	public java.lang.String rolename; // 名字
	public int shapeid; // 头像
	public int titleid; // 称谓
	public int messagetype; // 频道
	public java.lang.String message; // 内容
	public java.util.ArrayList<fire.pb.talk.DisplayInfo> displayinfos; // 显示道具、宠物、技能

	public STransChatMessage2Client() {
		rolename = "";
		message = "";
		displayinfos = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
	}

	public STransChatMessage2Client(long _roleid_, java.lang.String _rolename_, int _shapeid_, int _titleid_, int _messagetype_, java.lang.String _message_, java.util.ArrayList<fire.pb.talk.DisplayInfo> _displayinfos_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.shapeid = _shapeid_;
		this.titleid = _titleid_;
		this.messagetype = _messagetype_;
		this.message = _message_;
		this.displayinfos = _displayinfos_;
	}

	public final boolean _validator_() {
		for (fire.pb.talk.DisplayInfo _v_ : displayinfos)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(shapeid);
		_os_.marshal(titleid);
		_os_.marshal(messagetype);
		_os_.marshal(message, "UTF-16LE");
		_os_.compact_uint32(displayinfos.size());
		for (fire.pb.talk.DisplayInfo _v_ : displayinfos) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		shapeid = _os_.unmarshal_int();
		titleid = _os_.unmarshal_int();
		messagetype = _os_.unmarshal_int();
		message = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.talk.DisplayInfo _v_ = new fire.pb.talk.DisplayInfo();
			_v_.unmarshal(_os_);
			displayinfos.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STransChatMessage2Client) {
			STransChatMessage2Client _o_ = (STransChatMessage2Client)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shapeid != _o_.shapeid) return false;
			if (titleid != _o_.titleid) return false;
			if (messagetype != _o_.messagetype) return false;
			if (!message.equals(_o_.message)) return false;
			if (!displayinfos.equals(_o_.displayinfos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += shapeid;
		_h_ += titleid;
		_h_ += messagetype;
		_h_ += message.hashCode();
		_h_ += displayinfos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(shapeid).append(",");
		_sb_.append(titleid).append(",");
		_sb_.append(messagetype).append(",");
		_sb_.append("T").append(message.length()).append(",");
		_sb_.append(displayinfos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

