package fire.pb.buff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.RoleImpl;
import fire.pb.item.EquipItem;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.SkillRole;

/**
 * 上线时加载所有buff，但是不发给客户端，最后在外面一起发
 *
 */
public class PRoleOnline extends mkdb.Procedure
{
	private long roleId;

	public PRoleOnline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@SuppressWarnings("null")
	@Override
	protected boolean process()
	{
		//如果角色上线时没有各种buff，说明服务器重启过，则重新加载各种buff，重新计算属性
		xbean.BuffRole xbrole = xtable.Buffroles.get(roleId);
		//TEMP 暂时加此代码，下线再上线全部重新计算属性
		//if(xbrole != null)
		xtable.Buffroles.remove(roleId);
		xbrole = null;
		xtable.Erole.remove(roleId);
		//TEMP END
		RoleImpl erole = new RoleImpl(roleId);
		BuffRoleImpl brole = new BuffRoleImpl(roleId);//新建BuffRole
//		Map<Integer, xbean.Buff> allxbuffs = new HashMap<Integer, xbean.Buff>();
		long now = System.currentTimeMillis();
		
		if(xbrole == null)//xbean.BuffRole为空表示停服后内存清空
		{
			//角色的
			int hp = erole.getHp();
//			int mp = erole.getMp();

			// 1.加装备Buff
			fire.pb.item.Equip equip = new fire.pb.item.Equip( roleId, false );
			fire.pb.skill.SceneSkillRole nrole = 
				fire.pb.skill.SkillManager.getSceneSkillRole( roleId );
			for (fire.pb.item.ItemBase item : equip) {
				if (item instanceof EquipItem) {
					if (((EquipItem) item).getExtInfo().getEndure() > 0) {
						Map<Integer, Float> effects = new HashMap<Integer, Float>();
						List<ConstantlyBuffConfig> buffs = new LinkedList<ConstantlyBuffConfig>();
						((EquipItem) item).getEffectsAndBuffs(effects, buffs);
						
						xbean.Equip equipAttr = ((EquipItem) item).getExtInfo();
						List<Integer> skills = new ArrayList<Integer>();
						if (equipAttr.getSkill() > 0) {
							skills.add(equipAttr.getSkill());
						}
						if (equipAttr.getEffect() > 0) {
							skills.add(equipAttr.getEffect());
						}
						nrole.equip(((EquipItem) item).getEquipType(), effects,
								skills);
					}
				}
			}
			// 2.加职业被动技能和生活被动技能
			SkillRole srole = new SkillRole(roleId,false);
			srole.addSkillBuffWhileOnline(null);
			
			// 3.加载各种保存到硬盘的buff
			List<Integer> timeoutbuffs = new LinkedList<Integer>();
			List<ConstantlyBuff> addbuffs = new LinkedList<ConstantlyBuff>();
			
			for(xbean.Buff buffBean : brole.getStoredBuffRole().getBuffs().values())
			{
				ConstantlyBuff cbuff = Module.getInstance().createConstantlyBuff(buffBean);
				if(!BuffAgent.processBuffTime(cbuff, now))
				{
					fire.pb.buff.Module.logger.info("角色"+roleId+"上线时，持续性buff"+cbuff.getId()+"时间超时:"+cbuff.getImpactTime()+","+cbuff.getTime());
					cbuff.setTime(0);
					timeoutbuffs.add(cbuff.getId());//到时间的执行detach
				}
				addbuffs.add(cbuff);//没到时间的加载
			}
			
			//先全部加载所有的buff
			brole.getStoredBuffRole().getBuffs().clear();
			for(ConstantlyBuff cbuff : addbuffs)
			{
				brole.addCBuff(cbuff);
			}
			//对过时的buff执行移除
			for(int delbuffid : timeoutbuffs)
			{
				brole.removeTimeoutCBuff(delbuffid);
			}
			erole.updateAllFinalAttrs();

			// 4.将人物最后的活动属性（hp等）还原，最后发送客户端人物的最终属性
			if (hp==0)
				hp=1;
			erole.setHp(hp);
		}
		
		//给客户端发送人物发送buff在后面一起发
		//宠物的（宠物上线加载的buff不用发给客户端）
		// 5.人物身上的宠物栏
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, false);

		for(Pet pet : petColumn.getPets())
			pet.online();
		
		// 6.仓库宠物栏
		PetColumn depotColumn = new PetColumn(roleId, PetColumnTypes.DEPOT, false);
		
		for(Pet pet : depotColumn.getPets())
			pet.online();
				
		return true;
	}
	

}
