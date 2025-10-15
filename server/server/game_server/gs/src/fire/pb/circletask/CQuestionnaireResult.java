
package fire.pb.circletask;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CQuestionnaireResult__ extends mkio.Protocol { }

/** 问卷调查答案
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CQuestionnaireResult extends __CQuestionnaireResult__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		if (!fire.pb.map.SceneNpcManager.checkDistance(npckey, roleid))
			return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807444;

	public int getType() {
		return 807444;
	}

	public long npckey; // NPCKEY
	public int step; // 调查问卷阶段 30级-1 35级-2
	public java.util.ArrayList<Integer> result; // 答案

	public CQuestionnaireResult() {
		result = new java.util.ArrayList<Integer>();
	}

	public CQuestionnaireResult(long _npckey_, int _step_, java.util.ArrayList<Integer> _result_) {
		this.npckey = _npckey_;
		this.step = _step_;
		this.result = _result_;
	}

	public final boolean _validator_() {
		if (step < 1 || step > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(step);
		_os_.compact_uint32(result.size());
		for (Integer _v_ : result) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		step = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			result.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CQuestionnaireResult) {
			CQuestionnaireResult _o_ = (CQuestionnaireResult)_o1_;
			if (npckey != _o_.npckey) return false;
			if (step != _o_.step) return false;
			if (!result.equals(_o_.result)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += step;
		_h_ += result.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(step).append(",");
		_sb_.append(result).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

