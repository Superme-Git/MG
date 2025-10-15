
package xbean;

public interface ProgressBarInfo extends mkdb.Bean {
	public ProgressBarInfo copy(); // deep clone
	public ProgressBarInfo toData(); // a Data instance
	public ProgressBarInfo toBean(); // a Bean instance
	public ProgressBarInfo toDataIf(); // a Data instance If need. else return this
	public ProgressBarInfo toBeanIf(); // a Bean instance If need. else return this

	public final static int TREASURE_MAP = 1; // 进度条类型:藏宝图
	public final static int SUPER_TREASURE_MAP = 2; // 进度条类型:超级藏宝图

	public int getBartype(); // 进度条类型
	public int getItemkey(); // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
	public int getInprogress(); // 进度条是否已经开始.1表示已经开始,0表示没有开始
	public long getStarttime(); // 进度条的开始时间

	public void setBartype(int _v_); // 进度条类型
	public void setItemkey(int _v_); // 物品的key,是在背包里的key,不是itemid,如果进度条不需要消耗物品,itemkey可以默认为0
	public void setInprogress(int _v_); // 进度条是否已经开始.1表示已经开始,0表示没有开始
	public void setStarttime(long _v_); // 进度条的开始时间
}
