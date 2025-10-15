
package fire.pb.skill;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUpdateInborn__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUpdateInborn extends __CUpdateInborn__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId <= 0)
			return;
		final int apId = this.id;
		if(apId <= 0) return;
		if(flag == 0){
			new mkdb.Procedure(){
				@Override
				protected boolean process(){
					SkillRole srole = new SkillRole(roleId);
					srole.updateInbornWithValid(apId);
					//更新玩家综合实力排行榜
					mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(roleId));
					return true;
				}
			}.submit();
		}else{
			new PInbornWithValidProc(roleId, apId).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800445;

	public int getType() {
		return 800445;
	}

	public int id;
	public byte flag; // 0默认方式  1=一键

	public CUpdateInborn() {
	}

	public CUpdateInborn(int _id_, byte _flag_) {
		this.id = _id_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(id);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUpdateInborn) {
			CUpdateInborn _o_ = (CUpdateInborn)_o1_;
			if (id != _o_.id) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += (int)flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CUpdateInborn _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

