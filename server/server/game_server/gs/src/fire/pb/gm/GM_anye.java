package fire.pb.gm;

import fire.pb.circletask.anye.RoleAnYeTask;

public class GM_anye extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length == 0) {
			this.sendToGM("参数错误." + usage());
			return false;
		}
		if (args[0].equals("random")) {
			long roleid = getGmroleid();
			randomAnYeTask(roleid);
			return true;
		}
		if (args[0].equals("clear")) {
			long roleid = getGmroleid();
			clearAnYeTask(roleid);
			return true;
		}
		if (args[0].equals("commit")) {
			if(args.length < 2){
				this.sendToGM("参数错误." + usage());
				return false;
			}
			long roleid = getGmroleid();
			int pos = Integer.parseInt(args[1]);
			commitAnYeTask(roleid, pos);
		}
		if (args[0].equals("addtimes")) {
			if(args.length < 2){
				this.sendToGM("参数错误." + usage());
				return false;
			}
			long roleid = getGmroleid();
			int times = Integer.parseInt(args[1]);
			addAnYeTimes(roleid, times);
		}
		if (args[0].equals("legend")) {
			if(args.length < 2){
				this.sendToGM("参数错误." + usage());
				return false;
			}
			long roleid = getGmroleid();
			int pos = Integer.valueOf(args[1]);
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					RoleAnYeTask rayt = new RoleAnYeTask(roleid);
					rayt.startLegendAnYeTask(pos);
					//rayt.refreshAnYeTasks(-1);
					return true;
				}
			}.submit();
			return true;
		}
		if (args[0].equals("legendend")) {
			if(args.length < 1){
				this.sendToGM("参数错误." + usage());
				return false;
			}
			long roleid = getGmroleid();
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					RoleAnYeTask rayt = new RoleAnYeTask(roleid);
					rayt.endLegendAnYeTask();
					//rayt.refreshAnYeTasks(-1);
					return true;
				}
			}.submit();
			return true;
		}
		if (args[0].equals("abandon")) {
			long roleid = getGmroleid();
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					RoleAnYeTask rayt = new RoleAnYeTask(roleid);
					rayt.abandonTask();
					return true;
				}
			}.submit();
			return true;
		}
		return false;
	}

	@Override
	String usage() {
		return "anye [random/clear/commit] [pos]";
	}

	private void randomAnYeTask(final long roleid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.randomTasks();
				rayt.endLegendAnYeTask();
				rayt.refreshAnYeTasks(-1);
				return true;
			}
		}.submit();
	}
	
	private void clearAnYeTask(final long roleid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.clearTaskTimes();
				rayt.refreshAnYeTasks(-1);
				return true;
			}
		}.submit();
	}
	
	private void commitAnYeTask(final long roleid, final int pos) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.handleTaskOver(pos);
				return true;
			}
		}.submit();
	}
	
	private void addAnYeTimes(final long roleid, final int times) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.addTaskTimes(times);
				rayt.refreshAnYeTasks(-1);
				return true;
			}
		}.submit();
	}
}
