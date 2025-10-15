
package xbean;

public interface EClanfightids extends mkdb.Bean {
	public EClanfightids copy(); // deep clone
	public EClanfightids toData(); // a Data instance
	public EClanfightids toBean(); // a Bean instance
	public EClanfightids toDataIf(); // a Data instance If need. else return this
	public EClanfightids toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getIds(); // 本周公会战id by changhao
	public java.util.List<Long> getIdsAsData(); // 本周公会战id by changhao

}
