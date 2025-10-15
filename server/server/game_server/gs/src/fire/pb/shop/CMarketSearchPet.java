
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketSearchPet__ extends mkio.Protocol { }

/** 摆摊宠物搜索
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketSearchPet extends __CMarketSearchPet__ {
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
				boolean success = proxy.searchPet(roleId, pettype, levelmin,
						levelmax, pricemin, pricemax, zizhi, skills, totalskillnum, attr, score, sellstate);
				return success;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810662;

	public int getType() {
		return 810662;
	}

	public int pettype; // 类型
	public int levelmin; // 最低等级
	public int levelmax; // 最高等级
	public int pricemin; // 最低价格
	public int pricemax; // 最高价格
	public java.util.ArrayList<fire.pb.shop.MarketSearchAttr> zizhi; // 资质成长
	public java.util.ArrayList<Integer> skills; // 包含技能
	public int totalskillnum; // 技能总数
	public java.util.ArrayList<fire.pb.shop.MarketSearchAttr> attr; // 基础属性
	public int score; // 评分
	public int sellstate; // 出售状态 1上架，2公示

	public CMarketSearchPet() {
		zizhi = new java.util.ArrayList<fire.pb.shop.MarketSearchAttr>();
		skills = new java.util.ArrayList<Integer>();
		attr = new java.util.ArrayList<fire.pb.shop.MarketSearchAttr>();
	}

	public CMarketSearchPet(int _pettype_, int _levelmin_, int _levelmax_, int _pricemin_, int _pricemax_, java.util.ArrayList<fire.pb.shop.MarketSearchAttr> _zizhi_, java.util.ArrayList<Integer> _skills_, int _totalskillnum_, java.util.ArrayList<fire.pb.shop.MarketSearchAttr> _attr_, int _score_, int _sellstate_) {
		this.pettype = _pettype_;
		this.levelmin = _levelmin_;
		this.levelmax = _levelmax_;
		this.pricemin = _pricemin_;
		this.pricemax = _pricemax_;
		this.zizhi = _zizhi_;
		this.skills = _skills_;
		this.totalskillnum = _totalskillnum_;
		this.attr = _attr_;
		this.score = _score_;
		this.sellstate = _sellstate_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.MarketSearchAttr _v_ : zizhi)
			if (!_v_._validator_()) return false;
		for (fire.pb.shop.MarketSearchAttr _v_ : attr)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pettype);
		_os_.marshal(levelmin);
		_os_.marshal(levelmax);
		_os_.marshal(pricemin);
		_os_.marshal(pricemax);
		_os_.compact_uint32(zizhi.size());
		for (fire.pb.shop.MarketSearchAttr _v_ : zizhi) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(skills.size());
		for (Integer _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.marshal(totalskillnum);
		_os_.compact_uint32(attr.size());
		for (fire.pb.shop.MarketSearchAttr _v_ : attr) {
			_os_.marshal(_v_);
		}
		_os_.marshal(score);
		_os_.marshal(sellstate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pettype = _os_.unmarshal_int();
		levelmin = _os_.unmarshal_int();
		levelmax = _os_.unmarshal_int();
		pricemin = _os_.unmarshal_int();
		pricemax = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.MarketSearchAttr _v_ = new fire.pb.shop.MarketSearchAttr();
			_v_.unmarshal(_os_);
			zizhi.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			skills.add(_v_);
		}
		totalskillnum = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.MarketSearchAttr _v_ = new fire.pb.shop.MarketSearchAttr();
			_v_.unmarshal(_os_);
			attr.add(_v_);
		}
		score = _os_.unmarshal_int();
		sellstate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketSearchPet) {
			CMarketSearchPet _o_ = (CMarketSearchPet)_o1_;
			if (pettype != _o_.pettype) return false;
			if (levelmin != _o_.levelmin) return false;
			if (levelmax != _o_.levelmax) return false;
			if (pricemin != _o_.pricemin) return false;
			if (pricemax != _o_.pricemax) return false;
			if (!zizhi.equals(_o_.zizhi)) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (totalskillnum != _o_.totalskillnum) return false;
			if (!attr.equals(_o_.attr)) return false;
			if (score != _o_.score) return false;
			if (sellstate != _o_.sellstate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pettype;
		_h_ += levelmin;
		_h_ += levelmax;
		_h_ += pricemin;
		_h_ += pricemax;
		_h_ += zizhi.hashCode();
		_h_ += skills.hashCode();
		_h_ += totalskillnum;
		_h_ += attr.hashCode();
		_h_ += score;
		_h_ += sellstate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pettype).append(",");
		_sb_.append(levelmin).append(",");
		_sb_.append(levelmax).append(",");
		_sb_.append(pricemin).append(",");
		_sb_.append(pricemax).append(",");
		_sb_.append(zizhi).append(",");
		_sb_.append(skills).append(",");
		_sb_.append(totalskillnum).append(",");
		_sb_.append(attr).append(",");
		_sb_.append(score).append(",");
		_sb_.append(sellstate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

