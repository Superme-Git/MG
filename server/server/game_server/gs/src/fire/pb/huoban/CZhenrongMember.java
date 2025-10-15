
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CZhenrongMember__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CZhenrongMember extends __CZhenrongMember__ {
	@Override
	protected void process() {
		// protocol handle		
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		
		if (members.size() > 4)
			return;
		
		java.util.Set<Integer> setMember = new java.util.HashSet<Integer>(members.size());
		setMember.addAll(members);
		
		//判断members里是否有重复的id
		if (setMember.size() != members.size()) {
			return;
		}
		
		new PUpdateHuoBanZhenRong(roleId, 0, zhenyingid, members, PUpdateHuoBanZhenRong.HUOBAN).submit();
		
//		new xdb.Procedure()
//		{
//			protected boolean process() throws Exception {
//				if (members.size() > 4)
//					return false;
//				xdb.Procedure.pexecute(new PUpdateHuoBanZhenRong(roleId, 0, zhenyingid, members, PUpdateHuoBanZhenRong.HUOBAN));
//				return true;
//				boolean suc = huobancol.setFightHuoBans(members);
//				if (suc) {
//					//伙伴阵容更新
//					xdb.Procedure.pexecute(new PUpdateHuoBanZhenRong(roleId, 0, zhenyingid, members, PUpdateHuoBanZhenRong.HUOBAN));
//				}
//				else {
//					SChangeZhenrong snd = new SChangeZhenrong();
//					snd.zhenrong = zhenyingid;
//					snd.zhenfa = 0;
//					snd.huobanlist.clear();;
//					snd.reason = 5;
//					xdb.Procedure.psendWhileCommit(roleId, snd);
//				}
//				return suc;
//			};
//		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818838;

	public int getType() {
		return 818838;
	}

	public int zhenyingid;
	public java.util.ArrayList<Integer> members;

	public CZhenrongMember() {
		members = new java.util.ArrayList<Integer>();
	}

	public CZhenrongMember(int _zhenyingid_, java.util.ArrayList<Integer> _members_) {
		this.zhenyingid = _zhenyingid_;
		this.members = _members_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(zhenyingid);
		_os_.compact_uint32(members.size());
		for (Integer _v_ : members) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zhenyingid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			members.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CZhenrongMember) {
			CZhenrongMember _o_ = (CZhenrongMember)_o1_;
			if (zhenyingid != _o_.zhenyingid) return false;
			if (!members.equals(_o_.members)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zhenyingid;
		_h_ += members.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zhenyingid).append(",");
		_sb_.append(members).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

