
package fire.pb.skill.particleskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestLearnParticleSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestLearnParticleSkill extends __CRequestLearnParticleSkill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800505;

	public int getType() {
		return 800505;
	}

	public int id; // 技能ID by changhao
	public int times; // 学习次数 by changhao
	public int itemid; // 使用的道具 by changhao

	public CRequestLearnParticleSkill() {
	}

	public CRequestLearnParticleSkill(int _id_, int _times_, int _itemid_) {
		this.id = _id_;
		this.times = _times_;
		this.itemid = _itemid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(id);
		_os_.marshal(times);
		_os_.marshal(itemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestLearnParticleSkill) {
			CRequestLearnParticleSkill _o_ = (CRequestLearnParticleSkill)_o1_;
			if (id != _o_.id) return false;
			if (times != _o_.times) return false;
			if (itemid != _o_.itemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += times;
		_h_ += itemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(times).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestLearnParticleSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = times - _o_.times;
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

