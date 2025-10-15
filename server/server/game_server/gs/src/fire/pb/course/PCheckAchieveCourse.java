package fire.pb.course;

import fire.pb.course.CourseManager.CourseKey;
import fire.pb.course.CourseManager.CourseVal;
import mkdb.Procedure;

public class PCheckAchieveCourse extends Procedure {

	private final long roleid; 
	private final int coursetype;
	private final int count;
	
	private final int para1;
	private final int para2;
	
	public PCheckAchieveCourse(final long roleid, final int coursetype, final int count) {
		this.roleid = roleid;
		this.coursetype = coursetype;
		this.count = count;
		this.para1 = 0;
		this.para2 = 0;
	}
	
	public PCheckAchieveCourse(final long roleid, final int coursetype, final int count,
			final int para1, final int para2) {
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
		
		CourseManager.logger.info(roleid + " update " + archiveid);
		if (courseCol.checkAchive(archiveid, coursetype, count)) {
			courseCol.achieved(archiveid);
			CourseManager.getInstance().updateArchiveInfo(roleid, archiveid);
			return true;
		}
	
		return true;
	}
	
	
}
