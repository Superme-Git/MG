
package xbean;

public interface ELiveSkill extends mkdb.Bean {
	public ELiveSkill copy(); // deep clone
	public ELiveSkill toData(); // a Data instance
	public ELiveSkill toBean(); // a Bean instance
	public ELiveSkill toDataIf(); // a Data instance If need. else return this
	public ELiveSkill toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllist(); // 
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskilllistAsData(); // 

}
