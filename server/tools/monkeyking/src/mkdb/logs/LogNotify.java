package mkdb.logs;

import java.util.List;
import java.util.ArrayList;

/**
 * 日志向上报告的时候辅助类。
 * 
 * @author cn
 *
 */
public final class LogNotify {
	private List<mkdb.LogKey> path = new ArrayList<mkdb.LogKey>();
	private Note note;
	private AddRemoveInfo addRemoveInfo;

	public LogNotify(mkdb.LogKey logkey, Note note) {
		path.add(logkey);
		this.note = note;
	}
	
	public LogNotify(AddRemoveInfo addRemoveInfo) {
		this.addRemoveInfo = addRemoveInfo;
	}

	public boolean isLast() {
		return path.isEmpty();
	}

	public mkdb.LogKey pop() {
		return path.remove(path.size() - 1);
	}

	public LogNotify push(mkdb.LogKey logkey) {
		path.add(logkey);
		return this;
	}
	
	public boolean isAddRemove() {
		return (null != addRemoveInfo);
	}
	
	public AddRemoveInfo getAddRemoveInfo() {
		return addRemoveInfo;
	}

	public Note getNote() {
		return note;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = path.size() - 1; i >= 0; --i)
			sb.append('.').append(path.get(i).getVarname());
		sb.append('=').append(note);
		return sb.toString();
	}
}
