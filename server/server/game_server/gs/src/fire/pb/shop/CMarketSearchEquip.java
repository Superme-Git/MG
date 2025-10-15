
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketSearchEquip__ extends mkio.Protocol { }

/** 摆摊珍品装备搜索
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketSearchEquip extends __CMarketSearchEquip__ {
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
				boolean success = proxy.searchEquip(roleId, euqiptype, level,
						pricemin, pricemax, effect, skill, color, basicattr, additionalattr, totalattr, score,
						sellstate);
				return success;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810661;

	public int getType() {
		return 810661;
	}

	public int euqiptype; // 类型
	public int level; // 等级
	public int pricemin; // 最小价格
	public int pricemax; // 最大价格
	public int effect; // 特效
	public int skill; // 特技
	public java.util.ArrayList<Integer> color; // 品质，4紫，5橙
	public java.util.ArrayList<fire.pb.shop.MarketSearchAttr> basicattr; // 基础属性
	public java.util.ArrayList<Integer> additionalattr; // 附加属性
	public int totalattr; // 属性总和
	public int score; // 评分
	public int sellstate; // 出售状态 1上架，2公示

	public CMarketSearchEquip() {
		color = new java.util.ArrayList<Integer>();
		basicattr = new java.util.ArrayList<fire.pb.shop.MarketSearchAttr>();
		additionalattr = new java.util.ArrayList<Integer>();
	}

	public CMarketSearchEquip(int _euqiptype_, int _level_, int _pricemin_, int _pricemax_, int _effect_, int _skill_, java.util.ArrayList<Integer> _color_, java.util.ArrayList<fire.pb.shop.MarketSearchAttr> _basicattr_, java.util.ArrayList<Integer> _additionalattr_, int _totalattr_, int _score_, int _sellstate_) {
		this.euqiptype = _euqiptype_;
		this.level = _level_;
		this.pricemin = _pricemin_;
		this.pricemax = _pricemax_;
		this.effect = _effect_;
		this.skill = _skill_;
		this.color = _color_;
		this.basicattr = _basicattr_;
		this.additionalattr = _additionalattr_;
		this.totalattr = _totalattr_;
		this.score = _score_;
		this.sellstate = _sellstate_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.MarketSearchAttr _v_ : basicattr)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(euqiptype);
		_os_.marshal(level);
		_os_.marshal(pricemin);
		_os_.marshal(pricemax);
		_os_.marshal(effect);
		_os_.marshal(skill);
		_os_.compact_uint32(color.size());
		for (Integer _v_ : color) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(basicattr.size());
		for (fire.pb.shop.MarketSearchAttr _v_ : basicattr) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(additionalattr.size());
		for (Integer _v_ : additionalattr) {
			_os_.marshal(_v_);
		}
		_os_.marshal(totalattr);
		_os_.marshal(score);
		_os_.marshal(sellstate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		euqiptype = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		pricemin = _os_.unmarshal_int();
		pricemax = _os_.unmarshal_int();
		effect = _os_.unmarshal_int();
		skill = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			color.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.MarketSearchAttr _v_ = new fire.pb.shop.MarketSearchAttr();
			_v_.unmarshal(_os_);
			basicattr.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			additionalattr.add(_v_);
		}
		totalattr = _os_.unmarshal_int();
		score = _os_.unmarshal_int();
		sellstate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketSearchEquip) {
			CMarketSearchEquip _o_ = (CMarketSearchEquip)_o1_;
			if (euqiptype != _o_.euqiptype) return false;
			if (level != _o_.level) return false;
			if (pricemin != _o_.pricemin) return false;
			if (pricemax != _o_.pricemax) return false;
			if (effect != _o_.effect) return false;
			if (skill != _o_.skill) return false;
			if (!color.equals(_o_.color)) return false;
			if (!basicattr.equals(_o_.basicattr)) return false;
			if (!additionalattr.equals(_o_.additionalattr)) return false;
			if (totalattr != _o_.totalattr) return false;
			if (score != _o_.score) return false;
			if (sellstate != _o_.sellstate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += euqiptype;
		_h_ += level;
		_h_ += pricemin;
		_h_ += pricemax;
		_h_ += effect;
		_h_ += skill;
		_h_ += color.hashCode();
		_h_ += basicattr.hashCode();
		_h_ += additionalattr.hashCode();
		_h_ += totalattr;
		_h_ += score;
		_h_ += sellstate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(euqiptype).append(",");
		_sb_.append(level).append(",");
		_sb_.append(pricemin).append(",");
		_sb_.append(pricemax).append(",");
		_sb_.append(effect).append(",");
		_sb_.append(skill).append(",");
		_sb_.append(color).append(",");
		_sb_.append(basicattr).append(",");
		_sb_.append(additionalattr).append(",");
		_sb_.append(totalattr).append(",");
		_sb_.append(score).append(",");
		_sb_.append(sellstate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

