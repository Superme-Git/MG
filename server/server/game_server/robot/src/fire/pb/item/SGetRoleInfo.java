
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetRoleInfo extends __SGetRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787710;

	public int getType() {
		return 787710;
	}

	public long roleid;
	public java.lang.String rolename;
	public int shape;
	public int school;
	public int level;
	public int equipscore;
	public fire.pb.Bag packinfo;
	public java.util.HashMap<Integer,com.locojoy.base.Octets> tips;

	public SGetRoleInfo() {
		rolename = "";
		packinfo = new fire.pb.Bag();
		tips = new java.util.HashMap<Integer,com.locojoy.base.Octets>();
	}

	public SGetRoleInfo(long _roleid_, java.lang.String _rolename_, int _shape_, int _school_, int _level_, int _equipscore_, fire.pb.Bag _packinfo_, java.util.HashMap<Integer,com.locojoy.base.Octets> _tips_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.shape = _shape_;
		this.school = _school_;
		this.level = _level_;
		this.equipscore = _equipscore_;
		this.packinfo = _packinfo_;
		this.tips = _tips_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		if (!packinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(equipscore);
		_os_.marshal(packinfo);
		_os_.compact_uint32(tips.size());
		for (java.util.Map.Entry<Integer, com.locojoy.base.Octets> _e_ : tips.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		equipscore = _os_.unmarshal_int();
		packinfo.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			tips.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetRoleInfo) {
			SGetRoleInfo _o_ = (SGetRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (equipscore != _o_.equipscore) return false;
			if (!packinfo.equals(_o_.packinfo)) return false;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += shape;
		_h_ += school;
		_h_ += level;
		_h_ += equipscore;
		_h_ += packinfo.hashCode();
		_h_ += tips.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(school).append(",");
		_sb_.append(level).append(",");
		_sb_.append(equipscore).append(",");
		_sb_.append(packinfo).append(",");
		_sb_.append(tips).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

