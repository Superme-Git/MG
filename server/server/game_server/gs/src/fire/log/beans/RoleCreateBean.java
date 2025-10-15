package fire.log.beans;

public class RoleCreateBean {
	private int shape;//种族
	private int school;//门派
	
	public RoleCreateBean(int shape,int school){
		this.shape=shape;
		this.school=school;
	}
	
	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public int getSchool() {
		return school;
	}

	public void setSchool(int school) {
		this.school = school;
	}
	
	
	
}
