package fire.pb.buff.continual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.buff.BuffConfig;
import fire.pb.buff.BuffConstant;

/**默认的持续性buff表配置*/
public class ConstantlyBuffConfig extends BuffConfig
{
	private int buffType;//buff类型，不同于BuffId,是buff比较细的归类类型，例如HOT类，毒类，增益类等等
	private long initTime = 0;//时间
	private int initRound = 0;//回合数/Periodbuff时的执行次数
	private long initValue = 0;//量（吸收量/攻击次数等）
	private long initDelay = 0;//开始延迟时间，用于PeriodBuff（周期性buff）
	private int cleanType;//
	private boolean saveToDisk;//硬盘保存
	private boolean sendClient;
	private boolean snedWithScript;//是否跟随战斗脚本一起发送，当需要在脚本播放时增删buff，并且脚本中的buff会被所有战斗中角色所得到
	private int roundBuffId;//回合结算的buff，记录每回合结束时释放的一次性BuffId
	private String effectJavaScript;
	private int sceneId = 0;//场景ID，没有则为0
	private Map<Integer,Float> effects = new HashMap<Integer, Float>();
	private String[] otherArgs;
	private int overrideSelfType = 0;
	private List<Integer> overrideAttrs = new ArrayList<Integer>(); 
	private List<Integer> limitedBattleOperations = new ArrayList<Integer>();	
	private boolean isShowTime;// 在客户端是否现显示计时
	
	public final static int OVERRIDE_TYPE_ALL = 0;	
	public final static int OVERRIDE_TYPE_ATTR_ADD = 1;
	public final static int OVERRIDE_TYPE_ATTR_HIGHER = 2;
	

	public List<Integer> getLimitedBattleOperations()
	{
		return limitedBattleOperations;
	}
	
	/**不要使用，初始化默认配置时使用*/
	public ConstantlyBuffConfig(fire.pb.buff.SCBuffConfig scbuffconfig) throws Exception
	{
		
		buffTypeId = scbuffconfig.id;
		className = scbuffconfig.classname;
		Class<?> cls = Class.forName(className);
		if(cls == null)
			throw new IllegalArgumentException("buff:" + buffTypeId + " class not exists.");
		isShowTime = ! fire.pb.buff.continual.PeriodBuff.class.isAssignableFrom(cls);
		if(buffTypeId== BuffConstant.CONTINUAL_CLANBOSS_SPASE)
			isShowTime = true;
		buffType = scbuffconfig.buffclass;
		initTime = scbuffconfig.initTime * 1000;
		initRound = scbuffconfig.initCount;
		initDelay = initValue = scbuffconfig.initAmount;
		cleanType = scbuffconfig.clearType;
		saveToDisk = scbuffconfig.storeToDisk;
		sendClient = scbuffconfig.sendtoclient == 1;
		snedWithScript = scbuffconfig.inBattleScript;
		roundBuffId = scbuffconfig.instantbuff;
		effectJavaScript = scbuffconfig.instantbuffeffect;
		overrideSelfType = scbuffconfig.overrideSelfType;
		if (scbuffconfig.limitOperations != null)
		{
			String[] strs = scbuffconfig.limitOperations.split(";");
			for (String str : strs)
			{
				limitedBattleOperations.add((int)Float.parseFloat(str));
			}
		}
		
		if(scbuffconfig.scenestateId != null && !scbuffconfig.equals(""))
		{
			sceneId = (int)Float.parseFloat(scbuffconfig.scenestateId);
		}
		
		if(scbuffconfig.overrideAttr != null)
		{
			String[] strs = scbuffconfig.overrideAttr.split(";");
			for (String str : strs)
			{
				overrideAttrs.add((int)Float.parseFloat(str));
			}
		}
/*			overrideAttr = 0;
		else
			overrideAttr = fire.pb.effect.Module.getInstance().getIdByName(scbuffconfig.overrideAttr);
*/
		effects.putAll(fire.pb.effect.Module.parseEffects(scbuffconfig.getEffects()));
		
	}
	
	public long getInitTime(java.util.concurrent.TimeUnit tu)
	{
		if(tu.equals(java.util.concurrent.TimeUnit.SECONDS))
			return initTime/1000;
		else if(tu.equals(java.util.concurrent.TimeUnit.MILLISECONDS))
			return initTime;
		else if(tu.equals(java.util.concurrent.TimeUnit.HOURS))
			return initTime/3600000;
		return initTime;
	}
	
	public long getInitTime()
	{
		return initTime;
	}

	public int getInitCount()
	{
		return initRound;
	}

	public long getInitValue()
	{
		return initValue;
	}

	public boolean isStoreToDisk()
	{
		return saveToDisk;
	}

	public Map<Integer,Float> getEffects()
	{
		return effects;
	}

	public String[] getOtherArgs()
	{
		return otherArgs;
	}

	public int getClearType()
	{
		return cleanType;
	}
	
	public int getPeriodCount()
	{
		return initRound;
	}

	public long getInitDelay()
	{
		return initDelay;
	}

	public int getRoundBuffId()
	{
		return roundBuffId;
	}

	public String getRoundBuffEffectJavaScript()
	{
		return effectJavaScript;
	}
	
	public boolean isSnedWithScript()
	{
		return snedWithScript;
	}

	public int getBuffType()
	{
		return buffType;
	}

	public int getInitRound()
	{
		return initRound;
	}
	
	public long getPeriod()
	{
		return initTime;
	}
	
	public int getOverrideSelfType()
	{
		return overrideSelfType;
	}

	public List<Integer> getOverrideAttrs()
	{
		return overrideAttrs;
	}
	
	public int getSceneId()
	{
		return sceneId;
	}
	
	public boolean isShowTime()
	{
		return isShowTime;
	}
	
	public boolean isSendClient()
	{
		return sendClient;
	}
	
}
