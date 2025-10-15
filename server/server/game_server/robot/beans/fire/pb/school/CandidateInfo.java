
package fire.pb.school;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class CandidateInfo implements Marshal {
	public long roleid;
	public java.lang.String rolename;
	public int tickets;
	public java.lang.String words; // 竞选词
	public int shape; // 外形,如果为0的话表示用npc默认的造型就可以了 by changhao
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装 by changhao

	public CandidateInfo() {
		rolename = "";
		words = "";
		components = new java.util.HashMap<Byte,Integer>();
	}

	public CandidateInfo(long _roleid_, java.lang.String _rolename_, int _tickets_, java.lang.String _words_, int _shape_, java.util.HashMap<Byte,Integer> _components_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.tickets = _tickets_;
		this.words = _words_;
		this.shape = _shape_;
		this.components = _components_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(tickets);
		_os_.marshal(words, "UTF-16LE");
		_os_.marshal(shape);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		tickets = _os_.unmarshal_int();
		words = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CandidateInfo) {
			CandidateInfo _o_ = (CandidateInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (tickets != _o_.tickets) return false;
			if (!words.equals(_o_.words)) return false;
			if (shape != _o_.shape) return false;
			if (!components.equals(_o_.components)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += tickets;
		_h_ += words.hashCode();
		_h_ += shape;
		_h_ += components.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(tickets).append(",");
		_sb_.append("T").append(words.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(components).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

