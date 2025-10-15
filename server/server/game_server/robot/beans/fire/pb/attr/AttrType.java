
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 不要擅自在此加名称与ID，如果要加一个新的属性和ID，先与策划商量，以免冲突
*/
public class AttrType implements Marshal , Comparable<AttrType>{
	public final static int CONS = 10; // 体质
	public final static int IQ = 20; // 智力
	public final static int STR = 30; // 力量
	public final static int ENDU = 40; // 耐力
	public final static int AGI = 50; // 敏捷
	public final static int MAX_HP = 60; // 最大生命上限
	public final static int UP_LIMITED_HP = 70; // 当前生命上限（小于等于最大生命上限）
	public final static int HP = 80; // 当前生命
	public final static int SPIRIT = 90; // 灵力
	public final static int MAX_MP = 90; // 最大法力
	public final static int MP = 100; // 当前法力
	public final static int MAX_SP = 110; // 最大怒气
	public final static int SP = 120; // 怒气
	public final static int ATTACK = 130; // 攻击
	public final static int DEFEND = 140; // 防御
	public final static int MAGIC_ATTACK = 150; // 法术攻击力
	public final static int MAGIC_DEF = 160; // 法术防御力
	public final static int MEDICAL = 170; // 治疗强度
	public final static int SEAL = 180; // 控制命中
	public final static int UNSEAL = 190; // 控制抗性
	public final static int SPEED = 200; // 速度
	public final static int HIT_RATE = 210; // 命中值
	public final static int DODGE_RATE = 220; // 躲避值
	public final static int PHY_CRIT_PCT = 250; // 物理暴击程度（初始为200%,即2倍普通伤害）
	public final static int MAGIC_CRIT_PCT = 280; // 魔法暴击程度（初始为200%）
	public final static int PHY_CRITC_LEVEL = 230; // 物理暴击等级
	public final static int ANTI_PHY_CRITC_LEVEL = 240; // 物理抗性等级
	public final static int MAGIC_CRITC_LEVEL = 260; // 魔法暴击等级
	public final static int ANTI_MAGIC_CRITC_LEVEL = 270; // 魔法抗性等级
	public final static int HEAL_CRIT_LEVEL = 290; // 治疗暴击等级
	public final static int HEAL_CRIT_PCT = 300; // 治疗暴击程度
	public final static int PHFORCE = 450; // 当前体力
	public final static int EXP = 470; // 经验
	public final static int NEXP = 480; // 升级经验
	public final static int RENQI = 610; // 人气值
	public final static int SCHOOLFUND = 850; // 职业贡献度
	public final static int WULI_CHUANTOU = 950; // 物理穿透
	public final static int WULI_DIKANG = 960; // 物理抵抗
	public final static int FASHU_CHUANTOU = 970; // 法术穿透
	public final static int FASHU_DIKANG = 980; // 法术抵抗
	public final static int ZHILIAO_JIASHEN = 990; // 治疗加深
	public final static int EFFECT_POINT = 1010; // 技能效果点
	public final static int TEMP_SP = 1020; // 临时怒气
	public final static int MASTER_REPUTATION = 1080; // 师徒声望
	public final static int PET_XUEMAI_MAX = 1150; // 宠物资质上限
	public final static int PET_LOW_SKILL = 1170; // 宠物低级技能数
	public final static int PET_HIGH_SKILL = 1180; // 宠物高级技能数
	public final static int PET_SUPER_SKILL = 1190; // 宠物超级技能数
	public final static int LEVEL = 1230; // 等级
	public final static int PET_LIFE = 1360; // 宠物寿命
	public final static int ACTIVESTAR = 1380; // 活跃度幸运星
	public final static int POINT = 1400; // 潜能
	public final static int QILIZHI = 1410; // 气力值
	public final static int QILIZHI_LIMIT = 1420; // 气力值上限
	public final static int PET_XUE_MAI_LEVEL = 1430; // 资质星级
	public final static int PET_FIGHT_LEVEL = 1430; // 宠物出战等级
	public final static int PET_ATTACK_APT = 1440; // 宠物攻击资质
	public final static int PET_DEFEND_APT = 1450; // 宠物防御资质
	public final static int PET_PHYFORCE_APT = 1460; // 宠物体力资质
	public final static int PET_MAGIC_APT = 1470; // 宠物法力资质
	public final static int PET_SPEED_APT = 1480; // 宠物速度资质
	public final static int PET_DODGE_APT = 1490; // 宠物躲闪资质
	public final static int PET_GROW_RATE = 1500; // 宠物成长率
	public final static int ENLIMIT = 1520; // 活力上限
	public final static int PFLIMIT = 1530; // 体力上限
	public final static int PET_SCALE = 1810; // 宠物大小 1-4
	public final static int ACTIVENESS = 1820; // 活跃度值
	public final static int ANTI_CRIT_LEVEL = 2090; // 暴击抗性等级
	public final static int KONGZHI_JIACHENG = 2130; // 控制加成
	public final static int KONGZHI_MIANYI = 2140; // 控制免疫
	public final static int ENERGY = 3010; // 当前活力


	public AttrType() {
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
		if (_o1_ instanceof AttrType) {
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

	public int compareTo(AttrType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

