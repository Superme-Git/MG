using System;

namespace CSTools
{
    class ToolsTableExcelOLE
    {
        static public bool ImportTableOLE(ToolsTable TT, string FullFileName, bool TableOrCode = true, int XLSVersion = 2003)
        {
            TT.Clear();
            //把EXCEL导入到DataSet
            System.Data.DataSet DS = new System.Data.DataSet();
            System.Data.DataTable DT = new System.Data.DataTable();
            string ConnStr = "";
            if (XLSVersion == 2003)
            {
                ConnStr = " Provider = Microsoft.Jet.OLEDB.4.0 ; Data Source = " + FullFileName + ";Extended Properties='Excel 8.0;HDR=YES;IMEX=1';";
            }
            else if (XLSVersion == 2007)
            {
                ConnStr = " Provider = Microsoft.Ace.OLEDB.12.0 ; Data Source = " + FullFileName + ";Extended Properties='Excel 12.0;HDR=YES;IMEX=1';";
            }
            else
            {
                return false;
            }
            using (System.Data.OleDb.OleDbConnection Conn = new System.Data.OleDb.OleDbConnection(ConnStr))
            {
                Conn.Open();
                System.Data.DataTable SheetNames = Conn.GetOleDbSchemaTable(System.Data.OleDb.OleDbSchemaGuid.Tables, new object[] { null, null, null, "TABLE" });
                string TableName = SheetNames.Rows[0]["TABLE_NAME"].ToString();
                System.Data.OleDb.OleDbDataAdapter DA;
                string SQLCommand = "select * from [" + TableName + "] ";
                DA = new System.Data.OleDb.OleDbDataAdapter(SQLCommand, Conn);
                //da.FillSchema(ds, System.Data.SchemaType.Mapped);
                DA.Fill(DS, TableName);
                DA.Dispose();
                DT = DS.Tables[0];
                Conn.Close();
                Conn.Dispose();
            }
            int RowCount = DT.Rows.Count + 1;//因为上面的HDR=YES;所以这里会把第一行放到表头里,如果不加的话表头会是F1..FN而且单元格的数据会不完整
            int ColCount = DT.Columns.Count;
            TT.SetColCount(ColCount);

            int ReadRowCount = RowCount;
            if (TableOrCode == false)
            {
                ReadRowCount = 4;
            }
            for (int i = 0; i < ReadRowCount; i++)
            {
                ToolsTable.ToolsTableRow Result = TT.AddRow();
                Result.SetColCount(TT.GetColCount());
                for (int i2 = 0; i2 < TT.GetColCount(); i2++)
                {
                    string CurCell = "";
                    if (i == 0)
                    {
                        CurCell = DT.Columns[i2].Caption;
                        if (CurCell == "")
                        {
                            TT.SetColCount(i2);
                            break;
                        }
                    }
                    else
                    {
                        CurCell = DT.Rows[i - 1].ItemArray[i2].ToString();
                    }
                    if (CurCell == "")
                    {
                        if (i2 == 0)
                        {
                            ReadRowCount = i;
                            TT.DelRow(Result);
                        }
                        else
                        {
                            TT.SetColCount(i2);
                        }
                        break;
                    }
                    Result.SetCell(i2, CurCell);
                }
            }
            return true;
        }
    }
}
