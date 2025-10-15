package fire.pb.skill;


public class SSkillInfoConfig implements mytools.ConvMain.Checkable ,Comparable<SSkillInfoConfig>{

	public int compareTo(SSkillInfoConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSkillInfoConfig(){
		super();
	}
	public SSkillInfoConfig(SSkillInfoConfig arg){
		this.id=arg.id ;
		this.SkillLevelRequireList=arg.SkillLevelRequireList ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> SkillLevelRequireList  ;
	
	public java.util.ArrayList<Integer> getSkillLevelRequireList(){
		return this.SkillLevelRequireList;
	}
	
	public void setSkillLevelRequireList(java.util.ArrayList<Integer> v){
		this.SkillLevelRequireList=v;
	}
	
	
};