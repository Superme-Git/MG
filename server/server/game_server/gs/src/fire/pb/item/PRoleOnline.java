package fire.pb.item;


public class PRoleOnline extends mkdb.Procedure{
	
	final long roleId;
	
	public PRoleOnline(final long roleId){
		this.roleId = roleId;
	}
	
	public boolean process(){
		// 上线，将背包里道具 摆摊已上架 的标识去掉。
		ItemMaps col = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		col.cleanAllItemFlag(fire.pb.Item.ONSTALL);
		return true;
	}

}
