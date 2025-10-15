
package fire.pb.npc;
import fire.pb.instancezone.PGiveUpAnswerQuestion;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGiveUpQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGiveUpQuestion extends __CGiveUpQuestion__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		
		if (questiontype == SAskQuestion.GUILD_ANSWER) {
			new PGiveUpAnswerQuestion(roleId, npckey).submit();
			return;
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795523;

	public int getType() {
		return 795523;
	}

	public int questiontype; // 答题类型，根据不同的类型，客户端/服务器可能会有不同的处理
	public long npckey;

	public CGiveUpQuestion() {
	}

	public CGiveUpQuestion(int _questiontype_, long _npckey_) {
		this.questiontype = _questiontype_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questiontype);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questiontype = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGiveUpQuestion) {
			CGiveUpQuestion _o_ = (CGiveUpQuestion)_o1_;
			if (questiontype != _o_.questiontype) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questiontype;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questiontype).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGiveUpQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questiontype - _o_.questiontype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

