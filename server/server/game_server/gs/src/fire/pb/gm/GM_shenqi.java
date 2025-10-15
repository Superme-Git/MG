package fire.pb.gm;

/**
 * 添加神器
 */

import fire.pb.util.BagUtil;

public class GM_shenqi extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final long roleid = getGmroleid();
				
				boolean isSucc = false;
				Integer [] itemids = {41600, 41601, 41602, 41603, 41604, 41605};
				for (Integer itemid : itemids) {
					isSucc = BagUtil.addItem(roleid, itemid, 1, "GM添加物品", fire.log.enums.YYLoggerTuJingEnum.GM, itemid) > 0;
				}
				
				if (isSucc) {
					String info = "添加神器到背包成功";
					sendToGM(info);
				} else
					sendToGM("添加神器到背包失败");
				return isSucc;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}

}
