package fire.pb.effect;


public class Effect
{
	//private long roleId;
	private xbean.Effect effectBean;
	
	public Effect(xbean.Effect effectBean)
	{
		this.effectBean = effectBean;
	}
	
	public Effect(int effectType , float value)
	{
		effectBean = xbean.Pod.newEffect();
		effectBean.setEffecttypeid(effectType);
		effectBean.setValue(value);
		effectBean.setEnable(false);
	}
	
	public void attach(long roleId)
	{		
		fire.pb.effect.Role eRole = new fire.pb.effect.RoleImpl(roleId);
		eRole.attachEffect(effectBean.getEffecttypeid(), effectBean.getValue());
		
		effectBean.setEnable(true);
	}	
	
	public void detach(long roleId)
	{
		fire.pb.effect.Role eRole = new fire.pb.effect.RoleImpl(roleId);
		eRole.detachEffect(effectBean.getEffecttypeid(), effectBean.getValue());
		
		effectBean.setEnable(false);
	}
	
	public void changeValue(float addValue,long roleId,xbean.Buff buffBean) 
	{
		float newValue = effectBean.getValue()+addValue;
		if(effectBean.getEnable())
		{
			detach(roleId);
			effectBean.setValue(newValue);
			attach(roleId);
		}
		else
			effectBean.setValue(newValue);		
	}

	public xbean.Effect getEffectBean()
	{
		return effectBean;
	}

	public void setEffectBean(xbean.Effect effectBean)
	{
		this.effectBean = effectBean;
	}

}
