
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqQuestion__ extends mkio.Protocol { }

/** 向NPC请求问题
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqQuestion extends __CReqQuestion__ {
	@Override
	protected void process() {
		// protocol handle
		final long	roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (!validate(roleid)) {
			return;
		}
//		final fire.pb.mission.schoolanswerques.PReqQuestion pReq=new fire.pb.mission.schoolanswerques.PReqQuestion(npckey, roleid);
//		pReq.submit();
	}
	protected boolean validate(long roleid) {

		if (roleid < 0)
			return false;
//		if (xtable.Properties.select(roleid).getLevel() < 10)
//			return false;
		// TODO:需要判断玩家和npc的位置关系吗？
		
		
		return true;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795439;

	public int getType() {
		return 795439;
	}

	public long npckey; // npckey为npc的唯一ID

	public CReqQuestion() {
	}

	public CReqQuestion(long _npckey_) {
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqQuestion) {
			CReqQuestion _o_ = (CReqQuestion)_o1_;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

