
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPetScore__ extends mkio.Protocol { }

/** 宠物评分
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPetScore extends __SRefreshPetScore__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788511;

	public int getType() {
		return 788511;
	}

	public int petkey; // 宠物key
	public int petscore; // 宠物评分
	public int petbasescore; // 一级属性评分

	public SRefreshPetScore() {
	}

	public SRefreshPetScore(int _petkey_, int _petscore_, int _petbasescore_) {
		this.petkey = _petkey_;
		this.petscore = _petscore_;
		this.petbasescore = _petbasescore_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(petscore);
		_os_.marshal(petbasescore);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		petscore = _os_.unmarshal_int();
		petbasescore = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPetScore) {
			SRefreshPetScore _o_ = (SRefreshPetScore)_o1_;
			if (petkey != _o_.petkey) return false;
			if (petscore != _o_.petscore) return false;
			if (petbasescore != _o_.petbasescore) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += petscore;
		_h_ += petbasescore;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(petscore).append(",");
		_sb_.append(petbasescore).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshPetScore _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = petscore - _o_.petscore;
		if (0 != _c_) return _c_;
		_c_ = petbasescore - _o_.petbasescore;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

