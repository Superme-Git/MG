
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAnswerActivityQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAnswerActivityQuestion extends __CAnswerActivityQuestion__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795533;

	public int getType() {
		return 795533;
	}

	public int questionid;
	public int answerid; // [1,) 最多几个答案选项？
	public int xiangguanid; // 可能需要的相关id

	public CAnswerActivityQuestion() {
	}

	public CAnswerActivityQuestion(int _questionid_, int _answerid_, int _xiangguanid_) {
		this.questionid = _questionid_;
		this.answerid = _answerid_;
		this.xiangguanid = _xiangguanid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questionid);
		_os_.marshal(answerid);
		_os_.marshal(xiangguanid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questionid = _os_.unmarshal_int();
		answerid = _os_.unmarshal_int();
		xiangguanid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAnswerActivityQuestion) {
			CAnswerActivityQuestion _o_ = (CAnswerActivityQuestion)_o1_;
			if (questionid != _o_.questionid) return false;
			if (answerid != _o_.answerid) return false;
			if (xiangguanid != _o_.xiangguanid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questionid;
		_h_ += answerid;
		_h_ += xiangguanid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questionid).append(",");
		_sb_.append(answerid).append(",");
		_sb_.append(xiangguanid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAnswerActivityQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questionid - _o_.questionid;
		if (0 != _c_) return _c_;
		_c_ = answerid - _o_.answerid;
		if (0 != _c_) return _c_;
		_c_ = xiangguanid - _o_.xiangguanid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

