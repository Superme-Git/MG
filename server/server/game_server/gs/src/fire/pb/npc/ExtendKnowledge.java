package fire.pb.npc;


public final class ExtendKnowledge {
//	private final long roleid;
//	private final boolean readonly;
	
//	final xbean.ExtendKnowledgeInfo extendknowledge;
//	public ExtendKnowledge( final long roleid, final boolean readonly ) {
//		this.roleid = roleid;
//		this.readonly = readonly;
//		if ( readonly ) {
//			xbean.ExtendKnowledgeInfo ek = 
//			  xtable.Extendknowledge.select( roleid );
//			if ( ek == null ) {
//				extendknowledge = xbean.Pod.newExtendKnowledgeInfoData();
//			//	throw new RuntimeException( "人物id=" + roleid + "杂学数据为null" );
//			} else {
//				extendknowledge = ek;
//			}
//		} else {
//			final xbean.ExtendKnowledgeInfo ek = xtable.Extendknowledge.get( roleid );
//			if ( ek == null ) {
//				extendknowledge = xbean.Pod.newExtendKnowledgeInfo();
//				xtable.Extendknowledge.insert( roleid, extendknowledge );
//			} else {
//				extendknowledge = ek;
//			}
//		}
//	}
	
//	public ExtendSkill getExtendSkill(final int skillid) {
//		xbean.ExtendSkill es = extendknowledge.getExtendskills().get( skillid );
//		if ( es == null ) {
//			return null;
//		}
//		return new ExtendSkill(roleid, skillid, es);
//	}
//	
//	public void extendskillAutoLevelup() {
//		for (Map.Entry<Integer, xbean.ExtendSkill> esElement : extendknowledge.getExtendskills().entrySet()) {
//			new ExtendSkill(roleid, esElement.getKey(), esElement.getValue()).autoLevelup();
//		}
//	}
//	
//	
//	public Map<Integer, xbean.ExtendSkill> getAllExtendSkill() {
//		return extendknowledge.getExtendskills();
//	}
//	
//	public boolean addExtendSkill( final int skillid ) {
//		if ( readonly ) 
//			throw new RuntimeException( "只读异常" );
//		return extendknowledge.getExtendskills().put(skillid, xbean.Pod.newExtendSkill()) == null;
//	}
//	public boolean useSpecailSkill(final int skillid) {
//		ZaxueSkill zs = fire.pb.skill.ZaxueSkillFactoryImp.getInstance().getZaxueSkillInstance(roleid, skillid, false);
//		if (zs == null) {
//			return false;
//		}
//		return zs.cast();
//	}
}
