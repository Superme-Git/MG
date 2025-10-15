package fire.pb.npc.NpcServiceCond;

/**
 * 检查创建时间段
 * @author XGM
 */
public class RoleCreateTime implements Condition {

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		Long t = xtable.Properties.selectCreatetime(roleid);
		if (t != null) {
			long createTime = t.longValue();
			long beginTime = (long)args1 * 1000;
			long endTime = (long)args2 * 1000;
			if (createTime >= beginTime && createTime <= endTime) {
				return true;
			}
		}
		return false;
	}

}
