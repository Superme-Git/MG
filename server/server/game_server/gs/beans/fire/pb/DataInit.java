
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 一些个别的初始数值
*/
public class DataInit implements Marshal , Comparable<DataInit>{
	public final static int ROLE_LEVEL_MAX = 155; // 角色最大等级
	public final static int ROLE_UP_POINT = 5; // 角色升级获得潜能数
	public final static int PET_UP_POINT = 5; // 宠物升级获得潜能数
	public final static int ROLE_UP_PHY = 5; // 每升一级增加5点体力
	public final static int ROLE_UP_ENERGY = 5; // 每升一级增加5点活力
	public final static int PET_INIT_LOY = 80; // 宠物初始忠诚度
	public final static int PET_MAX_LOY = 100; // 宠物最大忠诚度。
	public final static int PET_MAX_LIFE = 20000; // 宠物最大寿命
	public final static int PET_FIGHT_LIFE_LIMIT = 50; // 宠物参战的最低寿命限制
	public final static int FULL_PETLOY_LEVEL = 30; // 满宠物忠诚度的宠物等级上限
	public final static int ROLE_PET_LEVEL_SPACE = 6; // 人物宠物的等级差最大为6级。
	public final static int ROLE_PET_LEVEL_SPACE_OFEXPITEM = 10; // 使用经验道具时,人物宠物的等级差最大为10级
	public final static int BASENUM = 1000; // 角色属性计算配置值的基数
	public final static int PET_LEVEL_MAX = 160; // 宠物最大等级
	public final static int PET_USELEVEL_SPACE = 10; // 宠物参战时与人的最大等级差
	public final static int AUTO_UPGRADE_LEVEL = 20; // 人物自动升级的等级上限
	public final static int WILD_PET_MAXGENGU = 40;
	public final static int WILD_PET_MINGENGU = 1;
	public final static int PET_UP_LEVEL_ADD_POINT = 5; // 宠物每升一级，给的潜能点数
	public final static int HAIR_COLOR_SCHEMES_NUM = 4; // 头发配色方案数量
	public final static int BODY_COLOR_SCHEMES_NUM = 4; // 衣服配色方案数量
	public final static int TURNON_REFINE_NEED_ONLINE_TIME = 200; // 开启装备重铸需要的累积时间数
	public final static int TURNON_REFINE_NEED_FRIEND_LEVEL = 1000; // 开启装备重铸需要的友好度
	public final static int TURNON_REFINE_NEED_ANTIQUE_NUM = 1; // 开启装备重铸需要的古董数
	public final static int COMMEN_ROLE_ADDPOINT = 100; // 人物可以手动加点的等级对应配置的ID
	public final static int EQUIP_CAN_REPAIR = 157; // 装备耐久度为80%时才可以修理


	public DataInit() {
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
		if (_o1_ instanceof DataInit) {
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

	public int compareTo(DataInit _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

