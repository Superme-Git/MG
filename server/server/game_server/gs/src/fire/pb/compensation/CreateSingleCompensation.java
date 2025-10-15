package fire.pb.compensation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建补偿邮件
 * @author XGM
 */
public class CreateSingleCompensation {

	/**
	 * 创建活动邮件
	 */
	public static PGiveSingleCompensition createFromActivity(Set<Long> roleIds, List<xbean.SingleCompensationAward> awards,
			String title, String content) {
		return PGiveSingleCompensition.createFromActivity(roleIds, awards, title, content, 0);
	}

	/**
	 * 创建单人运营邮件
	 */
	public static PGiveSingleCompensition createFromYunYing(long roleId, List<xbean.SingleCompensationAward> awards,
			String title, String content, long endTime, String opid, String sign) {
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(roleId);
		return PGiveSingleCompensition.createFromYunYing(roleIds, awards, title, content, endTime, opid, sign);
	}

	/**
	 * 创建多人运营邮件
	 */
	public static PGiveSingleCompensition createFromYunYing(Set<Long> roleIds, List<xbean.SingleCompensationAward> awards,
			String title, String content, long endTime, String opid, String sign) {
		return PGiveSingleCompensition.createFromYunYing(roleIds, awards, title, content, endTime, opid, sign);
	}

	/**
	 * 创建多人运营邮件
	 */
	public static PGiveSingleCompensition createFromYunYingCond(Map<Integer, List<String>> conds, List<xbean.SingleCompensationAward> awards,
			String title, String content, long endTime, String opid, String sign) {
		return PGiveSingleCompensition.createFromYunYingCond(conds, awards, title, content, endTime, opid, sign);
	}
}
