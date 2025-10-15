
package xbean;

public interface WeekGiveReceGift extends mkdb.Bean {
	public WeekGiveReceGift copy(); // deep clone
	public WeekGiveReceGift toData(); // a Data instance
	public WeekGiveReceGift toBean(); // a Bean instance
	public WeekGiveReceGift toDataIf(); // a Data instance If need. else return this
	public WeekGiveReceGift toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.GiveReceGift> getWeekdata(); // 
	public java.util.Map<Long, xbean.GiveReceGift> getWeekdataAsData(); // 

}
