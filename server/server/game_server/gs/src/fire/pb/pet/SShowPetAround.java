
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SShowPetAround__ extends mkio.Protocol { }

/** 服务器通知周围玩家，玩家roleid展示了宠物pet
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SShowPetAround extends __SShowPetAround__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788434;

	public int getType() {
		return 788434;
	}

	public long roleid;
	public int showpetkey;
	public int showpetid;
	public java.lang.String showpetname;
	public byte colour;
	public byte size;
	public byte showeffect;

	public SShowPetAround() {
		showpetname = "";
	}

	public SShowPetAround(long _roleid_, int _showpetkey_, int _showpetid_, java.lang.String _showpetname_, byte _colour_, byte _size_, byte _showeffect_) {
		this.roleid = _roleid_;
		this.showpetkey = _showpetkey_;
		this.showpetid = _showpetid_;
		this.showpetname = _showpetname_;
		this.colour = _colour_;
		this.size = _size_;
		this.showeffect = _showeffect_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		if (showpetkey < 0) return false;
		if (showpetid < 0) return false;
		if (colour < 0) return false;
		if (size < 0) return false;
		if (showeffect < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(showpetkey);
		_os_.marshal(showpetid);
		_os_.marshal(showpetname, "UTF-16LE");
		_os_.marshal(colour);
		_os_.marshal(size);
		_os_.marshal(showeffect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		showpetkey = _os_.unmarshal_int();
		showpetid = _os_.unmarshal_int();
		showpetname = _os_.unmarshal_String("UTF-16LE");
		colour = _os_.unmarshal_byte();
		size = _os_.unmarshal_byte();
		showeffect = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SShowPetAround) {
			SShowPetAround _o_ = (SShowPetAround)_o1_;
			if (roleid != _o_.roleid) return false;
			if (showpetkey != _o_.showpetkey) return false;
			if (showpetid != _o_.showpetid) return false;
			if (!showpetname.equals(_o_.showpetname)) return false;
			if (colour != _o_.colour) return false;
			if (size != _o_.size) return false;
			if (showeffect != _o_.showeffect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += showpetkey;
		_h_ += showpetid;
		_h_ += showpetname.hashCode();
		_h_ += (int)colour;
		_h_ += (int)size;
		_h_ += (int)showeffect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(showpetkey).append(",");
		_sb_.append(showpetid).append(",");
		_sb_.append("T").append(showpetname.length()).append(",");
		_sb_.append(colour).append(",");
		_sb_.append(size).append(",");
		_sb_.append(showeffect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

