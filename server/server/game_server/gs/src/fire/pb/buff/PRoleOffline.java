package fire.pb.buff;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.RoleImpl;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.timer.ScheduledFutureMap;


public class PRoleOffline extends mkdb.Procedure
{
	private long roleId;

	public PRoleOffline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		//1 先清理角色的
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		RoleImpl erole = new RoleImpl(roleId);
		int hp = erole.getHp();
		int mp = erole.getMp();
		
		long now = System.currentTimeMillis();
		List<Integer> rmBuffIds = new ArrayList<Integer>();
		for(int buffId : buffagent.getAllBuffBeans().keySet())
		{
			//先停掉所有的计时器
			for(long futureId : buffagent.getAgent().getTimerfutures().values())
			{
				ScheduledFuture<?> timerFuture = ScheduledFutureMap.getInstance().get(futureId);
				if(timerFuture != null)
				{
					timerFuture.cancel(false);
					ScheduledFutureMap.getInstance().remove(futureId);
				}
			}
			
			ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
			if(cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_OFFLINE
				||	cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_OUT_BATTLE
				|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_DEATH
				|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_HURT)
				rmBuffIds.add(buffId);
			
			if(cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_OFFLINE_STOP_TIMING)
			{
				ConstantlyBuff cbuff = buffagent.getBuff(buffId);
				
				if(!BuffAgent.processBuffTime(cbuff, now))
				{
					fire.pb.buff.Module.logger.error("角色"+roleId+"下线时，持续性buff"+buffId+"时间超时:"+cbuff.getImpactTime()+","+cbuff.getTime());
					rmBuffIds.add(buffId);
				}
			}
		}
		for(int rmBuffId : rmBuffIds)
		{
			buffagent.removeCBuff(rmBuffId);
		}
		
		
		//将人物最后的活动属性（hp等）还原，最后发送客户端人物的最终属性
		erole.setHp(hp);
		erole.setMp(mp);
		
		xtable.Buffroles.remove(roleId);
		xtable.Erole.remove(roleId);
		
		//2 再清理两个宠物栏里的
		//直接清掉身上宠物和宠物栏宠物的buff和效果，上线彻底重新加载
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, false);
		for(Pet pet : petColumn.getPets())
			pet.offline();

		PetColumn depotColumn = new PetColumn(roleId, PetColumnTypes.DEPOT, false);
		for(Pet pet : depotColumn.getPets())
			pet.offline();
		
		return true;
	}
}
