
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleScore__ extends mkio.Protocol { }

/** 刷新人物评分的消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleScore extends __SRefreshRoleScore__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799436;

	public int getType() {
		return 799436;
	}

	public final static int TOTAL_SCORE = 1; // 综合评分
	public final static int EQUIP_SCORE = 2; // 装备评分
	public final static int MANY_PET_SCORE = 3; // 多宠物评分
	public final static int PET_SCORE = 4; // 单宠物评分
	public final static int LEVEL_SCORE = 5; // 等级评分
	public final static int XIULIAN_SCORE = 6; // 修炼评分
	public final static int ROLE_SCORE = 7; // 人物评分
	public final static int SKILL_SCORE = 8; // 技能评分

	public java.util.HashMap<Integer,Integer> datas; // 变量值ID，变量值

	public SRefreshRoleScore() {
		datas = new java.util.HashMap<Integer,Integer>();
	}

	public SRefreshRoleScore(java.util.HashMap<Integer,Integer> _datas_) {
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, Integer> _e_ : datas.entrySet()) {
			if (_e_.getKey().intValue() <= 0) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			datas.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleScore) {
			SRefreshRoleScore _o_ = (SRefreshRoleScore)_o1_;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

