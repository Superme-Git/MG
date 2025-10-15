package fire.pb.shop.srv;


import org.apache.log4j.Logger;

import fire.pb.PropRole;
import fire.pb.item.ItemShuXing;
import fire.pb.pet.PetAttr;
import fire.pb.shop.SGoods;

public abstract class Shop {
	public static final Logger LOG = Logger.getLogger("SHOP");
	
	protected int silver = 0, gold = 0;// 银币、金币
	protected long originalSilver = 0, originalGold = 0;// 用来记log的

	protected int num;// 买卖数量
	protected PropRole role; // 角色
	protected SGoods goods; // 商品
	protected ItemShuXing itemAttr;// 道具属性
	protected PetAttr petAttr;// 宠物属性
	protected String name;// 配表中的名称, 非商品表中的名称。因为策划会对配表中名称更改，商品表中的名称更新不过来。
	

	public Shop(PropRole role, SGoods goods, int num) {
		super();
		this.role = role;
		this.goods = goods;
		this.num = num;
		if (goods.getType() == 1) {
			this.itemAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(goods.getItemId());
			this.name = itemAttr.getName();
		} else {
			this.petAttr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(goods.getItemId());
			this.name = petAttr.getName();
		}
		
	}

}
