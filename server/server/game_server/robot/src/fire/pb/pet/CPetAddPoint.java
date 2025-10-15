
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetAddPoint__ extends mkio.Protocol { }

/** 客户端请求宠物加点
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetAddPoint extends __CPetAddPoint__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788439;

	public int getType() {
		return 788439;
	}

	public int petkey; // 宠物key值,加点的宠物
	public int str; // 力量加点数
	public int iq; // 智力加点数
	public int cons; // 体质加点数
	public int endu; // 耐力加点数
	public int agi; // 敏捷加点数

	public CPetAddPoint() {
	}

	public CPetAddPoint(int _petkey_, int _str_, int _iq_, int _cons_, int _endu_, int _agi_) {
		this.petkey = _petkey_;
		this.str = _str_;
		this.iq = _iq_;
		this.cons = _cons_;
		this.endu = _endu_;
		this.agi = _agi_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (str < 0) return false;
		if (iq < 0) return false;
		if (cons < 0) return false;
		if (endu < 0) return false;
		if (agi < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(str);
		_os_.marshal(iq);
		_os_.marshal(cons);
		_os_.marshal(endu);
		_os_.marshal(agi);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		str = _os_.unmarshal_int();
		iq = _os_.unmarshal_int();
		cons = _os_.unmarshal_int();
		endu = _os_.unmarshal_int();
		agi = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetAddPoint) {
			CPetAddPoint _o_ = (CPetAddPoint)_o1_;
			if (petkey != _o_.petkey) return false;
			if (str != _o_.str) return false;
			if (iq != _o_.iq) return false;
			if (cons != _o_.cons) return false;
			if (endu != _o_.endu) return false;
			if (agi != _o_.agi) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += str;
		_h_ += iq;
		_h_ += cons;
		_h_ += endu;
		_h_ += agi;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(str).append(",");
		_sb_.append(iq).append(",");
		_sb_.append(cons).append(",");
		_sb_.append(endu).append(",");
		_sb_.append(agi).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetAddPoint _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = str - _o_.str;
		if (0 != _c_) return _c_;
		_c_ = iq - _o_.iq;
		if (0 != _c_) return _c_;
		_c_ = cons - _o_.cons;
		if (0 != _c_) return _c_;
		_c_ = endu - _o_.endu;
		if (0 != _c_) return _c_;
		_c_ = agi - _o_.agi;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

