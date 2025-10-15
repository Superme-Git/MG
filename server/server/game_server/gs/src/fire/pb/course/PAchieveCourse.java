package fire.pb.course;

import fire.pb.course.CourseManager.CourseKey;
import fire.pb.course.CourseManager.CourseVal;
import mkdb.Procedure;

public class PAchieveCourse extends Procedure {

	private final long roleid; 
	private final int coursetype;
	private final int para1;
	private final int para2;
	
	public PAchieveCourse(final long roleid, final int coursetype, final int para1, final int para2) {
		this.roleid = roleid;
		this.coursetype = coursetype;
		this.para1 = para1;
		this.para2 = para2;
	}

	@Override
	protected boolean process() throws Exception {
		CourseColumn courseCol = CourseManager.getCourse(roleid, false);
		CourseKey courseKey = new CourseKey(coursetype, para1, para2);
		CourseVal courseVal = CourseManager.courseCfg.get(courseKey);
		if (courseVal == null) {
			return false;
		}
		
		final int archiveid = courseVal.getActiveId();
		if (courseCol.isAchieved(archiveid)) {
			return true;
		}
		
		//分别对不同的历程,进行不同的逻辑判断
		//需要对各个参数进行判断
		switch (coursetype) {
			case CourseType.TASK_COURSE:
			case CourseType.PET_COURSE:
			case CourseType.EQUIP_SPECIAL_COURSE:
			case CourseType.HUOBAN_COURSE:
			case CourseType.HAVE_PET_COURSE:
			case CourseType.EQUIP_ADDGEM_COURSE:
			case CourseType.OPEN_PAIHANGBANG_COURSE:
			{
				courseCol.achieved(archiveid);
				CourseManager.logger.info(roleid + " archive " + archiveid);
				CourseManager.getInstance().updateArchiveInfo(roleid, archiveid);
				return true;
			}
			case CourseType.FRIEND_COURSE:
			case CourseType.ACTIVE_COURSE:
			case CourseType.GUILD_SPEAKING_COURSE:
			case CourseType.WORLD_SPEAKING_COURSE:
			case CourseType.DAZHAO_COURSE:
			case CourseType.USE_YAOPIN_COURSE:
			case CourseType.WABAO_COURSE:
			case CourseType.XIANGQIAN_COURSE:
			case CourseType.PLAY_PK_COURSE://完成切磋次数
			case CourseType.YUAN_ZHU_ZHAN_DOU:
			case CourseType.GONG_HUI_JUAN_ZHU:
			{
				CourseManager.logger.info(roleid + " update " + archiveid);
				courseCol.achiveUpdate(archiveid, coursetype);
				return true;
			}
			case CourseType.NB_SKILL_COURSE:
			{
				CourseManager.logger.info(roleid + " update " + archiveid);
				if (courseCol.checkAchive(archiveid, coursetype, para2)) {
					courseCol.achieved(archiveid);
					CourseManager.getInstance().updateArchiveInfo(roleid, archiveid);
					return true;
				}
				break;
			}
			default: {
				courseCol.achieved(archiveid);
				CourseManager.logger.info(roleid + " archive " + archiveid);
				CourseManager.getInstance().updateArchiveInfo(roleid, archiveid);
				return true;
			}
		}
		return true;
	}
}
