package fire.pb.util;
import fire.pb.attr.AttrType;

public enum RoleAttrType {
	CONS(AttrType.CONS),
	IQ (AttrType.IQ), 
	STR(AttrType.STR),
	ENDU(AttrType.ENDU),
	AGI(AttrType.AGI);
	
	private  int value;

	private RoleAttrType(int value) {
		this.value = value;
	}
	//不推荐使用这个方法,除非是以前的方法必须要int值作为参数
	public int getValue() {
		return this.value;
		
	}
	public static RoleAttrType getAttrType(int value){
	 switch (value) {
	case AttrType.CONS:
		return CONS;
	case AttrType.AGI:
		return AGI;
	case AttrType.IQ:
		return IQ;
	case AttrType.STR:
		return STR;
	case AttrType.ENDU:
		return ENDU;
	default:
		return null;
	}
	}
}
