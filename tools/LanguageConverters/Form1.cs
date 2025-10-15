using System;
using System.IO;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;

namespace LanguageConverters
{
    public partial class v : Form
    {
        public v()
        {
            mInstance = this;

            InitializeComponent();

            listView1.View = View.Details;
            listView1.GridLines = true;
            listView1.LabelEdit = true;
            listView1.HeaderStyle = ColumnHeaderStyle.Clickable;

            listView1.Columns.Add("索引",-2,HorizontalAlignment.Left);
            listView1.Columns.Add("中文", -2, HorizontalAlignment.Left);
            listView1.Columns.Add("目标", -2, HorizontalAlignment.Left);

            mPathSChineseVersion = mOptionIni.FindAndAddApp("SChineseVersionPath", "Path").FindAndAddItem("Path").GetValue();
            if (mPathSChineseVersion == "")
            {
                mPathSChineseVersion = "点击选择路径";
            }
            mPathTChineseVersion = mOptionIni.FindAndAddApp("TChineseVersionPath", "Path").FindAndAddItem("Path").GetValue();
            if (mPathTChineseVersion == "")
            {
                mPathTChineseVersion = "点击选择路径";
            }
            mPathImportFileName = mOptionIni.FindAndAddApp("ImportPath", "Path").FindAndAddItem("Path").GetValue();
            if (mPathImportFileName == "")
            {
                mPathImportFileName = "点击选择要导入的文件";
            }
            mPathLayout = mOptionIni.FindAndAddApp("LayoutPath", "Path").FindAndAddItem("Path").GetValue();
            if (mPathLayout == "")
            {
                mPathLayout = "点击选择路径";
            }
            
            label1.Text = mPathSChineseVersion;
            label2.Text = mPathTChineseVersion;
            label_InputFile.Text = mPathImportFileName;
            label_LayoutPath.Text = mPathLayout;

            toolTip1.IsBalloon = true;
            toolTip1.ShowAlways = true;
            toolTip1.SetToolTip(label1, "中文版本数据表路径，点击可以修改！");

            toolTip2.IsBalloon = true;
            toolTip2.ShowAlways = true;
            toolTip2.SetToolTip(label2, "目标版本数据表路径，点击可以修改！");

            toolTip3.IsBalloon = true;
            toolTip3.ShowAlways = true;
            toolTip3.SetToolTip(label_InputFile, "要导入的数据表路径，点击可以修改！");
            toolTip3.SetToolTip(button1, "导出路径中所有的含有中文数据的表");
            toolTip3.SetToolTip(button2, "导出路径中所有的含有中文数据的表");
            toolTip3.SetToolTip(button4, "导入修改过的数据表并写入有改动的项");
            toolTip3.SetToolTip(button_Import, "针对有源和目标的数据表，导入修改过的数据表");
            toolTip3.SetToolTip(button_ReadTarget, "读取下面路径里所有的表格");
            toolTip3.SetToolTip(button_ReadSource, "读取下面路径里所有的表格");
            toolTip3.SetToolTip(button_ApplyImport, "应用导入的表格，写入有修改的数据");

            
            
            

            textBox_SChinese.Enabled = false;

            buttonModify.Enabled = false;

            label_FileState.Text = "还未读取数据表";
            label_ValueState.Text = "无数据";

            button_ExportAll.Enabled = false;

            progressBar1.Minimum = 0;
            progressBar1.Maximum = 100;


            mDialogOldWidth = Size.Width;
            mDialogOldHeight = Size.Height;
        }

        public static v getInstance()
        {
            return mInstance;
        }

        public static void SetProgressMax(int nValue)
        {
            ThreadStart ts = delegate
            {
                getInstance().progressBar1.Maximum = nValue;
            };
            getInstance().BeginInvoke(ts);
        }
        public static void SetProgress(int nValue)
        {
            ThreadStart ts = delegate
            {
                getInstance().progressBar1.Value = nValue;
            };
            getInstance().BeginInvoke(ts);
        }
        public static void Error(string info)
        {
            ThreadStart ts = delegate
            {
                getInstance().listBox_output.Items.Insert(0, "error:" + info);
            };
            getInstance().BeginInvoke(ts);
            //getInstance().listBox_output.Items.Insert(0, "error:" + info);
        }
        public static void Warning(string info)
        {
            ThreadStart ts = delegate
            {
                getInstance().listBox_output.Items.Insert(0, "warning:" + info);
            };
            getInstance().BeginInvoke(ts);
            //getInstance().listBox_output.Items.Insert(0, "warning:" + info);
        }
        public static void Info(string info)
        {
            ThreadStart ts = delegate
            {
                getInstance().listBox_output.Items.Insert(0, "info:" + info);
            };
            getInstance().BeginInvoke(ts);
            //getInstance().listBox_output.Items.Insert(0, "info:" + info);
        }

        public static string GetSelectedFilePath(string sTitle, string sPrePath = "")
        {
            OpenFileDialog dialog = new OpenFileDialog();
            dialog.Title = sTitle;
            string foldPath = "";
            try
            {
                if (dialog.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                {
                    foldPath = dialog.FileName;
                }
            }
            catch (System.InvalidOperationException e)
            {
                v.Error("异常信息=" + e.Message);
            }

            return foldPath;
        }
        public static string GetSelectedFolderPath(string sTitle, string sPrePath = "")
        {
            FolderBrowserDialog dialog = new FolderBrowserDialog();
            dialog.Description = sTitle;
            dialog.SelectedPath = sPrePath;
            string foldPath = "";
            try
            {
                if (dialog.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                {
                    foldPath = dialog.SelectedPath;
                }
            }
            catch (System.InvalidOperationException e)
            {
                v.Error("异常信息=" + e.Message);
            }

            return foldPath;
        }
        public static List<string> GetFiles(string path, string filter)
        {
            List<string> result = new List<string>();
            try
            {
                DirectoryInfo parentFolder = new DirectoryInfo(path);
                FileInfo[] parentFolderInfo = parentFolder.GetFiles(filter, SearchOption.AllDirectories);
                for (int i = 0; i < parentFolderInfo.Length; i++)
                {
                    result.Add(parentFolderInfo[i].FullName);
                }
            }
            catch (ArgumentException e)
            {
                v.Error("路径" + path + "无效,异常信息=" + e.Message);
            }
            return result;
        }
        public static bool IsBaseType(string type)
        {
            return type == "bool" || type == "int" || type == "long" || type == "double" || type == "string" || type == "String";
        }
        //public static bool IsBeanType(string beanName)
        //{
        //    BeanDef beanDef;
        //    return Form1.GetBeanDef(out beanDef, beanName);
        //}
        public static bool IsArrayType(string type)
        {
            if (type == "vector")
            {
                return true;
            }
            return false;
        }
        public static bool IsInvalidValue(string type, string value)
        {
            if (type == "int" && value != "n!u@l#l")
            {
                for (int i = 0; i < value.Length; i++)
                {
                    if (value[i] == '-')
                        continue;
                    if (value[i] < '0' || value[i] > '9')
                    {
                        return false;
                    }
                }
            }
            else if (type == "float" && value != "n!u@l#l")
            {
                for (int i = 0; i < value.Length; i++)
                {
                    if (value[i] == '-')
                        continue;
                    if (value[i] == '.')
                        continue;
                    if (value[i] < '0' || value[i] > '9')
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        public static bool IsInvalidName(string name)
        {
            return name == "n!u@l#l";
        }
        public static string ConverXmlName(string name)
        {
            string retName = "";
            for (int i = 0; i < name.Length; i++)
            {
                retName += name[i];
                if (name[i] == '_')
                    retName += name[i];
            }
            return retName;
        }
        public static string ConverXmlString(string text)
        {
            string xmlText = "";
            //if (text.Length > 2 && text[0] == '<')
            //{
            for (int i = 0; i < text.Length; i++)
            {
                if (text[i] == '<')
                {
                    xmlText += "&lt;";
                }
                else if (text[i] == '>')
                {
                    xmlText += "&gt;";
                }
                else if (text[i] == '"')
                {
                    xmlText += "&quot;";
                }
                else if (text[i] == '&')
                {
                    xmlText += "&amp;";
                }
                else
                    xmlText += text[i];
            }
            //}

            return xmlText;
        }
        public static string ConverPath(string path)
        {
            string retString = "";
            for (int i = 0; i < path.Length; i++)
            {
                if (path[i] == '/')
                {
                    retString += '\\';
                }
                else
                    retString += path[i];
            }
            return retString;
        }
        public static List<string> stovs(string src, char splitChar)
        {
            List<string> vs = new List<string>();
            string s = "";
            for (int i = 0; i < src.Length; i++)
            {
                if (src[i] == splitChar)
                {
                    vs.Add(s);
                    s = "";
                }
                else
                    s += src[i];
            }
            if (s.Length > 0)
                vs.Add(s);

            return vs;
        }
        public static string vstos(List<string> vsParams, char splitChar)
        {
            string retString = "";
            for (int i = 0; i < vsParams.Count; i++)
            {
                retString += vsParams[i];
                if (i + 1 < vsParams.Count)
                {
                    retString += splitChar;
                }
            }
            return retString;
        }
        public static bool GetAbsDataPath(out string absPathFileName)//获取绝对数据路径,配置的都是相对的.
        {
            absPathFileName = "";
            string strExecPath = System.IO.Directory.GetCurrentDirectory();
            List<string> vsExecPath = v.stovs(strExecPath, '\\');
            string strconfigpath = "";
            //if (mSelectFileType == 0)
            //{
            //    strconfigpath = Form1.mSrcDataXlsPath;
            //}
            //else if (mSelectFileType == 1)
            //{
            //    strconfigpath = Form1.mSrcDataCsvPath;
            //}
            //else if (mSelectFileType == 2)
            //{
            //    strconfigpath = Form1.mSrcDataTxtPath;
            //}
            List<string> vsConfigPath = v.stovs(strconfigpath, '\\');
            for (int i = 0; i < vsConfigPath.Count; i++)
            {
                if (vsConfigPath[i] == "..")
                {
                    vsConfigPath.RemoveAt(i);
                    int removeIndex = vsExecPath.Count - 1;
                    if (removeIndex > 0)
                    {
                        vsExecPath.RemoveAt(removeIndex);
                    }
                    else
                    {
                        v.Error("计算绝对路径失败，工作目录为" + strExecPath + ",配置目录为" + strconfigpath);
                        return false;
                    }
                    i--;
                }
                else
                {
                    break;
                }
            }
            List<string> absPath = vsExecPath;
            for (int i = 0; i < vsConfigPath.Count; i++)
            {
                absPath.Add(vsConfigPath[i]);
            }
            absPathFileName = vstos(absPath, '\\');
            return true;
        }

        private void CloseBtn()
        {
            button_ReadSource.Enabled = false;
            button_ReadTarget.Enabled = false;
            button_Import.Enabled = false;
            buttonModify.Enabled = false;
            button_ApplyImport.Enabled = false;
            button_ExportAll.Enabled = false;

            label1.Enabled = false;
            label2.Enabled = false;
            label_InputFile.Enabled = false;


            listBox_FileList.Enabled = false;
            listView1.Enabled = false;
            textBox_Target.Enabled = false;

            button1.Enabled = false;
            button2.Enabled = false;
            button4.Enabled = false;

            button_ReadLayout.Enabled = false;
            button_ExportLayout.Enabled = false;
            button_ImportApplyLayout.Enabled = false;
            label_LayoutPath.Enabled = false;
        }
        private void OpenBtn()
        {

            ThreadStart ts = delegate
            {
                button_ReadSource.Enabled = true;
                button_ReadTarget.Enabled = true;
                button_Import.Enabled = true;
                buttonModify.Enabled = true;
                button_ApplyImport.Enabled = true;
                button_ExportAll.Enabled = true;

                label1.Enabled = true;
                label2.Enabled = true;
                label_InputFile.Enabled = true;



                listBox_FileList.Enabled = true;
                listView1.Enabled = true;
                textBox_Target.Enabled = true;

                button1.Enabled = true;
                button2.Enabled = true;
                button4.Enabled = true;

                button_ReadLayout.Enabled = true;
                button_ExportLayout.Enabled = true;
                button_ImportApplyLayout.Enabled = true;
                label_LayoutPath.Enabled = true;
            };
            getInstance().BeginInvoke(ts);

            
        }


        private void listView1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            string ss = ((System.Windows.Forms.ListView)sender).SelectedItems.ToString();

            System.Windows.Forms.ListView lv = (System.Windows.Forms.ListView)sender;
            if(lv.SelectedItems.Count > 0)
            {
                for(int i = 0; i < lv.SelectedItems[0].SubItems.Count; ++ i)
                {
                    string v1 = lv.SelectedItems[0].SubItems[i].Text;
                }
            }
        }

        public static void Set_TChinese_ValueState(string sText)
        {
            ThreadStart ts = delegate
            {
                getInstance().label_ValueState.Text = sText;
            };
            getInstance().BeginInvoke(ts);
            
        }

        public static void Set_SChinese_FileState(string sText)
        {
            ThreadStart ts = delegate
            {
                getInstance().label_FileState.Text = sText;
                getInstance().label_ValueState.Text = "无数据";
            };
            getInstance().BeginInvoke(ts);
        }
        public static void Clear_SChinese_FileList()
        {
            ThreadStart ts = delegate
            {
                getInstance().listBox_FileList.Items.Clear();
            };
            getInstance().BeginInvoke(ts);
            
        }
        public static void Output_SChinese_FileList(string info)
        {
            ThreadStart ts = delegate
            {
                getInstance().listBox_FileList.Items.Insert(0, info);
            };
            getInstance().BeginInvoke(ts);
            
        }

        private void LoadConfigDataImple_SChinese()
        {
            SetProgressMax(150);
            v.SetProgress(0);
            Clear_SChinese_FileList();
            Set_SChinese_FileState("读取数据表中...");
            string tempPath = mPathSChineseVersion;
            mDataMgr_SChinese.Clear();
            List<string> files = v.GetFiles(tempPath, "*.xlsm");
            List<string> files1 = v.GetFiles(tempPath, "*.xlsx");
            int nLoadNum = 0;
            for (int i = 0; i < files.Count; i++)
            {
                if (!mDataMgr_SChinese.loadXlsFile(files[i]))
                {
                    return;
                }
                v.SetProgress((int)((++nLoadNum)*100 / (files.Count + files1.Count)));
            }
            for (int i = 0; i < files1.Count; i++)
            {
                if (!mDataMgr_SChinese.loadXlsFile(files1[i]))
                {
                    return;
                }
                v.SetProgress((int)((++nLoadNum) * 100 / (files.Count + files1.Count)));
            }

            nLoadNum = 0;
            int nChineseFileCount = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_SChinese.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr_SChinese.mXls.TryGetValue(name, out data))
                {

                    int ValueCount = 0;
                    // 从第一行数据开始
                    for (int i = data.colCount; i < data.mData.Count; ++i)
                    {
                        string sValue = data.mData.ElementAt(i);
                        for (int j = 0; j < sValue.Length; ++j)
                        {
                            if ((int)sValue[j] > 127)
                            {
                                // is not ascii
                                ValueCount++;
                                break;
                            }
                        }
                        if (ValueCount > 0)
                        {
                            break;
                        }
                    }
                    if (ValueCount > 0)
                    {
                        Output_SChinese_FileList(name);
                        nChineseFileCount++;
                    }
                }
                v.SetProgress(100+(int)((++nLoadNum) * 50 / (mDataMgr_SChinese.mXls.Keys.Count)));
            }
            Set_SChinese_FileState("含有中文的数据表数量:" + nChineseFileCount);

            v.Info("中文语言版加载完成");

            ThreadStart ts = delegate
            {
                button_ExportAll.Enabled = true;
            };
            getInstance().BeginInvoke(ts);

            v.mEditType = eEditType.edittype_ReadTableSource;

            OpenBtn();
        }

        private void LoadConfigDataImple_Target()
        {
            SetProgressMax(100);
            v.SetProgress(0);
            int nLoadNum = 0;
            // 加载对应版。。
            string stargetpath = mPathTChineseVersion;
            string tempPathTarget = stargetpath;
            mDataMgr_TChinese.Clear();
            List<string> filesTarget = v.GetFiles(tempPathTarget, "*.xlsm");
            List<string> files1Target = v.GetFiles(tempPathTarget, "*.xlsx");
            for (int i = 0; i < filesTarget.Count; i++)
            {
                if (!mDataMgr_TChinese.loadXlsFile(filesTarget[i]))
                {
                    return;
                }
                v.SetProgress((int)((++nLoadNum) * 100 / (filesTarget.Count + files1Target.Count)));
            }
            for (int i = 0; i < files1Target.Count; i++)
            {
                if (!mDataMgr_TChinese.loadXlsFile(files1Target[i]))
                {
                    return;
                }
                v.SetProgress((int)((++nLoadNum) * 100 / (filesTarget.Count + files1Target.Count)));
            }
            v.Info("目标语言版加载完成");

            ThreadStart ts = delegate
            {
                button_ExportAll.Enabled = true;
            };
            getInstance().BeginInvoke(ts);


            v.mEditType = eEditType.edittype_ReadTableTarget;

            OpenBtn();
        }

        private void button1_Click(object sender, EventArgs e)
        {

            
            Thread thread = new Thread(new ThreadStart(LoadConfigDataImple_SChinese));
            thread.Start();
            CloseBtn();

        }

        private void listBox_FileList_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (((System.Windows.Forms.ListBox)(sender)).SelectedItems.Count == 1)
            {
                CloseBtn();

                mSelectFileName = (string)((System.Windows.Forms.ListBox)(sender)).SelectedItems[0];
                

                listView1.Items.Clear();
                switch(mEditType)
                {
                    case eEditType.edittype_ReadTableSource:
                    case eEditType.edittype_ReadTableTarget:
                        {
                            Info("选中源版本中的:->" + mSelectFileName);

                            XlsData data_s, data_t;
                            bool bGetSChineseFileOk = mDataMgr_SChinese.mXls.TryGetValue(mSelectFileName, out data_s);
                            bool bGetTChineseFileOk = mDataMgr_TChinese.mXls.TryGetValue(mSelectFileName, out data_t);
                            if (bGetSChineseFileOk == false)
                            {
                                Error("中文版本中未发现:->" + mSelectFileName);
                                goto ENDLINE;
                            }
                            if (bGetTChineseFileOk == false)
                            {
                                Error("目标版本中未发现:->" + mSelectFileName);
                                goto ENDLINE;
                            }
                            if (data_s.colCount != data_t.colCount)
                            {
                                Error("两方表的列数不同:->" + data_s.colCount + ":" + data_t.colCount);
                                goto ENDLINE;
                            }
                            //if (data_s.mData.Count != data_t.mData.Count)
                            //{
                            //    goto ENDLINE;
                            //}
                            listView1.BeginUpdate();
                            int nValueCount = 0;
                            for (int i = data_s.colCount; i < data_s.mData.Count; ++i)
                            {
                                string sValue = data_s.mData.ElementAt(i);
                                for (int j = 0; j < sValue.Length; ++j)
                                {
                                    if ((int)sValue[j] > 127)
                                    {
                                        // is not ascii
                                        ListViewItem i_item = listView1.Items.Add(i + "");
                                        i_item.SubItems.Add(sValue);
                                        if (i >= data_t.mData.Count)
                                        {
                                            i_item.SubItems.Add("目标缺失");
                                            Error("目标版本中缺行数据:->" + sValue);
                                        }
                                        else
                                        {
                                            i_item.SubItems.Add(data_t.mData.ElementAt(i));
                                        }
                                        nValueCount++;

                                        break;
                                    }
                                }
                            }
                            listView1.EndUpdate();
                            Set_TChinese_ValueState("数量:->" + nValueCount);
                        }break;

                    case eEditType.edittype_ImportUpdateDiffTable:
                        {
                            Info("选中导入数据中的:->" + mSelectFileName);
                            XlsData data_s;
                            bool bGetSChineseFileOk = mDataMgr_Import.mXls.TryGetValue(mSelectFileName, out data_s);
                            if (bGetSChineseFileOk == false)
                            {
                                Error("导入数据中未发现:->" + mSelectFileName);
                                goto ENDLINE;
                            }
                            listView1.BeginUpdate();
                            int nValueCount = 0;
                            for (int i = 0; i < data_s.mData.Count; i += 3)
                            {
                                string sValue = data_s.mData.ElementAt(i);

                                ListViewItem i_item = listView1.Items.Add(data_s.mData.ElementAt(i));
                                i_item.SubItems.Add(data_s.mData.ElementAt(i + 1));
                                i_item.SubItems.Add(data_s.mData.ElementAt(i + 2));
                                nValueCount++;
                            }
                            listView1.EndUpdate();
                            Set_TChinese_ValueState("数量:->" + nValueCount);
                        } break;

                    case eEditType.edittype_ReadLayout:
                        {
                            Info("选中导入数据中的:->" + mSelectFileName);

                            xmlData data_s;
                            bool bGetLayoutOk = mDataMgr_Layout.mXml.TryGetValue(mSelectFileName, out data_s);
                            if (bGetLayoutOk == false)
                            {
                                Error("导入数据中未发现:->" + mSelectFileName);
                                goto ENDLINE;
                            }

                            listView1.BeginUpdate();
                            int nValueCount = 0;
                            for (int i = 0; i < data_s.mPath2Value.Count; i += 3)
                            {
                                string sValue = data_s.mPath2Value.ElementAt(i).Value;

                                ListViewItem i_item = listView1.Items.Add(data_s.mPath2Value.ElementAt(i).Key);
                                i_item.SubItems.Add(sValue);
                                nValueCount++;
                            }
                            listView1.EndUpdate();
                            Set_TChinese_ValueState("数量:->" + nValueCount);
                        } break;

                    default:
                        {
                            return;
                        }
                }


                buttonModify.Enabled = false;
                OpenBtn();
            }

        ENDLINE: OpenBtn();
        }

        private void listView1_SelectedIndexChanged(object sender, EventArgs e)
        {
            System.Windows.Forms.ListView lv = (System.Windows.Forms.ListView)sender;
            mSelectValueIndex = lv.FocusedItem.Index;
            if (lv.SelectedItems.Count > 0)
            {
                switch (mEditType)
                {
                    case eEditType.edittype_ReadTableSource:
                    case eEditType.edittype_ReadTableTarget:
                        {
                            mSelectValueText = lv.SelectedItems[0].SubItems[0].Text;

                            textBox_SChinese.Text = lv.SelectedItems[0].SubItems[1].Text;
                            textBox_Target.Text = lv.SelectedItems[0].SubItems[2].Text;

                        } break;

                    case eEditType.edittype_ImportUpdateDiffTable:
                        {

                        } break;

                    case eEditType.edittype_ReadLayout:
                        {

                        } break;

                    default:
                        {
                            return;
                        }
                }

                

                buttonModify.Enabled = true;
            }
        }

        private void buttonModify_Click(object sender, EventArgs e)
        {
            

            XlsData data;
            bool bGetTChineseFileOk = mDataMgr_TChinese.mXls.TryGetValue(mSelectFileName, out data);
            if(bGetTChineseFileOk)
            {
                int nReallyIndex = int.Parse(mSelectValueText);
                string sValue = data.mData.ElementAt(nReallyIndex);
                if(sValue != textBox_Target.Text)
                {
                    data.mData[nReallyIndex] = textBox_Target.Text;

                    string sOldText = listView1.Items[mSelectValueIndex].SubItems[2].Text;
                    listView1.BeginUpdate();
                    listView1.Items[mSelectValueIndex].SubItems[2].Text = textBox_Target.Text;
                    listView1.EndUpdate();

                    Info("修改数据:->" + mPathTChineseVersion + "\\" + mSelectFileName + "== (" + sOldText + ")->(" + textBox_Target.Text + ")");


                    mDataMgr_TChinese.saveXlsFileForNewValue(mPathTChineseVersion + "\\" + mSelectFileName, nReallyIndex, textBox_Target.Text);
                }
            }
        }


        private void label1_Click(object sender, EventArgs e)
        {
            string sTempPath = GetSelectedFolderPath("请选择中文版本路径", mPathSChineseVersion);
            if (sTempPath == "")
                return;

            mPathSChineseVersion = sTempPath;
            label1.Text = mPathSChineseVersion;
            mOptionIni.Find("SChineseVersionPath").Find("Path").SetValue(mPathSChineseVersion);
            mOptionIni.WriteToFile();
        }

        private void label2_Click(object sender, EventArgs e)
        {
            string sTempPath = GetSelectedFolderPath("请选择目标版本路径", mPathTChineseVersion);
            if (sTempPath == "")
                return;

            mPathTChineseVersion = sTempPath;
            label2.Text = mPathTChineseVersion;
            mOptionIni.Find("TChineseVersionPath").Find("Path").SetValue(mPathTChineseVersion);
            mOptionIni.WriteToFile();
        }

        private void funcExportAllImpl()
        {
            v.SetProgressMax(110);
            v.SetProgress(0);
            int nLoadNum = 0;
            int nIndexNum = 0;

            NPOI.XSSF.UserModel.XSSFWorkbook HSSFWB = new NPOI.XSSF.UserModel.XSSFWorkbook();

            NPOI.SS.UserModel.ISheet nameindexSheet = HSSFWB.CreateSheet("nameindex");

            int nChineseFileCount = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_SChinese.mXls.Keys;
            foreach (string name in ids)
            {
                string tempfilename = name;
                if (name == "")
                {
                    tempfilename = "无名文件";
                    Error("源中发现有文件名为空！！！");
                }


                XlsData data_s, data_t;//,data_k,data_e;
                bool bGetSChineseFileOk = mDataMgr_SChinese.mXls.TryGetValue(name, out data_s);
                bool bGetTChineseFileOk = mDataMgr_TChinese.mXls.TryGetValue(name, out data_t);

                if (bGetSChineseFileOk && bGetTChineseFileOk)
                {
                    // 源和目标都有对应文件，可以写入
                    int nValueCount = 0;
                    bool bHasCreateSheet = false;
                    NPOI.SS.UserModel.ISheet curSheet = null;
                    for (int i = data_s.colCount; i < data_s.mData.Count; ++i)
                    {
                        string sValue = data_s.mData.ElementAt(i);
                        for (int j = 0; j < sValue.Length; ++j)
                        {
                            if ((int)sValue[j] > 127)
                            {
                                // try create sheet..
                                if (bHasCreateSheet == false)
                                {
                                    curSheet = HSSFWB.CreateSheet(nLoadNum+"");
                                    bHasCreateSheet = true;
                                }
                                // is not ascii
                                string sTargetValue = "";
                                if (i >= data_t.mData.Count)// 源的数据比目标多。。
                                {
                                    sTargetValue = "目标缺失";
                                    Error("目标版本中缺行数据:->" + sValue);
                                }
                                else
                                {
                                    sTargetValue = data_t.mData.ElementAt(i);
                                }


                                // write to
                                NPOI.SS.UserModel.IRow row = curSheet.CreateRow(nValueCount);
                                NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                                cell.SetCellValue(i);
                                cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                                cell.SetCellValue(sValue);
                                cell = row.CreateCell(2, NPOI.SS.UserModel.CellType.String);
                                cell.SetCellValue(sTargetValue);

                                nValueCount++;
                                break;
                            }
                        }
                    }

                    {
                        // index to name
                        NPOI.SS.UserModel.IRow row = nameindexSheet.CreateRow(nIndexNum++);
                        NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                        cell.SetCellValue(nLoadNum);
                        cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(tempfilename);
                    }
                }
                else
                {
                    if (bGetTChineseFileOk == false)
                    {
                        // 目标中未找到文件
                        Error("目标版本中缺文件:->" + name);
                    }
                    else
                    {
                        // 源中没有对应文件，不可能。
                        Error("源版本中缺文件:->" + name);
                    }
                }
                v.SetProgress((int)((++nLoadNum) * 100 / mDataMgr_SChinese.mXls.Keys.Count));
            }
            Info("已处理文件数:" + nChineseFileCount);

            int[] datearray = { DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, DateTime.Now.Hour, DateTime.Now.Minute, DateTime.Now.Second };
            string fullFileName = mExportAllFileName + "\\exportalltable_" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + datearray[4] + datearray[5] + ".xlsx";
            using (FileStream FSO = new FileStream(fullFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
            }
            v.SetProgress(110);
            OpenBtn();
        }

        private void button_ExportAll_Click(object sender, EventArgs e)
        {
            string sTempPath = GetSelectedFolderPath("选择要导出的路径", mPathTChineseVersion);
            if (sTempPath == "")
            {
                return;
            }

            mExportAllFileName = sTempPath;

            Info("导出开始");
            Thread thread = new Thread(new ThreadStart(funcExportAllImpl));
            thread.Start();
            CloseBtn();

        }

        private void ImportDataFileImpl()
        {
            SetProgressMax(100);
            v.SetProgress(0);

            Clear_SChinese_FileList();
            //mDataMgr_SChinese.Clear();
            //mDataMgr_TChinese.Clear();

            mDataMgr_Import.Clear();
            if(false == mDataMgr_Import.loadMultiPageXlsFile(label_InputFile.Text))
            {
                OpenBtn();
                return;
            }

            v.SetProgress(10);

            int nImportFileCount = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_Import.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr_Import.mXls.TryGetValue(name, out data))
                {
                    Output_SChinese_FileList(name);
                    nImportFileCount++;
                }
                v.SetProgress(10+(int)(nImportFileCount * 90 / mDataMgr_Import.mXls.Keys.Count));
            }
            Set_SChinese_FileState("导入的数据表数量:" + nImportFileCount);

            v.mEditType = eEditType.edittype_ImportUpdateDiffTable;
            v.Info("导入完成");

            OpenBtn();
        }
        private void label_InputFile_Click(object sender, EventArgs e)
        {
            string sTempPath = GetSelectedFilePath("请选择要导入的数据表", mPathTChineseVersion);
            if (sTempPath == "")
                return;
            label_InputFile.Text = sTempPath;
            mPathImportFileName = sTempPath;

            mOptionIni.Find("ImportPath").Find("Path").SetValue(mPathImportFileName);
            mOptionIni.WriteToFile();

            button_ExportAll.Enabled = false;
            

        }

        private void ApplyImportDataImpl()
        {
            v.SetProgressMax(100);
            v.SetProgress(0);
            int nLoadNum = 0;

            int nApplyImportFileCount = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_Import.mXls.Keys;
            foreach (string name in ids)
            {
                v.SetProgress((int)((++nLoadNum) * 100 / mDataMgr_Import.mXls.Keys.Count));

                string tempfilename = name;
                int nFlagPos = tempfilename.IndexOf("+");
                while (nFlagPos >= 0)
                {
                    tempfilename = tempfilename.Replace("+", "\\");
                    nFlagPos = tempfilename.IndexOf("+");
                }

                XlsData data;
                bool bFindImport = mDataMgr_Import.mXls.TryGetValue(name, out data);
                XlsData data_old;
                bool bFindOld = mDataMgr_TChinese.mXls.TryGetValue(tempfilename, out data_old);
                if (!bFindImport)
                {
                    v.Error("导入表中找不到文件:" + tempfilename);
                    continue;
                }
                if (!bFindOld)
                {
                    v.Error("目标目录找不到文件:" + tempfilename);
                    continue;
                }

                for(int i = 0; i < data.mData.Count; i+=3)
                {
                    int nValue1 = int.Parse(data.mData.ElementAt(i));
                    string sValue2 = data.mData.ElementAt(i+1);
                    string sValue3 = data.mData.ElementAt(i+2);

                    if(nValue1 >= data_old.mData.Count)
                    {
                        v.Error("目标文件中无对应项:" + sValue2 + ":" + sValue3);
                        continue;
                    }
                    string oldValue = data_old.mData.ElementAt(nValue1);

                    if (sValue3 != oldValue && oldValue != "")
                    {
                        // find different,write this...

                        //data.mData[nReallyIndex] = textBox_Target.Text;

                        //string sOldText = listView1.Items[mSelectValueIndex].SubItems[2].Text;
                        //listView1.BeginUpdate();
                        //listView1.Items[mSelectValueIndex].SubItems[2].Text = textBox_Target.Text;
                        //listView1.EndUpdate();

                        Info("修改文件:->" + mPathTChineseVersion + "\\" + mSelectFileName + "== (" + oldValue + ")->(" + sValue3 + ")");


                        mDataMgr_Import.saveXlsFileForNewValue(mPathTChineseVersion + "\\" + tempfilename, nValue1, sValue3);

                        nApplyImportFileCount++;
                    }
                }
                
            }


            v.Info("导入完成");

            OpenBtn();
        }

        private void button_ApplyImport_Click(object sender, EventArgs e)
        {
            Info("开始应用导入的数据表");
            CloseBtn();

            Thread thread = new Thread(new ThreadStart(ApplyImportDataImpl));
            thread.Start();
        }

        private void button_ReadTarget_Click(object sender, EventArgs e)
        {
            Info("开始加载目标语言版数据");
            
            Thread thread = new Thread(new ThreadStart(LoadConfigDataImple_Target));
            thread.Start();
            CloseBtn();
        }

        private void button_Import_Click(object sender, EventArgs e)
        {
            Info("导入源目标差异表开始");

            
            Thread thread = new Thread(new ThreadStart(ImportDataFileImpl));
            thread.Start();
            CloseBtn();
        }

        private void outputSourceDataImpl()
        {
            SetProgressMax(110);
            v.SetProgress(0);

            NPOI.XSSF.UserModel.XSSFWorkbook HSSFWB = new NPOI.XSSF.UserModel.XSSFWorkbook();

            NPOI.SS.UserModel.ISheet nameindexSheet = HSSFWB.CreateSheet("nameindex");

            int nLoadNum = 0;
            int nIndexNum = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_SChinese.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr_SChinese.mXls.TryGetValue(name, out data))
                {
                    string tempfilename = name;
                    if (name == "")
                    {
                        tempfilename = "无名文件";
                        Error("源中发现有文件名为空！！！");
                    }

                    bool bHasCreateSheet = false;
                    NPOI.SS.UserModel.ISheet curSheet = null;
                    int ValueCount = 0;
                    // 从第一行数据开始
                    for (int i = data.colCount; i < data.mData.Count; ++i)
                    {
                        string sValue = data.mData.ElementAt(i);
                        for (int j = 0; j < sValue.Length; ++j)
                        {
                            if ((int)sValue[j] > 127)
                            {
                                // is not ascii
                                // 加入一个数据
                                if (bHasCreateSheet == false)
                                {
                                    curSheet = HSSFWB.CreateSheet(nLoadNum+"");
                                    bHasCreateSheet = true;
                                }
                                NPOI.SS.UserModel.IRow row = curSheet.CreateRow(ValueCount);
                                NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                                cell.SetCellValue(i);
                                cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                                cell.SetCellValue(sValue);

                                ValueCount++;
                                break;
                            }
                        }
                    }

                    {
                        // index to name
                        NPOI.SS.UserModel.IRow row = nameindexSheet.CreateRow(nIndexNum++);
                        NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                        cell.SetCellValue(nLoadNum);
                        cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(tempfilename);
                    }
                }
                v.SetProgress((int)((++nLoadNum) * 100 / (mDataMgr_SChinese.mXls.Keys.Count)));
            }

            int[] datearray = { DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, DateTime.Now.Hour, DateTime.Now.Minute, DateTime.Now.Second };
            string fullFileName = mExportSourceFileName + "\\exportsourcetable_" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + datearray[4] + datearray[5] + ".xlsx";
            using (FileStream FSO = new FileStream(fullFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
                v.SetProgress(110);
            }

            v.Info("导出源文件夹中含有中文的表完成");
            OpenBtn();
        }
        private void button1_Click_1(object sender, EventArgs e)
        {
            if (mDataMgr_SChinese.GetFileCount() <= 0)
            {
                Info("未读取源文件夹中的数据");
                return;
            }

            string sTempPath = GetSelectedFolderPath("选择要导出的路径", mPathSChineseVersion);
            if (sTempPath == "")
            {
                return;
            }
            mExportSourceFileName = sTempPath;


            Info("导出源文件夹中含有中文的表开始");
            Thread thread = new Thread(new ThreadStart(outputSourceDataImpl));
            thread.Start();
            CloseBtn();
        }
        private void outputTargetDataImpl()
        {
            SetProgressMax(110);
            v.SetProgress(0);

            NPOI.XSSF.UserModel.XSSFWorkbook HSSFWB = new NPOI.XSSF.UserModel.XSSFWorkbook();

            NPOI.SS.UserModel.ISheet nameindexSheet = HSSFWB.CreateSheet("nameindex");
            

            int nLoadNum = 0;
            int nIndexNum = 0;
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_TChinese.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr_TChinese.mXls.TryGetValue(name, out data))
                {
                    string tempfilename = name;
                    if (name == "")
                    {
                        tempfilename = "无名文件";
                        Error("目标中发现有文件名为空！！！");
                    }

                    bool bHasCreateSheet = false;
                    NPOI.SS.UserModel.ISheet curSheet = null;
                    int ValueCount = 0;
                    // 从第一行数据开始
                    for (int i = data.colCount; i < data.mData.Count; ++i)
                    {
                        string sValue = data.mData.ElementAt(i);
                        for (int j = 0; j < sValue.Length; ++j)
                        {
                            if ((int)sValue[j] > 127)
                            {
                                // is not ascii
                                // 加入一个数据
                                if (bHasCreateSheet == false)
                                {
                                    curSheet = HSSFWB.CreateSheet(nLoadNum+"");
                                    bHasCreateSheet = true;
                                }

                                NPOI.SS.UserModel.IRow row = curSheet.CreateRow(ValueCount);
                                NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                                cell.SetCellValue(i);
                                cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                                cell.SetCellValue(sValue);

                                ValueCount++;
                                break;
                            }
                        }
                    }

                    if (bHasCreateSheet)
                    {
                        // index to name
                        NPOI.SS.UserModel.IRow row = nameindexSheet.CreateRow(nIndexNum++);
                        NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                        cell.SetCellValue(nLoadNum);
                        cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(tempfilename);
                    }

                }
                v.SetProgress((int)((++nLoadNum) * 100 / (mDataMgr_TChinese.mXls.Keys.Count)));
            }

            int[] datearray = { DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, DateTime.Now.Hour, DateTime.Now.Minute, DateTime.Now.Second };
            string fullFileName = mExportTargetFileName + "\\exporttargettable_" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + datearray[4] + datearray[5] + ".xlsx";
            using (FileStream FSO = new FileStream(fullFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
                v.SetProgress(110);
            }

            v.Info("导出目标文件夹中含有中文的表完成");
            OpenBtn();
        }
        private void button2_Click(object sender, EventArgs e)
        {
            if (mDataMgr_TChinese.GetFileCount() <= 0)
            {
                Info("未读取目标文件夹中的数据");
                return;
            }

            string sTempPath = GetSelectedFolderPath("选择要导出的路径", mPathTChineseVersion);
            if (sTempPath == "")
            {
                return;
            }
            mExportTargetFileName = sTempPath;

            Info("导出目标文件夹含有中文的表开始");
            Thread thread = new Thread(new ThreadStart(outputTargetDataImpl));
            thread.Start();
            CloseBtn();
        }

        private void ImportTargetDataAndApplyImpl()
        {
            SetProgressMax(100);
            v.SetProgress(0);
            int nLoadNum = 0;

            mDataMgr_ImportTarget.Clear();
            if(false == mDataMgr_ImportTarget.loadMultiPageXlsFile(mPathImportTargetFileName))
            {
                OpenBtn();
                return;
            }
            v.SetProgress(10);


            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_ImportTarget.mXls.Keys;
            foreach (string name in ids)
            {
                v.SetProgress((int)((++nLoadNum) * 100 / mDataMgr_ImportTarget.mXls.Keys.Count));

                string tempfilename = name;
                

                XlsData data;
                bool bFindImport = mDataMgr_ImportTarget.mXls.TryGetValue(name, out data);
                XlsData data_old;
                bool bFindOld = mDataMgr_TChinese.mXls.TryGetValue(tempfilename, out data_old);
                if (!bFindImport)
                {
                    v.Error("导入表中找不到文件:" + tempfilename);
                    continue;
                }
                if (!bFindOld)
                {
                    v.Error("目标目录找不到文件:" + tempfilename);
                    continue;
                }

                int nModifyNum = 0;
                for (int i = 0; i < data.mData.Count; i += 2)
                {
                    int nValue1 = int.Parse(data.mData.ElementAt(i));
                    string sValue2 = data.mData.ElementAt(i + 1);

                    if (nValue1 >= data_old.mData.Count)
                    {
                        v.Error("目标文件中无对应项:" + nValue1+":"+sValue2);
                        continue;
                    }
                    string oldValue = data_old.mData.ElementAt(nValue1);

                    if (sValue2 != oldValue && oldValue != "")
                    {
                        Info("修改目标文件:->" + mPathTChineseVersion + "\\" + tempfilename + "== (" + oldValue + ")->(" + sValue2 + ")");


                        mDataMgr_Import.saveXlsFileForNewValue(mPathTChineseVersion + "\\" + tempfilename, nValue1, sValue2);

                        nModifyNum++;
                    }
                }
                if (nModifyNum > 0)
                { 
                    Info("目标文件:->" + mPathTChineseVersion + "\\" + tempfilename + "---应用修改的条数:== (" + nModifyNum + ")"); 
                }
                

            }


            v.Info("导入数据并重写到目标文件夹完成");

            OpenBtn();
        }
        private void button4_Click(object sender, EventArgs e)
        {
            if (mDataMgr_TChinese.GetFileCount() <= 0)
            {
                Info("未读取目标文件夹中的数据");
                return;
            }

            string sTempPath = GetSelectedFilePath("请选择要导入的数据表", mPathTChineseVersion);
            if (sTempPath == "")
                return;
            mPathImportTargetFileName = sTempPath;

            Info("导入数据并重写到目标文件夹开始");
            Thread thread = new Thread(new ThreadStart(ImportTargetDataAndApplyImpl));
            thread.Start();
            CloseBtn();
        }

        private void label7_Click(object sender, EventArgs e)
        {
            string sTempPath = GetSelectedFolderPath("请选择界面布局路径", mPathLayout);
            if (sTempPath == "")
                return;

            mPathLayout = sTempPath;
            label_LayoutPath.Text = mPathLayout;

            mOptionIni.FindAndAddApp("LayoutPath", "Path", mPathLayout,true);
            mOptionIni.WriteToFile();
        }


        private void LoadAllLayoutDataImpl()
        {
            SetProgressMax(120);
            v.SetProgress(0);

            List<string> files = v.GetFiles(mPathLayout, "*.layout");
            mDataMgr_Layout.Clear();
            int nLoadNum = 0;
            for (int i = 0; i < files.Count; i++)
            {
                if (!mDataMgr_Layout.loadXmlFileAndParseCKJText(files[i]))
                {

                }
                v.SetProgress((int)((++nLoadNum) * 100 / (files.Count)));
            }


            v.Info("界面布局加载完成");

            Clear_SChinese_FileList();
            nLoadNum = 0;
            int nChineseFileCount = 0;
            Dictionary<string, xmlData>.KeyCollection ids = mDataMgr_Layout.mXml.Keys;
            foreach (string name in ids)
            {
                Output_SChinese_FileList(name);
                nChineseFileCount++;

                v.SetProgress(100 + (int)((++nLoadNum) * 20 / (mDataMgr_Layout.mXml.Keys.Count)));
            }
            Set_SChinese_FileState("含有中文的界面数量:" + nChineseFileCount);



            v.mEditType = eEditType.edittype_ReadLayout;

            OpenBtn();
        }

        private void button_ReadLayout_Click(object sender, EventArgs e)
        {
            Info("读入界面布局数据开始");

            
            Thread thread = new Thread(new ThreadStart(LoadAllLayoutDataImpl));
            thread.Start();
            CloseBtn();
        }


        private void ExportLayoutCJKDataImpl()
        {
            SetProgressMax(110);
            v.SetProgress(0);


            NPOI.XSSF.UserModel.XSSFWorkbook HSSFWB = new NPOI.XSSF.UserModel.XSSFWorkbook();

            NPOI.SS.UserModel.ISheet nameindexSheet = HSSFWB.CreateSheet("nameindex");


            int nLoadNum = 0;
            int nIndexNum = 0;
            Dictionary<string, xmlData>.KeyCollection ids = mDataMgr_Layout.mXml.Keys;
            foreach (string name in ids)
            {
                xmlData data;
                if (mDataMgr_Layout.mXml.TryGetValue(name, out data))
                {
                    string tempfilename = name;

                    bool bHasCreateSheet = false;
                    NPOI.SS.UserModel.ISheet curSheet = null;
                    int ValueCount = 0;
                    // 从第一行数据开始
                    for (int i = 0; i < data.mPath2Value.Count; i++)
                    {
                        string sKey = data.mPath2Value.ElementAt(i).Key;
                        string sValue = data.mPath2Value.ElementAt(i).Value;
                       
                        // 加入一个数据
                        if (bHasCreateSheet == false)
                        {
                            curSheet = HSSFWB.CreateSheet(nLoadNum + "");
                            bHasCreateSheet = true;
                        }

                        NPOI.SS.UserModel.IRow row = curSheet.CreateRow(ValueCount);
                        NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(sKey);
                        cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(sValue);

                        ValueCount++;
                    }

                    if (bHasCreateSheet)
                    {
                        // index to name
                        NPOI.SS.UserModel.IRow row = nameindexSheet.CreateRow(nIndexNum++);
                        NPOI.SS.UserModel.ICell cell = row.CreateCell(0, NPOI.SS.UserModel.CellType.Numeric);
                        cell.SetCellValue(nLoadNum);
                        cell = row.CreateCell(1, NPOI.SS.UserModel.CellType.String);
                        cell.SetCellValue(tempfilename);
                    }

                }
                v.SetProgress((int)((++nLoadNum) * 100 / (mDataMgr_Layout.mXml.Keys.Count)));
            }

            int[] datearray = { DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, DateTime.Now.Hour, DateTime.Now.Minute, DateTime.Now.Second };
            string fullFileName = mExportLayoutFileName + "\\exportlayouttable_" + datearray[0] + datearray[1] + datearray[2] + datearray[3] + datearray[4] + datearray[5] + ".xlsx";
            using (FileStream FSO = new FileStream(fullFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
                v.SetProgress(110);
            }

            v.Info("导出目标文件夹中CJK数据完成");
            OpenBtn();
        }


        private void button_ExportLayout_Click(object sender, EventArgs e)
        {
            if (mDataMgr_Layout.GetFileCount() <= 0)
            {
                Info("未读取界面布局文件夹中的数据");
                return;
            }

            string sTempPath = GetSelectedFolderPath("选择要导出的路径", mExportLayoutFileName);
            if (sTempPath == "")
            {
                return;
            }
            mExportLayoutFileName = sTempPath;

            Info("导出界面布局中CJK数据开始");
            Thread thread = new Thread(new ThreadStart(ExportLayoutCJKDataImpl));
            thread.Start();
            CloseBtn();
        }
        private void ImportLayoutDataAndApplyImpl()
        {
            SetProgressMax(100);
            v.SetProgress(0);
            int nLoadNum = 0;

            mDataMgr_LayoutImport.Clear();
            if(false == mDataMgr_LayoutImport.loadMultiPageXlsFile(mPathImportLayoutFileName))
            {
                OpenBtn();
                return;
            }
            v.SetProgress(10);


            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr_LayoutImport.mXls.Keys;
            foreach (string name in ids)
            {
                v.SetProgress((int)((++nLoadNum) * 100 / mDataMgr_LayoutImport.mXls.Keys.Count));

                string tempfilename = name;


                xmlData data;
                if (!mDataMgr_Layout.mXml.TryGetValue(name, out data))
                {
                    continue;
                }
                XlsData importdata;
                if (!mDataMgr_LayoutImport.mXls.TryGetValue(name, out importdata))
                {
                    continue;
                }
                int nModifyNum = 0;

                Dictionary<string, string> pNeedModifyData = new Dictionary<string, string>();
                for (int i = 0; i < importdata.mData.Count; i+=2)
                {
                    string spathname = importdata.mData[i].ToString();
                    string sValue = importdata.mData[i+1].ToString();

                    string oldValue;
                    if(data.mPath2Value.TryGetValue(spathname,out oldValue))
                    {
                        if (oldValue != sValue)
                        {
                            data.mPath2Value[spathname] = sValue;
                            pNeedModifyData.Add(spathname, sValue);
                            v.Info("修改布局文件:->" + name + "== (" + oldValue + ")->(" + sValue + ")");
                            //mDataMgr_LayoutImport.saveXmlFileForNewValue(name, spathname, sValue);
                            nModifyNum++;
                        }
                    }
                }
                if (pNeedModifyData.Count > 0)
                {
                    mDataMgr_LayoutImport.saveXmlFileForNewValue(name, pNeedModifyData);
                    v.Info("目标文件:->" + name + "---应用修改的条数:== (" + nModifyNum + ")");
                }
                
            }


            v.Info("导入数据并重写到目标文件夹完成");

            OpenBtn();
        }
        private void button_ImportApplyLayout_Click(object sender, EventArgs e)
        {
            if (mDataMgr_Layout.GetFileCount() <= 0)
            {
                Info("未读取布局文件夹中的数据");
                return;
            }

            string sTempPath = GetSelectedFilePath("请选择要导入的数据表", mPathLayout);
            if (sTempPath == "")
                return;
            mPathImportLayoutFileName = sTempPath;

            Info("导入数据并重写到布局文件夹开始");
            Thread thread = new Thread(new ThreadStart(ImportLayoutDataAndApplyImpl));
            thread.Start();
            CloseBtn();
        }

        private void v_ClientSizeChanged(object sender, EventArgs e)
        {
            //Info("v_ClientSizeChanged:->"+this.Size.ToString());

            
        }

        private void v_ResizeEnd(object sender, EventArgs e)
        {
            float widthrate = (float)Size.Width / mDialogOldWidth;
            float heightrate = (float)Size.Height / mDialogOldHeight;

            List<System.Windows.Forms.Control> pResizeCtrls = new List<System.Windows.Forms.Control>();
            pResizeCtrls.Add(listBox_FileList);
            pResizeCtrls.Add(listView1);
            pResizeCtrls.Add(listBox_output);
            pResizeCtrls.Add(progressBar1);
            pResizeCtrls.Add(label_FileState);
            pResizeCtrls.Add(label_ValueState);

            for (int i = 0; i < pResizeCtrls.Count; ++i)
            {
                pResizeCtrls[i].Width = (int)(pResizeCtrls[i].Width * widthrate);
                pResizeCtrls[i].Height = (int)(pResizeCtrls[i].Height * heightrate);
                pResizeCtrls[i].Left = (int)(pResizeCtrls[i].Left * widthrate);
                pResizeCtrls[i].Top = (int)(pResizeCtrls[i].Top * heightrate);
            }


            mDialogOldWidth = Size.Width;
            mDialogOldHeight = Size.Height;
        }
    }
}
