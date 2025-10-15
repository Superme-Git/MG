
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOpenClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOpenClan extends __SOpenClan__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808449;

	public int getType() {
		return 808449;
	}

	public int index; // 公会序号
	public java.lang.String clanname; // 公会名称
	public long clanid; // 公会id
	public int clanlevel; // 公会等级
	public int membersnum; // 成员人数
	public java.lang.String clanmaster; // 帮主
	public long masterid; // 帮主ID
	public long vicemasterid; // 副帮主ID
	public java.lang.String clancreator; // 创始人
	public java.lang.String clanaim; // 公会宗旨
	public java.util.ArrayList<fire.pb.clan.ClanMember> memberlist; // 公会成员列表
	public int money; // 公会资金 by changhao
	public java.util.HashMap<Integer,Integer> house; // 2是金库3是药房4是旅店   3个建筑的等级 by changhao
	public java.lang.String oldclanname; // 公会曾用名
	public long clancreatorid; // 创始人ID
	public int autostate; // 公会自动接收申请人入会的状态：0关闭 1开启
	public short requestlevel; // 申请公会等级限制
	public int costeverymoney; // 每天维护需要的资金
	public java.util.HashMap<Integer,Integer> costmax; // 1是大厅 2是金库3是药房4是旅店   4个建筑升级需要的最大资金
	public java.util.HashMap<Integer,Integer> claninstservice; // 公会副本  key 是进入副本服务编号 value 是是否是选中的副本

	public SOpenClan() {
		clanname = "";
		clanmaster = "";
		clancreator = "";
		clanaim = "";
		memberlist = new java.util.ArrayList<fire.pb.clan.ClanMember>();
		house = new java.util.HashMap<Integer,Integer>();
		oldclanname = "";
		costmax = new java.util.HashMap<Integer,Integer>();
		claninstservice = new java.util.HashMap<Integer,Integer>();
	}

	public SOpenClan(int _index_, java.lang.String _clanname_, long _clanid_, int _clanlevel_, int _membersnum_, java.lang.String _clanmaster_, long _masterid_, long _vicemasterid_, java.lang.String _clancreator_, java.lang.String _clanaim_, java.util.ArrayList<fire.pb.clan.ClanMember> _memberlist_, int _money_, java.util.HashMap<Integer,Integer> _house_, java.lang.String _oldclanname_, long _clancreatorid_, int _autostate_, short _requestlevel_, int _costeverymoney_, java.util.HashMap<Integer,Integer> _costmax_, java.util.HashMap<Integer,Integer> _claninstservice_) {
		this.index = _index_;
		this.clanname = _clanname_;
		this.clanid = _clanid_;
		this.clanlevel = _clanlevel_;
		this.membersnum = _membersnum_;
		this.clanmaster = _clanmaster_;
		this.masterid = _masterid_;
		this.vicemasterid = _vicemasterid_;
		this.clancreator = _clancreator_;
		this.clanaim = _clanaim_;
		this.memberlist = _memberlist_;
		this.money = _money_;
		this.house = _house_;
		this.oldclanname = _oldclanname_;
		this.clancreatorid = _clancreatorid_;
		this.autostate = _autostate_;
		this.requestlevel = _requestlevel_;
		this.costeverymoney = _costeverymoney_;
		this.costmax = _costmax_;
		this.claninstservice = _claninstservice_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.ClanMember _v_ : memberlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(index);
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(clanid);
		_os_.marshal(clanlevel);
		_os_.marshal(membersnum);
		_os_.marshal(clanmaster, "UTF-16LE");
		_os_.marshal(masterid);
		_os_.marshal(vicemasterid);
		_os_.marshal(clancreator, "UTF-16LE");
		_os_.marshal(clanaim, "UTF-16LE");
		_os_.compact_uint32(memberlist.size());
		for (fire.pb.clan.ClanMember _v_ : memberlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(money);
		_os_.compact_uint32(house.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : house.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(oldclanname, "UTF-16LE");
		_os_.marshal(clancreatorid);
		_os_.marshal(autostate);
		_os_.marshal(requestlevel);
		_os_.marshal(costeverymoney);
		_os_.compact_uint32(costmax.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : costmax.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(claninstservice.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : claninstservice.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		index = _os_.unmarshal_int();
		clanname = _os_.unmarshal_String("UTF-16LE");
		clanid = _os_.unmarshal_long();
		clanlevel = _os_.unmarshal_int();
		membersnum = _os_.unmarshal_int();
		clanmaster = _os_.unmarshal_String("UTF-16LE");
		masterid = _os_.unmarshal_long();
		vicemasterid = _os_.unmarshal_long();
		clancreator = _os_.unmarshal_String("UTF-16LE");
		clanaim = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.ClanMember _v_ = new fire.pb.clan.ClanMember();
			_v_.unmarshal(_os_);
			memberlist.add(_v_);
		}
		money = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			house.put(_k_, _v_);
		}
		oldclanname = _os_.unmarshal_String("UTF-16LE");
		clancreatorid = _os_.unmarshal_long();
		autostate = _os_.unmarshal_int();
		requestlevel = _os_.unmarshal_short();
		costeverymoney = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			costmax.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			claninstservice.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOpenClan) {
			SOpenClan _o_ = (SOpenClan)_o1_;
			if (index != _o_.index) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (clanid != _o_.clanid) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (membersnum != _o_.membersnum) return false;
			if (!clanmaster.equals(_o_.clanmaster)) return false;
			if (masterid != _o_.masterid) return false;
			if (vicemasterid != _o_.vicemasterid) return false;
			if (!clancreator.equals(_o_.clancreator)) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			if (!memberlist.equals(_o_.memberlist)) return false;
			if (money != _o_.money) return false;
			if (!house.equals(_o_.house)) return false;
			if (!oldclanname.equals(_o_.oldclanname)) return false;
			if (clancreatorid != _o_.clancreatorid) return false;
			if (autostate != _o_.autostate) return false;
			if (requestlevel != _o_.requestlevel) return false;
			if (costeverymoney != _o_.costeverymoney) return false;
			if (!costmax.equals(_o_.costmax)) return false;
			if (!claninstservice.equals(_o_.claninstservice)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += index;
		_h_ += clanname.hashCode();
		_h_ += (int)clanid;
		_h_ += clanlevel;
		_h_ += membersnum;
		_h_ += clanmaster.hashCode();
		_h_ += (int)masterid;
		_h_ += (int)vicemasterid;
		_h_ += clancreator.hashCode();
		_h_ += clanaim.hashCode();
		_h_ += memberlist.hashCode();
		_h_ += money;
		_h_ += house.hashCode();
		_h_ += oldclanname.hashCode();
		_h_ += (int)clancreatorid;
		_h_ += autostate;
		_h_ += requestlevel;
		_h_ += costeverymoney;
		_h_ += costmax.hashCode();
		_h_ += claninstservice.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index).append(",");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append(clanid).append(",");
		_sb_.append(clanlevel).append(",");
		_sb_.append(membersnum).append(",");
		_sb_.append("T").append(clanmaster.length()).append(",");
		_sb_.append(masterid).append(",");
		_sb_.append(vicemasterid).append(",");
		_sb_.append("T").append(clancreator.length()).append(",");
		_sb_.append("T").append(clanaim.length()).append(",");
		_sb_.append(memberlist).append(",");
		_sb_.append(money).append(",");
		_sb_.append(house).append(",");
		_sb_.append("T").append(oldclanname.length()).append(",");
		_sb_.append(clancreatorid).append(",");
		_sb_.append(autostate).append(",");
		_sb_.append(requestlevel).append(",");
		_sb_.append(costeverymoney).append(",");
		_sb_.append(costmax).append(",");
		_sb_.append(claninstservice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

