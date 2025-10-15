package fire.pb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fire.pb.main.ConfigManager;
import fire.pb.role.SRoleSymbolRepository;
import mkdb.util.UniqName;

public class NameRepositoryAdditional {

    private static NameRepositoryAdditional instance = null;

    private static Object lock = new Object();

    
    private List<SRoleSymbolRepository> malePrefix;
    
    private List<SRoleSymbolRepository> femalePrefix;

    
    private List<SRoleSymbolRepository> mailSuffix;
    
    private List<SRoleSymbolRepository> femaleSuffix;

    private TreeMap<Integer, SRoleSymbolRepository> symbolMap = ConfigManager.getInstance().getConf(
            SRoleSymbolRepository.class);

    public static NameRepositoryAdditional getInstance() {
        if (null == instance) {
            synchronized (lock) {
                if (null == instance) {
                    instance = new NameRepositoryAdditional();
                }
            }
        }
        return instance;
    }

    private NameRepositoryAdditional() {
        malePrefix = new ArrayList<SRoleSymbolRepository>(symbolMap.size() * 1 / 2);
        mailSuffix = new ArrayList<SRoleSymbolRepository>(symbolMap.size() * 1 / 2);
        femalePrefix = new ArrayList<SRoleSymbolRepository>(symbolMap.size() * 1 / 2);
        femaleSuffix = new ArrayList<SRoleSymbolRepository>(symbolMap.size() * 1 / 2);
        init();
    }

    
    private void init() {
        SRoleSymbolRepository symbol = null;
        for (Map.Entry<Integer, SRoleSymbolRepository> entry : symbolMap.entrySet()) {
            symbol = entry.getValue();
            if (1 == symbol.getType()) {
                if (2 != symbol.getGender()) {
                    //0 1
                    malePrefix.add(symbol);
                }
                if (1 != symbol.getGender()) {
                    //0 2
                    femalePrefix.add(symbol);
                }
            }
            if (2 == symbol.getType()) {
                if (2 != symbol.getGender()) {
                    //0 1
                    mailSuffix.add(symbol);

                }
                if (1 != symbol.getGender()) {
                    //0 2
                    femaleSuffix.add(symbol);
                }
            }
        }
    }

    
    public String getPrefix(boolean male) {
        String prefix = "";
        if (male) {
            if (malePrefix.isEmpty()) {
                prefix = "";
            }
            int symbolIndex = Misc.getRandomBetween(0, malePrefix.size() - 1);
            prefix = malePrefix.get(symbolIndex).getSymbol();
        } else {
            if (femalePrefix.isEmpty()) {
                prefix = "";
            }
            int symbolIndex = Misc.getRandomBetween(0, femalePrefix.size() - 1);
            prefix = femalePrefix.get(symbolIndex).getSymbol();
        }
        return prefix.trim();

    }

    
    public String getSuffix(boolean male) {
        String suffix = "";
        if (male) {
            if (mailSuffix.isEmpty()) {
                suffix = "";
            }
            int symbolIndex = Misc.getRandomBetween(0, mailSuffix.size() - 1);
            suffix = mailSuffix.get(symbolIndex).getSymbol();
        } else {
            if (femaleSuffix.isEmpty()) {
                suffix = "";
            }
            int symbolIndex = Misc.getRandomBetween(0, femaleSuffix.size() - 1);
            suffix = femaleSuffix.get(symbolIndex).getSymbol();
        }
        return suffix.trim();

    }

    
    public List<StringBuilder> addSymbols(StringBuilder result, int num, boolean male) {
        List<StringBuilder> resultList = new ArrayList<StringBuilder>();
        for (int i = 0; i < 10000; i++) {
            boolean oneSymbol = Misc.checkRatePercent(50);
            StringBuilder tmp = new StringBuilder();
            if (oneSymbol) {
                tmp.append(getPrefix(male)).append(result);
            } else {
                tmp.append(result).append(getSuffix(male));
            }
            if (UniqName.exist("role", tmp.toString()) == UniqName.RPC_NOT_EXISTS) {
                resultList.add(tmp);
                if (resultList.size() == num)
                    break;
            }
        }
        return resultList;
    }
}
