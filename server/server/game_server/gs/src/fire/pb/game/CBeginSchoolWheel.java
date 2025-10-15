
package fire.pb.game;
import java.util.ArrayList;
import java.util.List;

import fire.pb.team.TeamManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBeginSchoolWheel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBeginSchoolWheel extends __CBeginSchoolWheel__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid <= 0) return;
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				TeamManager.getTeamByRoleId(roleid);//锁队伍
				List<Long> list = new ArrayList<Long>();
				list.add(roleid);
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));//锁当前角色
				xbean.SchoolWheel schoolWheel = xtable.Openschoolwheeltable.get(roleid);
				if (schoolWheel == null) {
					schoolWheel = xbean.Pod.newSchoolWheel();
					xtable.Openschoolwheeltable.insert(roleid, schoolWheel);
				}
				
				int itemIndex = GameManager.getInstance().getAwardItemIndex(GameManager.SCHOOLWHEEL);
				if (itemIndex == -1) {
					return false;
				}
				
				schoolWheel.setItemindex(itemIndex);
				schoolWheel.setAwardid(GameManager.SCHOOLWHEEL);
				
				//TODO 扣除转盘花费
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				long ret = bag.subCurrency(-60, MoneyType.MoneyType_ProfContribute, "schoolwheel", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhuanpan, 0);
				if (ret == 0)
					return false;
				
				SBeginSchoolWheel beginWheel = new SBeginSchoolWheel();
				beginWheel.wheelindex = itemIndex;
				
				mkdb.Procedure.psendWhileCommit(roleid, beginWheel);
				
				return true;
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810363;

	public int getType() {
		return 810363;
	}


	public CBeginSchoolWheel() {
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
		if (_o1_ instanceof CBeginSchoolWheel) {
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

	public int compareTo(CBeginSchoolWheel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

