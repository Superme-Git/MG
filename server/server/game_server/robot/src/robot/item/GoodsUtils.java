package robot.item;

import java.util.ArrayList;
import java.util.List;

import robot.LoginRole;
import robot.ride.PetUtils;
import robot.ride.RideMgr;

public class GoodsUtils {

	static int goodsSize = 0;

	public static List<Integer> itemIdList = new ArrayList<Integer>();
	static {
		// 所有坐骑
		itemIdList.addAll(RideMgr.rideItemIdList);
		// 模具
		for (int i = 331101; i <= 331109; i++) {
			itemIdList.add(i);
		}
		// 图纸
		for (int i = 350000; i <= 350012; i++) {
			itemIdList.add(i);
		}
		// 升级礼包
		for (int i = 334001; i <= 334009; i++) {
			itemIdList.add(i);
		}
		// 升级礼包
		for (int i = 334001; i <= 334009; i++) {
			itemIdList.add(i);
		}
		// 装备
		itemIdList.add(4050103);
		itemIdList.add(4050104);
		itemIdList.add(4050105);
		itemIdList.add(4050106);
		itemIdList.add(4050207);
		itemIdList.add(4050208);
		itemIdList.add(4050309);
		itemIdList.add(4050310);
		itemIdList.add(4050411);
		itemIdList.add(4050512);
		itemIdList.add(4050613);
		itemIdList.add(4060101);
		itemIdList.add(4060102);
		itemIdList.add(4060103);
		itemIdList.add(4060104);
		itemIdList.add(4060105);
		itemIdList.add(4060106);
		itemIdList.add(4060207);
		itemIdList.add(4060208);
		itemIdList.add(4060309);
		itemIdList.add(4060310);
		itemIdList.add(4060411);
		itemIdList.add(4060512);
		itemIdList.add(4060613);
		itemIdList.add(4070101);
		itemIdList.add(4070102);
		itemIdList.add(4070103);
		itemIdList.add(4070104);
		goodsSize = itemIdList.size();
	}

	/**
	 * 登录的时候初始化数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月30日 上午10:48:29
	 * @version 1.0
	 * @param role
	 */
	public static void initLogin(LoginRole role) {
		// 随机一个宠物
		int petid = PetUtils.getNetRandomPetId();
		role.sendCommand("//addpet " + petid + " 45");
		// 随机一部分道具进入背包
		for (int i = 0; i < 50; i++) {
			int itemId = itemIdList.get((int) (Math.random() * goodsSize));
			role.sendCommand("//additem " + itemId);
		}
	}
}
