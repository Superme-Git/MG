
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReMarketUp__ extends mkio.Protocol { }

/** 摆摊重新上架
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReMarketUp extends __CReMarketUp__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0) 
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				fire.pb.shop.srv.market.IMarket market = fire.pb.shop.srv.market.MarketManager.getInstance();
				fire.pb.shop.srv.market.proxy.MarketTransactionProxy handler = new fire.pb.shop.srv.market.proxy.MarketTransactionProxy(market);
				fire.pb.shop.srv.market.IMarket proxy = (fire.pb.shop.srv.market.IMarket) handler.getProxy();
				boolean success = proxy.remarketUp(itemtype, roleId, id, money);
				if (success) {
					// 刷新前台数据
					fire.pb.shop.srv.market.MarketManager.getInstance().sendMarketContainerByRoleId(roleId);
				}
				return success;
//				return fire.pb.shop.srv.market.MarketManager.getInstance().remarketUp(itemtype, roleId, id);
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810656;

	public int getType() {
		return 810656;
	}

	public int itemtype; // 1普通道具 2宠物 3装备
	public long id; // 唯一id(是我服务器数据库的id,传给前端的目的是在重新上架物品时给我返回)
	public int money; // 价钱

	public CReMarketUp() {
	}

	public CReMarketUp(int _itemtype_, long _id_, int _money_) {
		this.itemtype = _itemtype_;
		this.id = _id_;
		this.money = _money_;
	}

	public final boolean _validator_() {
		if (itemtype < 1 || itemtype > 3) return false;
		if (money < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemtype);
		_os_.marshal(id);
		_os_.marshal(money);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemtype = _os_.unmarshal_int();
		id = _os_.unmarshal_long();
		money = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReMarketUp) {
			CReMarketUp _o_ = (CReMarketUp)_o1_;
			if (itemtype != _o_.itemtype) return false;
			if (id != _o_.id) return false;
			if (money != _o_.money) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemtype;
		_h_ += (int)id;
		_h_ += money;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemtype).append(",");
		_sb_.append(id).append(",");
		_sb_.append(money).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReMarketUp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemtype - _o_.itemtype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		_c_ = money - _o_.money;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

