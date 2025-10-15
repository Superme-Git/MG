
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetRoleEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetRoleEquip extends __SGetRoleEquip__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787479;

	public int getType() {
		return 787479;
	}

	public java.lang.String rolename;
	public int totalscore;
	public fire.pb.Bag equipinfo;
	public java.util.HashMap<Integer,com.locojoy.base.Octets> tips;
	public java.util.HashMap<Byte,Integer> components;
	public int profession; // 职业 by changhao
	public int rolelevel; // 玩家等级 by changhao
	public long roleid;
	public int shape;

	public SGetRoleEquip() {
		rolename = "";
		equipinfo = new fire.pb.Bag();
		tips = new java.util.HashMap<Integer,com.locojoy.base.Octets>();
		components = new java.util.HashMap<Byte,Integer>();
	}

	public SGetRoleEquip(java.lang.String _rolename_, int _totalscore_, fire.pb.Bag _equipinfo_, java.util.HashMap<Integer,com.locojoy.base.Octets> _tips_, java.util.HashMap<Byte,Integer> _components_, int _profession_, int _rolelevel_, long _roleid_, int _shape_) {
		this.rolename = _rolename_;
		this.totalscore = _totalscore_;
		this.equipinfo = _equipinfo_;
		this.tips = _tips_;
		this.components = _components_;
		this.profession = _profession_;
		this.rolelevel = _rolelevel_;
		this.roleid = _roleid_;
		this.shape = _shape_;
	}

	public final boolean _validator_() {
		if (!equipinfo._validator_()) return false;
		if (roleid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(totalscore);
		_os_.marshal(equipinfo);
		_os_.compact_uint32(tips.size());
		for (java.util.Map.Entry<Integer, com.locojoy.base.Octets> _e_ : tips.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(profession);
		_os_.marshal(rolelevel);
		_os_.marshal(roleid);
		_os_.marshal(shape);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolename = _os_.unmarshal_String("UTF-16LE");
		totalscore = _os_.unmarshal_int();
		equipinfo.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			tips.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		profession = _os_.unmarshal_int();
		rolelevel = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		shape = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetRoleEquip) {
			SGetRoleEquip _o_ = (SGetRoleEquip)_o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (totalscore != _o_.totalscore) return false;
			if (!equipinfo.equals(_o_.equipinfo)) return false;
			if (!tips.equals(_o_.tips)) return false;
			if (!components.equals(_o_.components)) return false;
			if (profession != _o_.profession) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (roleid != _o_.roleid) return false;
			if (shape != _o_.shape) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += totalscore;
		_h_ += equipinfo.hashCode();
		_h_ += tips.hashCode();
		_h_ += components.hashCode();
		_h_ += profession;
		_h_ += rolelevel;
		_h_ += (int)roleid;
		_h_ += shape;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(totalscore).append(",");
		_sb_.append(equipinfo).append(",");
		_sb_.append(tips).append(",");
		_sb_.append(components).append(",");
		_sb_.append(profession).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

