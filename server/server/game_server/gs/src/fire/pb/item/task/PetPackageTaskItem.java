package fire.pb.item.task;

import java.util.HashMap;
import java.util.Map;

import fire.pb.item.Commontext;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetTypeEnum;
import fire.pb.talk.MessageMgr;

public class PetPackageTaskItem extends CanuseItem {

	public PetPackageTaskItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	private static final int RewardPetid = 500051;

	public PetPackageTaskItem(ItemMgr im, xbean.Item xitem) {
		super(im, xitem);
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
			PetColumn col = new PetColumn(roleId, PetColumnTypes.PET, false);
			if(col.size() >= col.getCapacity())
			{
				MessageMgr.sendMsgNotify(roleId, 142377, null);
				return Commontext.UseResult.FAIL;//提示，宠物栏已满
			}
			PetAttr pattr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(RewardPetid);
			if(pattr == null)
				return Commontext.UseResult.FAIL;
			int takelevel = pattr.getTakelevel();
			fire.pb.PropRole prole = new fire.pb.PropRole(roleId, false);
			if (prole.getLevel()< takelevel)
			{
				MessageMgr.sendMsgNotify(roleId, 141610, null);
				return Commontext.UseResult.FAIL;//提示，宠物的携带等级过高
			}
			Map<Integer,Object> initattrs = new HashMap<Integer, Object>();
			col.addpet(RewardPetid, 0, PetTypeEnum.BABY,null, PetColumn.ADD_REASON_PACKAGE, PetColour.BLUE, true,initattrs);
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 142378, MessageMgr.getStringList(pattr.getName()));
			Commontext.UseResult ur =
				fire.pb.mission.Module.getInstance().onUseMissionItem(roleId, bi.getItemId());
			return ur;
		}
		
	}
}
