
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetPetcolumnInfo__ extends mkio.Protocol { }

/** 服务器发送给客户端 宠物栏信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetPetcolumnInfo extends __SGetPetcolumnInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788447;

	public int getType() {
		return 788447;
	}

	public int columnid; // 宠物栏id
	public java.util.ArrayList<fire.pb.Pet> pets; // 宠物信息
	public int colunmsize;

	public SGetPetcolumnInfo() {
		pets = new java.util.ArrayList<fire.pb.Pet>();
	}

	public SGetPetcolumnInfo(int _columnid_, java.util.ArrayList<fire.pb.Pet> _pets_, int _colunmsize_) {
		this.columnid = _columnid_;
		this.pets = _pets_;
		this.colunmsize = _colunmsize_;
	}

	public final boolean _validator_() {
		if (columnid < 1) return false;
		for (fire.pb.Pet _v_ : pets)
			if (!_v_._validator_()) return false;
		if (colunmsize < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.compact_uint32(pets.size());
		for (fire.pb.Pet _v_ : pets) {
			_os_.marshal(_v_);
		}
		_os_.marshal(colunmsize);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.Pet _v_ = new fire.pb.Pet();
			_v_.unmarshal(_os_);
			pets.add(_v_);
		}
		colunmsize = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetPetcolumnInfo) {
			SGetPetcolumnInfo _o_ = (SGetPetcolumnInfo)_o1_;
			if (columnid != _o_.columnid) return false;
			if (!pets.equals(_o_.pets)) return false;
			if (colunmsize != _o_.colunmsize) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += pets.hashCode();
		_h_ += colunmsize;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(pets).append(",");
		_sb_.append(colunmsize).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

