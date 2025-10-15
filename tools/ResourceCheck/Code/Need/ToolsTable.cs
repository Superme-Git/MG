using System;
using System.Collections.Generic;
using System.IO;

namespace CSTools
{
    class ToolsTable
    {
        public class ToolsTableRow
        {
            List<string> mCells;
            public ToolsTableRow()
            {
                mCells = new List<string>();
            }
            public void Clear()
            {
                mCells.Clear();
            }
            public void SetColCount(int V)
            {
                if (mCells.Count > V)
                {
                    mCells.RemoveRange(V, mCells.Count - V);
                }
                while (mCells.Count < V)
                {
                    mCells.Add("");
                }
            }
            public int GetColCount()
            {
                return mCells.Count;
            }
            public string GetCell(int ColIndex)
            {
                return mCells[ColIndex];
            }
            public void SetCell(int ColIndex,string V)
            {
                mCells[ColIndex] = V;
            }
            public bool SetRowData(string V)
            {
                Clear();
                string[] SplitResultStr = V.Split(ToolsStr.StrArr("\t"), StringSplitOptions.RemoveEmptyEntries);
                if (SplitResultStr.Length == 0)
                {
                    return false;
                }
                for (int i = 0; i < SplitResultStr.Length; i++)
                {
                    mCells.Add(SplitResultStr[i]);
                }
                return true;
            }
            public string GetRowData()
            {
                string Result = "";
                for (int i = 0; i < GetColCount(); i++)
                {
                    if (i > 0)
                    {
                        Result = Result + ToolsStr.Tabs(1);
                    }
                    Result = Result + GetCell(i);
                }
                return Result;
            }
        }
        string mFullFileName;
        List<ToolsTableRow> mRows;
        int mHeadRowCount;
        int mColCount;

        public ToolsTable()
        {
            SetHeadRowCount(1);
            mRows = new List<ToolsTableRow>();
        }
        public ToolsTable(string FullFileName, EMXLSReadMode XLSReadMode = EMXLSReadMode.XLSRM_ByNPOI, bool TableOrCode = true) : this()
        {
            if (FullFileName.EndsWith("txt"))
            {
                LoadFromFileTXT(FullFileName, TableOrCode);
            }
            else if (FullFileName.EndsWith("xls"))
            {
                LoadFromFileXLS(FullFileName, XLSReadMode, TableOrCode, 2003);
            }
            else if (FullFileName.EndsWith("xlsx"))
            {
                LoadFromFileXLS(FullFileName, XLSReadMode, TableOrCode, 2007);
            }
            else if (FullFileName.EndsWith("bin"))
            {
                LoadFromFileBIN(FullFileName, TableOrCode);
            }
        }
        public void Free()
        {
            Clear();
            mRows = null;
        }
        public bool LoadFromFileTXT(string FullFileName, bool TableOrCode = true)
        {
            mFullFileName = FullFileName;
            ToolsStream TS = new ToolsStream(FullFileName, FileMode.OpenOrCreate, FileAccess.Read);
            ToolsTableTxt.ImportTable(this, TS, TableOrCode);
            TS.Close();
            TS = null;
            return true;
        }
        public bool LoadFromFileXLS(string FullFileName, EMXLSReadMode XLSReadMode = EMXLSReadMode.XLSRM_ByNPOI, bool TableOrCode = true, int XLSVersion = 2007)
        {
            mFullFileName = FullFileName;
            return ToolsTableExcel.ImportTable(this, FullFileName, XLSReadMode, TableOrCode, XLSVersion);
        }
        public bool LoadFromFileBIN(string FullFileName, bool TableOrCode = true)
        {
            //mFullFileName = FullFileName;
            //ToolsStream TS = new ToolsStream(FullFileName, FileMode.OpenOrCreate, FileAccess.Read);
            //ToolsKunTaTable.ImportTable(this, TS, TableOrCode);
            //TS.Close();
            //TS = null;
            return true;
        }
        public ToolsTableRow AddRow(ToolsTableRow V)
        {
            mRows.Add(V);
            return V;
        }
        public ToolsTableRow AddRow()
        {
            ToolsTableRow NewRow = new ToolsTableRow();
            NewRow.SetColCount(GetColCount());
            mRows.Add(NewRow);
            return NewRow;
        }
        public ToolsTableRow AddRow(string V)
        {
            ToolsTableRow Result = AddRow();
            Result.SetRowData(V);
            return Result;
        }
        public void DelRow(int Index)
        {
            mRows.RemoveAt(Index);
        }
        public void DelRow(ToolsTableRow V)
        {
            mRows.Remove(V);
        }
        public void SetHeadRowCount(int V)
        {
            mHeadRowCount = V;
        }
        public int GetHeadRowCount()
        {
            return mHeadRowCount;
        }
        public void Clear()
        {
            for (int i = 0; i < mRows.Count; i++)
            {
                mRows[i].Clear();
            }
            mRows.Clear();
        }
        public int GetRowCount()
        {
            return mRows.Count;
        }
        public int GetDataRowCount()
        {
            return GetRowCount() - GetHeadRowCount();
        }
        public int GetColCount()
        {
            return mColCount;
        }
        public void SetColCount(int V)
        {
            mColCount = V;
            for (int i = 0; i < GetRowCount(); i++)
            {
                GetRow(i).SetColCount(V);
            }
        }
        public int ColIndexOfName(string ColName)
        {
            string CurColName;
            for (int i = 0; i < GetColCount(); i++)
            {
                CurColName = GetCell(0, i);
                if (CurColName == ColName)
                {
                    return i;
                }
            }            
            return -1;
        }
        public ToolsTableRow GetRow(int RowIndex)
        {
            return mRows[RowIndex];
        }
        public ToolsTableRow GetDataRow(int RowIndex)
        {
            return GetRow(GetHeadRowCount() + RowIndex);
        }
        public string GetCell(int RowIndex, int ColIndex)
        {
            return GetRow(RowIndex).GetCell(ColIndex);
        }
        public string GetDataCell(int RowIndex, int ColIndex)
        {
            return GetRow(GetHeadRowCount() + RowIndex).GetCell(ColIndex);
        }
        public string GetTableString()
        {
            string Result = "";
            for (int i = 0; i < GetRowCount(); i++)
            {
                string CurRow = GetRow(i).GetRowData();
                Result = Result + CurRow;
                if (i < GetRowCount() - 1)
                {
                    Result = Result + ToolsStr.Enter();
                }
            }
            return Result;
        }

        public void ExportToDataTable(ref System.Data.DataTable DT)
        {
            DT = null;
            DT = new System.Data.DataTable();
            for (int i = 0; i < GetColCount(); i++)
            {
                int ABMod = i % 26;
                if (ABMod == 0)
                {
                    ABMod = 26;
                }
                int ABCount = i / 26;
                if (ABCount == 0)
                {
                    DT.Columns.Add(Convert.ToChar(((int)'A') + i).ToString());
                } 
                else
                {
                    DT.Columns.Add(Convert.ToChar(((int)'A') + ABCount - 1).ToString() + Convert.ToChar(((int)'A') + ABMod - 1).ToString());
                }
            }
            for (int i = 0; i < GetRowCount(); i++)
            {
                System.Data.DataRow DR = DT.NewRow();
                for (int i2 = 0; i2 < GetColCount(); i2++)
                {
                    DR[i2] = GetCell(i, i2);
                }
                DT.Rows.Add(DR);
            }
        }
    }
}
