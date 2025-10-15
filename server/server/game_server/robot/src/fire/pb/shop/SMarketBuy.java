
package fire.pb.shop;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.BuyAndSell;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketBuy__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketBuy extends __SMarketBuy__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role =  LoginRoleMgr.findLoginRole(this);
		if(role == null)
			return;
		if(role.getRoleTask() instanceof BuyAndSell) // 摆摊机器人
			role.onRoleAddProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810642;

	public int getType() {
		return 810642;
	}

	public long id; // 唯一id(浏览时候发给的id)
	public int surplusnum; // 购买完后剩余数量

	public SMarketBuy() {
	}

	public SMarketBuy(long _id_, int _surplusnum_) {
		this.id = _id_;
		this.surplusnum = _surplusnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(id);
		_os_.marshal(surplusnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_long();
		surplusnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketBuy) {
			SMarketBuy _o_ = (SMarketBuy)_o1_;
			if (id != _o_.id) return false;
			if (surplusnum != _o_.surplusnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)id;
		_h_ += surplusnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(surplusnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SMarketBuy _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		_c_ = surplusnum - _o_.surplusnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

