
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanInfo implements Marshal {
	public java.lang.String clanname; // 公会名称
	public long clanid; // 公会id
	public long clankey; // 公会key
	public int clanlevel; // 公会等级
	public int membersnum; // 成员人数
	public java.lang.String clanmaster; // 帮主
	public java.lang.String clanvicemaster; // 副帮主
	public java.lang.String clancreator; // 创始人
	public java.lang.String clanrank; // 公会排名
	public java.lang.String clanaim; // 公会宗旨
	public java.util.HashMap<Long,fire.pb.clan.ClanMember> memberlist;
	public java.util.HashMap<Integer,fire.pb.clan.ClanSkill> clanskilllist; // 公会技能列表
	public java.util.HashMap<Long,fire.pb.clan.ClanDMapInfo> clandsceneids; // 公会所有内景副本地图场景id

	public ClanInfo() {
		clanname = "";
		clanmaster = "";
		clanvicemaster = "";
		clancreator = "";
		clanrank = "";
		clanaim = "";
		memberlist = new java.util.HashMap<Long,fire.pb.clan.ClanMember>();
		clanskilllist = new java.util.HashMap<Integer,fire.pb.clan.ClanSkill>();
		clandsceneids = new java.util.HashMap<Long,fire.pb.clan.ClanDMapInfo>();
	}

	public ClanInfo(java.lang.String _clanname_, long _clanid_, long _clankey_, int _clanlevel_, int _membersnum_, java.lang.String _clanmaster_, java.lang.String _clanvicemaster_, java.lang.String _clancreator_, java.lang.String _clanrank_, java.lang.String _clanaim_, java.util.HashMap<Long,fire.pb.clan.ClanMember> _memberlist_, java.util.HashMap<Integer,fire.pb.clan.ClanSkill> _clanskilllist_, java.util.HashMap<Long,fire.pb.clan.ClanDMapInfo> _clandsceneids_) {
		this.clanname = _clanname_;
		this.clanid = _clanid_;
		this.clankey = _clankey_;
		this.clanlevel = _clanlevel_;
		this.membersnum = _membersnum_;
		this.clanmaster = _clanmaster_;
		this.clanvicemaster = _clanvicemaster_;
		this.clancreator = _clancreator_;
		this.clanrank = _clanrank_;
		this.clanaim = _clanaim_;
		this.memberlist = _memberlist_;
		this.clanskilllist = _clanskilllist_;
		this.clandsceneids = _clandsceneids_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Long, fire.pb.clan.ClanMember> _e_ : memberlist.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		for (java.util.Map.Entry<Integer, fire.pb.clan.ClanSkill> _e_ : clanskilllist.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		for (java.util.Map.Entry<Long, fire.pb.clan.ClanDMapInfo> _e_ : clandsceneids.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(clanid);
		_os_.marshal(clankey);
		_os_.marshal(clanlevel);
		_os_.marshal(membersnum);
		_os_.marshal(clanmaster, "UTF-16LE");
		_os_.marshal(clanvicemaster, "UTF-16LE");
		_os_.marshal(clancreator, "UTF-16LE");
		_os_.marshal(clanrank, "UTF-16LE");
		_os_.marshal(clanaim, "UTF-16LE");
		_os_.compact_uint32(memberlist.size());
		for (java.util.Map.Entry<Long, fire.pb.clan.ClanMember> _e_ : memberlist.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clanskilllist.size());
		for (java.util.Map.Entry<Integer, fire.pb.clan.ClanSkill> _e_ : clanskilllist.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clandsceneids.size());
		for (java.util.Map.Entry<Long, fire.pb.clan.ClanDMapInfo> _e_ : clandsceneids.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanname = _os_.unmarshal_String("UTF-16LE");
		clanid = _os_.unmarshal_long();
		clankey = _os_.unmarshal_long();
		clanlevel = _os_.unmarshal_int();
		membersnum = _os_.unmarshal_int();
		clanmaster = _os_.unmarshal_String("UTF-16LE");
		clanvicemaster = _os_.unmarshal_String("UTF-16LE");
		clancreator = _os_.unmarshal_String("UTF-16LE");
		clanrank = _os_.unmarshal_String("UTF-16LE");
		clanaim = _os_.unmarshal_String("UTF-16LE");
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			fire.pb.clan.ClanMember _v_ = new fire.pb.clan.ClanMember();
			_v_.unmarshal(_os_);
			memberlist.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.clan.ClanSkill _v_ = new fire.pb.clan.ClanSkill();
			_v_.unmarshal(_os_);
			clanskilllist.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			fire.pb.clan.ClanDMapInfo _v_ = new fire.pb.clan.ClanDMapInfo();
			_v_.unmarshal(_os_);
			clandsceneids.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanInfo) {
			ClanInfo _o_ = (ClanInfo)_o1_;
			if (!clanname.equals(_o_.clanname)) return false;
			if (clanid != _o_.clanid) return false;
			if (clankey != _o_.clankey) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (membersnum != _o_.membersnum) return false;
			if (!clanmaster.equals(_o_.clanmaster)) return false;
			if (!clanvicemaster.equals(_o_.clanvicemaster)) return false;
			if (!clancreator.equals(_o_.clancreator)) return false;
			if (!clanrank.equals(_o_.clanrank)) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			if (!memberlist.equals(_o_.memberlist)) return false;
			if (!clanskilllist.equals(_o_.clanskilllist)) return false;
			if (!clandsceneids.equals(_o_.clandsceneids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanname.hashCode();
		_h_ += (int)clanid;
		_h_ += (int)clankey;
		_h_ += clanlevel;
		_h_ += membersnum;
		_h_ += clanmaster.hashCode();
		_h_ += clanvicemaster.hashCode();
		_h_ += clancreator.hashCode();
		_h_ += clanrank.hashCode();
		_h_ += clanaim.hashCode();
		_h_ += memberlist.hashCode();
		_h_ += clanskilllist.hashCode();
		_h_ += clandsceneids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append(clanid).append(",");
		_sb_.append(clankey).append(",");
		_sb_.append(clanlevel).append(",");
		_sb_.append(membersnum).append(",");
		_sb_.append("T").append(clanmaster.length()).append(",");
		_sb_.append("T").append(clanvicemaster.length()).append(",");
		_sb_.append("T").append(clancreator.length()).append(",");
		_sb_.append("T").append(clanrank.length()).append(",");
		_sb_.append("T").append(clanaim.length()).append(",");
		_sb_.append(memberlist).append(",");
		_sb_.append(clanskilllist).append(",");
		_sb_.append(clandsceneids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

