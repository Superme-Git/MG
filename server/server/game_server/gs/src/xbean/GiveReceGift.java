
package xbean;

public interface GiveReceGift extends mkdb.Bean {
	public GiveReceGift copy(); // deep clone
	public GiveReceGift toData(); // a Data instance
	public GiveReceGift toBean(); // a Bean instance
	public GiveReceGift toDataIf(); // a Data instance If need. else return this
	public GiveReceGift toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getGivegift(); // 发送礼物 key 为道具itemid value 为数量 by changhao
	public java.util.Map<Integer, Integer> getGivegiftAsData(); // 发送礼物 key 为道具itemid value 为数量 by changhao
	public java.util.Map<Integer, Integer> getRecegift(); // 接收礼物 key 为道具itemid value 为数量 by changhao
	public java.util.Map<Integer, Integer> getRecegiftAsData(); // 接收礼物 key 为道具itemid value 为数量 by changhao

}
