package fire.pb.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.TimeUnit;

import fire.pb.game.ImperialExamProvinceRepo;
import fire.pb.game.ImperialExamStateRepo;
import fire.pb.game.ImperialExamVillageRepo;
import fire.pb.game.SImperialExamProvinceRepo;
import fire.pb.game.SImperialExamStateRepo;
import fire.pb.game.SImperialExamVillageRepo;
import fire.pb.main.ConfigManager;
import fire.pb.school.shouxi.ProfessionLeaderManager;
import fire.pb.school.shouxi.RefreshShouxiTask;
import fire.pb.shop.SNpcSale;
import fire.pb.util.Misc;

public class NpcManager
{
	private static NpcManager _instance = new NpcManager();
	private NpcManager(){};
	public static NpcManager getInstance()
	{
		return _instance;
	}
	
	private java.util.NavigableMap<Integer, ImperialExamVillageRepo> keju1RepoMap=new java.util.TreeMap<>();
	private java.util.NavigableMap<Integer, ImperialExamProvinceRepo> keju2RepoMap=new java.util.TreeMap<>();
	private java.util.NavigableMap<Integer, ImperialExamStateRepo> keju3RepoMap=new java.util.TreeMap<>();
	private java.util.NavigableMap<Integer, SNpcSale> npcSaleMap= new java.util.TreeMap<Integer, SNpcSale>();
	private java.util.NavigableMap<Integer, SNpcShare> npcShareMap= new java.util.TreeMap<Integer, SNpcShare>();
	private Map<Integer, List<NpcNameTable>> npcNameTableMap = new HashMap<Integer, List<NpcNameTable>>();
	private Map<Integer, List<NpcPreNameTable>> npcPreNameTableMap = new HashMap<Integer, List<NpcPreNameTable>>();
	void init() throws Exception
	{
		fire.pb.main.ConfigManager cm = ConfigManager.getInstance();
		npcSaleMap = (java.util.TreeMap<Integer, SNpcSale>) fire.pb.shop.Module.sNpcSaleMap;
		Module.logger.info("NPC买卖物品表加载完毕。一共加载NPC" + npcSaleMap.size() + "个");
		
		int nGroup = -1;
		Map<Integer, SNpcNameRandom> npcnameTable = cm.getConf(fire.pb.npc.SNpcNameRandom.class);
		for (SNpcNameRandom npc : npcnameTable.values()) {
			NpcNameTable npcname = new NpcNameTable(npc);
			if (nGroup != npc.group) {
				nGroup = npc.group;
				List<NpcNameTable> nameList = new ArrayList<NpcNameTable>();
				nameList.add(npcname);
				npcNameTableMap.put(nGroup, nameList);
			}
			else {
				List<NpcNameTable> nameList = npcNameTableMap.get(nGroup);
				nameList.add(npcname);
			}
		}
		
		nGroup = -1;
		Map<Integer, SNpcPreNameRandom> npcpreNameTable = cm.getConf(fire.pb.npc.SNpcPreNameRandom.class);
		for (SNpcPreNameRandom npc : npcpreNameTable.values()) {
			NpcPreNameTable npcPreName = new NpcPreNameTable(npc);
			if (nGroup != npc.group) {
				nGroup = npc.group;
				List<NpcPreNameTable> nameList = new ArrayList<NpcPreNameTable>();
				nameList.add(npcPreName);
				npcPreNameTableMap.put(nGroup, nameList);
			}
			else {
				List<NpcPreNameTable> nameList = npcPreNameTableMap.get(nGroup);
				nameList.add(npcPreName);
			}
		}
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { // 普通服
			keju1RepoMap.putAll(cm.getConf(fire.pb.game.SImperialExamVillageRepo.class));
			keju2RepoMap.putAll(cm.getConf(fire.pb.game.SImperialExamProvinceRepo.class));
			keju3RepoMap.putAll(cm.getConf(fire.pb.game.SImperialExamStateRepo.class));
		} else {
			keju1RepoMap.putAll(cm.getConf(fire.pb.game.SPointCardImperialExamVillageRepo.class));
			keju2RepoMap.putAll(cm.getConf(fire.pb.game.SPointCardImperialExamProvinceRepo.class));
			keju3RepoMap.putAll(cm.getConf(fire.pb.game.SPointCardImperialExamStateRepo.class));
		}

		npcShareMap = cm.getConf(fire.pb.npc.SNpcShare.class);
		
		ProfessionLeaderManager.getInstance().setVoteTime();
		ProfessionLeaderManager.getInstance().setChallengeTime();
		
		long delta = ProfessionLeaderManager.getInstance().getVoteEndTime() - System.currentTimeMillis() + 1000;
		mkdb.Mkdb.executor().scheduleAtFixedRate(new RefreshShouxiTask(), delta, 7 * 24 * 3600000, TimeUnit.MILLISECONDS);
		
		long delta1 = ProfessionLeaderManager.getInstance().getChallengeEndTime() - System.currentTimeMillis() + 1000;
		if (delta1 < 0)
		{
			long v = ProfessionLeaderManager.getInstance().getVoteEndTime() + 4 * 24 * 3600000;
			mkdb.Mkdb.executor().scheduleAtFixedRate(new fire.pb.school.shouxi.RefreshProfessionLeaderTask(), v, 7 * 24 * 3600000, TimeUnit.MILLISECONDS);	
		}
		else
		{
			mkdb.Mkdb.executor().scheduleAtFixedRate(new fire.pb.school.shouxi.RefreshProfessionLeaderTask(), delta1, 7 * 24 * 3600000, TimeUnit.MILLISECONDS);				
		}	
	}
	
	void reload()
	{
		npcSaleMap = (java.util.TreeMap<Integer, SNpcSale>) fire.pb.shop.Module.sNpcSaleMap;
		npcShareMap = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class);
	}
	
	public int getRoleChangeShape(short shape, int num){
		if (shape > 10)
			return 0;
		return 0;
	}
	
	
	public NavigableMap<Integer, ImperialExamVillageRepo> getKeju1RepositoryMap() {
		
		return keju1RepoMap;
	}
	
    public NavigableMap<Integer, ImperialExamProvinceRepo> getKeju2RepositoryMap() {
		
		return keju2RepoMap;
	}
    
    public NavigableMap<Integer, ImperialExamStateRepo> getKeju3RepositoryMap() {
		
		return keju3RepoMap;
	}
	
	public final SNpcSale getNpcSale( int npcid ) {
		return npcSaleMap.get( npcid );
	}
	
	public final boolean isShareNpc( int npckey ) {
		SNpcShare result = npcShareMap.get( npckey );
		if ( result == null )
			return false;
		return result.getShare() == 1;
	}
	
	
	public SNpcShare getNpcShareByID(int npcId){
		return npcShareMap.get(npcId);
	}
	
	
	public String getNpcNameByID(int npcid){
		SNpcShare result = npcShareMap.get(npcid);
		if ( result == null )
			return "";
		return result.getName();
	}
	
	public int getShape(int npcid){
		SNpcShare result = npcShareMap.get(npcid);
		if ( result == null)
			return 0;
		else return result.shape;
	}
	
	public int getNpcZaxueID( final int npcid ) {
		SNpcShare result = npcShareMap.get( npcid );
		if ( result == null )
			return -1;
		return result.get杂学id();
	}
	
	
	public NavigableMap<Integer, SNpcSale> getNpcSaleMap() {
		return this.npcSaleMap;
	}
	
	
	
	public String getNpcName(SNpcShare npcShare) {
		if (npcShare == null) {
			return "";
		}
		
		StringBuilder strBuilder = new StringBuilder();
		//先取前缀1
		if (npcShare.namepre1 != 0) {
			if (npcPreNameTableMap.containsKey(npcShare.namepre1)) {
				List<NpcPreNameTable> nameList = npcPreNameTableMap.get(npcShare.namepre1);
				int nIndex = Misc.getRandomBetween(0, nameList.size() - 1);
				if (nIndex < nameList.size()) {
					NpcPreNameTable npc = nameList.get(nIndex);
					strBuilder.append(npc.preName);
				}
			}
		}
		//再取前缀2
		if (npcShare.namepre2 != 0) {
			if (npcPreNameTableMap.containsKey(npcShare.namepre2)) {
				List<NpcPreNameTable> nameList = npcPreNameTableMap.get(npcShare.namepre2);
				int nIndex = Misc.getRandomBetween(0, nameList.size() - 1);
				if (nIndex < nameList.size()) {
					NpcPreNameTable npc = nameList.get(nIndex);
					strBuilder.append(npc.preName);
				}
			}
		}
		//再随机姓名
		if (npcShare.nametable != 0) {
			if (npcNameTableMap.containsKey(npcShare.nametable)) {
				List<NpcNameTable> nameList = npcNameTableMap.get(npcShare.nametable);
				//随机出姓
				int nIndex = Misc.getRandomBetween(0, nameList.size() - 1);
				if (nIndex < nameList.size()) {
					NpcNameTable npc = nameList.get(nIndex);
					strBuilder.append(npc.firstName);
				}
				//随机出姓
				nIndex = Misc.getRandomBetween(0, nameList.size() - 1);
				if (nIndex < nameList.size()) {
					NpcNameTable npc = nameList.get(nIndex);
					strBuilder.append(npc.secondName);
				}
			}
		}
		else {
			strBuilder.append(npcShare.getName());
		}
		
		String name = strBuilder.toString();
		if (!name.equals("")) {
			return name;
		}
		
		return "";
	}
}
