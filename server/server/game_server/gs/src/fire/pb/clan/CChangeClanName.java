
package fire.pb.clan;

import java.util.Arrays;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.clan.srv.ClanManage;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.talk.MessageMgr;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangeClanName__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangeClanName extends __CChangeClanName__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || newname == null)
			return;
		
		final int userid = ((gnet.link.Dispatch) this.getContext()).userid;
		
		int resultCode = fire.pb.util.CheckName.checkValid(newname);
		if (resultCode == fire.pb.util.CheckName.WORD_ILLEGALITY) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142260, null);
			return;
		} else if (resultCode == fire.pb.util.CheckName.SPECIAL_WORD_TOO_MANY) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142294, null);
			return;
		} else if (resultCode == fire.pb.util.CheckName.NONE_CHARACTER) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 146238, null);
			return;
		}

		if (!fire.pb.util.StringValidateUtil.checkIllegalWord(newname)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145025, null);
			return;
		}
		int chineseCnt = 0;
		int otherCnt = 0;
		for (int i = 0; i < newname.length(); i++) {
			// 匹配汉字
			String regexStr = fire.pb.util.CheckName.getRegexStr();
			if (newname.substring(i, i + 1).matches(regexStr)) {
				chineseCnt++;
			} else {
				otherCnt++;
			}
		}
		double total = chineseCnt + otherCnt / 2.0;
		int factionNameLen = 5;
		if (total < 2 || total > factionNameLen) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145088, null);
			return;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				
				if (!mkdb.util.UniqName.allocate("clan", newname)) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142402, null);
					return false;
				}
				
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 145077, null);
					return false;
				}
				//判断修改权限，只有会长可以修改
				if (clanInfo.getClanmaster() != roleid) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 150127, null);
					return false;
				}
				clanInfo.setOldclanname(clanInfo.getClanname());// 将当前公会名称设置到曾用名中
				clanInfo.setClanname(newname);
				
				int costHearthStone = fire.pb.clan.srv.ClanAuthManager.getInstance().getChangeClanNameCostHearthStone();
				
				if (!fire.pb.fushi.FushiManager.subFushiFromUser(userid, 
						roleid, costHearthStone, 0, 0,
						fire.pb.fushi.FushiConst.REASON_SUB_CHANGEFACTIONNAME,
						YYLoggerTuJingEnum.tujing_Value_gonghuigaiming,true
						)) {
					return false;
				}
				
				for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()) {
					mkdb.Procedure.pexecuteWhileCommit(ClanUtils.addClanTitleByPositionProc(entry.getKey(), clanInfo.getClanname(), entry.getValue().getClanposition()));
				}
				
				mkdb.Procedure.psendWhileCommit(roleid, new SChangeClanName(newname));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160068, null);
				
				//公会频道发送消息
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160396, 0,Arrays.asList(newname));
				//好友频道提示
				MessageMgr.psendSystemMessageToRoles(clanInfo.getMembers().keySet(), 160396, Arrays.asList(newname));
				
				fire.pb.clan.srv.ClanManage.logger.info("玩家角色id "+roleid+"\t修改公会名称，消耗符石 "+costHearthStone);
				
				for (long members : clanInfo.getMembers().keySet())
				{
					mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(members));
				}
				
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PFactionZongHeProc(clanInfo.getKey(), false));
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.srv.PClanUpdateChangeName(clanInfo.getKey(), newname));	
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.clan.fight.PClanFightUpdateChangeName(clanInfo.getKey(), clanInfo.getClanname()));					
				
				String mastername = new PropRole(roleid, true).getName();
				//处理帮派事件
				ClanManage.addDealClanEventInfo(clanInfo,ClanManage.EVENT_CHANGE_NAME,roleid,mastername,newname,"");
								
				//工会竞速榜删除 by changhao
				xbean.ClanProgressRankList clanprogressrankmclist = xtable.Clanprogressranklist.get(RankType.FACTION_COPY);
				if(null != clanprogressrankmclist)
				{
					List<xbean.ClanProgressRankRecord> list = clanprogressrankmclist.getRecords();
					for(xbean.ClanProgressRankRecord record : list)
					{
						if(record.getClankey() == clanInfo.getKey())
						{
							record.setClanname(newname);
							break;
						}
					}
				}
				
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808484;

	public int getType() {
		return 808484;
	}

	public java.lang.String newname;

	public CChangeClanName() {
		newname = "";
	}

	public CChangeClanName(java.lang.String _newname_) {
		this.newname = _newname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(newname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		newname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CChangeClanName) {
			CChangeClanName _o_ = (CChangeClanName)_o1_;
			if (!newname.equals(_o_.newname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += newname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(newname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

