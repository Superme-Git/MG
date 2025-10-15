package fire.pb.compensation;

/**
 * 帮助类
 * @author XGM
 */
public class Helper {

	// 邮件奖励类型
	public static final int AWARDTYPE_ITEM = 0; // 道具
	public static final int AWARDTYPE_MONEY = 1; // 银币
	public static final int AWARDTYPE_GOLD = 2; // 金币
	public static final int AWARDTYPE_FUSHI = 3; // 符石
	public static final int AWARDTYPE_EXP = 4; // 经验
	public static final int AWARDTYPE_VIPEXP = 5; // VIP经验

	// 邮件奖励标记
	public static final int AWARDFLAG_BIND = 0x00000001; // 绑定

	// 邮件发送类型
	public static final int GIVETYPE_ROLEID = 0; // 发送给指定的角色
	public static final int GIVETYPE_CONDITION = 1; // 按条件发送

	/**
	 * 把角色加到排除列表
	 */
	public static mkdb.Procedure roleToExcept(long key, long roleId) {

		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 存储
				xbean.SingleCompensation xsingle = xtable.Singlecompensations.get(key);
				if (xsingle == null) {
					return false;
				}
				if (xsingle.getGivetype() != GIVETYPE_CONDITION) {
					return false;
				}
				xsingle.getRoleidsofexcept().add(roleId);
				// 缓存
				Module.addGiveRoleId(key, roleId);
				return true;
			}
		};

		return proc;
	}
}
