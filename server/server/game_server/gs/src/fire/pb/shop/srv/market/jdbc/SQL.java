package fire.pb.shop.srv.market.jdbc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.utils.MarketUtils;

public class SQL {

	/**
	 * sql in函数查询条件包装
	 * @param conditions  条件
	 * @return  in (1,2,3)
	 */
	public static String inWrapper(List<? extends Object> conditions) {
		if (conditions.size() == 0)
			return "";
		
		StringBuilder sentence = new StringBuilder("in (");
		for (Object condition : conditions) {
			sentence.append(condition);
			sentence.append(",");
		}
		sentence.deleteCharAt(sentence.length() - 1);
		sentence.append(")");
		return sentence.toString();
	}
	
	public static void main(String[] args) {
//		Integer[] arr = new Integer[]{1, 2, 3};
		ArrayList<Integer> arr = new ArrayList<>(); 
		arr.add(1);
		arr.add(2);
		arr.add(3);
		System.out.println(inWrapper(arr));
		
		System.out.println(MarketUtils.getShieldRoleId(123456789));
		
	}
	
}
