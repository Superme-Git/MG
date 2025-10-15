
package fire.pb;
import java.util.Properties;

import gnet.link.Dispatch;
import gnet.link.Kick;
import gnet.link.Onlines;
import mkdb.util.UniqName;
import fire.log.Module;
import fire.pb.main.ConfigManager;
import fire.pb.state.StateManager;

import com.locojoy.base.Octets;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleList__ extends mkio.Protocol { }

/** 客户端发给服务器，请求已有角色列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleList extends __CRoleList__ {
	@Override
	protected void process() {
		
		final int userID = ((Dispatch)this.getContext()).userid;
		if(userID == -1){
			kickUser(-1000);
			 mkdb.Trace.info("CRoleList====userid = -1  踢掉  =============");
			return;
		}
		StateManager.logger.info(new StringBuilder("userID=").append(userID).append(" CRoleList"));
		
		fire.pb.main.ConfigManager manager = fire.pb.main.ConfigManager.getInstance();
		if (manager.isNeedActive()) {
			if (!checkUserAccount(userID)) {
				SUserNeedActive sUserNeedActive = new SUserNeedActive((byte) 0);
				Onlines.getInstance().sendResponse(this, sUserNeedActive);	
				return;
			}
		}
		if(manager.getLoginLimitType() != Module.LIMIT_NORMAL_MODE && manager.getUserIDList().size() != 0){
			if(manager.getLoginLimitType() == Module.LIMIT_BLACLIST_MODE){
				if(manager.getUserIDList().contains(userID)){
					Onlines.getInstance().sendResponse(this, new SCreateRoleError(manager.getErrorType()));	
					return;
				}
			}else if(manager.getLoginLimitType() == Module.LIMIT_WHITELIST_MODE){
				if(!manager.getUserIDList().contains(userID)){
					Onlines.getInstance().sendResponse(this, new SCreateRoleError(manager.getErrorType()));	
					return;
				}
			}
		}
		
		long curTime = System.currentTimeMillis();
		xbean.UserPunish userPunish = xtable.Userpunish.select(userID);
		if (userPunish!=null){
			if (curTime<userPunish.getReleasetime()){
				if (!userPunish.getRecords().isEmpty()) {
					try {
						String reason = userPunish.getRecords().get(userPunish.getRecords().size()-1).getReason();
						long endtime = userPunish.getReleasetime();
						Onlines.getInstance().sendResponse(this, new SGACDKickoutMsg(reason, endtime));
					} catch (Exception e) {
						Module.logger.error("send kickoutmsg error", e);
					}
				}
			//	Onlines.getInstance().kick(roleid, -1000);
				kickUser(-1000);
				//p1.send(this.getConnection());
				return;
			}
		}
		
		Integer blisgm = xtable.Auuserinfo.selectBlisgm(userID);
		boolean isGm = false;
		if (blisgm!=null&&(blisgm==1||blisgm==2)) 
			isGm = true;
		Onlines.getInstance().getConnectedUsers().online(this,isGm);
		Properties prop = ConfigManager.getInstance().getPropConf("sys");
		String value = prop.getProperty("sys.apps.cheat");
		fire.pb.SNotifyShieldState sNotifyShieldState = new SNotifyShieldState((byte) 0);
		if (value!=null&&value.equals("0")){
			sNotifyShieldState.state = 1;
		}
		Onlines.getInstance().sendResponse(this, sNotifyShieldState);	
	}
	
	private boolean checkUserAccount(int userID) {
		if (UniqName.exist("activeuser", String.valueOf(userID))!= UniqName.RPC_NOT_EXISTS) //存在说明已经激活,可以直接进入游戏
			return true;
		
		return false;
	}

	private void kickUser(int error) {
		Kick p1 = new Kick();
		p1.linksid = ((Dispatch)this.getContext()).linksid;
		p1.action = Kick.A_QUICK_CLOSE;
		p1.error = error;
		Onlines.sendProtocl(p1, this.getConnection());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786433;

	public int getType() {
		return 786433;
	}


	public CRoleList() {
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
		if (_o1_ instanceof CRoleList) {
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

	public int compareTo(CRoleList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

