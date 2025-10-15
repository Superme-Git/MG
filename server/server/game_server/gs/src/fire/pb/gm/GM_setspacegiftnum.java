package fire.pb.gm;

/**
 * 设置说不得大师空间礼物数量
 *
 * @author yangtao
 * @dateTime 2016年6月22日 下午3:42:12
 * @version 1.0
 */
public class GM_setspacegiftnum extends GMCommand {

	@Override
	boolean exec(final String[] args) {
		final int giftNum = Integer.valueOf(args[0]);
		if ((giftNum <= 0) || (giftNum >= Integer.MAX_VALUE)) {
			this.sendToGM("参数错误 ,正确格式:" + this.usage());
			return false;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() {
				final xbean.RoleSpace rs = xtable.Rolespaces.get(Long.MAX_VALUE);
				if (rs == null) {
					GM_setspacegiftnum.this.sendToGM("沒有数据:" + GM_setspacegiftnum.this.usage());
					return false;
				} else {
					rs.setGift(giftNum);
				}
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "设置空间大师礼物数量//setspacegiftnum $num$";
	}
}
