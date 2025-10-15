package fire.pb.course;

import fire.pb.course.CourseManager.CourseKey;
import fire.pb.course.CourseManager.CourseVal;
import mkdb.Procedure;

public class PUpdateAchieveCourse extends Procedure {

	private final long roleid; 
	private final int coursetype;
	private final int count;
	
	private final int para1;
	private final int para2;
	
	public PUpdateAchieveCourse(final long roleid, final int coursetype, final int count, final int para1, final int para2) {
		this.roleid = roleid;
		this.coursetype = coursetype;
		this.count = count;
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
		case CourseType.GET_SHENGWANG:
		case CourseType.NB_SKILL_COURSE:{
			CourseManager.logger.info(roleid + " update:" + archiveid + " count:" + count);
			courseCol.achiveUpdate(archiveid, coursetype, count);
			return true;
		}
		default:
			break;
		}
		
		return true;
	}
	
	
}
