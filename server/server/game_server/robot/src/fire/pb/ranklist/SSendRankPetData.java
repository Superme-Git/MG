
package fire.pb.ranklist;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRankPetData__ extends mkio.Protocol { }

/** 服务器返回给客户端榜单上宠物的详细信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRankPetData extends __SSendRankPetData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810241;

	public int getType() {
		return 810241;
	}

	public long uniquepetid; // 宠物的唯一id
	public fire.pb.Pet petinfo; // 宠物数据

	public SSendRankPetData() {
		petinfo = new fire.pb.Pet();
	}

	public SSendRankPetData(long _uniquepetid_, fire.pb.Pet _petinfo_) {
		this.uniquepetid = _uniquepetid_;
		this.petinfo = _petinfo_;
	}

	public final boolean _validator_() {
		if (!petinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(uniquepetid);
		_os_.marshal(petinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniquepetid = _os_.unmarshal_long();
		petinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRankPetData) {
			SSendRankPetData _o_ = (SSendRankPetData)_o1_;
			if (uniquepetid != _o_.uniquepetid) return false;
			if (!petinfo.equals(_o_.petinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniquepetid;
		_h_ += petinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniquepetid).append(",");
		_sb_.append(petinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

