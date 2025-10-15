package fire.pb.gm;

public class GM_offlinetime extends GMCommand {
	public static int offLineMin = 0;

	public static long getAndResetGMOffLineMillis() {
		long retValue = offLineMin * 1000 * 60;
		resetOffLineMin();
		return retValue;
	}

	public static void resetOffLineMin() {
		offLineMin = 0;
	}

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			sendToGM(usage());
			return true;
		}
		Integer val = Integer.parseInt(args[0]);
		if(val <= 0){
			sendToGM(usage());
			return true;
		}
		offLineMin = val;
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				final xbean.Properties pro = xtable.Properties.get(getGmroleid());
				if (null == pro)
					return false;
				pro.setOnlinetime(System.currentTimeMillis() - offLineMin * 60 * 1000);// 设置登录时间
				return true;
			}
		}.submit();
		sendToGM("增加离线托管时间成功!");
		return true;
	}

	@Override
	String usage() {
		return "// offlinetime 15(min) 参数必须大于15才能取消托管状态";
	}

}
