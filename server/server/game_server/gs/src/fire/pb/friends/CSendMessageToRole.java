
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendMessageToRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendMessageToRole extends __CSendMessageToRole__ {
	@Override
	protected void process() {
		final long senderRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (senderRoleId < 0)
			return;
		if (fire.pb.talk.ChatChannel.isShutUp(senderRoleId))
			return;
		PSendFriendMsgProc proc = new PSendFriendMsgProc(senderRoleId, roleid, content, checkshiedmsg, displayinfo);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806435;

	public int getType() {
		return 806435;
	}

	public long roleid;
	public java.lang.String content;
	public java.lang.String checkshiedmsg; // 检测屏蔽字的纯文本
	public java.util.ArrayList<fire.pb.talk.DisplayInfo> displayinfo; // 展示的物品，宠物，技能或者修炼的信息

	public CSendMessageToRole() {
		content = "";
		checkshiedmsg = "";
		displayinfo = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
	}

	public CSendMessageToRole(long _roleid_, java.lang.String _content_, java.lang.String _checkshiedmsg_, java.util.ArrayList<fire.pb.talk.DisplayInfo> _displayinfo_) {
		this.roleid = _roleid_;
		this.content = _content_;
		this.checkshiedmsg = _checkshiedmsg_;
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
		_os_.marshal(checkshiedmsg, "UTF-16LE");
		_os_.compact_uint32(displayinfo.size());
		for (fire.pb.talk.DisplayInfo _v_ : displayinfo) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		content = _os_.unmarshal_String("UTF-16LE");
		checkshiedmsg = _os_.unmarshal_String("UTF-16LE");
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
		if (_o1_ instanceof CSendMessageToRole) {
			CSendMessageToRole _o_ = (CSendMessageToRole)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!content.equals(_o_.content)) return false;
			if (!checkshiedmsg.equals(_o_.checkshiedmsg)) return false;
			if (!displayinfo.equals(_o_.displayinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += content.hashCode();
		_h_ += checkshiedmsg.hashCode();
		_h_ += displayinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(content.length()).append(",");
		_sb_.append("T").append(checkshiedmsg.length()).append(",");
		_sb_.append(displayinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

