package fire.pb.item.task;

import java.util.LinkedList;
import java.util.List;

import fire.pb.item.Commontext;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.triggers.TriggerRole;
import fire.pb.triggers.event.TriggerEvent;

public class TriggerItem extends CanuseItem
{

	public TriggerItem(ItemMgr im, int itemid ) {
		super(im, itemid);
	}
	
	public TriggerItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new TriggerHandler();
	}
	
	private static class TriggerHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			List<Integer> args = new LinkedList<Integer>();
			args.add(bi.getItemId());
			TriggerRole trole = new TriggerRole(roleId, true);
			int triggered = trole.getTriggerByType(TriggerEvent.TRIGGER_TYPE_USE_ITEM, args);
			if(triggered == 0)
				return Commontext.UseResult.FAIL;
			trole = new TriggerRole(roleId, false);
			if(trole.trigger(triggered)) 
				return Commontext.UseResult.SUCC;
			else
				return Commontext.UseResult.FAIL;
		}
		
	}
}
