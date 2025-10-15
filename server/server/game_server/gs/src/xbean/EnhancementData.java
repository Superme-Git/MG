
package xbean;

public interface EnhancementData extends mkdb.Bean {
	public EnhancementData copy(); // deep clone
	public EnhancementData toData(); // a Data instance
	public EnhancementData toBean(); // a Bean instance
	public EnhancementData toDataIf(); // a Data instance If need. else return this
	public EnhancementData toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getEnhancementattr(); // 附魔属性 by changhao
	public java.util.Map<Integer, Integer> getEnhancementattrAsData(); // 附魔属性 by changhao
	public long getEnhancementtime(); // 附魔时间 by changhao

	public void setEnhancementtime(long _v_); // 附魔时间 by changhao
}
