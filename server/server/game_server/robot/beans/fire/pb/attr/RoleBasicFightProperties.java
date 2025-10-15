
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 角色基础战斗属性
*/
public class RoleBasicFightProperties implements Marshal {
	public short cons; // 体质
	public short iq; // 智力
	public short str; // 力量
	public short endu; // 耐力
	public short agi; // 敏捷
	public java.util.HashMap<Integer,Integer> cons_save; // 已分配体质
	public java.util.HashMap<Integer,Integer> iq_save; // 已分配智力
	public java.util.HashMap<Integer,Integer> str_save; // 已分配力量
	public java.util.HashMap<Integer,Integer> endu_save; // 已分配耐力
	public java.util.HashMap<Integer,Integer> agi_save; // 已分配敏捷

	public RoleBasicFightProperties() {
		cons_save = new java.util.HashMap<Integer,Integer>();
		iq_save = new java.util.HashMap<Integer,Integer>();
		str_save = new java.util.HashMap<Integer,Integer>();
		endu_save = new java.util.HashMap<Integer,Integer>();
		agi_save = new java.util.HashMap<Integer,Integer>();
	}

	public RoleBasicFightProperties(short _cons_, short _iq_, short _str_, short _endu_, short _agi_, java.util.HashMap<Integer,Integer> _cons_save_, java.util.HashMap<Integer,Integer> _iq_save_, java.util.HashMap<Integer,Integer> _str_save_, java.util.HashMap<Integer,Integer> _endu_save_, java.util.HashMap<Integer,Integer> _agi_save_) {
		this.cons = _cons_;
		this.iq = _iq_;
		this.str = _str_;
		this.endu = _endu_;
		this.agi = _agi_;
		this.cons_save = _cons_save_;
		this.iq_save = _iq_save_;
		this.str_save = _str_save_;
		this.endu_save = _endu_save_;
		this.agi_save = _agi_save_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(cons);
		_os_.marshal(iq);
		_os_.marshal(str);
		_os_.marshal(endu);
		_os_.marshal(agi);
		_os_.compact_uint32(cons_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : cons_save.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(iq_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : iq_save.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(str_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : str_save.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(endu_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : endu_save.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(agi_save.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : agi_save.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cons = _os_.unmarshal_short();
		iq = _os_.unmarshal_short();
		str = _os_.unmarshal_short();
		endu = _os_.unmarshal_short();
		agi = _os_.unmarshal_short();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			cons_save.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			iq_save.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			str_save.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			endu_save.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			agi_save.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleBasicFightProperties) {
			RoleBasicFightProperties _o_ = (RoleBasicFightProperties)_o1_;
			if (cons != _o_.cons) return false;
			if (iq != _o_.iq) return false;
			if (str != _o_.str) return false;
			if (endu != _o_.endu) return false;
			if (agi != _o_.agi) return false;
			if (!cons_save.equals(_o_.cons_save)) return false;
			if (!iq_save.equals(_o_.iq_save)) return false;
			if (!str_save.equals(_o_.str_save)) return false;
			if (!endu_save.equals(_o_.endu_save)) return false;
			if (!agi_save.equals(_o_.agi_save)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += cons;
		_h_ += iq;
		_h_ += str;
		_h_ += endu;
		_h_ += agi;
		_h_ += cons_save.hashCode();
		_h_ += iq_save.hashCode();
		_h_ += str_save.hashCode();
		_h_ += endu_save.hashCode();
		_h_ += agi_save.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cons).append(",");
		_sb_.append(iq).append(",");
		_sb_.append(str).append(",");
		_sb_.append(endu).append(",");
		_sb_.append(agi).append(",");
		_sb_.append(cons_save).append(",");
		_sb_.append(iq_save).append(",");
		_sb_.append(str_save).append(",");
		_sb_.append(endu_save).append(",");
		_sb_.append(agi_save).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

