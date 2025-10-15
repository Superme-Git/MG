
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SModPetName__ extends mkio.Protocol { }

/** 服务器通知客户端修改宠物的名字
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SModPetName extends __SModPetName__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788451;

	public int getType() {
		return 788451;
	}

	public long roleid; // 角色id
	public int petkey; // 宠物key
	public java.lang.String petname; // 宠物的名字

	public SModPetName() {
		petname = "";
	}

	public SModPetName(long _roleid_, int _petkey_, java.lang.String _petname_) {
		this.roleid = _roleid_;
		this.petkey = _petkey_;
		this.petname = _petname_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		if (petkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(petkey);
		_os_.marshal(petname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		petkey = _os_.unmarshal_int();
		petname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SModPetName) {
			SModPetName _o_ = (SModPetName)_o1_;
			if (roleid != _o_.roleid) return false;
			if (petkey != _o_.petkey) return false;
			if (!petname.equals(_o_.petname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += petkey;
		_h_ += petname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append("T").append(petname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

