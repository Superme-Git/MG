package fire.pb.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class FormationConfig
{
	private int formId;
	@SuppressWarnings("unchecked")
	private Map<Integer,Float>[] effects = new Map[10];
	@SuppressWarnings("unchecked")
	private Map<Integer,Float>[] suppressEffects = new Map[10];	
	private Map<Integer,BattleField.Position> positions = new HashMap<Integer, BattleField.Position>();	
	private int suppressform1 = 0;
	private int suppressform2 = 0;	
	private Map<Integer, Float> mapSuppressform1 = new HashMap<Integer, Float>();	
	private Map<Integer, Float> mapSuppressform2 = new HashMap<Integer, Float>();	
	private int formLevel;	
	private int exp; //升级需要的经验 by changhao	
	private String name; //光环名字 by changhao
	
	public FormationConfig(int formId, int formLevel)
	{
		this.formId = formId;
		this.formLevel = formLevel;
	}
	
	public FormationConfig(int formId)
	{
		this.formId = formId;
	}
	
	public int getFormId()
	{
		return formId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getFormLevel(){
		return formLevel;
	}
	public Map<Integer, Float>[] getEffects()
	{
		return effects;
	}

	public void setEffects(Map<Integer, Float>[] effects)
	{
		this.effects = effects;
	}

	
	
	public Map<Integer, BattleField.Position> getPositions()
	{
		return positions;
	}


	public Map<Integer, Float>[] getSuppressEffects()
	{
		return suppressEffects;
	}

	public void setSuppressEffects(Map<Integer, Float>[] suppressEffects)
	{
		this.suppressEffects = suppressEffects;
	}


	public int getSuppressform1()
	{
		return suppressform1;
	}

	public void setSuppressform1(int suppressform1)
	{
		this.suppressform1 = suppressform1;
	}

	public int getSuppressform2()
	{
		return suppressform2;
	}
	
	public Map<Integer, Float> getmapSuppressform1()
	{
		return mapSuppressform1;
	}
	
	public Map<Integer, Float> getmapSuppressform2()
	{
		return mapSuppressform2;
	}
	
	public int getExp()
	{
		return exp;
	}
	
	void setExp(int exp)
	{
		this.exp = exp;
	}

	public void setSuppressform2(int suppressform2)
	{
		this.suppressform2 = suppressform2;
	}

	//初始位置ID与客户端点位的关系，因阵型表里填的是客户端坐标，要做一个转化
	//在普通阵时，FighterId是与PosiotnId对应的，即FighterId(1)==BATTLE_POSITION_1,但在其他阵型时不一定对应
	//位置内存的值是像素坐标，与阵型表（formationbase.xlsx）中的普通阵对应
	public static BattleField.Position[] bps = new BattleField.Position[BattleField.A_SIDE_FIGHTER_NUM];
	static
	{
		bps[0] = new BattleField.Position(3,2);
		bps[1] = new BattleField.Position(4,2);
		bps[2] = new BattleField.Position(2,2);
		bps[3] = new BattleField.Position(5,2);
		bps[4] = new BattleField.Position(1,2);
		bps[5] = new BattleField.Position(3,1);
		bps[6] = new BattleField.Position(4,1);
		bps[7] = new BattleField.Position(2,1);
		bps[8] = new BattleField.Position(5,1);
		bps[9] = new BattleField.Position(1,1);
		bps[10] = new BattleField.Position(3,3);
		bps[11] = new BattleField.Position(4,3);
		bps[12] = new BattleField.Position(2,3);
		bps[13] = new BattleField.Position(3,4);
	}
	public static Map<Integer,BattleField.Position> cposition2sposition = new HashMap<Integer, BattleField.Position>();
	
	public static Map<Integer,List<Integer>> cposition2fightid = new HashMap<Integer, List<Integer>>();
	
	public static int GetPosbyFighteid(int formId, int Fighteid)
	{
		int noffset = Fighteid<15?0:BattleField.A_SIDE_FIGHTER_NUM;
		if(cposition2fightid.containsKey(formId)&& cposition2fightid.get(formId).contains(Fighteid-noffset))
		{
			return cposition2fightid.get(formId).get(Fighteid-1-noffset);
		}
		return -1;
	}

	public static int GetFighteidbyPos(int formId, int pos)
	{
		if(cposition2fightid.containsKey(formId)&& cposition2fightid.get(formId).contains(pos))
		{
			for(int i = 0; i < cposition2fightid.get(formId).size(); i++)
			{
				if(cposition2fightid.get(formId).get(i) == pos)
					return i+1;
			}
		}
		return -1;
	}
	
	
	public static List<Integer> GetPosbyFighteids(int formId, List<Integer> poss, boolean ishost)
	{
		if(poss==null)
			return null;
		List<Integer> Fighteids = new ArrayList<Integer>();
				
		for(int pos:poss)
		{
			Fighteids.add(GetFighteidbyPos(formId,pos)+(ishost?0:BattleField.A_SIDE_FIGHTER_NUM));
		}
		return Fighteids;
	}	
	
	public static BattleField.Position getSPosition(Integer cposition)
	{
		return cposition2sposition.get(cposition);
	}
	
	
}