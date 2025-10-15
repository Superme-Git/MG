
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CModPetName__ extends mkio.Protocol { }

/** 客户端请求修改宠物名字
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CModPetName extends __CModPetName__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788450;

	public int getType() {
		return 788450;
	}

	public final static int NAMELEN_MAX = 6; // 名字的最大长度
	public final static int NAMELEN_MIN = 1; // 名字的最短长度

	public int petkey; // 宠物的key
	public java.lang.String petname; // 宠物的名字

	public CModPetName() {
		petname = "";
	}

	public CModPetName(int _petkey_, java.lang.String _petname_) {
		this.petkey = _petkey_;
		this.petname = _petname_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(petname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		petname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CModPetName) {
			CModPetName _o_ = (CModPetName)_o1_;
			if (petkey != _o_.petkey) return false;
			if (!petname.equals(_o_.petname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += petname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append("T").append(petname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

