package fire.pb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.role.SRoleNameData;
import fire.pb.role.SRoleSymbolRepository;
import mkdb.util.UniqName;


public class NameManage {
	public static int x_man_num = 0;
	
	public static int x_woman_num = 0;
	
	public static int m_man_num = 0;
	
	public static int m_woman_num = 0;
	
	public static int symbol_num = 0;

	public static List<String> x_man_list = new ArrayList<String>();//男姓
	public static List<String> x_woman_list = new ArrayList<String>();//女姓

	public static List<String> m_man_list = new ArrayList<String>();
	
	public static List<String> m_woman_list = new ArrayList<String>();

	public static List<String> symbol_list = new ArrayList<String>();


	public static void init() {
		Map<Integer, SRoleNameData> sRoleNameDatas = ConfigManager.getInstance().getConf(SRoleNameData.class);
		for (SRoleNameData repository : sRoleNameDatas.values()) {
			if (repository.getXManName() != null)
				x_man_list.add(repository.getXManName());
			if (repository.getXWomanName() != null)
				x_woman_list.add(repository.getXWomanName());
			if (repository.getMManName() != null)
				m_man_list.add(repository.getMManName());
			if (repository.getMWomanName() != null)
				m_woman_list.add(repository.getMWomanName());
		}

		Map<Integer, SRoleSymbolRepository> roleSymbolRepositoryMap = ConfigManager.getInstance().getConf(SRoleSymbolRepository.class);
		for (SRoleSymbolRepository sRoleSymbolRepository : roleSymbolRepositoryMap.values()) {
			if (sRoleSymbolRepository.getSymbol() != null)
				symbol_list.add(sRoleSymbolRepository.getSymbol());
		}

		x_man_num = x_man_list.size();
		x_woman_num = x_woman_list.size();
		m_man_num = m_man_list.size();
		m_woman_num = m_woman_list.size();
		symbol_num = symbol_list.size();
	}

	private static StringBuilder getOneSymbol(StringBuilder str) {
        StringBuilder result_symbol = new StringBuilder();
        result_symbol.append(str);
		int symbolIndex = Misc.getRandomBetween(0, NameManage.symbol_num - 1);
		String symbol = NameManage.symbol_list.get(symbolIndex);
		boolean ahead = Misc.checkRatePercent(50);
		if (ahead)
			result_symbol.insert(0, symbol);
		else
			result_symbol.append(symbol);
		return result_symbol;
	}

	private static StringBuilder getTwoSymbol(StringBuilder str) {
		StringBuilder result_symbol = new StringBuilder();
        result_symbol.append(str);
		int symbolIndex1 = Misc.getRandomBetween(0, NameManage.symbol_num - 1);
		String symbol1 = NameManage.symbol_list.get(symbolIndex1);
		int symbolIndex2 = Misc.getRandomBetween(0, NameManage.symbol_num - 1);
		String symbol2 = NameManage.symbol_list.get(symbolIndex2);
		int[] symbolPos = new int[] { 40, 40, 20 };
		int pos = Misc.getProbability(symbolPos, 100);
		if (pos == 0) {
			// 两个字符都在前面
			result_symbol.insert(0, symbol1);
			result_symbol.insert(1, symbol2);
		} else if (pos == 1) {
			// 两个字符都在后面
			result_symbol.append(symbol1).append(symbol2);
		} else if (pos == 2) {
			// 两个字符一边一个
			result_symbol.insert(0, symbol1);
			result_symbol.append(symbol2);
		}
		return result_symbol;
	}

	public static List<StringBuilder> addSymbols(StringBuilder result, int num) {
		List<StringBuilder> result_list = new ArrayList<StringBuilder>();
		for (int i = 0; i < 10000; i++) {
			boolean oneSymbol = Misc.checkRatePercent(50);
			if (oneSymbol || result.length() == 7) {
				StringBuilder tmp = NameManage.getOneSymbol(result);
				if (UniqName.exist("role", tmp.toString()) == UniqName.RPC_NOT_EXISTS) {
					result_list.add(tmp);
					if (result_list.size() == num)
						break;
				}
			} else {
				// 如果是两个字符
				StringBuilder tmp = NameManage.getTwoSymbol(result);
				if (UniqName.exist("role", tmp.toString()) == UniqName.RPC_NOT_EXISTS) {
					result_list.add(tmp);
					if (result_list.size() == num)
						break;
				}
			}
		}
		return result_list;
	}
}
