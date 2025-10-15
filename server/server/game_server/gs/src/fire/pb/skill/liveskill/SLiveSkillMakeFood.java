
package fire.pb.skill.liveskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLiveSkillMakeFood__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLiveSkillMakeFood extends __SLiveSkillMakeFood__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800522;

	public int getType() {
		return 800522;
	}

	public int itemid; // 如果成功返回道具ID 否则0
	public int ret; // 返回结果0是制作成功1是制作失败了 by changhao

	public SLiveSkillMakeFood() {
	}

	public SLiveSkillMakeFood(int _itemid_, int _ret_) {
		this.itemid = _itemid_;
		this.ret = _ret_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemid);
		_os_.marshal(ret);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		ret = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLiveSkillMakeFood) {
			SLiveSkillMakeFood _o_ = (SLiveSkillMakeFood)_o1_;
			if (itemid != _o_.itemid) return false;
			if (ret != _o_.ret) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += ret;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(ret).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SLiveSkillMakeFood _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = ret - _o_.ret;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

