package fire.pb.effect;


public class SAttrEffectID2Name implements mytools.ConvMain.Checkable ,Comparable<SAttrEffectID2Name>{

	public int compareTo(SAttrEffectID2Name o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAttrEffectID2Name(){
		super();
	}
	public SAttrEffectID2Name(SAttrEffectID2Name arg){
		this.id=arg.id ;
		this.classname=arg.classname ;
		this.attrname=arg.attrname ;
		this.initValue=arg.initValue ;
		this.ablEffctId=arg.ablEffctId ;
		this.ablEffctName=arg.ablEffctName ;
		this.ablLimit=arg.ablLimit ;
		this.pctEffctId=arg.pctEffctId ;
		this.pctEffctName=arg.pctEffctName ;
		this.pctLimit=arg.pctLimit ;
		this.needSendAttr=arg.needSendAttr ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 属性id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 属性名
	 */
	public String classname  = null  ;
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String v){
		this.classname=v;
	}
	
	/**
	 * 属性名
	 */
	public String attrname  = null  ;
	
	public String getAttrname(){
		return this.attrname;
	}
	
	public void setAttrname(String v){
		this.attrname=v;
	}
	
	/**
	 * 
	 */
	public double initValue  = 0.0  ;
	
	public double getInitValue(){
		return this.initValue;
	}
	
	public void setInitValue(double v){
		this.initValue=v;
	}
	
	/**
	 * 数值型效果id
	 */
	public int ablEffctId  = 0  ;
	
	public int getAblEffctId(){
		return this.ablEffctId;
	}
	
	public void setAblEffctId(int v){
		this.ablEffctId=v;
	}
	
	/**
	 * 数值型变量名
	 */
	public String ablEffctName  = null  ;
	
	public String getAblEffctName(){
		return this.ablEffctName;
	}
	
	public void setAblEffctName(String v){
		this.ablEffctName=v;
	}
	
	/**
	 * 
	 */
	public double ablLimit  = 0.0  ;
	
	public double getAblLimit(){
		return this.ablLimit;
	}
	
	public void setAblLimit(double v){
		this.ablLimit=v;
	}
	
	/**
	 * 百分比型效果id
	 */
	public int pctEffctId  = 0  ;
	
	public int getPctEffctId(){
		return this.pctEffctId;
	}
	
	public void setPctEffctId(int v){
		this.pctEffctId=v;
	}
	
	/**
	 * 百分比型变量名
	 */
	public String pctEffctName  = null  ;
	
	public String getPctEffctName(){
		return this.pctEffctName;
	}
	
	public void setPctEffctName(String v){
		this.pctEffctName=v;
	}
	
	/**
	 * 
	 */
	public double pctLimit  = 0.0  ;
	
	public double getPctLimit(){
		return this.pctLimit;
	}
	
	public void setPctLimit(double v){
		this.pctLimit=v;
	}
	
	/**
	 * 初始是否发送属性
	 */
	public int needSendAttr  = 0  ;
	
	public int getNeedSendAttr(){
		return this.needSendAttr;
	}
	
	public void setNeedSendAttr(int v){
		this.needSendAttr=v;
	}
	
	
};