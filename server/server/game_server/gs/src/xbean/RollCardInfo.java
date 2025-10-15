
package xbean;

public interface RollCardInfo extends mkdb.Bean {
	public RollCardInfo copy(); // deep clone
	public RollCardInfo toData(); // a Data instance
	public RollCardInfo toBean(); // a Bean instance
	public RollCardInfo toDataIf(); // a Data instance If need. else return this
	public RollCardInfo toBeanIf(); // a Bean instance If need. else return this

	public int getServiceid(); // 
	public int getTakeflag(); // 0没有领取 1=提取过
	public int getBasemoney(); // 
	public int getBasesmoney(); // 
	public int getBaseexp(); // 
	public int getIndex(); // the real card
	public java.util.List<xbean.WheelItem> getWheelitems(); // 4张卡
	public java.util.List<xbean.WheelItem> getWheelitemsAsData(); // 4张卡

	public void setServiceid(int _v_); // 
	public void setTakeflag(int _v_); // 0没有领取 1=提取过
	public void setBasemoney(int _v_); // 
	public void setBasesmoney(int _v_); // 
	public void setBaseexp(int _v_); // 
	public void setIndex(int _v_); // the real card
}
