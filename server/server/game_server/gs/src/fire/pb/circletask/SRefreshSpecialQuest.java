
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshSpecialQuest__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshSpecialQuest extends __SRefreshSpecialQuest__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807432;

	public int getType() {
		return 807432;
	}

	public int questid; // 任务id
	public int queststate; // 任务状态
	public int round; // 当前任务的环数
	public int sumnum; // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。
	public int questtype; // 任务类型 （见 SpecialQuestType）
	public int dstmapid; // 目的地图id
	public long dstnpckey; // 目的npc的key
	public java.lang.String dstnpcname; // 目的npc名字
	public int dstnpcid; // 目的npc的id
	public int dstitemid; // 目的道具的id
	public int dstitemnum; // 目的道具的数量，
	public int dstitemid2; // 目的道具2
	public int dstitemidnum2; // 目的道具2的数量
	public int dstx; // 目的坐标x
	public int dsty; // 目的坐标y
	public long validtime; // 任务截止效期（职业中的示威、援救、清理职业，降服）
	public int islogin; // 是否是登陆状态

	public SRefreshSpecialQuest() {
		dstnpcname = "";
	}

	public SRefreshSpecialQuest(int _questid_, int _queststate_, int _round_, int _sumnum_, int _questtype_, int _dstmapid_, long _dstnpckey_, java.lang.String _dstnpcname_, int _dstnpcid_, int _dstitemid_, int _dstitemnum_, int _dstitemid2_, int _dstitemidnum2_, int _dstx_, int _dsty_, long _validtime_, int _islogin_) {
		this.questid = _questid_;
		this.queststate = _queststate_;
		this.round = _round_;
		this.sumnum = _sumnum_;
		this.questtype = _questtype_;
		this.dstmapid = _dstmapid_;
		this.dstnpckey = _dstnpckey_;
		this.dstnpcname = _dstnpcname_;
		this.dstnpcid = _dstnpcid_;
		this.dstitemid = _dstitemid_;
		this.dstitemnum = _dstitemnum_;
		this.dstitemid2 = _dstitemid2_;
		this.dstitemidnum2 = _dstitemidnum2_;
		this.dstx = _dstx_;
		this.dsty = _dsty_;
		this.validtime = _validtime_;
		this.islogin = _islogin_;
	}

	public final boolean _validator_() {
		if (questid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(queststate);
		_os_.marshal(round);
		_os_.marshal(sumnum);
		_os_.marshal(questtype);
		_os_.marshal(dstmapid);
		_os_.marshal(dstnpckey);
		_os_.marshal(dstnpcname, "UTF-16LE");
		_os_.marshal(dstnpcid);
		_os_.marshal(dstitemid);
		_os_.marshal(dstitemnum);
		_os_.marshal(dstitemid2);
		_os_.marshal(dstitemidnum2);
		_os_.marshal(dstx);
		_os_.marshal(dsty);
		_os_.marshal(validtime);
		_os_.marshal(islogin);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		queststate = _os_.unmarshal_int();
		round = _os_.unmarshal_int();
		sumnum = _os_.unmarshal_int();
		questtype = _os_.unmarshal_int();
		dstmapid = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		dstnpcname = _os_.unmarshal_String("UTF-16LE");
		dstnpcid = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		dstitemnum = _os_.unmarshal_int();
		dstitemid2 = _os_.unmarshal_int();
		dstitemidnum2 = _os_.unmarshal_int();
		dstx = _os_.unmarshal_int();
		dsty = _os_.unmarshal_int();
		validtime = _os_.unmarshal_long();
		islogin = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshSpecialQuest) {
			SRefreshSpecialQuest _o_ = (SRefreshSpecialQuest)_o1_;
			if (questid != _o_.questid) return false;
			if (queststate != _o_.queststate) return false;
			if (round != _o_.round) return false;
			if (sumnum != _o_.sumnum) return false;
			if (questtype != _o_.questtype) return false;
			if (dstmapid != _o_.dstmapid) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (!dstnpcname.equals(_o_.dstnpcname)) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (dstitemnum != _o_.dstitemnum) return false;
			if (dstitemid2 != _o_.dstitemid2) return false;
			if (dstitemidnum2 != _o_.dstitemidnum2) return false;
			if (dstx != _o_.dstx) return false;
			if (dsty != _o_.dsty) return false;
			if (validtime != _o_.validtime) return false;
			if (islogin != _o_.islogin) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += queststate;
		_h_ += round;
		_h_ += sumnum;
		_h_ += questtype;
		_h_ += dstmapid;
		_h_ += (int)dstnpckey;
		_h_ += dstnpcname.hashCode();
		_h_ += dstnpcid;
		_h_ += dstitemid;
		_h_ += dstitemnum;
		_h_ += dstitemid2;
		_h_ += dstitemidnum2;
		_h_ += dstx;
		_h_ += dsty;
		_h_ += (int)validtime;
		_h_ += islogin;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(queststate).append(",");
		_sb_.append(round).append(",");
		_sb_.append(sumnum).append(",");
		_sb_.append(questtype).append(",");
		_sb_.append(dstmapid).append(",");
		_sb_.append(dstnpckey).append(",");
		_sb_.append("T").append(dstnpcname.length()).append(",");
		_sb_.append(dstnpcid).append(",");
		_sb_.append(dstitemid).append(",");
		_sb_.append(dstitemnum).append(",");
		_sb_.append(dstitemid2).append(",");
		_sb_.append(dstitemidnum2).append(",");
		_sb_.append(dstx).append(",");
		_sb_.append(dsty).append(",");
		_sb_.append(validtime).append(",");
		_sb_.append(islogin).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

