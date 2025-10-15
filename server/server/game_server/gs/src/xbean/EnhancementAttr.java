
package xbean;

public interface EnhancementAttr extends mkdb.Bean {
	public EnhancementAttr copy(); // deep clone
	public EnhancementAttr toData(); // a Data instance
	public EnhancementAttr toBean(); // a Bean instance
	public EnhancementAttr toDataIf(); // a Data instance If need. else return this
	public EnhancementAttr toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 等级 by changhao
	public java.util.Map<Integer, Integer> getAttrmap(); // 附魔增加的属性 by changhao
	public java.util.Map<Integer, Integer> getAttrmapAsData(); // 附魔增加的属性 by changhao

	public void setLevel(int _v_); // 等级 by changhao
}
