package fire.pb.battle.operate;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.BattleField;
import fire.pb.battle.BattleSequenceHelper;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultItem;

/**回合末总动作*/
public class RoundEndOperate extends Operate
{
	public ResultItem process(ListIterator<Operate> lit)
	{
		List<RoundEndFighterOperate> extacts = getRoundEndFighterOperates(getOriginSequence());
		BattleSequenceHelper.insertExtraActs(getBattle(), lit, getBattle().getFieldfighter(), extacts);
		return null;
	}
	
	private List<RoundEndFighterOperate> getRoundEndFighterOperates(List<xbean.Fighter> sequence)
	{
		List<RoundEndFighterOperate> extActs = new LinkedList<RoundEndFighterOperate>();
		List<Integer> extActerids = new LinkedList<Integer>();
		for (xbean.Fighter fighterbean : sequence)
		{
			if (BattleField.checkOutBattle(getBattle(), fighterbean.getBattleindex()))
				continue;
			if(extActerids.contains(fighterbean.getBattleindex()))
				continue;
			Fighter fighter = getBattle().getFighterobjects().get(fighterbean.getBattleindex());
			extActs.add(new RoundEndFighterOperate(fighter));
			extActerids.add(fighterbean.getBattleindex());
		}
		return extActs;
	}

}
