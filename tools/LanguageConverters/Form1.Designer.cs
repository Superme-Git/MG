using System.Collections.Generic;


namespace LanguageConverters
{
    partial class v
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.listView1 = new System.Windows.Forms.ListView();
            this.listBox_output = new System.Windows.Forms.ListBox();
            this.button_ReadSource = new System.Windows.Forms.Button();
            this.listBox_FileList = new System.Windows.Forms.ListBox();
            this.textBox_SChinese = new System.Windows.Forms.TextBox();
            this.textBox_Target = new System.Windows.Forms.TextBox();
            this.buttonModify = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
            this.toolTip2 = new System.Windows.Forms.ToolTip(this.components);
            this.label_FileState = new System.Windows.Forms.Label();
            this.label_ValueState = new System.Windows.Forms.Label();
            this.button_ExportAll = new System.Windows.Forms.Button();
            this.label5 = new System.Windows.Forms.Label();
            this.label_InputFile = new System.Windows.Forms.Label();
            this.toolTip3 = new System.Windows.Forms.ToolTip(this.components);
            this.button_ApplyImport = new System.Windows.Forms.Button();
            this.button_Import = new System.Windows.Forms.Button();
            this.button_ReadTarget = new System.Windows.Forms.Button();
            this.progressBar1 = new System.Windows.Forms.ProgressBar();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.button4 = new System.Windows.Forms.Button();
            this.label6 = new System.Windows.Forms.Label();
            this.label_LayoutPath = new System.Windows.Forms.Label();
            this.button_ImportApplyLayout = new System.Windows.Forms.Button();
            this.button_ExportLayout = new System.Windows.Forms.Button();
            this.button_ReadLayout = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // listView1
            // 
            this.listView1.Location = new System.Drawing.Point(361, 227);
            this.listView1.Margin = new System.Windows.Forms.Padding(0);
            this.listView1.MultiSelect = false;
            this.listView1.Name = "listView1";
            this.listView1.Size = new System.Drawing.Size(441, 268);
            this.listView1.TabIndex = 0;
            this.listView1.UseCompatibleStateImageBehavior = false;
            this.listView1.SelectedIndexChanged += new System.EventHandler(this.listView1_SelectedIndexChanged);
            this.listView1.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.listView1_MouseDoubleClick);
            // 
            // listBox_output
            // 
            this.listBox_output.FormattingEnabled = true;
            this.listBox_output.ItemHeight = 12;
            this.listBox_output.Location = new System.Drawing.Point(3, 505);
            this.listBox_output.Margin = new System.Windows.Forms.Padding(0);
            this.listBox_output.Name = "listBox_output";
            this.listBox_output.Size = new System.Drawing.Size(799, 160);
            this.listBox_output.TabIndex = 1;
            // 
            // button_ReadSource
            // 
            this.button_ReadSource.Location = new System.Drawing.Point(140, 4);
            this.button_ReadSource.Name = "button_ReadSource";
            this.button_ReadSource.Size = new System.Drawing.Size(40, 23);
            this.button_ReadSource.TabIndex = 2;
            this.button_ReadSource.Text = "读取";
            this.button_ReadSource.UseVisualStyleBackColor = true;
            this.button_ReadSource.Click += new System.EventHandler(this.button1_Click);
            // 
            // listBox_FileList
            // 
            this.listBox_FileList.FormattingEnabled = true;
            this.listBox_FileList.ItemHeight = 12;
            this.listBox_FileList.Location = new System.Drawing.Point(3, 227);
            this.listBox_FileList.Margin = new System.Windows.Forms.Padding(0);
            this.listBox_FileList.Name = "listBox_FileList";
            this.listBox_FileList.Size = new System.Drawing.Size(352, 268);
            this.listBox_FileList.TabIndex = 3;
            this.listBox_FileList.SelectedIndexChanged += new System.EventHandler(this.listBox_FileList_SelectedIndexChanged);
            // 
            // textBox_SChinese
            // 
            this.textBox_SChinese.Location = new System.Drawing.Point(444, 4);
            this.textBox_SChinese.Multiline = true;
            this.textBox_SChinese.Name = "textBox_SChinese";
            this.textBox_SChinese.Size = new System.Drawing.Size(325, 52);
            this.textBox_SChinese.TabIndex = 5;
            // 
            // textBox_Target
            // 
            this.textBox_Target.Location = new System.Drawing.Point(444, 62);
            this.textBox_Target.Multiline = true;
            this.textBox_Target.Name = "textBox_Target";
            this.textBox_Target.Size = new System.Drawing.Size(325, 52);
            this.textBox_Target.TabIndex = 6;
            // 
            // buttonModify
            // 
            this.buttonModify.Location = new System.Drawing.Point(772, 62);
            this.buttonModify.Name = "buttonModify";
            this.buttonModify.Size = new System.Drawing.Size(38, 36);
            this.buttonModify.TabIndex = 7;
            this.buttonModify.Text = "修改";
            this.buttonModify.UseVisualStyleBackColor = true;
            this.buttonModify.Click += new System.EventHandler(this.buttonModify_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 30);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(113, 12);
            this.label1.TabIndex = 11;
            this.label1.Text = "请选择中文版本路径";
            this.label1.Click += new System.EventHandler(this.label1_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(11, 76);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(113, 12);
            this.label2.TabIndex = 12;
            this.label2.Text = "请选择目标版本路径";
            this.label2.Click += new System.EventHandler(this.label2_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label3.Location = new System.Drawing.Point(12, 9);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(122, 12);
            this.label3.TabIndex = 13;
            this.label3.Text = "中文表格版本路径：";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label4.Location = new System.Drawing.Point(11, 55);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(122, 12);
            this.label4.TabIndex = 14;
            this.label4.Text = "目标版本版本路径：";
            // 
            // label_FileState
            // 
            this.label_FileState.AutoSize = true;
            this.label_FileState.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label_FileState.Location = new System.Drawing.Point(1, 212);
            this.label_FileState.Margin = new System.Windows.Forms.Padding(0);
            this.label_FileState.Name = "label_FileState";
            this.label_FileState.Size = new System.Drawing.Size(89, 12);
            this.label_FileState.TabIndex = 15;
            this.label_FileState.Text = "目标版本路径：";
            // 
            // label_ValueState
            // 
            this.label_ValueState.AutoSize = true;
            this.label_ValueState.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label_ValueState.Location = new System.Drawing.Point(359, 212);
            this.label_ValueState.Margin = new System.Windows.Forms.Padding(0);
            this.label_ValueState.Name = "label_ValueState";
            this.label_ValueState.Size = new System.Drawing.Size(89, 12);
            this.label_ValueState.TabIndex = 16;
            this.label_ValueState.Text = "目标版本路径：";
            // 
            // button_ExportAll
            // 
            this.button_ExportAll.Location = new System.Drawing.Point(319, 9);
            this.button_ExportAll.Name = "button_ExportAll";
            this.button_ExportAll.Size = new System.Drawing.Size(36, 61);
            this.button_ExportAll.TabIndex = 17;
            this.button_ExportAll.Text = "导出对比";
            this.button_ExportAll.UseVisualStyleBackColor = true;
            this.button_ExportAll.Visible = false;
            this.button_ExportAll.Click += new System.EventHandler(this.button_ExportAll_Click);
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label5.Location = new System.Drawing.Point(10, 102);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(109, 12);
            this.label5.TabIndex = 18;
            this.label5.Text = "导入修改的表格：";
            this.label5.Visible = false;
            // 
            // label_InputFile
            // 
            this.label_InputFile.AutoSize = true;
            this.label_InputFile.Location = new System.Drawing.Point(12, 125);
            this.label_InputFile.Name = "label_InputFile";
            this.label_InputFile.Size = new System.Drawing.Size(113, 12);
            this.label_InputFile.TabIndex = 19;
            this.label_InputFile.Text = "请选择导入版本路径";
            this.label_InputFile.Visible = false;
            this.label_InputFile.Click += new System.EventHandler(this.label_InputFile_Click);
            // 
            // button_ApplyImport
            // 
            this.button_ApplyImport.Location = new System.Drawing.Point(186, 97);
            this.button_ApplyImport.Name = "button_ApplyImport";
            this.button_ApplyImport.Size = new System.Drawing.Size(44, 23);
            this.button_ApplyImport.TabIndex = 20;
            this.button_ApplyImport.Text = "应用";
            this.button_ApplyImport.UseVisualStyleBackColor = true;
            this.button_ApplyImport.Visible = false;
            this.button_ApplyImport.Click += new System.EventHandler(this.button_ApplyImport_Click);
            // 
            // button_Import
            // 
            this.button_Import.Location = new System.Drawing.Point(140, 97);
            this.button_Import.Name = "button_Import";
            this.button_Import.Size = new System.Drawing.Size(40, 23);
            this.button_Import.TabIndex = 21;
            this.button_Import.Text = "导入";
            this.button_Import.UseVisualStyleBackColor = true;
            this.button_Import.Visible = false;
            this.button_Import.Click += new System.EventHandler(this.button_Import_Click);
            // 
            // button_ReadTarget
            // 
            this.button_ReadTarget.Location = new System.Drawing.Point(140, 50);
            this.button_ReadTarget.Name = "button_ReadTarget";
            this.button_ReadTarget.Size = new System.Drawing.Size(40, 23);
            this.button_ReadTarget.TabIndex = 22;
            this.button_ReadTarget.Text = "读取";
            this.button_ReadTarget.UseVisualStyleBackColor = true;
            this.button_ReadTarget.Click += new System.EventHandler(this.button_ReadTarget_Click);
            // 
            // progressBar1
            // 
            this.progressBar1.Location = new System.Drawing.Point(3, 671);
            this.progressBar1.Margin = new System.Windows.Forms.Padding(0);
            this.progressBar1.Name = "progressBar1";
            this.progressBar1.Size = new System.Drawing.Size(799, 13);
            this.progressBar1.TabIndex = 23;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(186, 4);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(48, 23);
            this.button1.TabIndex = 24;
            this.button1.Text = "导出";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click_1);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(186, 50);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(48, 23);
            this.button2.TabIndex = 25;
            this.button2.Text = "导出";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // button4
            // 
            this.button4.Location = new System.Drawing.Point(236, 50);
            this.button4.Name = "button4";
            this.button4.Size = new System.Drawing.Size(75, 23);
            this.button4.TabIndex = 27;
            this.button4.Text = "导入并重写";
            this.button4.UseVisualStyleBackColor = true;
            this.button4.Click += new System.EventHandler(this.button4_Click);
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label6.Location = new System.Drawing.Point(12, 157);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(96, 12);
            this.label6.TabIndex = 28;
            this.label6.Text = "界面布局路径：";
            // 
            // label_LayoutPath
            // 
            this.label_LayoutPath.AutoSize = true;
            this.label_LayoutPath.Location = new System.Drawing.Point(12, 183);
            this.label_LayoutPath.Name = "label_LayoutPath";
            this.label_LayoutPath.Size = new System.Drawing.Size(113, 12);
            this.label_LayoutPath.TabIndex = 29;
            this.label_LayoutPath.Text = "请选择界面布局路径";
            this.label_LayoutPath.Click += new System.EventHandler(this.label7_Click);
            // 
            // button_ImportApplyLayout
            // 
            this.button_ImportApplyLayout.Location = new System.Drawing.Point(236, 152);
            this.button_ImportApplyLayout.Name = "button_ImportApplyLayout";
            this.button_ImportApplyLayout.Size = new System.Drawing.Size(75, 23);
            this.button_ImportApplyLayout.TabIndex = 32;
            this.button_ImportApplyLayout.Text = "导入并重写";
            this.button_ImportApplyLayout.UseVisualStyleBackColor = true;
            this.button_ImportApplyLayout.Click += new System.EventHandler(this.button_ImportApplyLayout_Click);
            // 
            // button_ExportLayout
            // 
            this.button_ExportLayout.Location = new System.Drawing.Point(186, 152);
            this.button_ExportLayout.Name = "button_ExportLayout";
            this.button_ExportLayout.Size = new System.Drawing.Size(44, 23);
            this.button_ExportLayout.TabIndex = 31;
            this.button_ExportLayout.Text = "导出";
            this.button_ExportLayout.UseVisualStyleBackColor = true;
            this.button_ExportLayout.Click += new System.EventHandler(this.button_ExportLayout_Click);
            // 
            // button_ReadLayout
            // 
            this.button_ReadLayout.Location = new System.Drawing.Point(140, 152);
            this.button_ReadLayout.Name = "button_ReadLayout";
            this.button_ReadLayout.Size = new System.Drawing.Size(40, 23);
            this.button_ReadLayout.TabIndex = 30;
            this.button_ReadLayout.Text = "读取";
            this.button_ReadLayout.UseVisualStyleBackColor = true;
            this.button_ReadLayout.Click += new System.EventHandler(this.button_ReadLayout_Click);
            // 
            // v
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(814, 686);
            this.Controls.Add(this.button_ImportApplyLayout);
            this.Controls.Add(this.button_ExportLayout);
            this.Controls.Add(this.button_ReadLayout);
            this.Controls.Add(this.label_LayoutPath);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.button4);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.progressBar1);
            this.Controls.Add(this.button_ReadTarget);
            this.Controls.Add(this.button_Import);
            this.Controls.Add(this.button_ApplyImport);
            this.Controls.Add(this.label_InputFile);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.button_ExportAll);
            this.Controls.Add(this.label_ValueState);
            this.Controls.Add(this.label_FileState);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.buttonModify);
            this.Controls.Add(this.textBox_Target);
            this.Controls.Add(this.textBox_SChinese);
            this.Controls.Add(this.listBox_FileList);
            this.Controls.Add(this.button_ReadSource);
            this.Controls.Add(this.listBox_output);
            this.Controls.Add(this.listView1);
            this.MinimumSize = new System.Drawing.Size(830, 724);
            this.Name = "v";
            this.Text = "多语言工具";
            this.ResizeEnd += new System.EventHandler(this.v_ResizeEnd);
            this.ClientSizeChanged += new System.EventHandler(this.v_ClientSizeChanged);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListView listView1;



        //实例化
        public static v mInstance = null;


        public static CSTools.ToolsIni mOptionIni = new CSTools.ToolsIni("LanguageConverters.ini");
        public static DataManager mDataMgr_SChinese = new DataManager();
        public static DataManager mDataMgr_TChinese = new DataManager();
        public static DataManager mDataMgr_Import = new DataManager();
        public static DataManager mDataMgr_ImportTarget = new DataManager();
        public static DataManager mDataMgr_Layout = new DataManager();
        public static DataManager mDataMgr_LayoutImport = new DataManager();

        public enum eEditType
        {
            edittype_Null = 0,
            edittype_ReadTableSource,
            edittype_ReadTableTarget,
            edittype_ImportUpdateDiffTable,
            //edittype_ImportUpdateTable,
            edittype_ReadLayout,
            //edittype_ImportUpdatedLayoutTable,
        }

        public static eEditType mEditType = eEditType.edittype_Null;

        public static string mPathSChineseVersion = "";
        public static string mPathTChineseVersion = "";
        public static string mPathLayout = "";
        public static string mPathImportFileName = "";
        public static string mPathImportTargetFileName = "";
        public static string mPathImportLayoutFileName = "";

        public static int mSelectFileType = 0;
        public static string mSelectFileName = "";
        public static int mSelectValueIndex = 0;
        public static string mSelectValueText = "";
        public static string mExportAllFileName = "";
        public static string mExportSourceFileName = "";
        public static string mExportTargetFileName = "";
        public static string mExportLayoutFileName = "";


        private System.Windows.Forms.ListBox listBox_output;
        private System.Windows.Forms.Button button_ReadSource;
        private System.Windows.Forms.ListBox listBox_FileList;
        private System.Windows.Forms.TextBox textBox_SChinese;
        private System.Windows.Forms.TextBox textBox_Target;
        private System.Windows.Forms.Button buttonModify;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.ToolTip toolTip1;
        private System.Windows.Forms.ToolTip toolTip2;
        private System.Windows.Forms.Label label_FileState;
        private System.Windows.Forms.Label label_ValueState;
        private System.Windows.Forms.Button button_ExportAll;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label_InputFile;
        private System.Windows.Forms.ToolTip toolTip3;
        private System.Windows.Forms.Button button_ApplyImport;
        private System.Windows.Forms.Button button_Import;
        private System.Windows.Forms.Button button_ReadTarget;
        private System.Windows.Forms.ProgressBar progressBar1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.Button button4;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label_LayoutPath;
        private System.Windows.Forms.Button button_ImportApplyLayout;
        private System.Windows.Forms.Button button_ExportLayout;
        private System.Windows.Forms.Button button_ReadLayout;


        private float mDialogOldWidth;
        private float mDialogOldHeight;
    }
}

