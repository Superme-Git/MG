
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAnsQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAnsQuestion extends __CAnsQuestion__ {
	@Override
	protected void process() {
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805528;

	public int getType() {
		return 805528;
	}

	public long npckey; // npckey
	public int questionid; // 问题ID
	public int answer; // 答案选项
	public byte flag;

	public CAnsQuestion() {
	}

	public CAnsQuestion(long _npckey_, int _questionid_, int _answer_, byte _flag_) {
		this.npckey = _npckey_;
		this.questionid = _questionid_;
		this.answer = _answer_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		if (questionid < 0) return false;
		if (answer < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(questionid);
		_os_.marshal(answer);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		questionid = _os_.unmarshal_int();
		answer = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAnsQuestion) {
			CAnsQuestion _o_ = (CAnsQuestion)_o1_;
			if (npckey != _o_.npckey) return false;
			if (questionid != _o_.questionid) return false;
			if (answer != _o_.answer) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += questionid;
		_h_ += answer;
		_h_ += (int)flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(questionid).append(",");
		_sb_.append(answer).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAnsQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = questionid - _o_.questionid;
		if (0 != _c_) return _c_;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

