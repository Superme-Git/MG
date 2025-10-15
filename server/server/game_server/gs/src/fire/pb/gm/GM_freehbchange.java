package fire.pb.gm;

import java.util.Calendar;

import fire.pb.huoban.PRefreshHeroWeekFree;


public class GM_freehbchange extends GMCommand {
	@Override
	boolean exec(String[] args) {
		/*final int addWeek;
		if(args.length == 1){
			addWeek = Integer.parseInt(args[0]);
		}else{
			addWeek = 1;
		}
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				Calendar cal = Calendar.getInstance();
				int weekInYear = cal.get(Calendar.WEEK_OF_YEAR) + addWeek;
				fire.pb.huoban.Module.getInstance().SetHeroWeekFree();
				new PRefreshHeroWeekFree(getGmroleid(), weekInYear).call();
				return true;
			}
		}.submit();*/
		sendToGM("该功能不再提供,敬请告知");
		return true;
	}

	@Override
	String usage() {
		return "//freehbchange 2(若不填,默认为1,即下周的)";
	}

}
