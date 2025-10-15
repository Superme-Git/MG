package fire.pb.item.task;

import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.item.Commontext;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.TaskItem;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;

public class CanuseItem extends TaskItem {
	// 任务物品应该有个 TaskItemAttr, 属性包括它对应的任务
	public CanuseItem(ItemMgr im, int itemid ) {
		super(im, itemid);
	}
	
	public CanuseItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseTaskHandler();
	}
	
	private static class UseTaskHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if (usednum!=1)
				return Commontext.UseResult.FAIL;
			int itemid = bi.getItemId();
			if (CircleTaskManager.getInstance().isCTItemUse(roleId,itemid)) {
				return new CircleTask(roleId, false).CTItemUse(itemid);
			}
			Commontext.UseResult ur =
				fire.pb.mission.Module.getInstance().onUseMissionItem(roleId, bi.getItemId());
			return ur;
		}
		
	}
	
}
