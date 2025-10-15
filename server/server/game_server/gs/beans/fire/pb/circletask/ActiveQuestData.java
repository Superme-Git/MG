
package fire.pb.circletask;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ActiveQuestData implements Marshal {
	public int questid; // 任务id
	public int queststate; // 任务状态
	public long dstnpckey; // 目的npckey
	public int dstnpcid; // 目的npcbaseid
	public int dstmapid; // 目的地图id
	public int dstx; // 目的x坐标
	public int dsty; // 目的y坐标
	public int dstitemid; // 目的道具的id"
	public int sumnum; // 累积次数.
	public java.lang.String npcname; // npc名字,目前仅用于强盗宝图
	public long rewardexp; // 奖励的经验
	public long rewardmoney; // 奖励的现金
	public long rewardsmoney; // 奖励的储备金
	public java.util.ArrayList<fire.pb.circletask.RewardItemUnit> rewarditems; // 奖励的道具

	public ActiveQuestData() {
		npcname = "";
		rewarditems = new java.util.ArrayList<fire.pb.circletask.RewardItemUnit>();
	}

	public ActiveQuestData(int _questid_, int _queststate_, long _dstnpckey_, int _dstnpcid_, int _dstmapid_, int _dstx_, int _dsty_, int _dstitemid_, int _sumnum_, java.lang.String _npcname_, long _rewardexp_, long _rewardmoney_, long _rewardsmoney_, java.util.ArrayList<fire.pb.circletask.RewardItemUnit> _rewarditems_) {
		this.questid = _questid_;
		this.queststate = _queststate_;
		this.dstnpckey = _dstnpckey_;
		this.dstnpcid = _dstnpcid_;
		this.dstmapid = _dstmapid_;
		this.dstx = _dstx_;
		this.dsty = _dsty_;
		this.dstitemid = _dstitemid_;
		this.sumnum = _sumnum_;
		this.npcname = _npcname_;
		this.rewardexp = _rewardexp_;
		this.rewardmoney = _rewardmoney_;
		this.rewardsmoney = _rewardsmoney_;
		this.rewarditems = _rewarditems_;
	}

	public final boolean _validator_() {
		if (questid < 1) return false;
		for (fire.pb.circletask.RewardItemUnit _v_ : rewarditems)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(questid);
		_os_.marshal(queststate);
		_os_.marshal(dstnpckey);
		_os_.marshal(dstnpcid);
		_os_.marshal(dstmapid);
		_os_.marshal(dstx);
		_os_.marshal(dsty);
		_os_.marshal(dstitemid);
		_os_.marshal(sumnum);
		_os_.marshal(npcname, "UTF-16LE");
		_os_.marshal(rewardexp);
		_os_.marshal(rewardmoney);
		_os_.marshal(rewardsmoney);
		_os_.compact_uint32(rewarditems.size());
		for (fire.pb.circletask.RewardItemUnit _v_ : rewarditems) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		queststate = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		dstnpcid = _os_.unmarshal_int();
		dstmapid = _os_.unmarshal_int();
		dstx = _os_.unmarshal_int();
		dsty = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		sumnum = _os_.unmarshal_int();
		npcname = _os_.unmarshal_String("UTF-16LE");
		rewardexp = _os_.unmarshal_long();
		rewardmoney = _os_.unmarshal_long();
		rewardsmoney = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.circletask.RewardItemUnit _v_ = new fire.pb.circletask.RewardItemUnit();
			_v_.unmarshal(_os_);
			rewarditems.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ActiveQuestData) {
			ActiveQuestData _o_ = (ActiveQuestData)_o1_;
			if (questid != _o_.questid) return false;
			if (queststate != _o_.queststate) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (dstmapid != _o_.dstmapid) return false;
			if (dstx != _o_.dstx) return false;
			if (dsty != _o_.dsty) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (sumnum != _o_.sumnum) return false;
			if (!npcname.equals(_o_.npcname)) return false;
			if (rewardexp != _o_.rewardexp) return false;
			if (rewardmoney != _o_.rewardmoney) return false;
			if (rewardsmoney != _o_.rewardsmoney) return false;
			if (!rewarditems.equals(_o_.rewarditems)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += queststate;
		_h_ += (int)dstnpckey;
		_h_ += dstnpcid;
		_h_ += dstmapid;
		_h_ += dstx;
		_h_ += dsty;
		_h_ += dstitemid;
		_h_ += sumnum;
		_h_ += npcname.hashCode();
		_h_ += (int)rewardexp;
		_h_ += (int)rewardmoney;
		_h_ += (int)rewardsmoney;
		_h_ += rewarditems.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(queststate).append(",");
		_sb_.append(dstnpckey).append(",");
		_sb_.append(dstnpcid).append(",");
		_sb_.append(dstmapid).append(",");
		_sb_.append(dstx).append(",");
		_sb_.append(dsty).append(",");
		_sb_.append(dstitemid).append(",");
		_sb_.append(sumnum).append(",");
		_sb_.append("T").append(npcname.length()).append(",");
		_sb_.append(rewardexp).append(",");
		_sb_.append(rewardmoney).append(",");
		_sb_.append(rewardsmoney).append(",");
		_sb_.append(rewarditems).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

