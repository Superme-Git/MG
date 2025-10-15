
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CNotifyTeamMemeberSubmitItem__ extends mkio.Protocol { }

/** 时光之穴副本 end
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CNotifyTeamMemeberSubmitItem extends __CNotifyTeamMemeberSubmitItem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805477;

	public int getType() {
		return 805477;
	}

	public int questid; // 提交的相关的任务
	public long npckey; // 提交的npc
	public int submittype; // 提交类型: 道具0 或者宠物1

	public CNotifyTeamMemeberSubmitItem() {
	}

	public CNotifyTeamMemeberSubmitItem(int _questid_, long _npckey_, int _submittype_) {
		this.questid = _questid_;
		this.npckey = _npckey_;
		this.submittype = _submittype_;
	}

	public final boolean _validator_() {
		if (questid < 0) return false;
		if (npckey <= 0) return false;
		if (submittype < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(npckey);
		_os_.marshal(submittype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CNotifyTeamMemeberSubmitItem) {
			CNotifyTeamMemeberSubmitItem _o_ = (CNotifyTeamMemeberSubmitItem)_o1_;
			if (questid != _o_.questid) return false;
			if (npckey != _o_.npckey) return false;
			if (submittype != _o_.submittype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += (int)npckey;
		_h_ += submittype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CNotifyTeamMemeberSubmitItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = submittype - _o_.submittype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

