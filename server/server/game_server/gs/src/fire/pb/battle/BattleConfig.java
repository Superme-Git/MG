package fire.pb.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.util.Parser.ID2Odds;
import fire.script.JavaScript;

public class BattleConfig
{
	int id;	
	int sceneId = -1;
	int fightAItype = 1;
	public int cameratype = 0;
	List<Integer> battleAIs = new ArrayList<Integer>();
	public JavaScript monsterNumScript;	
	public int environment = 0;//战场环境	
	public int award = 0;	
	public int background = 0;	
	public int bgm = 0;	
	public JavaScript leveljs = null;	
	public final Map<Integer, ID2Odds> monsterRands = new HashMap<Integer, ID2Odds>(); 	
	public int xiezhannum;
	public int victoryconditionType = 1;
	public int isNotDecEndure = 0;
	List<Integer> randomPos = new ArrayList<Integer>(); //站位随机 by changhao

	public static class MonsterRandoms
	{
		private final int[] monsterId;
		private final int[] odds;
		
		public MonsterRandoms(int num)
		{
			monsterId = new int[num];
			odds = new int[num];
		}

		public int[] getMonsterId()
		{
			return monsterId;
		}

		public int[] getOdds()
		{
			return odds;
		}
		
	}
}


