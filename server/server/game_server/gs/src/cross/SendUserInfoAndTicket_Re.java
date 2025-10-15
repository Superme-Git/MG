
package cross;
import fire.pb.cross.Constant;
import fire.pb.cross.CrossManager;
import fire.pb.cross.SendRoleInfo;
import fire.pb.main.ConfigManager;

import fire.pb.main.Gs;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SendUserInfoAndTicket_Re__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SendUserInfoAndTicket_Re extends __SendUserInfoAndTicket_Re__ {
	@Override
	protected void process() {
		CrossManager.logger.info("SendUserInfoAndTicket_Re.原服收到来自跨服userId=" + userid + "的SendUserInfoAndTicket_Re协议!");
		long roleid = CrossManager.getInstance().getTransferRoleByUserId(userid);
		if (roleid <= 0){
			CrossManager.logger.error("SendUserInfoAndTicket_Re.原服正在跨服的角色中不存在:" + userid + "!");
			return;	
		}
		CrossManager.logger.info("SendUserInfoAndTicket_Re.原服收到来自跨服回复.roleId=" + roleid + ",retcode=" + retcode);
		if (retcode != RET_OK) {
			return;
		}
		
		boolean needResetData = xtable.Crosstemproles.select(roleid) == null;
		SendRoleInfo send = new SendRoleInfo();
		send.myzoneid = Integer.parseInt(Gs.serverid);
		send.userid = userid;
		send.roleid = roleid;
		send.flag = Constant.TO_CROSS;
		send.needcleardata = (byte)(needResetData ? 1 : 0);
		CrossManager.getInstance().sendToCrossServer(Constant.SendRoleInfo, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 229;

	public int getType() {
		return 229;
	}

	public final static int RET_OK = 0;
	public final static int RET_NETWORK_ERROR = 1;
	public final static int RET_MULTI_LOGIN = 2;
	public final static int RET_SERVER_OVERLOAD = 3;

	public int retcode;
	public int userid;
	public byte flag;

	public SendUserInfoAndTicket_Re() {
	}

	public SendUserInfoAndTicket_Re(int _retcode_, int _userid_, byte _flag_) {
		this.retcode = _retcode_;
		this.userid = _userid_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(retcode);
		_os_.marshal(userid);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SendUserInfoAndTicket_Re) {
			SendUserInfoAndTicket_Re _o_ = (SendUserInfoAndTicket_Re)_o1_;
			if (retcode != _o_.retcode) return false;
			if (userid != _o_.userid) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += userid;
		_h_ += (int)flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SendUserInfoAndTicket_Re _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

