
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetLearnSkillByBook__ extends mkio.Protocol { }

/** 宠物打技能书
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetLearnSkillByBook extends __CPetLearnSkillByBook__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788479;

	public int getType() {
		return 788479;
	}

	public int petkey;
	public int bookkey;

	public CPetLearnSkillByBook() {
	}

	public CPetLearnSkillByBook(int _petkey_, int _bookkey_) {
		this.petkey = _petkey_;
		this.bookkey = _bookkey_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (bookkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(bookkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		bookkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetLearnSkillByBook) {
			CPetLearnSkillByBook _o_ = (CPetLearnSkillByBook)_o1_;
			if (petkey != _o_.petkey) return false;
			if (bookkey != _o_.bookkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += bookkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(bookkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetLearnSkillByBook _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = bookkey - _o_.bookkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

