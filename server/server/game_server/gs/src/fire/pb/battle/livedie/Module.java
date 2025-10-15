package fire.pb.battle.livedie;

import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

public class Module implements ModuleInterface {

	public static final Logger logger = Logger.getLogger("BATTLE");

	@Override
	public void exit() {
		
	}
	
	@Override
	public void init() throws Exception {
		initList();
	}
	
	
	public void initList() {
		xtable.Ldvideoroleinfodeslisttab.getTable().browse(
			new mkdb.TTable.IWalk<Integer, xbean.LDVideoRoleInfoDesList>(){
				@Override
				public boolean onRecord(Integer k, xbean.LDVideoRoleInfoDesList v) {
					LiveDieMange.initdb(v.toData());
					return true;
				}
			}
		);
	}
	
	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
}
