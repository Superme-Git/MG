package fire.pb.item;

import java.util.Set;

import fire.pb.item.ItemMgrImp.LiBao;
import xbean.Item;

public interface ItemMgr {
	public ItemBase genItemBase(int itemid, int num, int numtype, mkdb.Bean extinfo, boolean calcScore) throws IllegalArgumentException;
	public ItemBase genItemBase(int itemid, int num, mkdb.Bean extinfo) throws IllegalArgumentException;
	public ItemBase genItemBase(int itemid, int num) throws IllegalArgumentException;
	public ItemBase genItemBase(int itemid, int num, int numtype) throws IllegalArgumentException;
	public ItemShuXing getAttr(int itemid);
	public java.util.NavigableMap<Integer, ItemShuXing> getAttrMap();
	
	public java.util.HashMap<Integer,fire.pb.Bag> getLoginPackInfo(long roleId);
	
	
	public int addItemToPack(final long roleId, final int packid,
			final int itemid, final int number,final int availtime,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid, final String reason);

	SItemBuff getItemBuff(int itemid);

	BagConfig getPackCfg(int packid);

	ItemBase toItemBase(Item i);

	ItemBase toItemBase(Item i, long roleId, int packid, int key);
	SEquipNaiJiuXiaoHao getLoseNaiJiu(int pos);
	LiBao getLiBao(int present, int sex, int shape, int school);
	Set<Integer> getItemByCard(int cardid);
	int getBuyDepotMoney(int hasGridNums);
}
