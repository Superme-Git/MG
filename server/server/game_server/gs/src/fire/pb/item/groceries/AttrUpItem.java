package fire.pb.item.groceries;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.attr.AttrType;
import fire.pb.item.Commontext;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemType;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.Module;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.util.Misc;


public class AttrUpItem extends GroceryItem{
	
	protected xbean.EnhancementAttr enhancement; // 道具附魔属性 by changhao
	
	public AttrUpItem(ItemMgr im, int itemid) {
		super(im, itemid);
		
		enhancement = xbean.Pod.newEnhancementAttr();
		final long extkey = xtable.Enhancement.insert(enhancement);
		itemData.setExtid(extkey);
	}
	
	public AttrUpItem(ItemMgr im, xbean.Item item) {
		super(im, item);
		
		enhancement = xtable.Enhancement.select(item.getExtid());
		if (enhancement == null) {
			Module.getInstance().getLogger().error( "食品@.@数据库不完整,请检查原因" );
		}
	}
	
	public AttrUpItem(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);
	}
	
	
	public void AddEnhancementAttr(int effectid, int value)
	{
		if (enhancement != null)
		{
			enhancement.getAttrmap().put(effectid, value);
		}
	}
	
	public void SetLevel(int level)
	{
		if (enhancement != null)
		{
			enhancement.setLevel(level);
		}
	}
	
	@Override
	public int getLevel()
	{
		if (enhancement != null)
		{
			return enhancement.getLevel();
		}		
		
		return 1;
	}
	
	public void GenAttr(int level)
	{
		fire.pb.item.GroceryItemShuXing groceryattr = (fire.pb.item.GroceryItemShuXing)itemAttr;
		if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_HP)
		{
			int random = Misc.getRandomBetween((int)((float)level * 3.0f), level * 5);
			if (random == 0)
				random = 1;
			AddEnhancementAttr(AttrType.MAX_HP + 1, random);
		}
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_PY_ATT)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.6f), level * 1);
			if (random == 0)
				random = 1;
			AddEnhancementAttr(AttrType.ATTACK + 1, random);
		}		
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_ATT)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.6f), level * 1);
			if (random == 0)
				random = 1;
			AddEnhancementAttr(AttrType.MAGIC_ATTACK + 1, random);
		}			
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_PHY_DEF)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.6f), level * 1);
			if (random == 0)
				random = 1;
			AddEnhancementAttr(AttrType.DEFEND + 1, random);
		}			
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_DEF)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.6f), level * 1);
			if (random == 0)
				random = 1;
			AddEnhancementAttr(AttrType.MAGIC_DEF + 1, random);
		}
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_ANGER)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.18f), (int)(level * 0.3f));
			if (random == 0)
				random = 1;
			
			AddEnhancementAttr(AttrType.TEMP_SP + 1, random);
		}
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_HEAL)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.3f), (int)(level * 0.5f));
			if (random == 0)
				random = 1;
			
			AddEnhancementAttr(AttrType.MEDICAL + 1, random);
		}
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_CONTROL)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.18f), (int)(level * 0.3f));
			if (random == 0)
				random = 1;
			
			AddEnhancementAttr(AttrType.SEAL + 1, random);
			
			random = Misc.getRandomBetween((int)((float)level * 0.18f), (int)(level * 0.3f));
			if (random == 0)
				random = 1;
			
			AddEnhancementAttr(AttrType.UNSEAL + 1, random);
		}
		else if (groceryattr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_SPEED)
		{
			int random = Misc.getRandomBetween((int)((float)level * 0.3f), (int)(level * 0.5f));
			if (random == 0)
				random = 1;
			
			AddEnhancementAttr(AttrType.SPEED + 1, random);
		}
		
		SetLevel(level);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseAttrUpItemHandler();
	}
	
	@Override
	public void onDeleted() {
		xtable.Enhancement.remove(itemData.getExtid());
	}
	
	public String getItemCode(){
		return "";
	}
	
	@Override
	public Octets getTips() {
		if ( os==null) {
			os = new OctetsStream();
			
			if (enhancement != null)
			{
				os.marshal(enhancement.getLevel()); //等级 by changhao				

			}
		}
		return os;
	}
	
	
	private static class UseAttrUpItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum)
		{			
			fire.pb.item.GroceryItemShuXing attr = (fire.pb.item.GroceryItemShuXing)bi.getItemAttr();
			
			AttrUpItem item = (AttrUpItem)bi;
			if (item.enhancement == null)
			{
				return Commontext.UseResult.FAIL;	
			}
			
			int pos = 0;
			if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_HP)
			{
				pos = EquipItemType.LORICAE;
			}
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_PY_ATT)
			{
				pos = EquipItemType.ARMS;				
			}		
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_ATT)
			{
				pos = EquipItemType.ADORN;					
			}			
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_PHY_DEF)
			{
				pos = EquipItemType.TIRE;				
			}			
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_IMAGE_DEF)
			{
				pos = EquipItemType.BOOT;				
			}			
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_ANGER)
			{
				pos = EquipItemType.WAISTBAND;
			}				
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_HEAL)
			{
				pos = EquipItemType.ARMS;
			}		
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_CONTROL)
			{
				pos = EquipItemType.LORICAE;
			}		
			else if (attr.specialType == LiveSkillManager.LIVE_SKILL_ENHANCEMENT_TYPE_SPEED)
			{
				pos = EquipItemType.BOOT;
			}		
			
			EquipItem equipItem = GetEquipItem(roleId, pos);
			if (equipItem != null)
			{
				equipItem.setEnhancementAttr(attr.specialType, item.enhancement.getAttrmap(), System.currentTimeMillis() + 12 * 3600 * 1000);			
				
				fire.pb.item.PPutOnEquip.freshEquipBuff(roleId, equipItem);
				
				fire.pb.item.SUseEnhancementItem msg = new fire.pb.item.SUseEnhancementItem();
				msg.equippos = pos;
				mkdb.Procedure.psendWhileCommit(roleId, msg);
				
				return Commontext.UseResult.SUCC;
			}
			else
			{
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 150097, null);				
			}
			
			return Commontext.UseResult.FAIL;			
		}
	}
	
	public static EquipItem GetEquipItem(long roleId, int pos)
	{
		fire.pb.item.Equip equip = new fire.pb.item.Equip( roleId, false );
		for ( fire.pb.item.ItemBase item : equip )
		{
			if (item instanceof EquipItem)
			{
				EquipItem equipItem = (EquipItem)item;
				if (equipItem.getEquipType() == pos)
				{
					return equipItem;
				}
			}
		}
		
		return null;
	}
}
