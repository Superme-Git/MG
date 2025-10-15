
package xbean;

public interface PetScoreListRecord extends mkdb.Bean {
	public PetScoreListRecord copy(); // deep clone
	public PetScoreListRecord toData(); // a Data instance
	public PetScoreListRecord toBean(); // a Bean instance
	public PetScoreListRecord toDataIf(); // a Data instance If need. else return this
	public PetScoreListRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 达到这个数量的时间
	public xbean.MarshalPetScoreRecord getMarshaldata(); // 

	public void setTime(long _v_); // 达到这个数量的时间
}
