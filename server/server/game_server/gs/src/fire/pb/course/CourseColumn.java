package fire.pb.course;

public class CourseColumn {
	private final long roleid;
	private final xbean.lichengxinxi lichengInfo;
	
	CourseColumn(final long roleid, final boolean readonly) {
		this.roleid = roleid;
		if (readonly) {
			xbean.lichengxinxi licheng = xtable.Courses.select(roleid);
			if (licheng == null)
				lichengInfo = xbean.Pod.newlichengxinxi();
			else
				lichengInfo = licheng;
		}
		else {
			xbean.lichengxinxi course = xtable.Courses.get(roleid);
			if (course == null) {
				lichengInfo = xbean.Pod.newlichengxinxi();
				xtable.Courses.insert(roleid, lichengInfo);
			}
			else
				lichengInfo = course;
		}
	}
	
	public boolean isAchieved(final int id) {
		xbean.Course course = lichengInfo.getLicheng().get(id);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(id, course);
		}
		
		return course.getState() > 0;
	}
	
	/**
	 * 设置一个历程为完成状态
	 * @param id
	 */
	public void achieved(final int id) {
		xbean.Course course = lichengInfo.getLicheng().get(id);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(id, course);
		}
		course.setState(1);
		return;
	}
	
	/**
	 * 设置一个历程为未完成状态
	 * @param id
	 */
	public void unachieved(final int id) {
		xbean.Course course = lichengInfo.getLicheng().get(id);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(id, course);
		}
		course.setState(0);
		return;
	}
	
	/**
	 * 更新一个历程,直到其为完成状态
	 * @param achiveid
	 */
	public void achiveUpdate(final int achiveid, final int coursetype) {
		xbean.Course course = lichengInfo.getLicheng().get(achiveid);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(achiveid, course);
		}
		
		if (course.getState() > 0)
			return;
		
		int count = course.getCount() + 1;
		if (CourseManager.getInstance().checkParam(achiveid, coursetype, count)) {
			course.setState(1);
			CourseManager.getInstance().updateArchiveInfo(roleid, achiveid);
		}
		course.setCount(count);
	}
	
	/**
	 * 更新一个历程,直到其为完成状态(每次更新一个addcount)
	 * @param achiveid
	 */
	public void achiveUpdate(final int achiveid, final int coursetype, final int addcount) {
		xbean.Course course = lichengInfo.getLicheng().get(achiveid);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(achiveid, course);
		}
		
		if (course.getState() > 0)
			return;
		
		int count = course.getCount() + addcount;
		if (CourseManager.getInstance().checkParam(achiveid, coursetype, count)) {
			course.setState(1);
			CourseManager.getInstance().updateArchiveInfo(roleid, achiveid);
		}
		course.setCount(count);
	}
	
	/**
	 * 更新一个历程,直到其为完成状态
	 * @param achiveid
	 */
	public boolean checkAchive(final int archiveid, final int coursetype, final int count) {
		xbean.Course course = lichengInfo.getLicheng().get(archiveid);
		if (course == null) {
			course = xbean.Pod.newCourse();
			lichengInfo.getLicheng().put(archiveid, course);
		}
		
		if (course.getState() > 0)
			return true;
		
		if (CourseManager.getInstance().checkParam(archiveid, coursetype, count)) {
			course.setState(1);
			CourseManager.getInstance().updateArchiveInfo(roleid, archiveid);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取历程状态
	 * @param achiveid
	 * @return
	 */
	public int getAchiveState(final int achiveid) {
		xbean.Course course = lichengInfo.getLicheng().get(achiveid);
		if (course == null)
			return 0;
		
		return course.getState();
	}
	
	/**
	 * 设置历程状态
	 * @param achiveid
	 * @return
	 */
	public void setAchiveState(final int achiveid, final int state) {
		xbean.Course course = lichengInfo.getLicheng().get(achiveid);
		if (course == null)
			return;
		
		course.setState(state);
	}

}
