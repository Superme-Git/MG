namespace GUIEditor2
{
	partial class CFormTab
	{
		private System.ComponentModel.IContainer components = null;

		protected override void Dispose(bool disposing)
		{
			if(disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows 窗体设计器生成的代码

		private void InitializeComponent()
		{
			this.lbName = new System.Windows.Forms.Label();
			this.tbName = new System.Windows.Forms.TextBox();
			this.lbType = new System.Windows.Forms.Label();
			this.tbType = new System.Windows.Forms.TextBox();
			this.lbX = new System.Windows.Forms.Label();
			this.tbX = new System.Windows.Forms.TextBox();
			this.lbW = new System.Windows.Forms.Label();
			this.tbW = new System.Windows.Forms.TextBox();
			this.lbY = new System.Windows.Forms.Label();
			this.lbH = new System.Windows.Forms.Label();
			this.tbY = new System.Windows.Forms.TextBox();
			this.tbH = new System.Windows.Forms.TextBox();
			this.lbBkColor = new System.Windows.Forms.Label();
			this.bnBkColor = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.gbTab = new System.Windows.Forms.GroupBox();
			this.lbColName = new System.Windows.Forms.Label();
			this.tbColName = new System.Windows.Forms.TextBox();
			this.lbColFont = new System.Windows.Forms.Label();
			this.cbbColFont = new System.Windows.Forms.ComboBox();
			this.gbScrollH = new System.Windows.Forms.GroupBox();
			this.cbbScrollH = new System.Windows.Forms.ComboBox();
			this.gbScrollV = new System.Windows.Forms.GroupBox();
			this.cbbScrollV = new System.Windows.Forms.ComboBox();
			this.bnBtnImage4 = new System.Windows.Forms.Button();
			this.bnBtnImage3 = new System.Windows.Forms.Button();
			this.bnBtnImage2 = new System.Windows.Forms.Button();
			this.bnBtnImage1 = new System.Windows.Forms.Button();
			this.bnBtnImage0 = new System.Windows.Forms.Button();
			this.lbBtnImage4 = new System.Windows.Forms.Label();
			this.lbBtnImage3 = new System.Windows.Forms.Label();
			this.lbBtnImage2 = new System.Windows.Forms.Label();
			this.lbBtnImage1 = new System.Windows.Forms.Label();
			this.lbBtnImage0 = new System.Windows.Forms.Label();
			this.tbPanelBorder = new System.Windows.Forms.TextBox();
			this.bnTabMoveDown = new System.Windows.Forms.Button();
			this.bnTabMoveUp = new System.Windows.Forms.Button();
			this.lbTabList = new System.Windows.Forms.ListBox();
			this.bnTabDelete = new System.Windows.Forms.Button();
			this.bnTabAdd = new System.Windows.Forms.Button();
			this.bnPanelImage1 = new System.Windows.Forms.Button();
			this.lbPanelBkColor = new System.Windows.Forms.Label();
			this.bnPanelBkColor = new System.Windows.Forms.Button();
			this.lbPanelBorder = new System.Windows.Forms.Label();
			this.lbPanelImage1 = new System.Windows.Forms.Label();
			this.lbHeadSize = new System.Windows.Forms.Label();
			this.tbHeadSize = new System.Windows.Forms.TextBox();
			this.lbBtnWidth = new System.Windows.Forms.Label();
			this.tbBtnWidth = new System.Windows.Forms.TextBox();
			this.lbBtnHeight = new System.Windows.Forms.Label();
			this.tbBtnHeight = new System.Windows.Forms.TextBox();
			this.cbbAspect = new System.Windows.Forms.ComboBox();
			this.lbAspect = new System.Windows.Forms.Label();
			this.lbBtnBorder = new System.Windows.Forms.Label();
			this.tbBtnBorder = new System.Windows.Forms.TextBox();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.gbTab.SuspendLayout();
			this.gbScrollH.SuspendLayout();
			this.gbScrollV.SuspendLayout();
			this.SuspendLayout();
			// 
			// lbName
			// 
			this.lbName.AutoSize = true;
			this.lbName.Location = new System.Drawing.Point(12, 44);
			this.lbName.Name = "lbName";
			this.lbName.Size = new System.Drawing.Size(35, 14);
			this.lbName.TabIndex = 2;
			this.lbName.Text = "名称";
			// 
			// tbName
			// 
			this.tbName.Location = new System.Drawing.Point(53, 41);
			this.tbName.Name = "tbName";
			this.tbName.Size = new System.Drawing.Size(145, 23);
			this.tbName.TabIndex = 3;
			this.tbName.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbType
			// 
			this.lbType.AutoSize = true;
			this.lbType.Location = new System.Drawing.Point(12, 15);
			this.lbType.Name = "lbType";
			this.lbType.Size = new System.Drawing.Size(35, 14);
			this.lbType.TabIndex = 0;
			this.lbType.Text = "类型";
			// 
			// tbType
			// 
			this.tbType.Location = new System.Drawing.Point(53, 12);
			this.tbType.Name = "tbType";
			this.tbType.ReadOnly = true;
			this.tbType.Size = new System.Drawing.Size(145, 23);
			this.tbType.TabIndex = 1;
			this.tbType.TabStop = false;
			this.tbType.Text = "选项卡(Tab)";
			// 
			// lbX
			// 
			this.lbX.AutoSize = true;
			this.lbX.Location = new System.Drawing.Point(33, 103);
			this.lbX.Name = "lbX";
			this.lbX.Size = new System.Drawing.Size(14, 14);
			this.lbX.TabIndex = 6;
			this.lbX.Text = "X";
			// 
			// tbX
			// 
			this.tbX.Location = new System.Drawing.Point(53, 99);
			this.tbX.Name = "tbX";
			this.tbX.Size = new System.Drawing.Size(55, 23);
			this.tbX.TabIndex = 7;
			this.tbX.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbW
			// 
			this.lbW.AutoSize = true;
			this.lbW.Location = new System.Drawing.Point(116, 103);
			this.lbW.Name = "lbW";
			this.lbW.Size = new System.Drawing.Size(21, 14);
			this.lbW.TabIndex = 10;
			this.lbW.Text = "宽";
			// 
			// tbW
			// 
			this.tbW.Location = new System.Drawing.Point(143, 99);
			this.tbW.Name = "tbW";
			this.tbW.Size = new System.Drawing.Size(55, 23);
			this.tbW.TabIndex = 11;
			this.tbW.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbY
			// 
			this.lbY.AutoSize = true;
			this.lbY.Location = new System.Drawing.Point(33, 132);
			this.lbY.Name = "lbY";
			this.lbY.Size = new System.Drawing.Size(14, 14);
			this.lbY.TabIndex = 8;
			this.lbY.Text = "Y";
			// 
			// lbH
			// 
			this.lbH.AutoSize = true;
			this.lbH.Location = new System.Drawing.Point(116, 132);
			this.lbH.Name = "lbH";
			this.lbH.Size = new System.Drawing.Size(21, 14);
			this.lbH.TabIndex = 12;
			this.lbH.Text = "高";
			// 
			// tbY
			// 
			this.tbY.Location = new System.Drawing.Point(53, 128);
			this.tbY.Name = "tbY";
			this.tbY.Size = new System.Drawing.Size(55, 23);
			this.tbY.TabIndex = 9;
			this.tbY.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// tbH
			// 
			this.tbH.Location = new System.Drawing.Point(143, 128);
			this.tbH.Name = "tbH";
			this.tbH.Size = new System.Drawing.Size(55, 23);
			this.tbH.TabIndex = 13;
			this.tbH.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbBkColor
			// 
			this.lbBkColor.AutoSize = true;
			this.lbBkColor.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor.Name = "lbBkColor";
			this.lbBkColor.Size = new System.Drawing.Size(49, 14);
			this.lbBkColor.TabIndex = 14;
			this.lbBkColor.Text = "背景色";
			// 
			// bnBkColor
			// 
			this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor.Location = new System.Drawing.Point(67, 157);
			this.bnBkColor.Name = "bnBkColor";
			this.bnBkColor.Size = new System.Drawing.Size(131, 22);
			this.bnBkColor.TabIndex = 15;
			this.bnBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor.UseVisualStyleBackColor = true;
			this.bnBkColor.Click += new System.EventHandler(this.bnBkColor_Click);
			// 
			// lbClass
			// 
			this.lbClass.AutoSize = true;
			this.lbClass.Location = new System.Drawing.Point(12, 73);
			this.lbClass.Name = "lbClass";
			this.lbClass.Size = new System.Drawing.Size(35, 14);
			this.lbClass.TabIndex = 4;
			this.lbClass.Text = "类名";
			// 
			// tbClass
			// 
			this.tbClass.Location = new System.Drawing.Point(53, 70);
			this.tbClass.Name = "tbClass";
			this.tbClass.Size = new System.Drawing.Size(145, 23);
			this.tbClass.TabIndex = 5;
			this.tbClass.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 189);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(49, 14);
			this.lbImage1.TabIndex = 16;
			this.lbImage1.Text = "背景图";
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(67, 185);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(131, 22);
			this.bnImage1.TabIndex = 17;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
			// 
			// gbTab
			// 
			this.gbTab.Controls.Add(this.lbColName);
			this.gbTab.Controls.Add(this.tbColName);
			this.gbTab.Controls.Add(this.lbColFont);
			this.gbTab.Controls.Add(this.cbbColFont);
			this.gbTab.Controls.Add(this.gbScrollH);
			this.gbTab.Controls.Add(this.gbScrollV);
			this.gbTab.Controls.Add(this.bnBtnImage4);
			this.gbTab.Controls.Add(this.bnBtnImage3);
			this.gbTab.Controls.Add(this.bnBtnImage2);
			this.gbTab.Controls.Add(this.bnBtnImage1);
			this.gbTab.Controls.Add(this.bnBtnImage0);
			this.gbTab.Controls.Add(this.lbBtnImage4);
			this.gbTab.Controls.Add(this.lbBtnImage3);
			this.gbTab.Controls.Add(this.lbBtnImage2);
			this.gbTab.Controls.Add(this.lbBtnImage1);
			this.gbTab.Controls.Add(this.lbBtnImage0);
			this.gbTab.Controls.Add(this.tbPanelBorder);
			this.gbTab.Controls.Add(this.bnTabMoveDown);
			this.gbTab.Controls.Add(this.bnTabMoveUp);
			this.gbTab.Controls.Add(this.lbTabList);
			this.gbTab.Controls.Add(this.bnTabDelete);
			this.gbTab.Controls.Add(this.bnTabAdd);
			this.gbTab.Controls.Add(this.bnPanelImage1);
			this.gbTab.Controls.Add(this.lbPanelBkColor);
			this.gbTab.Controls.Add(this.bnPanelBkColor);
			this.gbTab.Controls.Add(this.lbPanelBorder);
			this.gbTab.Controls.Add(this.lbPanelImage1);
			this.gbTab.Location = new System.Drawing.Point(6, 359);
			this.gbTab.Name = "gbTab";
			this.gbTab.Size = new System.Drawing.Size(199, 544);
			this.gbTab.TabIndex = 25;
			this.gbTab.TabStop = false;
			this.gbTab.Text = "定义按钮及面板";
			// 
			// lbColName
			// 
			this.lbColName.AutoSize = true;
			this.lbColName.Location = new System.Drawing.Point(7, 148);
			this.lbColName.Name = "lbColName";
			this.lbColName.Size = new System.Drawing.Size(63, 14);
			this.lbColName.TabIndex = 23;
			this.lbColName.Text = "标签名称";
			// 
			// tbColName
			// 
			this.tbColName.Enabled = false;
			this.tbColName.Location = new System.Drawing.Point(75, 144);
			this.tbColName.Name = "tbColName";
			this.tbColName.Size = new System.Drawing.Size(117, 23);
			this.tbColName.TabIndex = 24;
			this.tbColName.TextChanged += new System.EventHandler(this.tbColName_TextChanged);
			this.tbColName.Leave += new System.EventHandler(this.tbColName_Leave);
			// 
			// lbColFont
			// 
			this.lbColFont.AutoSize = true;
			this.lbColFont.Location = new System.Drawing.Point(7, 176);
			this.lbColFont.Name = "lbColFont";
			this.lbColFont.Size = new System.Drawing.Size(63, 14);
			this.lbColFont.TabIndex = 25;
			this.lbColFont.Text = "字体样式";
			// 
			// cbbColFont
			// 
			this.cbbColFont.DropDownHeight = 240;
			this.cbbColFont.DropDownWidth = 155;
			this.cbbColFont.Enabled = false;
			this.cbbColFont.IntegralHeight = false;
			this.cbbColFont.Location = new System.Drawing.Point(75, 173);
			this.cbbColFont.Name = "cbbColFont";
			this.cbbColFont.Size = new System.Drawing.Size(117, 22);
			this.cbbColFont.Sorted = true;
			this.cbbColFont.TabIndex = 26;
			this.cbbColFont.SelectedIndexChanged += new System.EventHandler(this.cbbColFont_Leave);
			this.cbbColFont.Leave += new System.EventHandler(this.cbbColFont_Leave);
			this.cbbColFont.DropDown += new System.EventHandler(this.cbbColFont_DropDown);
			// 
			// gbScrollH
			// 
			this.gbScrollH.Controls.Add(this.cbbScrollH);
			this.gbScrollH.Location = new System.Drawing.Point(9, 484);
			this.gbScrollH.Name = "gbScrollH";
			this.gbScrollH.Size = new System.Drawing.Size(184, 52);
			this.gbScrollH.TabIndex = 22;
			this.gbScrollH.TabStop = false;
			this.gbScrollH.Text = "水平滚动条";
			// 
			// cbbScrollH
			// 
			this.cbbScrollH.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollH.Enabled = false;
			this.cbbScrollH.FormattingEnabled = true;
			this.cbbScrollH.Location = new System.Drawing.Point(6, 22);
			this.cbbScrollH.Name = "cbbScrollH";
			this.cbbScrollH.Size = new System.Drawing.Size(171, 22);
			this.cbbScrollH.Sorted = true;
			this.cbbScrollH.TabIndex = 0;
			this.cbbScrollH.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// gbScrollV
			// 
			this.gbScrollV.Controls.Add(this.cbbScrollV);
			this.gbScrollV.Location = new System.Drawing.Point(8, 426);
			this.gbScrollV.Name = "gbScrollV";
			this.gbScrollV.Size = new System.Drawing.Size(184, 52);
			this.gbScrollV.TabIndex = 21;
			this.gbScrollV.TabStop = false;
			this.gbScrollV.Text = "垂直滚动条";
			// 
			// cbbScrollV
			// 
			this.cbbScrollV.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollV.Enabled = false;
			this.cbbScrollV.FormattingEnabled = true;
			this.cbbScrollV.Location = new System.Drawing.Point(7, 22);
			this.cbbScrollV.Name = "cbbScrollV";
			this.cbbScrollV.Size = new System.Drawing.Size(171, 22);
			this.cbbScrollV.Sorted = true;
			this.cbbScrollV.TabIndex = 0;
			this.cbbScrollV.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// bnBtnImage4
			// 
			this.bnBtnImage4.Enabled = false;
			this.bnBtnImage4.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage4.Location = new System.Drawing.Point(95, 313);
			this.bnBtnImage4.Name = "bnBtnImage4";
			this.bnBtnImage4.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage4.TabIndex = 14;
			this.bnBtnImage4.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage4.UseVisualStyleBackColor = true;
			this.bnBtnImage4.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage4.Click += new System.EventHandler(this.bnBtnImage4_Click);
			this.bnBtnImage4.MouseEnter += new System.EventHandler(this.bnBtnImage4_MouseEnter);
			// 
			// bnBtnImage3
			// 
			this.bnBtnImage3.Enabled = false;
			this.bnBtnImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage3.Location = new System.Drawing.Point(95, 285);
			this.bnBtnImage3.Name = "bnBtnImage3";
			this.bnBtnImage3.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage3.TabIndex = 12;
			this.bnBtnImage3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage3.UseVisualStyleBackColor = true;
			this.bnBtnImage3.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage3.Click += new System.EventHandler(this.bnBtnImage3_Click);
			this.bnBtnImage3.MouseEnter += new System.EventHandler(this.bnBtnImage3_MouseEnter);
			// 
			// bnBtnImage2
			// 
			this.bnBtnImage2.Enabled = false;
			this.bnBtnImage2.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage2.Location = new System.Drawing.Point(95, 257);
			this.bnBtnImage2.Name = "bnBtnImage2";
			this.bnBtnImage2.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage2.TabIndex = 10;
			this.bnBtnImage2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage2.UseVisualStyleBackColor = true;
			this.bnBtnImage2.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage2.Click += new System.EventHandler(this.bnBtnImage2_Click);
			this.bnBtnImage2.MouseEnter += new System.EventHandler(this.bnBtnImage2_MouseEnter);
			// 
			// bnBtnImage1
			// 
			this.bnBtnImage1.Enabled = false;
			this.bnBtnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage1.Location = new System.Drawing.Point(95, 229);
			this.bnBtnImage1.Name = "bnBtnImage1";
			this.bnBtnImage1.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage1.TabIndex = 8;
			this.bnBtnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage1.UseVisualStyleBackColor = true;
			this.bnBtnImage1.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage1.Click += new System.EventHandler(this.bnBtnImage1_Click);
			this.bnBtnImage1.MouseEnter += new System.EventHandler(this.bnBtnImage1_MouseEnter);
			// 
			// bnBtnImage0
			// 
			this.bnBtnImage0.Enabled = false;
			this.bnBtnImage0.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage0.Location = new System.Drawing.Point(95, 201);
			this.bnBtnImage0.Name = "bnBtnImage0";
			this.bnBtnImage0.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage0.TabIndex = 6;
			this.bnBtnImage0.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage0.UseVisualStyleBackColor = true;
			this.bnBtnImage0.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage0.Click += new System.EventHandler(this.bnBtnImage0_Click);
			this.bnBtnImage0.MouseEnter += new System.EventHandler(this.bnBtnImage0_MouseEnter);
			// 
			// lbBtnImage4
			// 
			this.lbBtnImage4.AutoSize = true;
			this.lbBtnImage4.Location = new System.Drawing.Point(6, 317);
			this.lbBtnImage4.Name = "lbBtnImage4";
			this.lbBtnImage4.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage4.TabIndex = 13;
			this.lbBtnImage4.Text = "按钮(按焦态)";
			// 
			// lbBtnImage3
			// 
			this.lbBtnImage3.AutoSize = true;
			this.lbBtnImage3.Location = new System.Drawing.Point(6, 289);
			this.lbBtnImage3.Name = "lbBtnImage3";
			this.lbBtnImage3.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage3.TabIndex = 11;
			this.lbBtnImage3.Text = "按钮(按下态)";
			// 
			// lbBtnImage2
			// 
			this.lbBtnImage2.AutoSize = true;
			this.lbBtnImage2.Location = new System.Drawing.Point(6, 261);
			this.lbBtnImage2.Name = "lbBtnImage2";
			this.lbBtnImage2.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage2.TabIndex = 9;
			this.lbBtnImage2.Text = "按钮(焦点态)";
			// 
			// lbBtnImage1
			// 
			this.lbBtnImage1.AutoSize = true;
			this.lbBtnImage1.Location = new System.Drawing.Point(6, 233);
			this.lbBtnImage1.Name = "lbBtnImage1";
			this.lbBtnImage1.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage1.TabIndex = 7;
			this.lbBtnImage1.Text = "按钮(正常态)";
			// 
			// lbBtnImage0
			// 
			this.lbBtnImage0.AutoSize = true;
			this.lbBtnImage0.Location = new System.Drawing.Point(6, 205);
			this.lbBtnImage0.Name = "lbBtnImage0";
			this.lbBtnImage0.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage0.TabIndex = 5;
			this.lbBtnImage0.Text = "按钮(无效态)";
			// 
			// tbPanelBorder
			// 
			this.tbPanelBorder.Enabled = false;
			this.tbPanelBorder.Location = new System.Drawing.Point(95, 397);
			this.tbPanelBorder.Name = "tbPanelBorder";
			this.tbPanelBorder.Size = new System.Drawing.Size(98, 23);
			this.tbPanelBorder.TabIndex = 20;
			this.tbPanelBorder.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// bnTabMoveDown
			// 
			this.bnTabMoveDown.Enabled = false;
			this.bnTabMoveDown.Location = new System.Drawing.Point(9, 111);
			this.bnTabMoveDown.Name = "bnTabMoveDown";
			this.bnTabMoveDown.Size = new System.Drawing.Size(45, 25);
			this.bnTabMoveDown.TabIndex = 3;
			this.bnTabMoveDown.Text = "下移";
			this.bnTabMoveDown.UseVisualStyleBackColor = true;
			this.bnTabMoveDown.Click += new System.EventHandler(this.bnTabMoveDown_Click);
			// 
			// bnTabMoveUp
			// 
			this.bnTabMoveUp.Enabled = false;
			this.bnTabMoveUp.Location = new System.Drawing.Point(9, 80);
			this.bnTabMoveUp.Name = "bnTabMoveUp";
			this.bnTabMoveUp.Size = new System.Drawing.Size(46, 25);
			this.bnTabMoveUp.TabIndex = 2;
			this.bnTabMoveUp.Text = "上移";
			this.bnTabMoveUp.UseVisualStyleBackColor = true;
			this.bnTabMoveUp.Click += new System.EventHandler(this.bnTabMoveUp_Click);
			// 
			// lbTabList
			// 
			this.lbTabList.FormattingEnabled = true;
			this.lbTabList.ItemHeight = 14;
			this.lbTabList.Location = new System.Drawing.Point(61, 22);
			this.lbTabList.Name = "lbTabList";
			this.lbTabList.Size = new System.Drawing.Size(131, 116);
			this.lbTabList.TabIndex = 4;
			this.lbTabList.SelectedIndexChanged += new System.EventHandler(this.lbTabList_SelectedIndexChanged);
			// 
			// bnTabDelete
			// 
			this.bnTabDelete.Enabled = false;
			this.bnTabDelete.Location = new System.Drawing.Point(9, 51);
			this.bnTabDelete.Name = "bnTabDelete";
			this.bnTabDelete.Size = new System.Drawing.Size(46, 23);
			this.bnTabDelete.TabIndex = 1;
			this.bnTabDelete.Text = "删除";
			this.bnTabDelete.UseVisualStyleBackColor = true;
			this.bnTabDelete.Click += new System.EventHandler(this.bnTabDelete_Click);
			// 
			// bnTabAdd
			// 
			this.bnTabAdd.Location = new System.Drawing.Point(9, 22);
			this.bnTabAdd.Name = "bnTabAdd";
			this.bnTabAdd.Size = new System.Drawing.Size(46, 23);
			this.bnTabAdd.TabIndex = 0;
			this.bnTabAdd.Text = "添加";
			this.bnTabAdd.UseVisualStyleBackColor = true;
			this.bnTabAdd.Click += new System.EventHandler(this.bnTabAdd_Click);
			// 
			// bnPanelImage1
			// 
			this.bnPanelImage1.Enabled = false;
			this.bnPanelImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnPanelImage1.Location = new System.Drawing.Point(95, 369);
			this.bnPanelImage1.Name = "bnPanelImage1";
			this.bnPanelImage1.Size = new System.Drawing.Size(98, 22);
			this.bnPanelImage1.TabIndex = 18;
			this.bnPanelImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnPanelImage1.UseVisualStyleBackColor = true;
			this.bnPanelImage1.Click += new System.EventHandler(this.bnPanelImage1_Click);
			// 
			// lbPanelBkColor
			// 
			this.lbPanelBkColor.AutoSize = true;
			this.lbPanelBkColor.Location = new System.Drawing.Point(6, 345);
			this.lbPanelBkColor.Name = "lbPanelBkColor";
			this.lbPanelBkColor.Size = new System.Drawing.Size(77, 14);
			this.lbPanelBkColor.TabIndex = 15;
			this.lbPanelBkColor.Text = "面板背景色";
			// 
			// bnPanelBkColor
			// 
			this.bnPanelBkColor.Enabled = false;
			this.bnPanelBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnPanelBkColor.Location = new System.Drawing.Point(95, 341);
			this.bnPanelBkColor.Name = "bnPanelBkColor";
			this.bnPanelBkColor.Size = new System.Drawing.Size(98, 22);
			this.bnPanelBkColor.TabIndex = 16;
			this.bnPanelBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnPanelBkColor.UseVisualStyleBackColor = true;
			this.bnPanelBkColor.Click += new System.EventHandler(this.bnPanelBkColor_Click);
			// 
			// lbPanelBorder
			// 
			this.lbPanelBorder.AutoSize = true;
			this.lbPanelBorder.Location = new System.Drawing.Point(6, 401);
			this.lbPanelBorder.Name = "lbPanelBorder";
			this.lbPanelBorder.Size = new System.Drawing.Size(63, 14);
			this.lbPanelBorder.TabIndex = 19;
			this.lbPanelBorder.Text = "面板边距";
			// 
			// lbPanelImage1
			// 
			this.lbPanelImage1.AutoSize = true;
			this.lbPanelImage1.Location = new System.Drawing.Point(6, 373);
			this.lbPanelImage1.Name = "lbPanelImage1";
			this.lbPanelImage1.Size = new System.Drawing.Size(77, 14);
			this.lbPanelImage1.TabIndex = 17;
			this.lbPanelImage1.Text = "面板背景图";
			// 
			// lbHeadSize
			// 
			this.lbHeadSize.AutoSize = true;
			this.lbHeadSize.Location = new System.Drawing.Point(12, 217);
			this.lbHeadSize.Name = "lbHeadSize";
			this.lbHeadSize.Size = new System.Drawing.Size(63, 14);
			this.lbHeadSize.TabIndex = 18;
			this.lbHeadSize.Text = "标头距离";
			// 
			// tbHeadSize
			// 
			this.tbHeadSize.Location = new System.Drawing.Point(88, 213);
			this.tbHeadSize.Name = "tbHeadSize";
			this.tbHeadSize.Size = new System.Drawing.Size(110, 23);
			this.tbHeadSize.TabIndex = 19;
			this.tbHeadSize.Leave += new System.EventHandler(this.tb_HeadSizeLeave);
			// 
			// lbBtnWidth
			// 
			this.lbBtnWidth.AutoSize = true;
			this.lbBtnWidth.Location = new System.Drawing.Point(12, 246);
			this.lbBtnWidth.Name = "lbBtnWidth";
			this.lbBtnWidth.Size = new System.Drawing.Size(49, 14);
			this.lbBtnWidth.TabIndex = 20;
			this.lbBtnWidth.Text = "按钮宽";
			// 
			// tbBtnWidth
			// 
			this.tbBtnWidth.Location = new System.Drawing.Point(88, 242);
			this.tbBtnWidth.Name = "tbBtnWidth";
			this.tbBtnWidth.Size = new System.Drawing.Size(110, 23);
			this.tbBtnWidth.TabIndex = 21;
			this.tbBtnWidth.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbBtnHeight
			// 
			this.lbBtnHeight.AutoSize = true;
			this.lbBtnHeight.Location = new System.Drawing.Point(11, 275);
			this.lbBtnHeight.Name = "lbBtnHeight";
			this.lbBtnHeight.Size = new System.Drawing.Size(49, 14);
			this.lbBtnHeight.TabIndex = 22;
			this.lbBtnHeight.Text = "按钮高";
			// 
			// tbBtnHeight
			// 
			this.tbBtnHeight.Location = new System.Drawing.Point(88, 271);
			this.tbBtnHeight.Name = "tbBtnHeight";
			this.tbBtnHeight.Size = new System.Drawing.Size(110, 23);
			this.tbBtnHeight.TabIndex = 23;
			this.tbBtnHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbbAspect
			// 
			this.cbbAspect.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbAspect.FormattingEnabled = true;
			this.cbbAspect.Items.AddRange(new object[] {
            "上",
            "下",
            "左",
            "右"});
			this.cbbAspect.Location = new System.Drawing.Point(88, 329);
			this.cbbAspect.Name = "cbbAspect";
			this.cbbAspect.Size = new System.Drawing.Size(110, 22);
			this.cbbAspect.TabIndex = 24;
			this.cbbAspect.SelectedIndexChanged += new System.EventHandler(this.cbbAspect_SelectedIndexChanged);
			// 
			// lbAspect
			// 
			this.lbAspect.AutoSize = true;
			this.lbAspect.Location = new System.Drawing.Point(11, 332);
			this.lbAspect.Name = "lbAspect";
			this.lbAspect.Size = new System.Drawing.Size(63, 14);
			this.lbAspect.TabIndex = 24;
			this.lbAspect.Text = "标签位置";
			// 
			// lbBtnBorder
			// 
			this.lbBtnBorder.AutoSize = true;
			this.lbBtnBorder.Location = new System.Drawing.Point(12, 303);
			this.lbBtnBorder.Name = "lbBtnBorder";
			this.lbBtnBorder.Size = new System.Drawing.Size(63, 14);
			this.lbBtnBorder.TabIndex = 22;
			this.lbBtnBorder.Text = "按钮边距";
			// 
			// tbBtnBorder
			// 
			this.tbBtnBorder.Location = new System.Drawing.Point(88, 300);
			this.tbBtnBorder.Name = "tbBtnBorder";
			this.tbBtnBorder.Size = new System.Drawing.Size(110, 23);
			this.tbBtnBorder.TabIndex = 23;
			this.tbBtnBorder.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(6, 909);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 26;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(72, 926);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 28;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(3, 930);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 27;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormTab
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 954);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.cbbAspect);
			this.Controls.Add(this.gbTab);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbBtnBorder);
			this.Controls.Add(this.tbBtnHeight);
			this.Controls.Add(this.tbBtnWidth);
			this.Controls.Add(this.tbHeadSize);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbBtnWidth);
			this.Controls.Add(this.lbAspect);
			this.Controls.Add(this.lbBtnBorder);
			this.Controls.Add(this.lbBtnHeight);
			this.Controls.Add(this.lbHeadSize);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormTab";
			this.gbTab.ResumeLayout(false);
			this.gbTab.PerformLayout();
			this.gbScrollH.ResumeLayout(false);
			this.gbScrollV.ResumeLayout(false);
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label lbName;
		private System.Windows.Forms.TextBox tbName;
		private System.Windows.Forms.Label lbType;
		private System.Windows.Forms.TextBox tbType;
		private System.Windows.Forms.Label lbX;
		private System.Windows.Forms.TextBox tbX;
		private System.Windows.Forms.Label lbW;
		private System.Windows.Forms.TextBox tbW;
		private System.Windows.Forms.Label lbY;
		private System.Windows.Forms.Label lbH;
		private System.Windows.Forms.TextBox tbY;
		private System.Windows.Forms.TextBox tbH;
		private System.Windows.Forms.Label lbBkColor;
		private System.Windows.Forms.Button bnBkColor;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.GroupBox gbTab;
		private System.Windows.Forms.Button bnTabDelete;
		private System.Windows.Forms.Button bnTabAdd;
		private System.Windows.Forms.Button bnPanelImage1;
		private System.Windows.Forms.Label lbPanelBkColor;
		private System.Windows.Forms.Button bnPanelBkColor;
		private System.Windows.Forms.Label lbPanelImage1;
		private System.Windows.Forms.ListBox lbTabList;
		private System.Windows.Forms.Button bnTabMoveDown;
		private System.Windows.Forms.Button bnTabMoveUp;
		private System.Windows.Forms.Label lbHeadSize;
		private System.Windows.Forms.TextBox tbHeadSize;
		private System.Windows.Forms.Label lbBtnWidth;
		private System.Windows.Forms.TextBox tbBtnWidth;
		private System.Windows.Forms.Label lbPanelBorder;
		private System.Windows.Forms.TextBox tbPanelBorder;
		private System.Windows.Forms.Button bnBtnImage3;
		private System.Windows.Forms.Button bnBtnImage2;
		private System.Windows.Forms.Button bnBtnImage1;
		private System.Windows.Forms.Button bnBtnImage0;
		private System.Windows.Forms.Label lbBtnImage3;
		private System.Windows.Forms.Label lbBtnImage2;
		private System.Windows.Forms.Label lbBtnImage1;
		private System.Windows.Forms.Label lbBtnImage0;
		private System.Windows.Forms.Label lbBtnHeight;
		private System.Windows.Forms.TextBox tbBtnHeight;
		private System.Windows.Forms.GroupBox gbScrollH;
		private System.Windows.Forms.ComboBox cbbScrollH;
		private System.Windows.Forms.GroupBox gbScrollV;
		private System.Windows.Forms.ComboBox cbbScrollV;
		private System.Windows.Forms.Button bnBtnImage4;
		private System.Windows.Forms.Label lbBtnImage4;
		private System.Windows.Forms.ComboBox cbbAspect;
		private System.Windows.Forms.Label lbAspect;
		private System.Windows.Forms.Label lbColName;
		private System.Windows.Forms.TextBox tbColName;
		private System.Windows.Forms.Label lbColFont;
		private System.Windows.Forms.ComboBox cbbColFont;
		private System.Windows.Forms.Label lbBtnBorder;
		private System.Windows.Forms.TextBox tbBtnBorder;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
