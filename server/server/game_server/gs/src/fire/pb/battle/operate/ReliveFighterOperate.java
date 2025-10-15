package fire.pb.battle.operate;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.BattleSequenceHelper;
import fire.pb.battle.ResultItem;

public class ReliveFighterOperate extends Operate
{
	public ResultItem process(ListIterator<Operate> lit)
	{
		if(getBattle().getReliveacts().isEmpty())
			return null;
		List<fire.pb.battle.operate.Operate> extActs = new LinkedList<Operate>();
		for(fire.pb.battle.operate.Operate act : getBattle().getReliveacts())
		{
			if(!(act instanceof fire.pb.battle.operate.FighterOperate))
				continue;
			extActs.add(act);
		}
		Collections.sort(extActs,new Comparator<fire.pb.battle.operate.Operate>()
		{
			@Override
			public int compare(Operate o1, Operate o2)
			{
				fire.pb.battle.operate.FighterOperate act1  = (fire.pb.battle.operate.FighterOperate)o1;
				fire.pb.battle.operate.FighterOperate act2  = (fire.pb.battle.operate.FighterOperate)o2;
				if(act1.getFighter().getSeqSpeed() >= act2.getFighter().getSeqSpeed())
					return -1;
				else
					return 1;
			}
		});
		extActs.add(new ReliveFighterOperate());
		getBattle().getReliveacts().clear();
		BattleSequenceHelper.insertExtraActs(getBattle(), lit, getBattle().getFieldfighter(), extActs);
		return null;
	}
	
}
