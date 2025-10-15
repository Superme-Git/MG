
package fire.pb.cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SendRoleInfo__ extends mkio.Protocol { }

/** 跨服和原服之间的协议  start  32632到 32682
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SendRoleInfo extends __SendRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819064;

	public int getType() {
		return 819064;
	}

	public int myzoneid;
	public int userid;
	public long roleid;
	public byte flag; // 1到跨服，2到原服
	public byte needcleardata; // 是否需要重置跨服临时数据

	public SendRoleInfo() {
	}

	public SendRoleInfo(int _myzoneid_, int _userid_, long _roleid_, byte _flag_, byte _needcleardata_) {
		this.myzoneid = _myzoneid_;
		this.userid = _userid_;
		this.roleid = _roleid_;
		this.flag = _flag_;
		this.needcleardata = _needcleardata_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(myzoneid);
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(flag);
		_os_.marshal(needcleardata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		myzoneid = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		flag = _os_.unmarshal_byte();
		needcleardata = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SendRoleInfo) {
			SendRoleInfo _o_ = (SendRoleInfo)_o1_;
			if (myzoneid != _o_.myzoneid) return false;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (flag != _o_.flag) return false;
			if (needcleardata != _o_.needcleardata) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += myzoneid;
		_h_ += userid;
		_h_ += (int)roleid;
		_h_ += (int)flag;
		_h_ += (int)needcleardata;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(myzoneid).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(needcleardata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SendRoleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = myzoneid - _o_.myzoneid;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		_c_ = needcleardata - _o_.needcleardata;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

