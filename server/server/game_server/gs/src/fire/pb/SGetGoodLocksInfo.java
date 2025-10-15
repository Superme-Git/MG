
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetGoodLocksInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetGoodLocksInfo extends __SGetGoodLocksInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786581;

	public int getType() {
		return 786581;
	}

	public java.lang.String password; // 安全锁密码
	public long forcedelpdtime; // 强制删除密码时间
	public long forcedelendtime; // 强制删除密码结束时间
	public byte isfistloginofday; // 是否第一次登陆
	public byte errortimes; // 密码输入错误次数
	public long lockendtime; // 锁定结束时间点
	public byte isopensafelock; // 是否开启了道具安全锁

	public SGetGoodLocksInfo() {
		password = "";
	}

	public SGetGoodLocksInfo(java.lang.String _password_, long _forcedelpdtime_, long _forcedelendtime_, byte _isfistloginofday_, byte _errortimes_, long _lockendtime_, byte _isopensafelock_) {
		this.password = _password_;
		this.forcedelpdtime = _forcedelpdtime_;
		this.forcedelendtime = _forcedelendtime_;
		this.isfistloginofday = _isfistloginofday_;
		this.errortimes = _errortimes_;
		this.lockendtime = _lockendtime_;
		this.isopensafelock = _isopensafelock_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(password, "UTF-16LE");
		_os_.marshal(forcedelpdtime);
		_os_.marshal(forcedelendtime);
		_os_.marshal(isfistloginofday);
		_os_.marshal(errortimes);
		_os_.marshal(lockendtime);
		_os_.marshal(isopensafelock);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		password = _os_.unmarshal_String("UTF-16LE");
		forcedelpdtime = _os_.unmarshal_long();
		forcedelendtime = _os_.unmarshal_long();
		isfistloginofday = _os_.unmarshal_byte();
		errortimes = _os_.unmarshal_byte();
		lockendtime = _os_.unmarshal_long();
		isopensafelock = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetGoodLocksInfo) {
			SGetGoodLocksInfo _o_ = (SGetGoodLocksInfo)_o1_;
			if (!password.equals(_o_.password)) return false;
			if (forcedelpdtime != _o_.forcedelpdtime) return false;
			if (forcedelendtime != _o_.forcedelendtime) return false;
			if (isfistloginofday != _o_.isfistloginofday) return false;
			if (errortimes != _o_.errortimes) return false;
			if (lockendtime != _o_.lockendtime) return false;
			if (isopensafelock != _o_.isopensafelock) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += password.hashCode();
		_h_ += (int)forcedelpdtime;
		_h_ += (int)forcedelendtime;
		_h_ += (int)isfistloginofday;
		_h_ += (int)errortimes;
		_h_ += (int)lockendtime;
		_h_ += (int)isopensafelock;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(password.length()).append(",");
		_sb_.append(forcedelpdtime).append(",");
		_sb_.append(forcedelendtime).append(",");
		_sb_.append(isfistloginofday).append(",");
		_sb_.append(errortimes).append(",");
		_sb_.append(lockendtime).append(",");
		_sb_.append(isopensafelock).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

