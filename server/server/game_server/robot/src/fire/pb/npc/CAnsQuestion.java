
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAnsQuestion__ extends mkio.Protocol { }

/** 向NPC请求问题的答案
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAnsQuestion extends __CAnsQuestion__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795441;

	public int getType() {
		return 795441;
	}

	public long npckey; // npckey为npc的唯一ID
	public int questionid;
	public int answer; // 玩家的回答

	public CAnsQuestion() {
	}

	public CAnsQuestion(long _npckey_, int _questionid_, int _answer_) {
		this.npckey = _npckey_;
		this.questionid = _questionid_;
		this.answer = _answer_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		if (questionid < 0) return false;
		if (answer < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(questionid);
		_os_.marshal(answer);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		questionid = _os_.unmarshal_int();
		answer = _os_.unmarshal_int();
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
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += questionid;
		_h_ += answer;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(questionid).append(",");
		_sb_.append(answer).append(",");
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
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

