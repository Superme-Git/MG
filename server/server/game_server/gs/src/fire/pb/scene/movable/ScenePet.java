package fire.pb.scene.movable;

/**
 * 场景上的宠物对象，只是用来显示时
 * 才有意义，本身无场景信息，跟随主人
 */
public class ScenePet extends BasicObject {
	
	ScenePet(long uid, String name, int petid){
		super(uid, name);
		
		this.petid = petid;
	}
	
	ScenePet(long uid, String name, int petid, int colour, int bodysize, int showeffect){
		super(uid, name);
		
		this.petid = petid;
		this.colour = colour;
		this.bodysize = bodysize;
		this.showeffect = showeffect;
	}
	
	public int getPetID(){
		return petid;
	}
	
	public int getColour(){
		return colour;
	}
	
	public void setColour(int colour){
		this.colour = colour;
	}
	
	public int getBodySize(){
		return bodysize;
	}
	
	public int getShowEffect()
	{
		return showeffect;
	}
	
	public void setShowEffect(int se)
	{
		showeffect = se;
	}
	public void setBodySize(int sz)
	{
		bodysize = sz;
	}
	
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * 	判断是否为同一只宠物
	 */
	public boolean equals(ScenePet another)
	{
		if(null == another)	return false;
		
		if(getName() != another.getName()) return false;
		
		if(getUniqueID() != another.getUniqueID()) return false;
		
		if(getPetID() != another.getPetID()) return false;
		
		return true;
	}
	
	/**
	 * checkAllTheSame: 同一宠物，并且完全相同
	 */
	public boolean checkAllTheSame(ScenePet another)
	{
		if(!this.equals(another)) return false;
		
		// 毛色、大小、效果完全相同
		if(getColour() != another.getColour()) return false;

		if( getBodySize() != another.getBodySize()) return false;

		if( getShowEffect() != another.getShowEffect()) return false;
		
		return true;
	}
	
	
	private final int petid;
	private int colour;
	private int bodysize;
	private int showeffect; 
	private int grade;
	
}
