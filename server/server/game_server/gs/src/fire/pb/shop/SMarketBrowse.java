
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketBrowse__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketBrowse extends __SMarketBrowse__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810640;

	public int getType() {
		return 810640;
	}

	public int browsetype; // 浏览类型  1 我要购买 2公示物品
	public int firstno; // 一级页签类型
	public int twono; // 二级页签类型
	public java.util.ArrayList<Integer> threeno; // 三级页签类型
	public int itemtype; // 物品类型
	public int limitmin; // 条件下限
	public int limitmax; // 条件上限
	public int currpage; // 当前页
	public int totalpage; // 总页数
	public java.util.ArrayList<fire.pb.shop.MarketGoods> goodslist; // 查看物品列表
	public int pricesort; // 价格排序  1升序  2降序

	public SMarketBrowse() {
		threeno = new java.util.ArrayList<Integer>();
		goodslist = new java.util.ArrayList<fire.pb.shop.MarketGoods>();
	}

	public SMarketBrowse(int _browsetype_, int _firstno_, int _twono_, java.util.ArrayList<Integer> _threeno_, int _itemtype_, int _limitmin_, int _limitmax_, int _currpage_, int _totalpage_, java.util.ArrayList<fire.pb.shop.MarketGoods> _goodslist_, int _pricesort_) {
		this.browsetype = _browsetype_;
		this.firstno = _firstno_;
		this.twono = _twono_;
		this.threeno = _threeno_;
		this.itemtype = _itemtype_;
		this.limitmin = _limitmin_;
		this.limitmax = _limitmax_;
		this.currpage = _currpage_;
		this.totalpage = _totalpage_;
		this.goodslist = _goodslist_;
		this.pricesort = _pricesort_;
	}

	public final boolean _validator_() {
		if (browsetype < 1 || browsetype > 2) return false;
		if (firstno < 1 || firstno > 20) return false;
		if (currpage < 1) return false;
		if (totalpage < 1) return false;
		for (fire.pb.shop.MarketGoods _v_ : goodslist)
			if (!_v_._validator_()) return false;
		if (pricesort < 1 || pricesort > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(browsetype);
		_os_.marshal(firstno);
		_os_.marshal(twono);
		_os_.compact_uint32(threeno.size());
		for (Integer _v_ : threeno) {
			_os_.marshal(_v_);
		}
		_os_.marshal(itemtype);
		_os_.marshal(limitmin);
		_os_.marshal(limitmax);
		_os_.marshal(currpage);
		_os_.marshal(totalpage);
		_os_.compact_uint32(goodslist.size());
		for (fire.pb.shop.MarketGoods _v_ : goodslist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(pricesort);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		browsetype = _os_.unmarshal_int();
		firstno = _os_.unmarshal_int();
		twono = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			threeno.add(_v_);
		}
		itemtype = _os_.unmarshal_int();
		limitmin = _os_.unmarshal_int();
		limitmax = _os_.unmarshal_int();
		currpage = _os_.unmarshal_int();
		totalpage = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.MarketGoods _v_ = new fire.pb.shop.MarketGoods();
			_v_.unmarshal(_os_);
			goodslist.add(_v_);
		}
		pricesort = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketBrowse) {
			SMarketBrowse _o_ = (SMarketBrowse)_o1_;
			if (browsetype != _o_.browsetype) return false;
			if (firstno != _o_.firstno) return false;
			if (twono != _o_.twono) return false;
			if (!threeno.equals(_o_.threeno)) return false;
			if (itemtype != _o_.itemtype) return false;
			if (limitmin != _o_.limitmin) return false;
			if (limitmax != _o_.limitmax) return false;
			if (currpage != _o_.currpage) return false;
			if (totalpage != _o_.totalpage) return false;
			if (!goodslist.equals(_o_.goodslist)) return false;
			if (pricesort != _o_.pricesort) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += browsetype;
		_h_ += firstno;
		_h_ += twono;
		_h_ += threeno.hashCode();
		_h_ += itemtype;
		_h_ += limitmin;
		_h_ += limitmax;
		_h_ += currpage;
		_h_ += totalpage;
		_h_ += goodslist.hashCode();
		_h_ += pricesort;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(browsetype).append(",");
		_sb_.append(firstno).append(",");
		_sb_.append(twono).append(",");
		_sb_.append(threeno).append(",");
		_sb_.append(itemtype).append(",");
		_sb_.append(limitmin).append(",");
		_sb_.append(limitmax).append(",");
		_sb_.append(currpage).append(",");
		_sb_.append(totalpage).append(",");
		_sb_.append(goodslist).append(",");
		_sb_.append(pricesort).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

