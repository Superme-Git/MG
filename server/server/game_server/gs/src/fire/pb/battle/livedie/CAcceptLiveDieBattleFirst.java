
package fire.pb.battle.livedie;

import fire.pb.PropRole;
import gnet.link.Onlines;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAcceptLiveDieBattleFirst__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAcceptLiveDieBattleFirst extends __CAcceptLiveDieBattleFirst__ {
	@Override
	protected void process() {
		final long guestid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (guestid <= 0)
			return;
		
		//判断下战书的人是否存在
		Long hostid=xtable.Livedie2key.select(guestid);
		//判断是否有下战书的人
		if(hostid==null){
			//提示没有给你下战书的人162129
//			fire.pb.talk.MessageMgr.sendMsgNotify(guestid, 162129, null);
			sendNoFightPlayer(guestid);
			return ;
		}
		//获得下战书信息，判断是否过期
		xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(hostid);
		if(hostliveDieRoleInfo==null){
			sendNoFightPlayer(guestid);
			return ;
		}
		if(System.currentTimeMillis()-hostliveDieRoleInfo.getInvitationtime()>LiveDieMange.getLiveDieTime()){
			sendNoFightPlayer(guestid);
			return ;
		}
		SAcceptLiveDieBattleFirst sAcceptLiveDieBattleFirst=new SAcceptLiveDieBattleFirst();
		sAcceptLiveDieBattleFirst.hostroleid=hostid;
		sAcceptLiveDieBattleFirst.hostrolename=new PropRole(hostid,true).getName();;
		Onlines.getInstance().send(guestid, sAcceptLiveDieBattleFirst);
	}
	
	public void sendNoFightPlayer(long guestid){
		SAcceptLiveDieBattleFirst sAcceptLiveDieBattleFirst=new SAcceptLiveDieBattleFirst();
		sAcceptLiveDieBattleFirst.hostroleid=0;
		sAcceptLiveDieBattleFirst.hostrolename="";
		Onlines.getInstance().send(guestid, sAcceptLiveDieBattleFirst);
	}
	
	
	

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793848;

	public int getType() {
		return 793848;
	}


	public CAcceptLiveDieBattleFirst() {
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
		if (_o1_ instanceof CAcceptLiveDieBattleFirst) {
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

	public int compareTo(CAcceptLiveDieBattleFirst _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

