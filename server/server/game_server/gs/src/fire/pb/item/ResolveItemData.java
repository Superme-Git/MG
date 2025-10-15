package fire.pb.item;

import java.util.ArrayList;
import java.util.List;

public class ResolveItemData {
	public int itemid;
	public int money;  //分解获得的银币
	
	public List<Integer> retItemid1;
	public List<Integer> retItemid1Num;
	public List<Integer> retItemid1Rate;
	
	public List<Integer> retItemid2;
	public List<Integer> retItemid2Num;
	public List<Integer> retItemid2Rate;
	
	public List<Integer> retItemid3;
	public List<Integer> retItemid3Num;
	public List<Integer> retItemid3Rate;
	
	public List<Integer> retItemid4;
	public List<Integer> retItemid4Num;
	public List<Integer> retItemid4Rate;
	
	public List<Integer> retItemid5;
	public List<Integer> retItemid5Num;
	public List<Integer> retItemid5Rate;
	
	public ResolveItemData(final EquipItemShuXing itemAttr) {
		retItemid1 = new ArrayList<Integer>();
		retItemid1Num = new ArrayList<Integer>();
		retItemid1Rate = new ArrayList<Integer>();
		retItemid2 = new ArrayList<Integer>();
		retItemid2Num = new ArrayList<Integer>();
		retItemid2Rate = new ArrayList<Integer>();
		retItemid3 = new ArrayList<Integer>();
		retItemid3Num = new ArrayList<Integer>();
		retItemid3Rate = new ArrayList<Integer>();
		retItemid4 = new ArrayList<Integer>();
		retItemid4Num = new ArrayList<Integer>();
		retItemid4Rate = new ArrayList<Integer>();
		retItemid5 = new ArrayList<Integer>();
		retItemid5Num = new ArrayList<Integer>();
		retItemid5Rate = new ArrayList<Integer>();
		
		this.itemid = itemAttr.getId();
		this.money = itemAttr.get分解获得银币();
		
		if (itemAttr.get分解额外获得物品1() != null && itemAttr.get分解额外获得物品1() != "") {
			String[] strItem1 = itemAttr.get分解额外获得物品1().split(";");
			for (String item1 : strItem1) {
				String[] item11 = item1.split(",");
				retItemid1.add(Integer.parseInt(item11[0]));
				retItemid1Num.add(Integer.parseInt(item11[1]));
				retItemid1Rate.add(Integer.parseInt(item11[2]));
			}
		}
		
		if (itemAttr.get分解额外获得物品2() != null && itemAttr.get分解额外获得物品2() != "") {
			String[] strItem2 = itemAttr.get分解额外获得物品2().split(";");
			for (String item2 : strItem2) {
				String[] item22 = item2.split(",");
				retItemid2.add(Integer.parseInt(item22[0]));
				retItemid2Num.add(Integer.parseInt(item22[1]));
				retItemid2Rate.add(Integer.parseInt(item22[2]));
			}
		}
		
		if (itemAttr.get分解额外获得物品3() != null && itemAttr.get分解额外获得物品3() != "") {
			String[] strItem3 = itemAttr.get分解额外获得物品3().split(";");
			for (String item3 : strItem3) {
				String[] item33 = item3.split(",");
				retItemid3.add(Integer.parseInt(item33[0]));
				retItemid3Num.add(Integer.parseInt(item33[1]));
				retItemid3Rate.add(Integer.parseInt(item33[2]));
			}
		}
		
		if (itemAttr.get分解额外获得物品4() != null && itemAttr.get分解额外获得物品4() != "") {
			String[] strItem4 = itemAttr.get分解额外获得物品4().split(";");
			for (String item4 : strItem4) {
				String[] item44 = item4.split(",");
				retItemid4.add(Integer.parseInt(item44[0]));
				retItemid4Num.add(Integer.parseInt(item44[1]));
				retItemid4Rate.add(Integer.parseInt(item44[2]));
			}
		}
		
		if (itemAttr.get分解额外获得物品5() != null && itemAttr.get分解额外获得物品5() != "") {
			String[] strItem5 = itemAttr.get分解额外获得物品5().split(";");
			for (String item5 : strItem5) {
				String[] item55 = item5.split(",");
				retItemid5.add(Integer.parseInt(item55[0]));
				retItemid5Num.add(Integer.parseInt(item55[1]));
				retItemid5Rate.add(Integer.parseInt(item55[2]));
			}
		}
	}

}
