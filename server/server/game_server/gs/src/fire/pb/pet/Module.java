package fire.pb.pet;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

public class Module implements ModuleInterface {

	static public final Logger logger = Logger.getLogger("PET");

	private PetManager petManager = null;

	private static Module module = null;

	public PetManager getPetManager() {
		return petManager;
	}

	public Module() {
		module = this;
	}

	public static Module getInstance() {
		return module;
	}

	@Override
	public void exit() {
	}

	@Override
	public void init() throws Exception {
		logger.info("Pet module init start");
		petManager = PetManager.getInstance();
		cleanPetRecycleBin();
		logger.info("Pet module init end");
	}

	/**
	 * 获得携带等级
	 */
	public int getPetTakeLevel(int petId) {
		PetAttr attr = petManager.getAttr(petId);
		if (attr != null)
			return attr.takelevel;
		return 0;
	}

	/**
	 * 通过颜色获得RGB值
	 */
	public String getPetColorRGB(int colour) {
		// return "";
		return "fffcfbfb"; // 弃用颜色 [2/25/2016 XGM]
	}

	public static String PET_AUTOKEY_NAME = "PET_UID";

	public final static int OUTTIME_OF_PET = 60; // 天

	public static void cleanPetRecycleBin() {

		final Set<Long> toRemove = new HashSet<Long>();
		final Calendar date = Calendar.getInstance();
		date.add(Calendar.HOUR, -24 * OUTTIME_OF_PET);

		xtable.Petrecyclebin.getTable().walk(
				new mkdb.TTable.IWalk<Long, xbean.DiscardPet>() {
					@Override
					public boolean onRecord(final Long key, final xbean.DiscardPet value) {
						if (value.getDeletedate() < date.getTimeInMillis()) {
							toRemove.add(key);
						}
						return true;
					}
				});

		logger.info("宠物回收站删除 " + toRemove.size() + " 个过期宠物");

		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				for (Long key : toRemove) {
//					long roleId = xtable.Petrecyclebin.selectRoleid(key);
					xtable.Petrecyclebin.remove(key);

//					// 删除宠物可回收key
//					pexecuteWhileCommit(new mkdb.Procedure() {
//						@Override
//						protected boolean process() {
//							xbean.Petrecoverlist petRecoverList = xtable.Petrecover.get(roleId);
//							if (petRecoverList != null) {
//								petRecoverList.getUniqids().remove(key);
//							}
//							return true;
//						}
//					});
				}
				return true;
			}
		};
		if (mkdb.Transaction.current() == null)
			proc.submit();
		else
			mkdb.Procedure.pexecute(proc);
	}

	@Override
	public ReloadResult reload() throws Exception {
		return new ReloadResult(false, "module" + this.getClass().getName() + "not support reload");
	}
}
