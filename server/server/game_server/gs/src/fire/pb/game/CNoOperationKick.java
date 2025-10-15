
package fire.pb.game;

import fire.log.LogManager;
import fire.pb.SReturnLogin;
import fire.pb.fushi.DayPayManager;
import fire.pb.fushi.Module;
import mkdb.Procedure;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CNoOperationKick__ extends mkio.Protocol { }

/** 踢掉长时间不操作的玩家
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CNoOperationKick extends __CNoOperationKick__ {
	@Override
	protected void process() {
		// protocol handle
		
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				
				xbean.Properties prop = xtable.Properties.select(roleId);
//				if (prop.getLevel() > 20) {
//					return false;
//				}
				
				boolean ok = false;
				if (Module.GetPayServiceType() == 1 ? true : false) {
					ok = DayPayManager.getInstance().CheckDayPay(roleId, DayPayManager.adddaypatime);
				} else {
					xbean.YbNums ybNums = xtable.Fushinum.select(prop.getUserid());
					int norpresentNum = 0;
					if (ybNums != null) {
						xbean.YbNum ybNum = ybNums.getRoleyb().get(roleId);
						if (ybNum != null) 
							norpresentNum = ybNum.getNopresentnum();
					}
					ok = norpresentNum == 0 ? false : true;
				}
				if (!ok) {// 踢掉
					Procedure.psend(roleId, new SReturnLogin());
					StringBuilder sbd = new StringBuilder();
					sbd.append("角色role=").append(roleId).append(", 长时间不操作踢掉！");
					LogManager.logger.error(sbd.toString());
				}
				return true;
			}
		
		}.submit();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810375;

	public int getType() {
		return 810375;
	}


	public CNoOperationKick() {
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
		if (_o1_ instanceof CNoOperationKick) {
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

	public int compareTo(CNoOperationKick _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

