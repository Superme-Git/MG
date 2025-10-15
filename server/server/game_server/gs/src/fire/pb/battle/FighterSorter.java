package fire.pb.battle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fire.pb.attr.FightAttrType;
import fire.pb.buff.BuffConstant;

public class FighterSorter
{
	public static class FighterComparator implements Comparator<Fighter> {
		private String sortnames;
		public FighterComparator(String sortnames) {
			this.sortnames = sortnames;
		}
		
		@Override
		public int compare(Fighter fighter1, Fighter fighter2) {
			
			// TODO 自动生成的方法存根
			if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

			if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
				return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;	
			

			String[] sortnamelist = sortnames.split(";");
			for(String sortname:sortnamelist)
			{
				int result = compare(fighter1, fighter2, sortname);
				if(result != 0)
				{
					return result;					
				}
			}
			
			if (Math.random() > 0.3)
				return 1;
			else
				return -1;
		}
		
		public int compare(Fighter fighter1, Fighter fighter2, String sortname) 
		{
			float value1 = 0;
			float value2 = 0;
			
			switch(sortname)
			{
			case "speed":
			{
				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP)? 1 : -1;		
				
				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2)? 1 : -1;		

				value1 = fighter1.getEffectRole().getAttrById(FightAttrType.SPEED);
				value2 = fighter2.getEffectRole().getAttrById(FightAttrType.SPEED);
			}
			break;
			case "hpmax":
			{
				value1 = fighter1.getEffectRole().getHpPercent();
				value2 = fighter2.getEffectRole().getHpPercent();
			}
			break;
			case "hpmin":
			{
				value1 = fighter2.getEffectRole().getHpPercent();
				value2 = fighter1.getEffectRole().getHpPercent();
			}
			break;
			case "mpmin":
			{
				value1 = fighter2.getEffectRole().getMpPercent();
				value2 = fighter1.getEffectRole().getMpPercent();
			}
			break;
			default:
			{
				if(sortname.contains("!"))
				{
					sortname = sortname.replace("!", "");
					int param = Integer.valueOf(sortname).intValue();
					if(fighter1.getBuffAgent().existBuff(param) != fighter2.getBuffAgent().existBuff(param))
						return  fighter1.getBuffAgent().existBuff(param)? 1 : -1;		

				}
				else
				{
					int param = Integer.valueOf(sortname).intValue();
					if(param < 10000)
					{
						if(param == 61)
						{
							value1 = fighter1.getEffectRole().getHpPercent();
							value2 = fighter2.getEffectRole().getHpPercent();					
						}
						else if(param == 91)
						{
							value1 = fighter1.getEffectRole().getMpPercent();
							value2 = fighter2.getEffectRole().getMpPercent();
						}
						else
						{
							value1 = fighter1.getEffectRole().getAttrById(param);
							value2 = fighter2.getEffectRole().getAttrById(param);
						}

					}	
					else
					{
						if(fighter1.getBuffAgent().existBuff(param) != fighter2.getBuffAgent().existBuff(param))
							return  fighter1.getBuffAgent().existBuff(param)? -1 : 1;		
					}					
				}				
			}
			}
			if (value1 > value2)
				return -1;
			if (value1 < value2)
				return 1;
			return 0;
		}
	}
	
	public static void sortFightersByAttrName(final List<Fighter> fighters, String sortname)
	{
		if(sortname == null)
		{
			// 将剩下的乱序返回
			Collections.shuffle(fighters);
			Collections.sort(fighters, new Comparator<Fighter>()
					{
						public int compare(Fighter fighter1, Fighter fighter2)
						{
							if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
								return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

							if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
								return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;
							
							if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP))
								return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP)? 1 : -1;		
							
							if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2))
								return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2)? 1 : -1;		
							
							return -1;
						}

					});
			return;
		}
		
		Collections.sort(fighters, new FighterComparator(sortname));
		//Comparator<T>.compare的返回值中，如果是负值，则不交换两者的顺序，如果是正值，交换顺序		 
/*		if(sortname.equals("speed"))
		{
			sortFightersByAttr(fighters, FightAttrType.SPEED, true, 0.3);
		} else if (sortname.equals("hpmax"))
		{// hp比例多的在前
			Collections.sort(fighters, new Comparator<Fighter>()
			{
				public int compare(Fighter fighter1, Fighter fighter2)
				{
					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;				

					float hpp1 = fighter1.getEffectRole().getHpPercent();
					float hpp2 = fighter2.getEffectRole().getHpPercent();
					if (hpp1 > hpp2)
						return -1;
					if (hpp1 < hpp2)
						return 1;
					if (Math.random() > 0.3)
						return 1;
					else
						return -1;
					// return 0;
				}

			});
		} else if (sortname.equals("hpmin"))
		{// hp比例少的在前
			Collections.sort(fighters, new Comparator<Fighter>()
			{
				public int compare(Fighter fighter1, Fighter fighter2)
				{
					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;				

					float hpp1 = fighter1.getEffectRole().getHpPercent();
					float hpp2 = fighter2.getEffectRole().getHpPercent();
					if (hpp1 < hpp2)
						return -1;
					if (hpp1 > hpp2)
						return 1;
					if (Math.random() > 0.3)
						return 1;
					else
						return -1;
					// return 0;
				}

			});
		}else if (sortname.equals("mpmin"))
		{// mp比例少的在前
			Collections.sort(fighters, new Comparator<Fighter>()
			{
				
				public int compare(Fighter fighter1, Fighter fighter2)
				{
					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

					if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
						return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;				

					float mpp1 = fighter1.getEffectRole().getMpPercent();
					float mpp2 = fighter2.getEffectRole().getMpPercent();
					if (mpp1 < mpp2)
						return -1;
					if (mpp1 > mpp2)
						return 1;
					if (Math.random() > 0.3)
						return -1;
					else
						return 1;
					// return 0;
				}

			});
		}*/
	}
	
	/**按属性值排序*/
	public static void sortFightersByAttr(final List<Fighter> fighters, final int attrId, final boolean maxmin, final double equalswap)
	{
		Collections.sort(fighters, new Comparator<Fighter>()
		{
			public int compare(Fighter fighter1, Fighter fighter2)
			{
				float v1 = 0.0f;				
				float v2 = 0.0f;
				if(attrId == 61)
				{
					v1 = fighter1.getEffectRole().getHpPercent();
					v2 = fighter2.getEffectRole().getHpPercent();					
				}
				else if(attrId == 91)
				{
					v1 = fighter1.getEffectRole().getMpPercent();
					v2 = fighter2.getEffectRole().getMpPercent();
				}
				else
				{
					v1 = fighter1.getEffectRole().getAttrById(attrId);
					v2 = fighter2.getEffectRole().getAttrById(attrId);
				}
				
				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)? 1 : -1;				

				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS)? 1 : -1;	
				
				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP)? 1 : -1;
				
				if(fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2) != fighter2.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2))
					return  fighter1.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2)? 1 : -1;				
				
				if (v1 > v2)
					return maxmin ? -1 : 1;
				if (v1 < v2)
					return maxmin ? 1 : -1;
				if (fighter1.getFighterId() > fighter2.getFighterId())
					return maxmin ? -1 : 1;
				if (fighter1.getFighterId() < fighter2.getFighterId())
					return maxmin ? 1 : -1;
				if (equalswap >= 1)
					return maxmin ? 1 : -1;
				if (equalswap <= 0)
					return 0;
				if (Math.random() < equalswap)
					return maxmin ? -1 : 1;
				return 0;
			}
		});
	}
}
