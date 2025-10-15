
package fire.msp.task;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MSpecailQuestUseItemVerifyPosSucc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MSpecailQuestUseItemVerifyPosSucc extends __MSpecailQuestUseItemVerifyPosSucc__ {
	@Override
	protected void process() {
		// protocol handle
//		new xdb.Procedure() {
//			@Override
//			protected boolean process() {
//				if ( specailquestid == SpecialQuestID.factiondailyquestid ) {
//					TaskFactionDaily task = TaskFactionDaily.getTaskFactionDaily( roleid, false );
//					if ( task == null )
//						return false;
//					return task.onUseItemSucc() && fire.pb.item.Module.getInstance().unlockAndDelItem(roleid, useitemid);
//				}
//				return false;
////				new fire.pb.specialquest.StepStateProc(roleid, specailquestid
////						, SpecialQuestState.DONE).call();
////				fire.pb.specialquest.SpecialQuestManager.getInstance().refreshQuestState(roleid,
////						specailquestid, fire.pb.specialquest.SpecialQuestState.DONE );
////				// 
////				return fire.pb.item.Module.getInstance().unlockAndDelItem( roleid );
//			}
//		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739898;

	public int getType() {
		return 739898;
	}

	public long roleid;
	public int specailquestid;
	public int useitemid;

	public MSpecailQuestUseItemVerifyPosSucc() {
	}

	public MSpecailQuestUseItemVerifyPosSucc(long _roleid_, int _specailquestid_, int _useitemid_) {
		this.roleid = _roleid_;
		this.specailquestid = _specailquestid_;
		this.useitemid = _useitemid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(specailquestid);
		_os_.marshal(useitemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		specailquestid = _os_.unmarshal_int();
		useitemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MSpecailQuestUseItemVerifyPosSucc) {
			MSpecailQuestUseItemVerifyPosSucc _o_ = (MSpecailQuestUseItemVerifyPosSucc)_o1_;
			if (roleid != _o_.roleid) return false;
			if (specailquestid != _o_.specailquestid) return false;
			if (useitemid != _o_.useitemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += specailquestid;
		_h_ += useitemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(specailquestid).append(",");
		_sb_.append(useitemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MSpecailQuestUseItemVerifyPosSucc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = specailquestid - _o_.specailquestid;
		if (0 != _c_) return _c_;
		_c_ = useitemid - _o_.useitemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

