
package xbean;

public interface RenXingData extends mkdb.Bean {
	public RenXingData copy(); // deep clone
	public RenXingData toData(); // a Data instance
	public RenXingData toBean(); // a Bean instance
	public RenXingData toDataIf(); // a Data instance If need. else return this
	public RenXingData toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getRenxinmap(); // key 为循环类型
	public java.util.Map<Integer, Integer> getRenxinmapAsData(); // key 为循环类型

}
