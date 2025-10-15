
package fire.pb.clan;

import fire.pb.clan.srv.ClanBaseManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.CheckName;
import fire.pb.util.StringValidateUtil;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCreateClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CCreateClan extends __CCreateClan__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || clanname == null || clanaim == null)
			return;
		int resultCode = CheckName.checkValid(clanname);
		if (resultCode == CheckName.WORD_ILLEGALITY) {
			MessageMgr.sendMsgNotify(roleid, 142260, null);
			return;
		} else if (resultCode == CheckName.SPECIAL_WORD_TOO_MANY) {
			MessageMgr.sendMsgNotify(roleid, 142294, null);
			return;
		} else if (resultCode == CheckName.NONE_CHARACTER) {
			MessageMgr.sendMsgNotify(roleid, 146238, null);
			return;
		}

		if (!StringValidateUtil.checkValidName(clanaim)) {
			MessageMgr.sendMsgNotify(roleid, 145025, null);
			return;
		}
		//需要屏蔽$符号，公告发送到聊天不能有$符号
		if(clanaim.contains("$")){
			MessageMgr.sendMsgNotify(roleid, 145025, null);
			return;
		}
		int chineseCnt = 0;
		int otherCnt = 0;
		for (int i = 0; i < clanname.length(); i++) {
			// 匹配汉字
			String regexStr = CheckName.getRegexStr();
			if (clanname.substring(i, i + 1).matches(regexStr)) {
				chineseCnt++;
			} else {
				otherCnt++;
			}
		}
		double total = chineseCnt + otherCnt / 2.0;
		int factionNameLen = 5;
		if (total < 2 || total > factionNameLen) {
			MessageMgr.sendMsgNotify(roleid, 145088, null);
			return;
		}
		if (clanaim.length() == 0) {
			MessageMgr.sendMsgNotify(roleid, 145091, null);
			return;
		}
		if (clanaim.length() > fire.pb.clan.srv.ClanManage.AIMWORD_MAX) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145112, null);
			return;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return ClanBaseManager.getInstance().createClan(roleid, clanname, clanaim);
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808450;

	public int getType() {
		return 808450;
	}

	public java.lang.String clanname; // 公会名字
	public java.lang.String clanaim; // 公会宗旨（公告）

	public CCreateClan() {
		clanname = "";
		clanaim = "";
	}

	public CCreateClan(java.lang.String _clanname_, java.lang.String _clanaim_) {
		this.clanname = _clanname_;
		this.clanaim = _clanaim_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(clanaim, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanname = _os_.unmarshal_String("UTF-16LE");
		clanaim = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CCreateClan) {
			CCreateClan _o_ = (CCreateClan)_o1_;
			if (!clanname.equals(_o_.clanname)) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanname.hashCode();
		_h_ += clanaim.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append("T").append(clanaim.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

