
package xbean;

public interface npcBattleInfoCol extends mkdb.Bean {
	public npcBattleInfoCol copy(); // deep clone
	public npcBattleInfoCol toData(); // a Data instance
	public npcBattleInfoCol toBean(); // a Bean instance
	public npcBattleInfoCol toDataIf(); // a Data instance If need. else return this
	public npcBattleInfoCol toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getBattleroles(); // key为roleid,value是role对应队伍的人数
	public java.util.Map<Long, Integer> getBattlerolesAsData(); // key为roleid,value是role对应队伍的人数

}
