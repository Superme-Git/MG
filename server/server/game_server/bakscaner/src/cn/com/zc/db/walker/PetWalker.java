
package cn.com.zc.db.walker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xbean.PetInfo;
import mkdb.util.DatabaseMetaData.Table;
import cn.com.zc.db.ScoreSnapShotHelper;
import cn.com.zc.db.bean.ScoreSnapShotBean;

/**
 * 
 */
public class PetWalker extends Walker {
	/**
	 * @param tableMetaData
	 */
	public PetWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	/**
	 * 宠物评分快照
	 
	 */
	private void petScoreSnapShot(xbean.Pets petInfos, ScoreSnapShotBean bean) {
		List<xbean.PetInfo> resultInfos=new ArrayList<PetInfo>(petInfos.getPetmap().size());
		for (xbean.PetInfo petInfo : petInfos.getPetmap().values()) {
			resultInfos.add(petInfo);
		}
		
		//排序，降序
		Collections.sort(resultInfos,new Comparator<xbean.PetInfo>() {
			@Override
			public int compare(PetInfo arg0, PetInfo arg1) {
				return arg1.getPetscore()-arg0.getPetscore();
			}
		});
		
		for (xbean.PetInfo petInfo : resultInfos) {
			bean.addPet(petInfo.getUniqid(), petInfo.getId(),
					petInfo.getName(), petInfo.getPetscore());
		}
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		Long roleId = (Long) key;
		ScoreSnapShotBean bean = ScoreSnapShotHelper
				.getScoreSnapShotBean(roleId);
		// 可能某些玩家不需要记录
		if (bean == null) {
			return true;
		}
		petScoreSnapShot((xbean.Pets) value, bean);

		return false;
	}

}
