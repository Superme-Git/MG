
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class NewDemoResult implements Marshal {
	public final static int HP_CHANGE = 1; // 受击者血量变化，为正是加血，为负是扣血
	public final static int MP_CHANGE = 2; // 受击者魔法变化，为正是加蓝，为负是扣蓝
	public final static int SP_CHANGE = 3; // 受击者怒气变化，为正是加怒气，为负是扣怒气
	public final static int UL_HP_CHANGE = 4; // 受击者当前血上限变化，为正是加，为负是减
	public final static int TARGET_RESULT = 5; // 受击者结果类型，ResultType型枚举值叠加
	public final static int RETURN_HURT = 6; // 受击方造成的反伤值，如果为0则代表没有反伤
	public final static int ATTACK_BACK = 7; // 受击方造成的反击值，如果为0则代表没有反击
	public final static int STEAL_HP = 8; // 攻击方产生的吸血值，如果为0则代表没有吸血
	public final static int ATTACKER_RESULT = 9; // 攻击者结果类型，ResultType型枚举值叠加
	public final static int PROTECTER_ID = 10; // 保护者ID
	public final static int PROTECTER_HP_CHANGE = 11; // 保护者血量变化，为正是加血，为负是扣血（显然是为负的）
	public final static int PROTECTER_RESULT = 12; // 保护者结果类型，ResultType型枚举值叠加
	public final static int ASSISTER_ID = 13; // 合击者ID
	public final static int STEAL_MP = 14; // 攻击方产生的吸蓝值，如果为0则代表没有吸蓝
	public final static int RETURN_HURT_DEATH = 15; // 攻击者因为被反伤或反击致死而产生的伤的变化
	public final static int PROTECTER_MAXHP_CHANGE = 16; // 保护者因为保护致死而产生的伤的变化
	public final static int MESSAGE_ID = 17; // 行动时弹的提示ID
	public final static int HP_GODBLESS = 18; // 神佑血量变化
	public final static int EP_CHANGE = 19; // 受击者效果点变化，为正是加效果点，为负是扣效果点
	public final static int SHAPE_CHANGE = 20; // 模型改变

	public int resulttype; // 结果类型 0普通 1反击 2连击 3追击 4溅射 5战斗结束 6破隐形
	public int targetid; // 目标id，必须有
	public int flagtype; // 服务器提供客户端标示
	public java.util.LinkedList<fire.pb.battle.DemoBuff> demobuffs; // 每一个demo的buff变化
	public java.util.LinkedList<fire.pb.battle.DemoAttr> demoattrs; // 每一个demo的属性最终值
	public java.util.HashMap<Integer,Integer> datas; // 存储数据，key参考以下值，如datas中某key值没有则value默认为0

	public NewDemoResult() {
		demobuffs = new java.util.LinkedList<fire.pb.battle.DemoBuff>();
		demoattrs = new java.util.LinkedList<fire.pb.battle.DemoAttr>();
		datas = new java.util.HashMap<Integer,Integer>();
	}

	public NewDemoResult(int _resulttype_, int _targetid_, int _flagtype_, java.util.LinkedList<fire.pb.battle.DemoBuff> _demobuffs_, java.util.LinkedList<fire.pb.battle.DemoAttr> _demoattrs_, java.util.HashMap<Integer,Integer> _datas_) {
		this.resulttype = _resulttype_;
		this.targetid = _targetid_;
		this.flagtype = _flagtype_;
		this.demobuffs = _demobuffs_;
		this.demoattrs = _demoattrs_;
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.DemoBuff _v_ : demobuffs)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.DemoAttr _v_ : demoattrs)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(resulttype);
		_os_.marshal(targetid);
		_os_.marshal(flagtype);
		_os_.compact_uint32(demobuffs.size());
		for (fire.pb.battle.DemoBuff _v_ : demobuffs) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(demoattrs.size());
		for (fire.pb.battle.DemoAttr _v_ : demoattrs) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		resulttype = _os_.unmarshal_int();
		targetid = _os_.unmarshal_int();
		flagtype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.DemoBuff _v_ = new fire.pb.battle.DemoBuff();
			_v_.unmarshal(_os_);
			demobuffs.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.DemoAttr _v_ = new fire.pb.battle.DemoAttr();
			_v_.unmarshal(_os_);
			demoattrs.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			datas.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof NewDemoResult) {
			NewDemoResult _o_ = (NewDemoResult)_o1_;
			if (resulttype != _o_.resulttype) return false;
			if (targetid != _o_.targetid) return false;
			if (flagtype != _o_.flagtype) return false;
			if (!demobuffs.equals(_o_.demobuffs)) return false;
			if (!demoattrs.equals(_o_.demoattrs)) return false;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += resulttype;
		_h_ += targetid;
		_h_ += flagtype;
		_h_ += demobuffs.hashCode();
		_h_ += demoattrs.hashCode();
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(resulttype).append(",");
		_sb_.append(targetid).append(",");
		_sb_.append(flagtype).append(",");
		_sb_.append(demobuffs).append(",");
		_sb_.append(demoattrs).append(",");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

