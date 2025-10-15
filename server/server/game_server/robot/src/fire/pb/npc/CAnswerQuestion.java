
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAnswerQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAnswerQuestion extends __CAnswerQuestion__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795521;

	public int getType() {
		return 795521;
	}

	public int questionid;
	public int answerid; // [1,) 最多几个答案选项？
	public int questiontype; // 答题类型，根据不同的类型，客户端/服务器可能会有不同的处理
	public long npckey;
	public int xiangguanid; // 可能需要的相关id

	public CAnswerQuestion() {
	}

	public CAnswerQuestion(int _questionid_, int _answerid_, int _questiontype_, long _npckey_, int _xiangguanid_) {
		this.questionid = _questionid_;
		this.answerid = _answerid_;
		this.questiontype = _questiontype_;
		this.npckey = _npckey_;
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
		_os_.marshal(questiontype);
		_os_.marshal(npckey);
		_os_.marshal(xiangguanid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questionid = _os_.unmarshal_int();
		answerid = _os_.unmarshal_int();
		questiontype = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		xiangguanid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAnswerQuestion) {
			CAnswerQuestion _o_ = (CAnswerQuestion)_o1_;
			if (questionid != _o_.questionid) return false;
			if (answerid != _o_.answerid) return false;
			if (questiontype != _o_.questiontype) return false;
			if (npckey != _o_.npckey) return false;
			if (xiangguanid != _o_.xiangguanid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questionid;
		_h_ += answerid;
		_h_ += questiontype;
		_h_ += (int)npckey;
		_h_ += xiangguanid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questionid).append(",");
		_sb_.append(answerid).append(",");
		_sb_.append(questiontype).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(xiangguanid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAnswerQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questionid - _o_.questionid;
		if (0 != _c_) return _c_;
		_c_ = answerid - _o_.answerid;
		if (0 != _c_) return _c_;
		_c_ = questiontype - _o_.questiontype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = xiangguanid - _o_.xiangguanid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

