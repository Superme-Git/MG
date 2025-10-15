
package xbean;

public interface DiscardPet extends mkdb.Bean {
	public DiscardPet copy(); // deep clone
	public DiscardPet toData(); // a Data instance
	public DiscardPet toBean(); // a Bean instance
	public DiscardPet toDataIf(); // a Data instance If need. else return this
	public DiscardPet toBeanIf(); // a Bean instance If need. else return this

	public xbean.PetInfo getPet(); // 宠物属性
	public long getRoleid(); // 宠物删除时的主人
	public long getDeletedate(); // 删除日期
	public int getReason(); // 删除原因

	public void setRoleid(long _v_); // 宠物删除时的主人
	public void setDeletedate(long _v_); // 删除日期
	public void setReason(int _v_); // 删除原因
}
