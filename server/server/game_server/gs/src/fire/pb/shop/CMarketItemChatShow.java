package fire.pb.shop;
import fire.pb.shop.utils.MarketUtils;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketItemChatShow__ extends mkio.Protocol { }

/** 聊天发送摆摊道具协议
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CMarketItemChatShow extends __CMarketItemChatShow__ {

	@Override
	protected void process() {
		if (!MarketUtils.isMarketItemChatShow)
			return;
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		
		
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				long beginTime = System.currentTimeMillis();
				
				fire.pb.shop.srv.market.IMarket market = fire.pb.shop.srv.market.MarketManager.getInstance();
				fire.pb.shop.srv.market.proxy.MarketTransactionProxy handler = new fire.pb.shop.srv.market.proxy.MarketTransactionProxy(
						market);
				fire.pb.shop.srv.market.IMarket proxy = (fire.pb.shop.srv.market.IMarket) handler.getProxy();
				proxy.chatBrowseMarketItem(roleId, id, itemtype);
				
				long endTime = System.currentTimeMillis();
				long excuteTime = endTime - beginTime;
				if (excuteTime > 50) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("展示触发定位拍卖浏览道具用时：").append(excuteTime).append("毫秒");
					fire.pb.shop.srv.market.MarketManager.LOG.error(sbd.toString());
				}
				return true;
			}
		}.submit();
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810665;

	public int getType() {
		return 810665;
	}

	public long id; // 数据库唯一id
	public int itemtype; // 物品类型:  1普通、2宠物、3装备 4范围

	public CMarketItemChatShow() {
	}

	public CMarketItemChatShow(long _id_, int _itemtype_) {
		this.id = _id_;
		this.itemtype = _itemtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(id);
		_os_.marshal(itemtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_long();
		itemtype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketItemChatShow) {
			CMarketItemChatShow _o_ = (CMarketItemChatShow)_o1_;
			if (id != _o_.id) return false;
			if (itemtype != _o_.itemtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)id;
		_h_ += itemtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(itemtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMarketItemChatShow _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		_c_ = itemtype - _o_.itemtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
