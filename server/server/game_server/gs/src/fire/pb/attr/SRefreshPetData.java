
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPetData__ extends mkio.Protocol { }

/** 刷新宠物属性的消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPetData extends __SRefreshPetData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799433;

	public int getType() {
		return 799433;
	}

	public int columnid; // 宠物栏ID，具体定义参见 protocol/pet.xml中的PetColumnTypes
	public int petkey; // 宠物的唯一key
	public java.util.HashMap<Integer,Float> datas; // 变量值ID，变量值

	public SRefreshPetData() {
		datas = new java.util.HashMap<Integer,Float>();
	}

	public SRefreshPetData(int _columnid_, int _petkey_, java.util.HashMap<Integer,Float> _datas_) {
		this.columnid = _columnid_;
		this.petkey = _petkey_;
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		if (columnid < 1) return false;
		if (petkey < 1) return false;
		for (java.util.Map.Entry<Integer, Float> _e_ : datas.entrySet()) {
			if (_e_.getKey().intValue() <= 0) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.marshal(petkey);
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		petkey = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			datas.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPetData) {
			SRefreshPetData _o_ = (SRefreshPetData)_o1_;
			if (columnid != _o_.columnid) return false;
			if (petkey != _o_.petkey) return false;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += petkey;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

