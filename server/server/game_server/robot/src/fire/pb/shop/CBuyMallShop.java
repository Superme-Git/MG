
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBuyMallShop__ extends mkio.Protocol { }

/** 商城购买协议
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBuyMallShop extends __CBuyMallShop__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810632;

	public int getType() {
		return 810632;
	}

	public int shopid; // 商店序号
	public int taskid; // 同一个道具可以接不同的任务,用taskid来区分
	public int goodsid; // 商品id
	public int num; // 购买数量

	public CBuyMallShop() {
	}

	public CBuyMallShop(int _shopid_, int _taskid_, int _goodsid_, int _num_) {
		this.shopid = _shopid_;
		this.taskid = _taskid_;
		this.goodsid = _goodsid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		if (num < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shopid);
		_os_.marshal(taskid);
		_os_.marshal(goodsid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shopid = _os_.unmarshal_int();
		taskid = _os_.unmarshal_int();
		goodsid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBuyMallShop) {
			CBuyMallShop _o_ = (CBuyMallShop)_o1_;
			if (shopid != _o_.shopid) return false;
			if (taskid != _o_.taskid) return false;
			if (goodsid != _o_.goodsid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += shopid;
		_h_ += taskid;
		_h_ += goodsid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shopid).append(",");
		_sb_.append(taskid).append(",");
		_sb_.append(goodsid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CBuyMallShop _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = shopid - _o_.shopid;
		if (0 != _c_) return _c_;
		_c_ = taskid - _o_.taskid;
		if (0 != _c_) return _c_;
		_c_ = goodsid - _o_.goodsid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

