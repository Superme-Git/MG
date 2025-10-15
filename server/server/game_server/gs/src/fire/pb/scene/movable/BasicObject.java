

package fire.pb.scene.movable;


/**
 * 	基本的物件
 */
public abstract class BasicObject {
	
	BasicObject(long uid, String nm)
	{
		uniqueID = uid;
		name = (nm == null) ? "" : nm;
	}
	
	public long getUniqueID(){
		return uniqueID;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String nm){
		name = nm;
	}
	
	private final long uniqueID;
	private String name;
}

