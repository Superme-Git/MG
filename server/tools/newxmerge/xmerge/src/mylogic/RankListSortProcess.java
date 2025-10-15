package mylogic;

import java.util.List;

public abstract class RankListSortProcess {
	
	
	public void sortCurrentRankList(List<? extends mkdb.Bean> rankList){
		for(int i=0; i<rankList.size(); i++){
			for(int j=rankList.size()-1; j>i; j--){
				if(compareTo(rankList.get(j), rankList.get(j-1))){
					
					replace(rankList, j, j-1);
					
				}
			}
		}
	}
	
	public <T extends mkdb.Bean> void replace(List<T> rankList, int src, int dest){
		int after = src > dest? src : dest;
		int befor = src > dest? dest : src;
		T tmp = rankList.get(befor);
		T tmp2 = rankList.get(after);
		rankList.add(after+1, tmp);
		rankList.add(befor+1, tmp2);
		rankList.remove(after);
		rankList.remove(befor);
	}
	
	public abstract boolean compareTo(Object src, Object dest);

	
	
}
