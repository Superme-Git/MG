package fire.pb.gm;

import fire.pb.npc.ImpExamAssistType;

public class GM_addieassist extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int type;
		final int num;
		final long roleid;
		type = Integer.parseInt(args[0]);
		if (type < 1 || type > 2) {
			return false;
		}

		num = Integer.parseInt(args[1]);
		roleid = getGmroleid();

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.ImpExamRecord record = xtable.Role2impexam.get(roleid);
				if(record == null){
					record =  xbean.Pod.newImpExamRecord();
					xtable.Role2impexam.insert(roleid, record);
				}
				
				if(ImpExamAssistType.DELWRONG == type){
					record.setDelwrongval(num);
				}else if(ImpExamAssistType.CHORIGHT == type){
					record.setChorightval(num);
				}
				
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addieassist type num( type == 1 or 2 )";
	}

}
