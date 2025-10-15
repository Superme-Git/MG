package fire.pb.http.beans;

public class AttrBean {
	private int id;//附加属性id
	private float basevalue;//附加属性初始value
	
	public AttrBean(int id,float basevalue){
		this.id=id;
		this.basevalue=basevalue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getBasevalue() {
		return basevalue;
	}

	public void setBasevalue(float basevalue) {
		this.basevalue = basevalue;
	}
	
	
	
	
	

}
