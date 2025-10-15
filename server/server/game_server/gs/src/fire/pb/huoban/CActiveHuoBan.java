
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CActiveHuoBan__ extends mkio.Protocol { }

/** 解锁伙伴请求
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CActiveHuoBan extends __CActiveHuoBan__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		new PActiveHuoBan(roleId, huobanid, activetype, activetime).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818840;

	public int getType() {
		return 818840;
	}

	public int huobanid; // 伙伴ID
	public int activetype; // 解锁类型 0 功力解锁; 1 银币解锁
	public int activetime; // 解锁时间 0 解锁7天; 1 解锁30天; 2 永久解锁;

	public CActiveHuoBan() {
	}

	public CActiveHuoBan(int _huobanid_, int _activetype_, int _activetime_) {
		this.huobanid = _huobanid_;
		this.activetype = _activetype_;
		this.activetime = _activetime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(huobanid);
		_os_.marshal(activetype);
		_os_.marshal(activetime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huobanid = _os_.unmarshal_int();
		activetype = _os_.unmarshal_int();
		activetime = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CActiveHuoBan) {
			CActiveHuoBan _o_ = (CActiveHuoBan)_o1_;
			if (huobanid != _o_.huobanid) return false;
			if (activetype != _o_.activetype) return false;
			if (activetime != _o_.activetime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobanid;
		_h_ += activetype;
		_h_ += activetime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobanid).append(",");
		_sb_.append(activetype).append(",");
		_sb_.append(activetime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CActiveHuoBan _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = huobanid - _o_.huobanid;
		if (0 != _c_) return _c_;
		_c_ = activetype - _o_.activetype;
		if (0 != _c_) return _c_;
		_c_ = activetime - _o_.activetime;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

