
package xbean;

public interface EDayPay extends mkdb.Bean {
	public EDayPay copy(); // deep clone
	public EDayPay toData(); // a Data instance
	public EDayPay toBean(); // a Bean instance
	public EDayPay toDataIf(); // a Data instance If need. else return this
	public EDayPay toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.DayPay> getRoleid2daypay(); // 队伍分赃链表 by changhao
	public java.util.Map<Long, xbean.DayPay> getRoleid2daypayAsData(); // 队伍分赃链表 by changhao

}
