package fire.pb.item;



public interface IDurableItem
{
	public int getDurability();//获取当前耐久度
	public int getMaxDurability();//获取最大耐久度
	public void addDurability(int addDurability);//改变耐久
	public void addDurabilityWithSP(int addDurability);//发送
}
