
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendVipInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendVipInfo extends __SSendVipInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812489;

	public int getType() {
		return 812489;
	}

	public final static int RIGHT_HUOBAN = 0; // 助战数量
	public final static int RIGHT_STORAGE = 1; // 仓库格数
	public final static int RIGHT_PACKAGE = 2; // 背包格数
	public final static int RIGHT_WEEKLY_DISCOUNT_COUNT = 3; // 周打折限购数量---预留2个。。。
	public final static int RIGHT_EXT_YINFURATE = 4; // 盈福经验转化率
	public final static int RIGHT_DAILY_GOODS_COUNT = 5; // 商会每日限购数量
	public final static int RIGHT_PET_SLOT = 6; // 宠物栏数量
	public final static int RIGHT_SUPPREG_COUNT = 7; // 补签次数

	public int vipexp; // 已充符石数量
	public int viplevel; // vip等级
	public int bounus; // 可领奖励
	public int gotbounus; // 已领奖励
	public java.util.ArrayList<Integer> viprights; // 特权数量

	public SSendVipInfo() {
		viprights = new java.util.ArrayList<Integer>();
	}

	public SSendVipInfo(int _vipexp_, int _viplevel_, int _bounus_, int _gotbounus_, java.util.ArrayList<Integer> _viprights_) {
		this.vipexp = _vipexp_;
		this.viplevel = _viplevel_;
		this.bounus = _bounus_;
		this.gotbounus = _gotbounus_;
		this.viprights = _viprights_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(vipexp);
		_os_.marshal(viplevel);
		_os_.marshal(bounus);
		_os_.marshal(gotbounus);
		_os_.compact_uint32(viprights.size());
		for (Integer _v_ : viprights) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		vipexp = _os_.unmarshal_int();
		viplevel = _os_.unmarshal_int();
		bounus = _os_.unmarshal_int();
		gotbounus = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			viprights.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendVipInfo) {
			SSendVipInfo _o_ = (SSendVipInfo)_o1_;
			if (vipexp != _o_.vipexp) return false;
			if (viplevel != _o_.viplevel) return false;
			if (bounus != _o_.bounus) return false;
			if (gotbounus != _o_.gotbounus) return false;
			if (!viprights.equals(_o_.viprights)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += vipexp;
		_h_ += viplevel;
		_h_ += bounus;
		_h_ += gotbounus;
		_h_ += viprights.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(vipexp).append(",");
		_sb_.append(viplevel).append(",");
		_sb_.append(bounus).append(",");
		_sb_.append(gotbounus).append(",");
		_sb_.append(viprights).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

