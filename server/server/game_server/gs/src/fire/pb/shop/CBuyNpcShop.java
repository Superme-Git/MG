
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBuyNpcShop__ extends mkio.Protocol { }

/** NPC购买协议
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBuyNpcShop extends __CBuyNpcShop__ {
	@Override
	protected void process() {
		// protocol handle
		
		// 判断购买条件
		final gnet.link.Role role = gnet.link.Onlines.getInstance().find(this);
		if (role == null)
			return;
		//场景中是否能找到npc
//		if (npckey != 0) {
//			if (!fire.pb.map.SceneNpcManager.checkDistance(npckey, role.getRoleid()))
//				return;
//		}
//		int npcid = 0;
		//常规npc购买
//		if (buytype == ShopBuyType.NORMAL_SHOP) {
//			npcid = fire.pb.map.SceneNpcManager.getNpcIDByKey(npckey);
//			if (npcid < 0) {
//				xdb.Trace.info("找不到该npc id=" + npckey);
//				return;
//			}
//		}
		
		fire.pb.shop.SGoods goods = fire.pb.shop.Module.sGoodsMap.get(goodsid);
		if (null == goods) {
			mkdb.Trace.info("没有该商品[" +  goodsid  + "]");
			return;
		}
		
		// 判断该npc是否有该物品
		if (buytype == ShopBuyType.NORMAL_SHOP) {
			fire.pb.shop.SNpcSale ns = fire.pb.npc.NpcManager.getInstance().getNpcSale(shopid);
			if (ns == null || !ns.getGoodsids().contains(goodsid)) {
				mkdb.Trace.info("NPC并不出售该物品.shopid:" + shopid + "goodsid:" + goodsid);
				return;
			}
		}
		
		fire.pb.shop.utils.ShopParameters params = new fire.pb.shop.utils.ShopParameters();
		params.role = new fire.pb.PropRole(role.getRoleid(), true);
		params.goods = goods;
		params.num = num;
		params.shopId = shopid;
		
		new fire.pb.shop.srv.BuyShopFactory(params, buytype).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810633;

	public int getType() {
		return 810633;
	}

	public int shopid; // 商店序号
	public int goodsid; // 商品id
	public int num; // 购买数量
	public int buytype; // 购买类型

	public CBuyNpcShop() {
	}

	public CBuyNpcShop(int _shopid_, int _goodsid_, int _num_, int _buytype_) {
		this.shopid = _shopid_;
		this.goodsid = _goodsid_;
		this.num = _num_;
		this.buytype = _buytype_;
	}

	public final boolean _validator_() {
		if (shopid < 1) return false;
		if (goodsid < 0) return false;
		if (num < 1) return false;
		if (buytype < 0 || buytype > 11) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shopid);
		_os_.marshal(goodsid);
		_os_.marshal(num);
		_os_.marshal(buytype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shopid = _os_.unmarshal_int();
		goodsid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		buytype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBuyNpcShop) {
			CBuyNpcShop _o_ = (CBuyNpcShop)_o1_;
			if (shopid != _o_.shopid) return false;
			if (goodsid != _o_.goodsid) return false;
			if (num != _o_.num) return false;
			if (buytype != _o_.buytype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += shopid;
		_h_ += goodsid;
		_h_ += num;
		_h_ += buytype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shopid).append(",");
		_sb_.append(goodsid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(buytype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CBuyNpcShop _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = shopid - _o_.shopid;
		if (0 != _c_) return _c_;
		_c_ = goodsid - _o_.goodsid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = buytype - _o_.buytype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

