
package fire.pb.cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SendWordMsg__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SendWordMsg extends __SendWordMsg__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819071;

	public int getType() {
		return 819071;
	}

	public java.lang.String rolename; // 角色名字
	public java.lang.String servername; // 服务器
	public int serverid; // 服务器id
	public java.lang.String worldmsg;
	public int flag; // 1=跨服战队赛消息 2=普通世界消息

	public SendWordMsg() {
		rolename = "";
		servername = "";
		worldmsg = "";
	}

	public SendWordMsg(java.lang.String _rolename_, java.lang.String _servername_, int _serverid_, java.lang.String _worldmsg_, int _flag_) {
		this.rolename = _rolename_;
		this.servername = _servername_;
		this.serverid = _serverid_;
		this.worldmsg = _worldmsg_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(servername, "UTF-16LE");
		_os_.marshal(serverid);
		_os_.marshal(worldmsg, "UTF-16LE");
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolename = _os_.unmarshal_String("UTF-16LE");
		servername = _os_.unmarshal_String("UTF-16LE");
		serverid = _os_.unmarshal_int();
		worldmsg = _os_.unmarshal_String("UTF-16LE");
		flag = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SendWordMsg) {
			SendWordMsg _o_ = (SendWordMsg)_o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!servername.equals(_o_.servername)) return false;
			if (serverid != _o_.serverid) return false;
			if (!worldmsg.equals(_o_.worldmsg)) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += servername.hashCode();
		_h_ += serverid;
		_h_ += worldmsg.hashCode();
		_h_ += flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append("T").append(servername.length()).append(",");
		_sb_.append(serverid).append(",");
		_sb_.append("T").append(worldmsg.length()).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

