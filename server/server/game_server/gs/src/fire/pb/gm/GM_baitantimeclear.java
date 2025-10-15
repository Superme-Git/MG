package fire.pb.gm;

import fire.pb.shop.srv.market.MarketStore;
public class GM_baitantimeclear extends GMCommand {
	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				MarketStore.overShowTime(0);
				sendToGM("去除摆摊道具公示期成功!");
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//baitantimeclear 去除摆摊道具公示期";
	}
}
