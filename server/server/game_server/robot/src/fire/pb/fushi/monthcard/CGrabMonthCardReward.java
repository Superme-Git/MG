
package fire.pb.fushi.monthcard;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGrabMonthCardReward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGrabMonthCardReward extends __CGrabMonthCardReward__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812689;

	public int getType() {
		return 812689;
	}

	public int itemid; // 自由分配道具id by changhao
	public java.util.HashMap<Integer,Integer> rewarddistribution; // 道具分配 道具id value数量 by changhao

	public CGrabMonthCardReward() {
		rewarddistribution = new java.util.HashMap<Integer,Integer>();
	}

	public CGrabMonthCardReward(int _itemid_, java.util.HashMap<Integer,Integer> _rewarddistribution_) {
		this.itemid = _itemid_;
		this.rewarddistribution = _rewarddistribution_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemid);
		_os_.compact_uint32(rewarddistribution.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : rewarddistribution.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			rewarddistribution.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGrabMonthCardReward) {
			CGrabMonthCardReward _o_ = (CGrabMonthCardReward)_o1_;
			if (itemid != _o_.itemid) return false;
			if (!rewarddistribution.equals(_o_.rewarddistribution)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += rewarddistribution.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(rewarddistribution).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

