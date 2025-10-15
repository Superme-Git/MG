
package fire.pb.fushi.outdate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import xbean.RoleFuShiRecord;
import xbean.FuShiRecord;


public class FuShiReocrdRole {
	private static Logger LOGGER = Logger.getLogger(FuShiReocrdRole.class);
	private RoleFuShiRecord roleRecords;

	/**
	 * 180天过期
	 */
	public static long DELAY_TIME = 180l * 24 * 60 * 60 * 1000;
	/**
	 * 角色id
	 */
	private long roleId;

	public FuShiReocrdRole(long roleId) {
		this(roleId, null);
		roleRecords = xtable.Fushirecordtable.get(roleId);
		if (roleRecords == null) {
			roleRecords = xbean.Pod.newRoleFuShiRecord();
			xtable.Fushirecordtable.insert(roleId,roleRecords);
		}
	}

	public FuShiReocrdRole(long roleId, RoleFuShiRecord record) {
		this.roleId = roleId;
		this.roleRecords = record;
	}

	/**
	 * 新增一条记录
	 * @author cn
	 * @param num
	 *            符石的数量
	 */
	public void addRecord(int num) {
		FuShiRecord newRecord = xbean.Pod.newFuShiRecord();
		newRecord.setAddtime(System.currentTimeMillis());
		newRecord.setCurrentnum(num);
		roleRecords.getRecords().add(newRecord);
	}

	/**
	 * 取得过期的订单
	 * @author cn
	 */
	public List<FuShiRecord> getOutdateRecords() {
		List<FuShiRecord> list = new ArrayList<FuShiRecord>();
		long now = System.currentTimeMillis();
		for (FuShiRecord tmp : roleRecords.getRecords()) {
			if (tmp.getAddtime() + DELAY_TIME < now) {
				list.add(tmp);
			}
		}
		// 返回过期的符石订单
		return list;
	}

	/**
	 * 删除订单
	 * @author cn
	 */
	public boolean remvoeRecord(List<FuShiRecord> targets) {
		return roleRecords.getRecords().removeAll(targets);
	}

	/**
	 * 消耗一定数量的符石
	 * 
	 * @author cn
	 */
	public void costFuShi(int costNum) {
		Iterator<FuShiRecord> iterator = roleRecords.getRecords().iterator();

		FuShiRecord tmp = null;
		int total = 0;
		while (iterator.hasNext()&&costNum>0) {
			tmp = iterator.next();
			if (costNum < tmp.getCurrentnum()) {
				tmp.setCurrentnum(tmp.getCurrentnum() - costNum);
				LOGGER.debug("cost fushi record num " + costNum);
				total += costNum;
				break;
			}
			costNum -= tmp.getCurrentnum();
			iterator.remove();
			LOGGER.debug("cost fushi record num " + tmp.getCurrentnum());
			total += tmp.getCurrentnum();
		}

		if (total != costNum) {
			LOGGER.error(" role " + roleId
					+ "cost fushi record error expected  " + costNum
					+ " but is " + total);
		}

	}

}
