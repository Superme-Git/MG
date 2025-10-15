
package xbean;

public interface DynamicMapInfo2 extends mkdb.Bean {
	public DynamicMapInfo2 copy(); // deep clone
	public DynamicMapInfo2 toData(); // a Data instance
	public DynamicMapInfo2 toBean(); // a Bean instance
	public DynamicMapInfo2 toDataIf(); // a Data instance If need. else return this
	public DynamicMapInfo2 toBeanIf(); // a Bean instance If need. else return this

	public int getBasemapid(); // 

	public void setBasemapid(int _v_); // 
}
