package fire.pb.friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;
import fire.pb.item.AddItemResult;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

/**
 * 赠送道具
 * @author XGM
 */
public class PGiveItem extends Procedure {

	private final long roleId;
	private final long targetRoleId; // 赠送目标的角色id
	private final Map<Integer, Integer> itemMap; // 赠送的道具列表 key=itemKey value=itemNum

	public PGiveItem(long roleId, long targetRoleId, Map<Integer, Integer> itemMap) {
		this.roleId = roleId;
		this.targetRoleId = targetRoleId;
		this.itemMap = itemMap;
	}

	@Override
	protected boolean process() {

		if (itemMap.isEmpty())
			return false;

		// 锁定角色
		List<Long> roleIds = new ArrayList<Long>();
		roleIds.add(roleId);
		roleIds.add(targetRoleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));

		FriendGiveProxy friendGiveProxy = FriendGiveProxy.getFriendGiveProxy(roleId, false);
		if (friendGiveProxy == null)
			return false;

		// 每日对目标的赠送数量限制
		int giveNum = friendGiveProxy.getGiveNum(targetRoleId);
		int addGiveNum = 0;
		for (int itemNum : itemMap.values()) {
			addGiveNum += itemNum;
		}
		if (giveNum + addGiveNum > FriendGiveProxy.getDayGiveMax())
			return false;

		// 赠送的物品id列表
		List<ItemBase> giveItems = new ArrayList<ItemBase>();

		// 检查道具
		Pack selfBag = new Pack(roleId, false);
		for (Map.Entry<Integer, Integer> e : itemMap.entrySet()) {
			int itemKey = e.getKey();
			int itemNum = e.getValue();
			ItemBase bi = selfBag.getItem(itemKey);
			if (bi == null) {
				return false; // 包裹中没有要赠送的道具
			}
			if (itemNum <= 0) {
				return false; // 赠送的道具数量错误
			}
			if (bi.getNumber() < itemNum) {
				return false; // 数量不足
			}
			if (isCanGiveByItemId(bi.getItemId()) == false) {
				return false; // 这个道具不能赠送
			}
		}
		// 删除自己的道具
		for (Map.Entry<Integer, Integer> e : itemMap.entrySet()) {
			int itemKey = e.getKey();
			int itemNum = e.getValue();
			ItemBase bi = selfBag.TransOut(itemKey, itemNum, "Friend give item cost");
			if (bi == null) {
				return false; // 包裹中没有要赠送的道具
			}
			giveItems.add(bi);
		}

		// 目标加道具
		Pack targetBag = new Pack(targetRoleId, false);
		for (ItemBase item : giveItems) {
			// 绑定,失效的物品不能赠送
			if (item.isBind() || item.isTimeout()) {
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 141488, null);
				return false;
			}
			// 清空归属
			fire.pb.item.ItemHelper.setOwnerid(item, 0);
			// 赠送的道具设置为绑定
			item.bind();
			int itemId = item.getItemId();
			AddItemResult result = targetBag.doAddItem(item, -1, "Friend give item add", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_friendgiveget, itemId);
			if (result != AddItemResult.SUCC) {
				if (result == AddItemResult.FULL) {
					// 目标包裹已满,无法赠送
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 162164, null);
				} else {
					// 添加失败
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 141488, null);
				}
				// 失败通知客户端刷新界面
				SGiveItem send = new SGiveItem();
				send.roleid = targetRoleId;
				send.itemnum = (byte) friendGiveProxy.getGiveNum(targetRoleId);
				psendWhileRollback(roleId, send);
				return false;
			} else {
				// 目标收到赠送的提示
				String roleName = xtable.Properties.selectRolename(roleId);
				String itemName = item.getName();
				MessageMgr.psendMsgNotifyWhileCommit(targetRoleId, 180028, MessageMgr.getStringList(roleName, item.getNumber(), itemName));
			}
		}

		// 校验
		if (giveItems.size() != itemMap.size()) {
			return false;
		}

		// 记录赠送道具数量
		friendGiveProxy.addGiveNum(targetRoleId, addGiveNum);

		// 通知客户端更新数据
		SGiveItem send = new SGiveItem();
		send.roleid = targetRoleId;
		send.itemnum = (byte) friendGiveProxy.getGiveNum(targetRoleId);
		psendWhileCommit(roleId, send);
		// 提示赠送成功
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 180014, null);

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			List<Integer> itemIds = new ArrayList<Integer>(giveItems.size());
			List<Integer> itemNums = new ArrayList<Integer>(giveItems.size());
			List<String> itemNames = new ArrayList<String>(giveItems.size());
			for (ItemBase item : giveItems) {
				itemIds.add(item.getItemId());
				itemNums.add(item.getNumber());
				itemNames.add(item.getName());
			}
			Module.logger.info("[PGiveItem] roleId:" + roleId
					+ " targetRoleId:" + targetRoleId
					+ " itemIds:" + Arrays.toString(itemIds.toArray())
					+ " itemNums:" + Arrays.toString(itemNums.toArray())
					+ " itemNames:" + Arrays.toString(itemNames.toArray()));
		}
		return true;
	}

	/**
	 * 通过道具id判断是否能赠送
	 * @param itemId
	 * @return
	 */
	public static boolean isCanGiveByItemId(int itemId) {
		java.util.Map<Integer, FriendGiveItem> confs = ConfigManager.getInstance().getConf(FriendGiveItem.class);
		return confs.get(itemId) != null;
	}

}
