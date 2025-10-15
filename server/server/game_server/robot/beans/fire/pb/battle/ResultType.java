
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ResultType implements Marshal , Comparable<ResultType>{
	public final static int RESULT_HPCHANGE = 1; // 1:目标HP变化
	public final static int RESULT_MPCHANGE = 2; // 2:目标MP变化
	public final static int RESULT_SPCHANGE = 4; // 3:目标SP变化
	public final static int RESULT_ULHPCHANGE = 8; // 4:当前血上限（伤）变化
	public final static int RESULT_REST = 16; // 5:休息
	public final static int RESULT_HURT = 32; // 6:目标受伤
	public final static int RESULT_CRITIC = 64; // 7:目标被暴击
	public final static int RESULT_DEFENCE = 128; // 8:目标防御
	public final static int RESULT_PARRY = 256; // 9:目标招架（类似躲闪，只有普通攻击会触发招架）
	public final static int RESULT_DODGE = 512; // 10:目标闪避
	public final static int RESULT_RUNAWAY = 1024; // 11:目标逃跑
	public final static int RESULT_SEIZE = 2048; // 12:目标被捕捉
	public final static int RESULT_SUMMONBACK = 4096; // 13:目标被召回
	public final static int RESULT_DEATH = 8192; // 14:目标死亡，倒在原地
	public final static int RESULT_KICKOUT = 16384; // 15:目标被击飞（没有鬼魂技能的怪和宠物死亡时）
	public final static int RESULT_GHOST = 32768; // 16:目标进入鬼魂状态（有鬼魂技能的怪和宠物死亡时）
	public final static int RESULT_RELIVE = 65536; // 17:复活
	public final static int RESULT_SUMMONPET = 131072; // 18:目标招唤宠物
	public final static int RESULT_IGNORE_PHYDIC_EFEN = 262144; // 19:忽略防御
	public final static int RESULT_ABORBE = 524288; // 20:吸收
	public final static int RESULT_FIRE_MANA = 1048576; // 21:烧蓝
	public final static int RESULT_GODBLESS = 2097152; // 22:神佑
	public final static int RESULT_EPCHANGE = 4194304; // 23:目标EP变化
	public final static int RESULT_DEAD_FULL_RELIVE = 8388608; // 24:满血复活
	public final static int RESULT_SHAPECHAGE = 16777216; // 25:模型改变


	public ResultType() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ResultType) {
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

	public int compareTo(ResultType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

