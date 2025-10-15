package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

public class OpPetMixBean {
	private List<String> MixPet = new ArrayList<>();// 合宠-宝宝id（判断是否为珍品，是珍品增加珍品字段）
	private int MixedPetId; // 合成后宝宝id（判断是否为珍品，是珍品增加珍品字段
	private int IsTrea; // 是否珍品 0否，1是

	public OpPetMixBean(int pet1Id, int isPet1Trea, int pet2Id, int isPet2Trea, int mixedPetId, int isTrea) {
		MixPet.add("(" + pet1Id + ","+  isPet1Trea + ")");
		MixPet.add("(" + pet2Id + ","+  isPet2Trea + ")");
		MixedPetId = mixedPetId;
		IsTrea = isTrea;
	}

	public List<String> getMixPet() {
		return MixPet;
	}

	public int getMixedPetId() {
		return MixedPetId;
	}

	public int getIsTrea() {
		return IsTrea;
	}
}
