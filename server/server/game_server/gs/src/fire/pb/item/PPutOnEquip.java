package fire.pb.item;

import java.util.List;

import fire.log.LogManager;
import fire.msp.move.GRoleEquipChange;
import fire.msp.role.GChangeEquipEffect;
import fire.pb.GsClient;
import fire.pb.course.CourseType;
import fire.pb.item.equip.WeaponItem;
import fire.pb.item.equip.diamond.EquipDiamondMgr;
import fire.pb.skill.SkillRole;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PPutOnEquip extends Procedure
{

	private final long roleId;
	private final int bagkey;
	private final int position;
	public PPutOnEquip( long roleId,int bagkey,int position ) {
		this.roleId = roleId;
		this.bagkey = bagkey;
		this.position = position;
	}
	@Override
	protected boolean process() throws Exception {
		final Pack bag = new Pack(roleId, false);
		final Equip equip = new Equip(roleId, false);
		
		ItemBase bi = bag.getItem(bagkey);
		if (bi == null) {
			return false;
		}
		
		if (((EquipItem)bi).getEndure() == 0) {
			MessageMgr.psendMsgNotify(roleId, 160319, null);
			return false;
		}
		
		final int pos = bi.getPosition();
		bi = bag.TransOut(bagkey, -1, "穿装备");
		if (!(bi instanceof EquipItem)) {
			return false;
		}
		EquipItem.EquipError errorcode = canEquip(equip, (EquipItem)bi, position);
		if (errorcode == EquipItem.EquipError.NO_ERROR) {
			ItemBase dstitem = equip.getItemByPos(position);
			ItemBase item = null;
			if (dstitem != null) {
				item = equip.TransOut(dstitem.getKey(), -1, "卸下装备");
				if (item == null)
					return false;
				if (!bag.TransIn(item, pos))
					return false;
			}
			
			if (!equip.TransIn(bi, position))
				return false;
			
			if ((bi.getFlags() & (fire.pb.Item.ONSTALL & fire.pb.Item.ONCOFCSELL)) != 0)
				return false;
			
			//添加自动分解, 判断是否需要自动分解
			if (item != null) {
				int nDstEquipLevel = ((EquipItem)item).getEquipAttr().getEquiplevel();
				int nSrcEquipLevel = ((EquipItem)bi).getEquipAttr().getEquiplevel();
				
				//被替换下的装备可以自动分解
				EquipItemShuXing eiAttr = (EquipItemShuXing) item.getItemAttr();
				if (eiAttr.get是否自动分解() == 1 && ((((EquipItem) item).getEquipAttr()).getDiamonds().size() == 0)) {
					if (nSrcEquipLevel > nDstEquipLevel) {
						new PResolveItem(roleId, item.getKey()).call();
						MessageMgr.psendMsgNotifyWhileCommit(roleId, 160218, null);
					}
				}
			}
			
			//装备宝石自动替换
			if (item != null) {
				ItemBase itemEquip = equip.getItemByPos(position);
				xbean.Equip equipSrcAttr = ((EquipItem) item).getEquipAttr();
				xbean.Equip equipDstAttr = ((EquipItem) itemEquip).getEquipAttr();
				EquipItemShuXing eiDesAttr = (EquipItemShuXing) itemEquip.getItemAttr();
				SEquipLvGemInfo equipLvGemInfo = EquipDiamondMgr.getEquipLvGemInfoByLv(eiDesAttr.level);
				if(equipLvGemInfo == null) {
					LogManager.logger.error("error equipLv in PPutOnEquip itemId=" + eiDesAttr.id);
					return false;
				}
				if (equipLvGemInfo.gemsLevel > 0) {
					if (equipDstAttr.getDiamonds().size() == 0) {
						// 源装备宝石列表
						List<Integer> diamonds = equipSrcAttr.getDiamonds();
						if (diamonds.size() > 0) {
							SReplaceGem repGem = new SReplaceGem();
							repGem.srckey = item.getKey();
							repGem.deskey = itemEquip.getKey();
							mkdb.Procedure.psendWhileCommit(roleId, repGem);
						}
					}
				}
			}
			
			if (item != null) {
				SkillRole srole = new SkillRole(roleId);
				srole.removeSpecialSkillWithSP(position);
			}
		
			freshEquipBuff(roleId, (EquipItem)bi);
			//更新玩家综合实力排行榜
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(roleId));
			//更新历程信息
			fire.pb.course.CourseManager.achieveCourse(roleId, CourseType.EQUIP_SPECIAL_COURSE, bi.getItemId(), 0);
			
			xbean.Equip equipAttrOne = ((EquipItem) bi).getEquipAttr();
			if (equipAttrOne.getSkill() != 0) {
				EquipItemShuXing eiAttrOne = (EquipItemShuXing) bi.getItemAttr();
				fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.HAVE_TE_JE_EQUIP, eiAttrOne.level);
			}
			
			if (equipAttrOne.getEffect() != 0) {
				fire.pb.course.CourseManager.achieveUpdate(roleId, CourseType.PUT_ON_EQUIP);
			}
			
			Equip.checkEquipDiamondCourse(roleId);
			
			mkdb.Procedure.pexecuteWhileCommit(new PEnhancementTimeout(roleId));
			
			return true;
		} else if (errorcode == EquipItem.EquipError.LEVEL_NOT_SUIT) {
			MessageMgr.psendMsgNotify(roleId, 100065, null);
			return false;
		} else if (errorcode == EquipItem.EquipError.SCHOOL_NOT_SUIT) {
			MessageMgr.psendMsgNotify(roleId, 174002, null);
			return false;
		}
		else {
			return false;
		}
	}
	
	public static void freshEquipBuff(final long roleId, EquipItem ei) {
		Equip equip = new Equip(roleId, true);
		if (ei != null) {
			//获取装备最低的品质
			Integer nquality = Integer.MAX_VALUE;
			if (equip.size() == 6) {
				for (fire.pb.item.ItemBase item : equip) {
					if (item instanceof EquipItem) {
						if (item.getItemAttr().nquality < nquality)
							nquality = item.getItemAttr().nquality;
					}
				}
				
				fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.ALL_PUT_ON_EQUIP, nquality, 0);
			}
			else {
				nquality = 0;
			}
			
			xbean.Properties pProp = xtable.Properties.get(roleId);
			pProp.setEquipeffect(nquality);
			
			GRoleEquipChange notifymap = new GRoleEquipChange();
			notifymap.roleid = roleId;
			notifymap.pos = ei.getPosition();
			notifymap.itemid = ei.getItemId();
			notifymap.ride = -1;
			notifymap.effect = nquality;
			
			if (ei instanceof WeaponItem)
				notifymap.itemcolor = ((WeaponItem)ei).getItemAttr().equipcolor;
			GsClient.pSendWhileCommit(notifymap);
			
			if (nquality > 0) {
				GChangeEquipEffect equipeffect = new GChangeEquipEffect();
				equipeffect.effect = nquality;
				equipeffect.roleid = roleId;
				GsClient.pSendWhileCommit(equipeffect);
			}
		}
		Long teamId = xtable.Roleid2teamid.get(roleId);
		if (teamId != null)
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
				protected boolean process() throws Exception{
					final fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
					if (team != null){
						team.updateTeamMemberComponents2Others(roleId);
					}
					return true;
				};
			});
		
		SAllEquipScore equipTotalScore = new SAllEquipScore();
		int totalScore = Module.getInstance().getEquipTotalScore(roleId);
		equipTotalScore.score = totalScore;
		mkdb.Procedure.psendWhileCommit(roleId, equipTotalScore);
		if (ei != null) {
			if (ei.getExtInfo().getEndure() > 0) {
				fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager
						.getSceneSkillRole(roleId);
				role.addEquipEffectAndSkillWithSP(ei);
			}
		}
	}
	
	private static EquipItem.EquipError canEquip(Equip equip, EquipItem item, int dstpos) {
		xbean.Properties prop = xtable.Properties.select(equip.roleId);
		int roleLevel = prop.getLevel();
		int sex = prop.getSex();
		int shape = prop.getShape();
		int school = prop.getSchool();
		EquipItem.EquipError ret = item.canEquipment(dstpos, roleLevel, sex, shape, school);
//		if ((ret = item.canEquipment(dstpos, roleLevel, sex, shape, school)) == EquipItem.EquipError.NO_ERROR) {
//		}
		return ret;
	}
}
