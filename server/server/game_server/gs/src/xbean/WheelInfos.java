
package xbean;

public interface WheelInfos extends mkdb.Bean {
	public WheelInfos copy(); // deep clone
	public WheelInfos toData(); // a Data instance
	public WheelInfos toBean(); // a Bean instance
	public WheelInfos toDataIf(); // a Data instance If need. else return this
	public WheelInfos toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.WheelInfo> getWheellist(); // 
	public java.util.List<xbean.WheelInfo> getWheellistAsData(); // 

}
