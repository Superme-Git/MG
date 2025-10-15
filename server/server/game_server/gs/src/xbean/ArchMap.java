
package xbean;

public interface ArchMap extends mkdb.Bean {
	public ArchMap copy(); // deep clone
	public ArchMap toData(); // a Data instance
	public ArchMap toBean(); // a Bean instance
	public ArchMap toDataIf(); // a Data instance If need. else return this
	public ArchMap toBeanIf(); // a Bean instance If need. else return this

	public int getMapid(); // 藏宝图上记录的地图id
	public int getPosx(); // 藏宝图上记录的x坐标
	public int getPosy(); // 藏宝图上记录的y坐标

	public void setMapid(int _v_); // 藏宝图上记录的地图id
	public void setPosx(int _v_); // 藏宝图上记录的x坐标
	public void setPosy(int _v_); // 藏宝图上记录的y坐标
}
