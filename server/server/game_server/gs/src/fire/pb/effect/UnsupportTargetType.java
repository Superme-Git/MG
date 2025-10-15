package fire.pb.effect;

public class UnsupportTargetType extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	private final int type;
	
	public int getType() {
		return type;
	}

	public UnsupportTargetType(int type) {
		super();
		this.type = type;
	}
	

}
