package fire.pb.gm;

import java.util.Map;
import java.util.Set;

import fire.pb.PropRole;
import fire.pb.team.SFormationsMap;

public class GM_openallform extends GMCommand {

	public final static int ALL_FORMATIONS = 1023;

	@Override
	boolean exec(String[] args) {
		try {
			if (args.length < 3) {
				sendToGM(usage());
				return true;
			}

			final Integer formationId = Integer.parseInt(args[0]);
			final Integer formLevel = Integer.parseInt(args[1]);
			final Integer activesTimes = Integer.parseInt(args[2]);

			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					final long roleid = getGmroleid();
					PropRole prole = new PropRole(roleid, false);
					Map<Integer, xbean.FormBean> formMap = prole.getFormtions();
					xbean.FormBean fmb = formMap.get(formationId);
					if (fmb == null) {
						fmb = xbean.Pod.newFormBean();
						formMap.put(formationId, fmb);
					}

					fmb.setActivetimes(activesTimes);
					fmb.setLevel(formLevel);

					SFormationsMap send = new SFormationsMap();
					Set<Integer> set = formMap.keySet();
					if (set.size() > 0) {
						for (int formId : set) {
							xbean.FormBean bean = formMap.get(formId);
							fire.pb.FormBean temp = new fire.pb.FormBean();
							temp.activetimes = bean.getActivetimes();
							temp.level = bean.getLevel();
							temp.exp = bean.getExp();
							send.formationmap.put(formId, temp);
						}
					}
					mkdb.Procedure.psendWhileCommit(roleid, send);
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	String usage() {

		return "openallform formId level activetimes";
	}
}
