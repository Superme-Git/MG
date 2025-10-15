
package fire.pb.shop;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.BuyAndSell;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketContainerBrowse__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketContainerBrowse extends __SMarketContainerBrowse__ {
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
	public static final int PROTOCOL_TYPE = 810648;

	public int getType() {
		return 810648;
	}

	public int actiontype; // 状态 1容器，2购买关注，3公示关注
	public java.util.ArrayList<fire.pb.shop.MarketGoods> goodslist; // 道具信息

	public SMarketContainerBrowse() {
		goodslist = new java.util.ArrayList<fire.pb.shop.MarketGoods>();
	}

	public SMarketContainerBrowse(int _actiontype_, java.util.ArrayList<fire.pb.shop.MarketGoods> _goodslist_) {
		this.actiontype = _actiontype_;
		this.goodslist = _goodslist_;
	}

	public final boolean _validator_() {
		if (actiontype < 1 || actiontype > 3) return false;
		for (fire.pb.shop.MarketGoods _v_ : goodslist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(actiontype);
		_os_.compact_uint32(goodslist.size());
		for (fire.pb.shop.MarketGoods _v_ : goodslist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		actiontype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.MarketGoods _v_ = new fire.pb.shop.MarketGoods();
			_v_.unmarshal(_os_);
			goodslist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketContainerBrowse) {
			SMarketContainerBrowse _o_ = (SMarketContainerBrowse)_o1_;
			if (actiontype != _o_.actiontype) return false;
			if (!goodslist.equals(_o_.goodslist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += actiontype;
		_h_ += goodslist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(actiontype).append(",");
		_sb_.append(goodslist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

