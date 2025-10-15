
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketBrowse__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketBrowse extends __CMarketBrowse__ {
	@Override
	protected void process() {
		// protocol handle
		
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		
		new mkdb.Procedure() {
			
			@Override
			protected boolean process() throws Exception {
				long beginTime = System.currentTimeMillis();
				
				fire.pb.shop.srv.market.IMarket market = fire.pb.shop.srv.market.MarketManager.getInstance();
				fire.pb.shop.srv.market.proxy.MarketTransactionProxy handler = new fire.pb.shop.srv.market.proxy.MarketTransactionProxy(market);
				fire.pb.shop.srv.market.IMarket proxy = (fire.pb.shop.srv.market.IMarket) handler.getProxy();
				proxy.marketBrowse(roleId, firstno, twono, threeno,
						itemtype, limitmin, limitmax, browsetype, currpage, pricesort, issearch);
				
				long endTime = System.currentTimeMillis();
				long excuteTime = endTime - beginTime;
				if (excuteTime > 20) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("拍卖浏览道具用时：").append(excuteTime).append("毫秒");
					fire.pb.shop.srv.market.MarketManager.LOG.error(sbd.toString());
				}
				return true;
			}
			
		}.submit();
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810639;

	public int getType() {
		return 810639;
	}

	public int browsetype; // 浏览类型  1 我要购买 2公示物品
	public int firstno; // 一级页签类型
	public int twono; // 二级页签类型
	public java.util.ArrayList<Integer> threeno; // 三级页签类型
	public int itemtype; // 物品类型
	public int limitmin; // 条件下限
	public int limitmax; // 条件上限
	public int currpage; // 当前页
	public int pricesort; // 价格排序  1升序  2降序
	public int issearch; // 0筛选 1搜索

	public CMarketBrowse() {
		threeno = new java.util.ArrayList<Integer>();
	}

	public CMarketBrowse(int _browsetype_, int _firstno_, int _twono_, java.util.ArrayList<Integer> _threeno_, int _itemtype_, int _limitmin_, int _limitmax_, int _currpage_, int _pricesort_, int _issearch_) {
		this.browsetype = _browsetype_;
		this.firstno = _firstno_;
		this.twono = _twono_;
		this.threeno = _threeno_;
		this.itemtype = _itemtype_;
		this.limitmin = _limitmin_;
		this.limitmax = _limitmax_;
		this.currpage = _currpage_;
		this.pricesort = _pricesort_;
		this.issearch = _issearch_;
	}

	public final boolean _validator_() {
		if (browsetype < 1 || browsetype > 2) return false;
		if (firstno < 1 || firstno > 20) return false;
		if (currpage < 1) return false;
		if (pricesort < 1 || pricesort > 2) return false;
		if (issearch < 0 || issearch > 1) return false;
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
		_os_.marshal(pricesort);
		_os_.marshal(issearch);
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
		pricesort = _os_.unmarshal_int();
		issearch = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketBrowse) {
			CMarketBrowse _o_ = (CMarketBrowse)_o1_;
			if (browsetype != _o_.browsetype) return false;
			if (firstno != _o_.firstno) return false;
			if (twono != _o_.twono) return false;
			if (!threeno.equals(_o_.threeno)) return false;
			if (itemtype != _o_.itemtype) return false;
			if (limitmin != _o_.limitmin) return false;
			if (limitmax != _o_.limitmax) return false;
			if (currpage != _o_.currpage) return false;
			if (pricesort != _o_.pricesort) return false;
			if (issearch != _o_.issearch) return false;
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
		_h_ += pricesort;
		_h_ += issearch;
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
		_sb_.append(pricesort).append(",");
		_sb_.append(issearch).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

