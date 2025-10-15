
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CClearApplyList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CClearApplyList extends __CClearApplyList__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null){
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 145077, null);
					return false;
				}
			
				if (clanInfo.getClanmaster() != roleid &&  clanInfo.getClanvicemaster() != roleid){
					return false;
				}
				java.util.Map<Long, Long> applicantsMap = clanInfo.getApplicantsAsData();
				for (java.util.Map.Entry<Long, Long> entry : applicantsMap.entrySet()) {
					mkdb.Procedure proc=new mkdb.Procedure(){
						@Override
						protected boolean process() throws Exception {
							long applyRoleId = entry.getKey();
							fire.pb.clan.srv.ClanBaseManager.getInstance().cancelApply(applyRoleId, clanInfo.getKey());
							return true;
						}
						
					};
					mkdb.Procedure.pexecuteWhileCommit(proc);
				}
				clanInfo.getApplicants().clear();
				psendWhileCommit(roleid, new SRequestApply());
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808455;

	public int getType() {
		return 808455;
	}


	public CClearApplyList() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CClearApplyList) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CClearApplyList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

