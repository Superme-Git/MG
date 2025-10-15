using System;

namespace CSTools
{
    public enum EMXLSReadMode
    {
        XLSRM_Unknow = 0,
        XLSRM_Interop = 1,
        XLSRM_ByOle = 2,
        XLSRM_ByNPOI = 3,
    }

    class ToolsTableExcel
    {
        static public bool ImportTable(ToolsTable TT, string FullFileName, EMXLSReadMode XLSReadMode = EMXLSReadMode.XLSRM_ByNPOI, bool TableOrCode = true, int XLSVersion = 2007)
        {
            if (XLSReadMode == EMXLSReadMode.XLSRM_Interop)
            {
                //return ToolsTableExcelInterop.ImportTableInterop(TT, FullFileName, TableOrCode);
            }
            else if (XLSReadMode == EMXLSReadMode.XLSRM_ByOle)
            {
                //return ToolsTableExcelOLE.ImportTableOLE(TT, FullFileName, TableOrCode, XLSVersion);
            }
            else if (XLSReadMode == EMXLSReadMode.XLSRM_ByNPOI)
            {
                if (XLSVersion == 2003)
                {
                    return ToolsTableExcelNPOIXLS.ImportTableNPOI(TT, FullFileName, TableOrCode);
                }
                else
                {
                    return ToolsTableExcelNPOIXLSX.ImportTableNPOI(TT, FullFileName, TableOrCode);
                }
            }
            else
            {
                return ToolsTableExcelNPOIXLSX.ImportTableNPOI(TT, FullFileName, TableOrCode);
            }
            return false;
        }
    }
}
