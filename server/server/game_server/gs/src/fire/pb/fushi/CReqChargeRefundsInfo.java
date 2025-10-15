
package fire.pb.fushi;

import org.apache.log4j.Logger;

import fire.pb.RoleConfigManager;
import fire.pb.common.SCommon;
import fire.pb.main.Gs;
import gnet.DataBetweenAuAnyAndGS;
import gnet.DeliveryManager;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqChargeRefundsInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqChargeRefundsInfo extends __CReqChargeRefundsInfo__ {
	private static Logger logger=Logger.getLogger("RECHARGE");
	
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		final int userid = ((gnet.link.Dispatch) this.getContext()).userid;
		if (roleid < 0 || userid <0) {
			logger.error("CReqChargeRefundsInfo.userid:" + userid + ",roleid:" + roleid + ".错误！");
			return;
		}
		final SCommon cc = RoleConfigManager.getRoleCommonConfig(309);
		if (cc == null) {
			logger.error("CReqChargeRefundsInfo.充值返还等级通用配置309为null!userid:" + userid + ",roleid:" + roleid);
			return;
		}
		int rolelevel = xtable.Properties.selectLevel(roleid);
		if(rolelevel < (cc == null ? 0 : Integer.parseInt(cc.getValue()))) {
			logger.error("CReqChargeRefundsInfo.等级不足,不返还!userid:" + userid + ",roleid:" + roleid);
			return;
		}
		
		xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(userid);
		if(userinfo == null) {
			logger.error("CReqChargeRefundsInfo.AUUserInfo为null!userid:" + userid + ",roleid:" + roleid);
			return;
		}
		String username = userinfo.getUsername();
		DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
		ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
		ausnd.qtype = 9001;
		ausnd.userid = userid;
		OctetsStream temp = new OctetsStream();
		temp.marshal(username);
		temp.marshal(Gs.serverid);
		ausnd.info = temp;
		DeliveryManager.getInstance().send(ausnd);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812483;

	public int getType() {
		return 812483;
	}


	public CReqChargeRefundsInfo() {
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
		if (_o1_ instanceof CReqChargeRefundsInfo) {
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

	public int compareTo(CReqChargeRefundsInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

