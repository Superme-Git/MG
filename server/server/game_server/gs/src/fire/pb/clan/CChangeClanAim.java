package fire.pb.clan;

import fire.log.beans.FactionOpbean;

import fire.pb.talk.MessageMgr;
import fire.pb.util.StringValidateUtil;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangeClanAim__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangeClanAim extends __CChangeClanAim__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;

		if (!fire.pb.util.StringValidateUtil.checkIllegalWord(newaim)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142261, null);
			return;
		}
		if (!StringValidateUtil.checkValidName(newaim)) {
			MessageMgr.sendMsgNotify(roleid, 145025, null);
			return;
		}
		//需要屏蔽$符号，公告发送到聊天不能有$符号
		if(newaim.contains("$")){
			MessageMgr.sendMsgNotify(roleid, 145025, null);
			return;
		}
		
		
		if (newaim.length() == 0) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145091, null);
			return;
		}
		if (newaim.length() > fire.pb.clan.srv.ClanManage.AIMWORD_MAX) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145112, null);
			return;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 145077, null);
					return false;
				}
				if (clanInfo.getClanmaster() != roleid && clanInfo.getClanvicemaster() != roleid) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 150127, null);
					return false;
				}
				clanInfo.setClanaim(newaim);
				mkdb.Procedure.psendWhileCommit(roleid, new SChangeClanAim(newaim));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 145074, null);
				// 在公会频道发送消息
				String name=xtable.Properties.selectRolename(roleid);
				if (name != null) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160367, 0,
							java.util.Arrays.asList(name, newaim));
				}
				// 运营日志
				xbean.ClanMemberInfo fmi = clanInfo.getMembers().get(roleid);
				FactionOpbean factionOpbean = new FactionOpbean(clanInfo.getKey(), clanInfo.getClanname(), 
						FactionOpbean.TYPE_CHANGE_AIM, fmi.getClanposition(), clanInfo.getMembers().size(),
						clanInfo.getClanlevel(), clanInfo.getHouse().entrySet());
				int weekpoint=fire.pb.clan.srv.ClanManage.getWeekclanpoint(roleid,clanInfo);
				fire.log.YYLogger.factionOPEventLog(roleid, factionOpbean,weekpoint);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808459;

	public int getType() {
		return 808459;
	}

	public java.lang.String newaim;

	public CChangeClanAim() {
		newaim = "";
	}

	public CChangeClanAim(java.lang.String _newaim_) {
		this.newaim = _newaim_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(newaim, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		newaim = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CChangeClanAim) {
			CChangeClanAim _o_ = (CChangeClanAim)_o1_;
			if (!newaim.equals(_o_.newaim)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += newaim.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(newaim.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
