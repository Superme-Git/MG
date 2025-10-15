
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPetGossip__ extends mkio.Protocol { }

/** 战斗内宠物闲话
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPetGossip extends __SPetGossip__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788453;

	public int getType() {
		return 788453;
	}

	public int battleid; // 战斗点位，宠物的话应该是（6-10，16-20）
	public int chatindex; // 闲话的序号（0-3）

	public SPetGossip() {
	}

	public SPetGossip(int _battleid_, int _chatindex_) {
		this.battleid = _battleid_;
		this.chatindex = _chatindex_;
	}

	public final boolean _validator_() {
		if (battleid < 1) return false;
		if (chatindex < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(battleid);
		_os_.marshal(chatindex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		battleid = _os_.unmarshal_int();
		chatindex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPetGossip) {
			SPetGossip _o_ = (SPetGossip)_o1_;
			if (battleid != _o_.battleid) return false;
			if (chatindex != _o_.chatindex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += battleid;
		_h_ += chatindex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleid).append(",");
		_sb_.append(chatindex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPetGossip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = battleid - _o_.battleid;
		if (0 != _c_) return _c_;
		_c_ = chatindex - _o_.chatindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

