package fire.pb.scene;

import java.util.Set;

/**
 * 区域信息、摆摊、切磋、投放
 */
public class AreaInfo {
	private boolean isBaitanArea; // 摆摊区域
	private boolean isQiecuoArea; // 切磋区域
	private boolean isToufangArea; // 投放物品npc区域
	private boolean isToufang2Area; // 投放物品npc区域,包含一级轻功区域
	private boolean isKite; // 放风筝区域
	private boolean isTnsh; // 天女散花区域
	final private Set<Short> areaTypes; // 区域类型,10 ~ 16

	public AreaInfo(boolean isBaitanArea, boolean isQiecuoArea, boolean isToufangArea, boolean isToufang2Area,
			boolean isKite, boolean isTnsh, Set<Short> areatypes) {

		this.isBaitanArea = isBaitanArea;
		this.isQiecuoArea = isQiecuoArea;
		this.isToufangArea = isToufangArea;
		this.isToufang2Area = isToufang2Area;
		this.isKite = isKite;
		this.isTnsh = isTnsh;
		this.areaTypes = areatypes;
	}

	public boolean isBaitanArea() {
		return isBaitanArea;
	}

	public boolean isQiecuoArea() {

		return isQiecuoArea;
	}

	public boolean isToufangArea() {
		return isToufangArea;
	}

	public boolean isToufang2Area() {
		return isToufang2Area;
	}

	public boolean isKite() {
		return isKite;
	}

	public boolean isTnsh() {
		return isTnsh;
	}

	public Set<Short> getAreaTypes() {
		return areaTypes;
	}

}
