package fire.log.beans;

public class AddZhenfaExpBean {
	private int id;// 光环ID
	private int level;// 光环当前等级
	private int addexp;// 经验变化数
	private int bfexp;// 变化前经验
	private int afterexp;// 变化后经验

	public AddZhenfaExpBean(int id, int level, int addexp, int bfexp, int afterexp) {
		this.id = id;
		this.level = level;
		this.addexp = addexp;
		this.bfexp = bfexp;
		this.afterexp = afterexp;
	}

	public int getCurexp() {
		return bfexp;
	}

	public void setCurexp(int curexp) {
		this.bfexp = curexp;
	}

	public int getAfterexp() {
		return afterexp;
	}

	public void setAfterexp(int afterexp) {
		this.afterexp = afterexp;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAddexp() {
		return addexp;
	}

	public void setAddexp(int addexp) {
		this.addexp = addexp;
	}

}
