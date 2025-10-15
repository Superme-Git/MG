
package xbean;

public interface HuoBanVip extends mkdb.Bean {
	public HuoBanVip copy(); // deep clone
	public HuoBanVip toData(); // a Data instance
	public HuoBanVip toBean(); // a Bean instance
	public HuoBanVip toDataIf(); // a Data instance If need. else return this
	public HuoBanVip toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getHuobans(); // vip免费的数量
	public java.util.List<Integer> getHuobansAsData(); // vip免费的数量

}
