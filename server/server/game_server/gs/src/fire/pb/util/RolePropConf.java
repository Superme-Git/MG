package fire.pb.util;


import fire.pb.role.RoleModData;
import xbean.BasicFightProperties;


public class RolePropConf {
    //获得人物基本属性
	public static int getBasicProp(BasicFightProperties bfp,RoleAttrType attrType){
		if(bfp==null) return 0;
		switch (attrType) {
		case AGI:
			return bfp.getAgi();
		case CONS:
			return bfp.getCons();
		case ENDU:
			return bfp.getEndu();
		case IQ:
			return bfp.getIq();
		case STR:
			return bfp.getStr();
		default:
			throw new IllegalArgumentException("no corresponding attr"+attrType.getValue());
		}
	}
	public static int getInitialProp(RoleModData rmd,RoleAttrType attrType){
		if(rmd==null) return 0;
		switch (attrType) {
		case AGI:
			return rmd.getAgiinit();
		case CONS:
			return rmd.getConsinit();
		case ENDU:
			return rmd.getEnduinit();
		case IQ:
			return rmd.getIqinit();
		case STR:
			return rmd.getStrinit();
		default:
			throw new IllegalArgumentException("no corresponding attr"+attrType.getValue());
		}

	}
	
	public static boolean setBasicProp(BasicFightProperties bfp,RoleAttrType attrType,int value){
		if(bfp==null||value<0) return false;
		switch (attrType) {
		case AGI:
			 bfp.setAgi((short) value);
			 break;
		case CONS:
			bfp.setCons((short) value);
			break;
		case ENDU:
			 bfp.setEndu((short) value);
			break;
		case IQ:
			bfp.setIq((short) value);
			break;
		case STR:
			bfp.setStr((short) value);
			break;
		default:
			return false;
		}
		return true;
	}
	
	public static int getShapeidByXshapeid(final int xshapeid) {
		return xshapeid % 100;
	}
	
	public static int getHeadidByXshapeid(final int xshapeid) {
		if (xshapeid < 1000000) {
			return 1;
		}
		return (xshapeid%1000000)/10000; //头部方案
	}
	
	public static int getBodyidByXshapeid(final int xshapeid) {
		if (xshapeid < 1000000) {
			return 1;
		}
		return (xshapeid%1000000)%10000/100; //身体方案
	}
	
	public static boolean isShapeSameStep(final int shape1, final int shape2) {
		return getShapeStep(shape1) == getShapeStep(shape2);
	}
	
	public static int getShapeStep(final int shape) {
		return shape/1000000;
	}
}
