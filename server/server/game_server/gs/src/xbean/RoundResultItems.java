
package xbean;

public interface RoundResultItems extends mkdb.Bean {
	public RoundResultItems copy(); // deep clone
	public RoundResultItems toData(); // a Data instance
	public RoundResultItems toBean(); // a Bean instance
	public RoundResultItems toDataIf(); // a Data instance If need. else return this
	public RoundResultItems toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<fire.pb.battle.NewResultItem> getResultitems(); // 一个回合战斗的demo
	public java.util.List<fire.pb.battle.AIOperation> getAiactions(); // 随战斗脚本播放的客户端AI动作
	public java.util.Map<Integer, Integer> getFighterfinallyhps(); // 回合结束时战斗者血量的最终值
	public java.util.Map<Integer, Integer> getFighterfinallyhpsAsData(); // 回合结束时战斗者血量的最终值
	public java.util.Map<Integer, Integer> getFighterfinallymps(); // 回合结束时战斗者兰量的最终值
	public java.util.Map<Integer, Integer> getFighterfinallympsAsData(); // 回合结束时战斗者兰量的最终值

}
