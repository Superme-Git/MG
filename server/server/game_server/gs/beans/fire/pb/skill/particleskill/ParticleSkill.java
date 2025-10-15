
package fire.pb.skill.particleskill;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ParticleSkill implements Marshal {
	public int id; // 修炼技能id by changhao
	public int level; // 等级 by changhao
	public int maxlevel; // 最大等级 by changhao
	public int exp; // 当前经验 by changhao
	public java.util.HashMap<Integer,Float> effects; // 属性效果 by changhao
	public java.util.HashMap<Integer,Float> nexteffect; // 下一级属性效果 by changhao

	public ParticleSkill() {
		effects = new java.util.HashMap<Integer,Float>();
		nexteffect = new java.util.HashMap<Integer,Float>();
	}

	public ParticleSkill(int _id_, int _level_, int _maxlevel_, int _exp_, java.util.HashMap<Integer,Float> _effects_, java.util.HashMap<Integer,Float> _nexteffect_) {
		this.id = _id_;
		this.level = _level_;
		this.maxlevel = _maxlevel_;
		this.exp = _exp_;
		this.effects = _effects_;
		this.nexteffect = _nexteffect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(level);
		_os_.marshal(maxlevel);
		_os_.marshal(exp);
		_os_.compact_uint32(effects.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(nexteffect.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : nexteffect.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		maxlevel = _os_.unmarshal_int();
		exp = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			effects.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			nexteffect.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ParticleSkill) {
			ParticleSkill _o_ = (ParticleSkill)_o1_;
			if (id != _o_.id) return false;
			if (level != _o_.level) return false;
			if (maxlevel != _o_.maxlevel) return false;
			if (exp != _o_.exp) return false;
			if (!effects.equals(_o_.effects)) return false;
			if (!nexteffect.equals(_o_.nexteffect)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += level;
		_h_ += maxlevel;
		_h_ += exp;
		_h_ += effects.hashCode();
		_h_ += nexteffect.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(level).append(",");
		_sb_.append(maxlevel).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(effects).append(",");
		_sb_.append(nexteffect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

