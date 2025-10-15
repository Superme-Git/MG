/**
 * 指引我吧
 *
 */

package fire.pb.course;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.log.beans.AchievementBean;
import fire.log.beans.ItemBaseBean;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.Items;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.main.ConfigManager;
import fire.pb.mission.ArchiveInfo;
import fire.pb.mission.GuideCourse;
import fire.pb.mission.SGetArchiveInfo;
import fire.pb.mission.SGuideCourse;
import fire.pb.mission.SGuideCourseDianKa;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class CourseManager {
	public static class CourseKey {
		private final int activeType;
		private final int param1;
		private final int param2;

		public CourseKey(final int activeType, final int param1, final int param2) {
			this.activeType = activeType;
			this.param1 = param1;
			this.param2 = param2;
		}
		
		@Override
		public boolean equals(final Object o) {
			if (o instanceof CourseKey) {
				final CourseKey oth = (CourseKey) o;
				return oth.activeType == activeType && oth.param1 == param1 && oth.param2 == param2;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return (activeType << 10) + (param1 << 3) + param2;
		}
	}
	
	public static class CourseVal {
		private final int activeId; //历程id
		private final int awardid;
		private final int itemid;
		private final int itemnum;
		private final int petid1;
		private List<Integer> param1;
		private List<Integer> param2;

		public CourseVal(final GuideCourse guideCourse) {
			this.activeId = guideCourse.id;
			this.awardid = guideCourse.awardid;
			this.itemid = guideCourse.itemid;
			this.itemnum = guideCourse.itemnum;
			this.petid1 = guideCourse.petid1;
			
			param1 = new ArrayList<Integer>();
			param2 = new ArrayList<Integer>();
			
			InitParam(guideCourse.ref1, guideCourse.ref2);
		}
		
		public void InitParam(final String paramStr1, final String paramStr2) {
			if (paramStr1 != null) {
				String [] param = paramStr1.split(",");
				
				for (String pm : param) {
					this.param1.add(Integer.parseInt(pm));
				}
			}
			
			if (paramStr2 != null) {
				String [] param = paramStr2.split(",");
				
				for (String pm : param) {
					this.param2.add(Integer.parseInt(pm));
				}
			}
		}
		
		public int getActiveId() {
			return activeId;
		}
		
		public int getItemId() {
			return itemid;
		}
		
		public int getItemNum() {
			return itemnum;
		}
		
		public int getAwardId() {
			return awardid;
		}
		
		public int getPetid1() {
			return petid1;
		}
	}
	
	//历程的配置信息map, 用CourseKey做key
	public static Map<CourseKey, CourseVal> courseCfg = new HashMap<CourseManager.CourseKey, CourseManager.CourseVal>();
	//用activeid做key的历程配置
	static Map<Integer, CourseVal> courseCfgById = new HashMap<Integer, CourseManager.CourseVal>();
	static List<Integer> courseList = new ArrayList<Integer>();
	
	public static final Logger logger = Logger.getLogger("AWARD");
	private static final CourseManager instance = new CourseManager();
	public static CourseManager getInstance() {
		return instance;
	}

	private CourseManager() {
	}
	
	public void Init() {
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			Map<Integer, SGuideCourse> guideCourse = fire.pb.main.ConfigManager.getInstance().getConf(SGuideCourse.class);
			for (final SGuideCourse guidecfg : guideCourse.values()) {
				CourseVal valById = new CourseVal(guidecfg);
				courseCfgById.put(guidecfg.getId(), valById);
				courseList.add(guidecfg.getId());
				
				//有的活动不需要解析参数,只在判断的时候需要参数
				if (guidecfg.finish == CourseType.GUILD_SPEAKING_COURSE ||
					guidecfg.finish == CourseType.WORLD_SPEAKING_COURSE ||
					guidecfg.finish == CourseType.DAZHAO_COURSE ||
					guidecfg.finish == CourseType.WABAO_COURSE ||
					guidecfg.finish == CourseType.SKILL_LEVELUP_COURSE ||
					guidecfg.finish == CourseType.XIULIAN_COURSE ||
					guidecfg.finish == CourseType.ZENG_SONG ||
					guidecfg.finish == CourseType.ZHI_HUI_SI_LIAN_CHU ||
					guidecfg.finish == CourseType.PUT_ON_EQUIP ||
					guidecfg.finish == CourseType.WASH_PET ||
					guidecfg.finish == CourseType.LEARN_PET ||
					guidecfg.finish == CourseType.XIANG_QIAN_EQUIP ||
					guidecfg.finish == CourseType.HE_PET ||
					guidecfg.finish == CourseType.XI_LIAN_PET ||
					guidecfg.finish == CourseType.PET_SCORE ||
					guidecfg.finish == CourseType.QI_ZUO_QI ||
					guidecfg.finish == CourseType.REN_ZHENG_PET ||
					guidecfg.finish == CourseType.HE_PET_JI_NENG ||
					guidecfg.finish == CourseType.YUAN_ZHU_ZHAN_DOU ||
					guidecfg.finish == CourseType.GONG_HUI_JUAN_ZHU ||
					guidecfg.finish == CourseType.HAVE_TE_JE_EQUIP ||
					guidecfg.finish == CourseType.XIANG_QIAN_EQUIP_LEVEL ||
					guidecfg.finish == CourseType.SHENG_HUO_JINENG ||
					guidecfg.finish == CourseType.GET_SHENGWANG
					) {
					
					CourseKey key = new CourseKey(guidecfg.finish, 0, 0);
					CourseVal val = new CourseVal(guidecfg);
					courseCfg.put(key, val);
				}
				else {
					int param1 = 0;
					int param2 = 0;
					
					if (guidecfg.ref1 == null)
						continue;
					
					String [] paramStr1 = guidecfg.ref1.split(",");
					for (String pm1 : paramStr1) {
						param1 = Integer.parseInt(pm1);
						//不需要第二个参数,只在完成时判断
						if (guidecfg.finish != CourseType.XIANGQIAN_COURSE
								&& guidecfg.finish != CourseType.ACTIVE_COURSE
								&& guidecfg.finish != CourseType.USE_YAOPIN_COURSE
								&& guidecfg.finish != CourseType.HAVE_PET_COURSE
								&& guidecfg.finish != CourseType.OPEN_PAIHANGBANG_COURSE
								&& guidecfg.finish != CourseType.QIANG_HONG_BAO
								&& guidecfg.finish != CourseType.HAO_YOU_DU
								&& guidecfg.finish != CourseType.XIANG_QIAN_GEM
								&& guidecfg.finish != CourseType.FRIEND_COURSE
								&& guidecfg.finish != CourseType.NB_SKILL_COURSE) {
							/*if (guidecfg.ref2 != null) {
								String [] paramStr2 = guidecfg.ref2.split(",");
								for (String pm2 : paramStr2) {
									param2 = Integer.parseInt(pm2);
								}
							}*/
							
							if (guidecfg.ref2 != null && !guidecfg.ref2.equals("")) {
								param2 = Integer.parseInt(guidecfg.ref2);
							}
						}
						
						CourseKey key = new CourseKey(guidecfg.finish, param1, param2);
						CourseVal val = new CourseVal(guidecfg);
						courseCfg.put(key, val);
					}
				}
			}
		}
		else { //点卡服
			Map<Integer, SGuideCourseDianKa> guideCourse = fire.pb.main.ConfigManager.getInstance().getConf(SGuideCourseDianKa.class);
			for (final SGuideCourseDianKa guidecfg : guideCourse.values()) {
				CourseVal valById = new CourseVal(guidecfg);
				courseCfgById.put(guidecfg.getId(), valById);
				courseList.add(guidecfg.getId());
				
				//有的活动不需要解析参数,只在判断的时候需要参数
				if (guidecfg.finish == CourseType.GUILD_SPEAKING_COURSE ||
					guidecfg.finish == CourseType.WORLD_SPEAKING_COURSE ||
					guidecfg.finish == CourseType.DAZHAO_COURSE ||
					guidecfg.finish == CourseType.WABAO_COURSE ||
					guidecfg.finish == CourseType.SKILL_LEVELUP_COURSE ||
					guidecfg.finish == CourseType.XIULIAN_COURSE ||
					guidecfg.finish == CourseType.ZENG_SONG ||
					guidecfg.finish == CourseType.ZHI_HUI_SI_LIAN_CHU ||
					guidecfg.finish == CourseType.PUT_ON_EQUIP ||
					guidecfg.finish == CourseType.WASH_PET ||
					guidecfg.finish == CourseType.LEARN_PET ||
					guidecfg.finish == CourseType.XIANG_QIAN_EQUIP ||
					guidecfg.finish == CourseType.HE_PET ||
					guidecfg.finish == CourseType.XI_LIAN_PET ||
					guidecfg.finish == CourseType.PET_SCORE ||
					guidecfg.finish == CourseType.QI_ZUO_QI ||
					guidecfg.finish == CourseType.REN_ZHENG_PET ||
					guidecfg.finish == CourseType.HE_PET_JI_NENG ||
					guidecfg.finish == CourseType.YUAN_ZHU_ZHAN_DOU ||
					guidecfg.finish == CourseType.GONG_HUI_JUAN_ZHU ||
					guidecfg.finish == CourseType.HAVE_TE_JE_EQUIP ||
					guidecfg.finish == CourseType.XIANG_QIAN_EQUIP_LEVEL ||
					guidecfg.finish == CourseType.SHENG_HUO_JINENG ||
					guidecfg.finish == CourseType.GET_SHENGWANG
					) {
					
					CourseKey key = new CourseKey(guidecfg.finish, 0, 0);
					CourseVal val = new CourseVal(guidecfg);
					courseCfg.put(key, val);
				}
				else {
					int param1 = 0;
					int param2 = 0;
					
					if (guidecfg.ref1 == null)
						continue;
					
					String [] paramStr1 = guidecfg.ref1.split(",");
					for (String pm1 : paramStr1) {
						param1 = Integer.parseInt(pm1);
						//不需要第二个参数,只在完成时判断
						if (guidecfg.finish != CourseType.XIANGQIAN_COURSE
								&& guidecfg.finish != CourseType.ACTIVE_COURSE
								&& guidecfg.finish != CourseType.USE_YAOPIN_COURSE
								&& guidecfg.finish != CourseType.HAVE_PET_COURSE
								&& guidecfg.finish != CourseType.OPEN_PAIHANGBANG_COURSE
								&& guidecfg.finish != CourseType.QIANG_HONG_BAO
								&& guidecfg.finish != CourseType.HAO_YOU_DU
								&& guidecfg.finish != CourseType.XIANG_QIAN_GEM
								&& guidecfg.finish != CourseType.FRIEND_COURSE
								&& guidecfg.finish != CourseType.NB_SKILL_COURSE) {
							/*if (guidecfg.ref2 != null) {
								String [] paramStr2 = guidecfg.ref2.split(",");
								for (String pm2 : paramStr2) {
									param2 = Integer.parseInt(pm2);
								}
							}*/
							
							if (guidecfg.ref2 != null && !guidecfg.ref2.equals("")) {
								param2 = Integer.parseInt(guidecfg.ref2);
							}
						}
						
						CourseKey key = new CourseKey(guidecfg.finish, param1, param2);
						CourseVal val = new CourseVal(guidecfg);
						courseCfg.put(key, val);
					}
				}
			}
		}
		
	}

	public static CourseColumn getCourse(final long roleid, final boolean readonly) {
		return new CourseColumn(roleid, readonly);
	}
	
	/**
	 * 更新一个历程,次数累加类型的历程需要调用此接口
	 * @param roleid
	 * @param coursetype
	 * @param count
	 */
	public static boolean achieveUpdate(final long roleid, final int coursetype) {
		return achieveCourse(roleid, coursetype, 0, 0);
	}
	
	/**
	 * 更新一个历程,次数累加类型的历程需要调用此接口,或者直接判断一个数量是否满足该成就,大于即为满足
	 * @param roleid
	 * @param coursetype
	 * @param count
	 */
	public static boolean achieveUpdate(final long roleid, final int coursetype, final int para1, final int para2, final int count) {
		PUpdateAchieveCourse archieve = new PUpdateAchieveCourse(roleid, coursetype, count, para1, para2);
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(archieve);
		}
		else {
			archieve.submit();
		}
		return true;
	}
	
	/**
	 * 检查一个历程是否完成,数量不是单个加的
	 * @param roleid
	 * @param coursetype
	 * @param count
	 */
	public static boolean checkAchieveCourse(final long roleid, final int coursetype, final int count) {
		PCheckAchieveCourse archieve = new PCheckAchieveCourse(roleid, coursetype, count);
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(archieve);
		}
		else {
			archieve.submit();
		}
		return true;
	}
	
	/**
	 * 检查一个历程是否完成,数量不是单个加的
	 * @param roleid
	 * @param coursetype
	 * @param count
	 */
	public static boolean checkAchieveCourse(final long roleid, final int coursetype, final int para1, final int para2, final int count) {
		PCheckAchieveCourse archieve = new PCheckAchieveCourse(roleid, coursetype, count, para1, para2);
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(archieve);
		}
		else {
			archieve.submit();
		}
		return true;
	}

	/**
	 * 完成一个操作后,触发是否完成一个历程,需要所有的参数都在初始化时进行过解析
	 * @param roleid
	 * @param coursetype
	 * @param para1 没有时填0
	 * @param para2 没有时填0
	 */
	public static boolean achieveCourse(final long roleid, final int coursetype, final int para1, final int para2) {
		PAchieveCourse archieve = new PAchieveCourse(roleid, coursetype, para1, para2);
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(archieve);
		}
		else {
			archieve.submit();
		}
		
		return true;
	}
	
	/**
	 * 根据id获取历程配置
	 * @param id
	 * @return
	 */
	public CourseVal getCourseValue(final int id) {
		if (courseCfgById.containsKey(id))
			return courseCfgById.get(id);
		
		return null;
	}
	
	/**
	 * 返回一个coursekey
	 * @param coursetype
	 * @param param1
	 * @param param2
	 * @return
	 */
	public CourseKey getCourseKey(final int coursetype, int param1, int param2) {
		return new CourseKey(coursetype, param1, param2);
	}
	
	/**
	 * 检查某个历程是否会完成
	 * @param id
	 * @param count
	 * @return
	 */
	public boolean checkParam(final int id, final int coursetype, final int count) {
		CourseVal courseVal = getCourseValue(id);
		if (courseVal == null) {
			logger.error("checkParam is error " + id);
			return false;
		}
		//根据历程类型不同,验证参数0或者参数1
		switch (coursetype) {
			case CourseType.GUILD_SPEAKING_COURSE:
			case CourseType.SKILL_LEVELUP_COURSE:
			case CourseType.WORLD_SPEAKING_COURSE:
			case CourseType.DAZHAO_COURSE:
			case CourseType.OPEN_PAIHANGBANG_COURSE:
			case CourseType.XIULIAN_COURSE:
			{
				if (courseVal.param1.size() == 0)
					return false;
				
				List<Integer> param = courseVal.param1;
				if (count >= param.get(0))
					return true;
				return false;
			}
			case CourseType.NB_SKILL_COURSE:
			case CourseType.XIANGQIAN_COURSE:
			case CourseType.ACTIVE_COURSE:
			case CourseType.USE_YAOPIN_COURSE:
			case CourseType.EQUIP_ADDGEM_COURSE:
			case CourseType.HAVE_PET_COURSE:
			case CourseType.QIANG_HONG_BAO:
			case CourseType.HAO_YOU_DU:
			case CourseType.XIANG_QIAN_GEM:
			case CourseType.FRIEND_COURSE:
			{
				if (courseVal.param2.size() == 0)
					return false;
				List<Integer> param = courseVal.param2;
				if (count >= param.get(0))
					return true;
				return false;
			}
			default: {
				if (courseVal.param1.size() == 0)
					return false;
				List<Integer> param = courseVal.param1;
				if (count >= param.get(0))
					return true;
				return false;
			}
		}
	}
	
	/**
	 * 返回角色所有历程信息
	 * @param roleid
	 */
	public void getCourseInfo(final long roleid) {
		Procedure pGetCourseInfo = new Procedure() {
			@Override
			protected boolean process() throws Exception {
				CourseColumn courseCol = getCourse(roleid, true);
				
				SGetArchiveInfo archiveInfo = new SGetArchiveInfo();
				for (Integer archiveid : courseList) {
					ArchiveInfo info = new ArchiveInfo();
					info.archiveid = archiveid;
					info.state = courseCol.getAchiveState(archiveid);
					archiveInfo.archiveinfos.add(info);
				}
				
				Procedure.psendWhileCommit(roleid, archiveInfo);
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			pGetCourseInfo.call();
		}
		else {
			pGetCourseInfo.submit();
		}
	}
	
	/**
	 * 更新单个历程信息
	 * @param roleid
	 * @param archiveid
	 */
	public void updateArchiveInfo(final long roleid, final int archiveid) {
		Procedure pGetCourseInfo = new Procedure() {
			@Override
			protected boolean process() throws Exception {
				CourseColumn courseCol = getCourse(roleid, true);
				
				SGetArchiveInfo archiveInfo = new SGetArchiveInfo();
				ArchiveInfo info = new ArchiveInfo();
				info.archiveid = archiveid;
				info.state = courseCol.getAchiveState(archiveid);
				archiveInfo.archiveinfos.add(info);
				
				Procedure.psendWhileCommit(roleid, archiveInfo);
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			pGetCourseInfo.call();
		}
		else {
			pGetCourseInfo.submit();
		}
	}
	
	/**
	 * 领取历程奖励
	 * @param roleid
	 * @param archiveid
	 */
	public void getArchiveAward(final long roleid, final int archiveid) {
		Procedure pGetCourseInfo = new Procedure() {
			@Override
			protected boolean process() throws Exception {
				CourseColumn courseCol = getCourse(roleid, false);
				if (courseCol.getAchiveState(archiveid) != 1)
					return false;
				
				CourseVal courseVal = getCourseValue(archiveid);
				if (courseVal == null)
					return false;
				
				if (courseVal.getAwardId() > 0) {
					Map<Integer, RewardObjs> awardResult = fire.pb.activity.award.RewardMgr
							.getInstance().distributeAllAward(roleid, courseVal.getAwardId(), null,
									fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chengjiu, courseVal.getAwardId(),
									PAddExpProc.OTHER_QUEST, "指引奖励");
					if (awardResult != null) {
						//设置已经领取奖励
						courseCol.setAchiveState(archiveid, 2);
						SGetArchiveInfo archiveInfo = new SGetArchiveInfo();
						ArchiveInfo info = new ArchiveInfo();
						info.archiveid = archiveid;
						info.state = courseCol.getAchiveState(archiveid);
						archiveInfo.archiveinfos.add(info);
						
						List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
						long money = 0;
						for (Map.Entry<Integer, RewardObjs> awardRet : awardResult.entrySet()) {
							if (awardRet.getKey() == RewardMgr.MONEY_AWARD) {
								money = awardRet.getValue().getValue();
							}
							if (awardRet.getKey() == RewardMgr.FIRSTC_AWARD) {
								List<Items> awardItems = awardRet.getValue().getItems();
								for (Items itemsub : awardItems) {
									fire.log.beans.ItemBaseBean itemBean = new ItemBaseBean(itemsub.getItemID(), itemsub.getItemNum());
									items.add(itemBean);
								}
							}
							
							if (awardRet.getKey() == RewardMgr.SECONDC_AWARD) {
								List<Items> awardItems = awardRet.getValue().getItems();
								for (Items itemsub : awardItems) {
									fire.log.beans.ItemBaseBean itemBean = new ItemBaseBean(itemsub.getItemID(), itemsub.getItemNum());
									items.add(itemBean);
								}
							}
						}
						logger.info(new StringBuffer().append("archive award:").append(roleid).append(":").append(archiveid).append(":").append(courseVal.getAwardId()));
						fire.log.beans.AchievementBean archieveBean = new AchievementBean(archiveid, items);
						fire.log.YYLogger.achivementLog(roleid, money, archieveBean);
						Procedure.psendWhileCommit(roleid, archiveInfo);
					}
				}
				
				if (courseVal.getItemId() > 0) {
					int realAdd = BagUtil.addItem(roleid, courseVal.getItemId(),
							courseVal.getItemNum(), "archive",
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chengjiu, courseVal.getItemId());
					if (realAdd == courseVal.getItemNum()) {
						MessageUtil.psendAddItemWhileCommit(roleid,	courseVal.getItemId(), realAdd);
						//设置已经领取奖励
						courseCol.setAchiveState(archiveid, 2);
						
						SGetArchiveInfo archiveInfo = new SGetArchiveInfo();
						ArchiveInfo info = new ArchiveInfo();
						info.archiveid = archiveid;
						info.state = courseCol.getAchiveState(archiveid);
						archiveInfo.archiveinfos.add(info);
						
						Procedure.psendWhileCommit(roleid, archiveInfo);
						
						List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
						fire.log.beans.ItemBaseBean itemBean = new ItemBaseBean(courseVal.getItemId(), realAdd);
						items.add(itemBean);
						
						fire.log.beans.AchievementBean archieveBean = new AchievementBean(archiveid, items);
						fire.log.YYLogger.achivementLog(roleid, 0, archieveBean);
						Procedure.psendWhileCommit(roleid, archiveInfo);
						
						logger.info(new StringBuffer().append("archive award:").append(roleid).append(":").append(archiveid).append(":").append(courseVal.getItemId()));
					} 
					else {
						return false;
					}
				}
				
				if (courseVal.getPetid1() > 0) {
					//判断宠物栏是否已经满了
					PetColumn petColumn = new PetColumn(roleid, PetColumnTypes.PET,	false);
					if (petColumn.size() >= petColumn.getCapacity()) {
						MessageMgr.sendMsgNotify(roleid, 144906, null);
						return false;
					}
					
					int ret = petColumn.addPetByID(courseVal.getPetid1(), true, PetColumn.ADD_REASON_ACHIEVE);
					if (ret < 0) {
						return false;
					}
					
					//设置已经领取奖励
					courseCol.setAchiveState(archiveid, 2);
					
					SGetArchiveInfo archiveInfo = new SGetArchiveInfo();
					ArchiveInfo info = new ArchiveInfo();
					info.archiveid = archiveid;
					info.state = courseCol.getAchiveState(archiveid);
					archiveInfo.archiveinfos.add(info);
					
					Procedure.psendWhileCommit(roleid, archiveInfo);
					
					PetAttr confs = ConfigManager.getInstance().getConf(PetAttr.class).get(courseVal.getPetid1());
					List<String> para = new ArrayList<String>();
					para.add(confs.getName());
					MessageMgr.sendMsgNotify(roleid, 160142, para);
					
					logger.info(new StringBuffer().append("archive award:").append(roleid).append(":").append(archiveid).append(":").append(courseVal.getPetid1()));
				}
				
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			pGetCourseInfo.call();
		}
		else {
			pGetCourseInfo.submit();
		}
	}

}

