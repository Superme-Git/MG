
package xbean;

public interface BingFengRank extends mkdb.Bean {
	public BingFengRank copy(); // deep clone
	public BingFengRank toData(); // a Data instance
	public BingFengRank toBean(); // a Bean instance
	public BingFengRank toDataIf(); // a Data instance If need. else return this
	public BingFengRank toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.BingFengRankList> getRanks(); // key为职业id
	public java.util.Map<Integer, xbean.BingFengRankList> getRanksAsData(); // key为职业id

}
