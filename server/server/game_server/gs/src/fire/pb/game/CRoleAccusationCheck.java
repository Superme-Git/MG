
package fire.pb.game;

import mkdb.Procedure;

import fire.pb.RoleConfigManager;
import fire.pb.item.Pack;
import fire.pb.product.Commontext;
import fire.pb.talk.MessageMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleAccusationCheck__ extends mkio.Protocol { }

/** 举报时候客户端给服务器发消息,用于扣费
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleAccusationCheck extends __CRoleAccusationCheck__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid( this );
		if (roleId < 0)
			return;
		new mkdb.Procedure()
		{
			protected boolean process() throws Exception {
				int money = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(332).getValue());
				Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleId,
						fire.pb.item.BagTypes.BAG, false);
				if (money > bag.getMoney()) {
					MessageMgr.sendMsgNotify(roleId, 120025, null);
					return true;
				}
				
				// 扣钱
				if (bag.subMoney(-money, Commontext.REASON_STRING,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jubao, 0) != -money) {
					MessageMgr.sendMsgNotify(roleId, 120025, null);
					return true;
				}
				
				// 扣款成功
				Procedure.psendWhileCommit(roleId, new SRoleAccusationCheck(0));
				return true;
			};
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810372;

	public int getType() {
		return 810372;
	}


	public CRoleAccusationCheck() {
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
		if (_o1_ instanceof CRoleAccusationCheck) {
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

	public int compareTo(CRoleAccusationCheck _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

