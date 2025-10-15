namespace GUIEditor2
{
	partial class CFormTable
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
			this.lbBkColor1 = new System.Windows.Forms.Label();
			this.bnBkColor1 = new System.Windows.Forms.Button();
			this.lbBkImage1 = new System.Windows.Forms.Label();
			this.bnBkImage1 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbFont = new System.Windows.Forms.Label();
			this.cbbFont = new System.Windows.Forms.ComboBox();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbHeadHeight = new System.Windows.Forms.Label();
			this.tbHeadHeight = new System.Windows.Forms.TextBox();
			this.cbDragWidth = new System.Windows.Forms.CheckBox();
			this.cbOnSelChange = new System.Windows.Forms.CheckBox();
			this.cbOnHeadClick = new System.Windows.Forms.CheckBox();
			this.gbCol = new System.Windows.Forms.GroupBox();
			this.bnColMoveDown = new System.Windows.Forms.Button();
			this.bnColMoveUp = new System.Windows.Forms.Button();
			this.lbColList = new System.Windows.Forms.ListBox();
			this.bnColDelete = new System.Windows.Forms.Button();
			this.bnColAdd = new System.Windows.Forms.Button();
			this.lbColName = new System.Windows.Forms.Label();
			this.tbColName = new System.Windows.Forms.TextBox();
			this.lbColWidth = new System.Windows.Forms.Label();
			this.tbColWidth = new System.Windows.Forms.TextBox();
			this.bnColImage1 = new System.Windows.Forms.Button();
			this.lbColBkColor = new System.Windows.Forms.Label();
			this.bnColBkColor = new System.Windows.Forms.Button();
			this.lbColImage1 = new System.Windows.Forms.Label();
			this.lbLineHeight = new System.Windows.Forms.Label();
			this.tbLineHeight = new System.Windows.Forms.TextBox();
			this.lbImageHead = new System.Windows.Forms.Label();
			this.bnImageHead = new System.Windows.Forms.Button();
			this.lbImageCell = new System.Windows.Forms.Label();
			this.bnImageCell = new System.Windows.Forms.Button();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.lbGridColor = new System.Windows.Forms.Label();
			this.bnGridColor = new System.Windows.Forms.Button();
			this.lbHeadFont = new System.Windows.Forms.Label();
			this.cbbHeadFont = new System.Windows.Forms.ComboBox();
			this.cbbDataType = new System.Windows.Forms.ComboBox();
			this.lbDataType = new System.Windows.Forms.Label();
			this.lbBkImage3 = new System.Windows.Forms.Label();
			this.lbBkColor3 = new System.Windows.Forms.Label();
			this.bnBkImage3 = new System.Windows.Forms.Button();
			this.bnBkColor3 = new System.Windows.Forms.Button();
			this.gbCol.SuspendLayout();
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
			this.tbType.Text = "表格框(Table)";
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
			// lbBkColor1
			// 
			this.lbBkColor1.AutoSize = true;
			this.lbBkColor1.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor1.Name = "lbBkColor1";
			this.lbBkColor1.Size = new System.Drawing.Size(77, 14);
			this.lbBkColor1.TabIndex = 14;
			this.lbBkColor1.Text = "焦点背景色";
			// 
			// bnBkColor1
			// 
			this.bnBkColor1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor1.Location = new System.Drawing.Point(94, 157);
			this.bnBkColor1.Name = "bnBkColor1";
			this.bnBkColor1.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor1.TabIndex = 15;
			this.bnBkColor1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor1.UseVisualStyleBackColor = true;
			this.bnBkColor1.Click += new System.EventHandler(this.bnBkColor1_Click);
			// 
			// lbBkImage1
			// 
			this.lbBkImage1.AutoSize = true;
			this.lbBkImage1.Location = new System.Drawing.Point(12, 189);
			this.lbBkImage1.Name = "lbBkImage1";
			this.lbBkImage1.Size = new System.Drawing.Size(77, 14);
			this.lbBkImage1.TabIndex = 16;
			this.lbBkImage1.Text = "焦点背景图";
			// 
			// bnBkImage1
			// 
			this.bnBkImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage1.Location = new System.Drawing.Point(94, 185);
			this.bnBkImage1.Name = "bnBkImage1";
			this.bnBkImage1.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage1.TabIndex = 17;
			this.bnBkImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkImage1.UseVisualStyleBackColor = true;
			this.bnBkImage1.Click += new System.EventHandler(this.bnBkImage1_Click);
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
			// lbFont
			// 
			this.lbFont.AutoSize = true;
			this.lbFont.Location = new System.Drawing.Point(12, 328);
			this.lbFont.Name = "lbFont";
			this.lbFont.Size = new System.Drawing.Size(63, 14);
			this.lbFont.TabIndex = 24;
			this.lbFont.Text = "表体样式";
			// 
			// cbbFont
			// 
			this.cbbFont.DropDownHeight = 240;
			this.cbbFont.DropDownWidth = 155;
			this.cbbFont.IntegralHeight = false;
			this.cbbFont.Location = new System.Drawing.Point(81, 325);
			this.cbbFont.Name = "cbbFont";
			this.cbbFont.Size = new System.Drawing.Size(117, 22);
			this.cbbFont.Sorted = true;
			this.cbbFont.TabIndex = 25;
			this.cbbFont.Leave += new System.EventHandler(this.tb_Leave);
			this.cbbFont.DropDown += new System.EventHandler(this.cbbFont_DropDown);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 358);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(49, 14);
			this.lbImage1.TabIndex = 26;
			this.lbImage1.Text = "背景图";
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(67, 354);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(131, 22);
			this.bnImage1.TabIndex = 27;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
			// 
			// lbHeadHeight
			// 
			this.lbHeadHeight.AutoSize = true;
			this.lbHeadHeight.Location = new System.Drawing.Point(12, 442);
			this.lbHeadHeight.Name = "lbHeadHeight";
			this.lbHeadHeight.Size = new System.Drawing.Size(49, 14);
			this.lbHeadHeight.TabIndex = 28;
			this.lbHeadHeight.Text = "表头高";
			// 
			// tbHeadHeight
			// 
			this.tbHeadHeight.Location = new System.Drawing.Point(67, 438);
			this.tbHeadHeight.Name = "tbHeadHeight";
			this.tbHeadHeight.Size = new System.Drawing.Size(131, 23);
			this.tbHeadHeight.TabIndex = 29;
			this.tbHeadHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbDragWidth
			// 
			this.cbDragWidth.AutoSize = true;
			this.cbDragWidth.Location = new System.Drawing.Point(15, 524);
			this.cbDragWidth.Name = "cbDragWidth";
			this.cbDragWidth.Size = new System.Drawing.Size(82, 18);
			this.cbDragWidth.TabIndex = 32;
			this.cbDragWidth.Text = "列宽可调";
			this.cbDragWidth.UseVisualStyleBackColor = true;
			// 
			// cbOnSelChange
			// 
			this.cbOnSelChange.AutoSize = true;
			this.cbOnSelChange.Location = new System.Drawing.Point(15, 548);
			this.cbOnSelChange.Name = "cbOnSelChange";
			this.cbOnSelChange.Size = new System.Drawing.Size(110, 18);
			this.cbOnSelChange.TabIndex = 33;
			this.cbOnSelChange.Text = "响应选择事件";
			this.cbOnSelChange.UseVisualStyleBackColor = true;
			// 
			// cbOnHeadClick
			// 
			this.cbOnHeadClick.AutoSize = true;
			this.cbOnHeadClick.Location = new System.Drawing.Point(15, 572);
			this.cbOnHeadClick.Name = "cbOnHeadClick";
			this.cbOnHeadClick.Size = new System.Drawing.Size(138, 18);
			this.cbOnHeadClick.TabIndex = 34;
			this.cbOnHeadClick.Text = "响应标题点击事件";
			this.cbOnHeadClick.UseVisualStyleBackColor = true;
			// 
			// gbCol
			// 
			this.gbCol.Controls.Add(this.bnColMoveDown);
			this.gbCol.Controls.Add(this.bnColMoveUp);
			this.gbCol.Controls.Add(this.lbColList);
			this.gbCol.Controls.Add(this.bnColDelete);
			this.gbCol.Controls.Add(this.bnColAdd);
			this.gbCol.Controls.Add(this.lbColName);
			this.gbCol.Controls.Add(this.tbColName);
			this.gbCol.Controls.Add(this.lbColWidth);
			this.gbCol.Controls.Add(this.tbColWidth);
			this.gbCol.Controls.Add(this.bnColImage1);
			this.gbCol.Controls.Add(this.lbColBkColor);
			this.gbCol.Controls.Add(this.bnColBkColor);
			this.gbCol.Controls.Add(this.lbColImage1);
			this.gbCol.Location = new System.Drawing.Point(4, 594);
			this.gbCol.Name = "gbCol";
			this.gbCol.Size = new System.Drawing.Size(199, 259);
			this.gbCol.TabIndex = 35;
			this.gbCol.TabStop = false;
			this.gbCol.Text = "定义列";
			// 
			// bnColMoveDown
			// 
			this.bnColMoveDown.Enabled = false;
			this.bnColMoveDown.Location = new System.Drawing.Point(9, 111);
			this.bnColMoveDown.Name = "bnColMoveDown";
			this.bnColMoveDown.Size = new System.Drawing.Size(45, 25);
			this.bnColMoveDown.TabIndex = 3;
			this.bnColMoveDown.Text = "下移";
			this.bnColMoveDown.UseVisualStyleBackColor = true;
			this.bnColMoveDown.Click += new System.EventHandler(this.bnColMoveDown_Click);
			// 
			// bnColMoveUp
			// 
			this.bnColMoveUp.Enabled = false;
			this.bnColMoveUp.Location = new System.Drawing.Point(9, 80);
			this.bnColMoveUp.Name = "bnColMoveUp";
			this.bnColMoveUp.Size = new System.Drawing.Size(46, 25);
			this.bnColMoveUp.TabIndex = 2;
			this.bnColMoveUp.Text = "上移";
			this.bnColMoveUp.UseVisualStyleBackColor = true;
			this.bnColMoveUp.Click += new System.EventHandler(this.bnColMoveUp_Click);
			// 
			// lbColList
			// 
			this.lbColList.FormattingEnabled = true;
			this.lbColList.ItemHeight = 14;
			this.lbColList.Location = new System.Drawing.Point(61, 22);
			this.lbColList.Name = "lbColList";
			this.lbColList.Size = new System.Drawing.Size(131, 116);
			this.lbColList.TabIndex = 4;
			this.lbColList.SelectedIndexChanged += new System.EventHandler(this.lbColList_SelectedIndexChanged);
			// 
			// bnColDelete
			// 
			this.bnColDelete.Enabled = false;
			this.bnColDelete.Location = new System.Drawing.Point(9, 51);
			this.bnColDelete.Name = "bnColDelete";
			this.bnColDelete.Size = new System.Drawing.Size(46, 23);
			this.bnColDelete.TabIndex = 1;
			this.bnColDelete.Text = "删除";
			this.bnColDelete.UseVisualStyleBackColor = true;
			this.bnColDelete.Click += new System.EventHandler(this.bnColDelete_Click);
			// 
			// bnColAdd
			// 
			this.bnColAdd.Location = new System.Drawing.Point(9, 22);
			this.bnColAdd.Name = "bnColAdd";
			this.bnColAdd.Size = new System.Drawing.Size(46, 23);
			this.bnColAdd.TabIndex = 0;
			this.bnColAdd.Text = "添加";
			this.bnColAdd.UseVisualStyleBackColor = true;
			this.bnColAdd.Click += new System.EventHandler(this.bnColAdd_Click);
			// 
			// lbColName
			// 
			this.lbColName.AutoSize = true;
			this.lbColName.Location = new System.Drawing.Point(7, 148);
			this.lbColName.Name = "lbColName";
			this.lbColName.Size = new System.Drawing.Size(49, 14);
			this.lbColName.TabIndex = 5;
			this.lbColName.Text = "列名称";
			// 
			// tbColName
			// 
			this.tbColName.Enabled = false;
			this.tbColName.Location = new System.Drawing.Point(61, 144);
			this.tbColName.Name = "tbColName";
			this.tbColName.Size = new System.Drawing.Size(131, 23);
			this.tbColName.TabIndex = 6;
			this.tbColName.TextChanged += new System.EventHandler(this.tb_ColLeave);
			this.tbColName.Leave += new System.EventHandler(this.tb_ColNameLeave);
			// 
			// lbColWidth
			// 
			this.lbColWidth.AutoSize = true;
			this.lbColWidth.Location = new System.Drawing.Point(8, 176);
			this.lbColWidth.Name = "lbColWidth";
			this.lbColWidth.Size = new System.Drawing.Size(35, 14);
			this.lbColWidth.TabIndex = 9;
			this.lbColWidth.Text = "列宽";
			// 
			// tbColWidth
			// 
			this.tbColWidth.Enabled = false;
			this.tbColWidth.Location = new System.Drawing.Point(61, 173);
			this.tbColWidth.Name = "tbColWidth";
			this.tbColWidth.Size = new System.Drawing.Size(131, 23);
			this.tbColWidth.TabIndex = 10;
			this.tbColWidth.Leave += new System.EventHandler(this.tb_ColWidthLeave);
			// 
			// bnColImage1
			// 
			this.bnColImage1.Enabled = false;
			this.bnColImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnColImage1.Location = new System.Drawing.Point(61, 230);
			this.bnColImage1.Name = "bnColImage1";
			this.bnColImage1.Size = new System.Drawing.Size(131, 22);
			this.bnColImage1.TabIndex = 14;
			this.bnColImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnColImage1.UseVisualStyleBackColor = true;
			this.bnColImage1.Click += new System.EventHandler(this.bnColImage1_Click);
			// 
			// lbColBkColor
			// 
			this.lbColBkColor.AutoSize = true;
			this.lbColBkColor.Location = new System.Drawing.Point(8, 206);
			this.lbColBkColor.Name = "lbColBkColor";
			this.lbColBkColor.Size = new System.Drawing.Size(49, 14);
			this.lbColBkColor.TabIndex = 11;
			this.lbColBkColor.Text = "背景色";
			// 
			// bnColBkColor
			// 
			this.bnColBkColor.Enabled = false;
			this.bnColBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnColBkColor.Location = new System.Drawing.Point(61, 202);
			this.bnColBkColor.Name = "bnColBkColor";
			this.bnColBkColor.Size = new System.Drawing.Size(131, 22);
			this.bnColBkColor.TabIndex = 12;
			this.bnColBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnColBkColor.UseVisualStyleBackColor = true;
			this.bnColBkColor.Click += new System.EventHandler(this.bnColBkColor_Click);
			// 
			// lbColImage1
			// 
			this.lbColImage1.AutoSize = true;
			this.lbColImage1.Location = new System.Drawing.Point(8, 234);
			this.lbColImage1.Name = "lbColImage1";
			this.lbColImage1.Size = new System.Drawing.Size(49, 14);
			this.lbColImage1.TabIndex = 13;
			this.lbColImage1.Text = "背景图";
			// 
			// lbLineHeight
			// 
			this.lbLineHeight.AutoSize = true;
			this.lbLineHeight.Location = new System.Drawing.Point(12, 471);
			this.lbLineHeight.Name = "lbLineHeight";
			this.lbLineHeight.Size = new System.Drawing.Size(35, 14);
			this.lbLineHeight.TabIndex = 30;
			this.lbLineHeight.Text = "行高";
			// 
			// tbLineHeight
			// 
			this.tbLineHeight.Location = new System.Drawing.Point(67, 467);
			this.tbLineHeight.Name = "tbLineHeight";
			this.tbLineHeight.Size = new System.Drawing.Size(131, 23);
			this.tbLineHeight.TabIndex = 31;
			this.tbLineHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbImageHead
			// 
			this.lbImageHead.AutoSize = true;
			this.lbImageHead.Location = new System.Drawing.Point(12, 386);
			this.lbImageHead.Name = "lbImageHead";
			this.lbImageHead.Size = new System.Drawing.Size(49, 14);
			this.lbImageHead.TabIndex = 26;
			this.lbImageHead.Text = "表头图";
			// 
			// bnImageHead
			// 
			this.bnImageHead.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageHead.Location = new System.Drawing.Point(67, 382);
			this.bnImageHead.Name = "bnImageHead";
			this.bnImageHead.Size = new System.Drawing.Size(131, 22);
			this.bnImageHead.TabIndex = 27;
			this.bnImageHead.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImageHead.UseVisualStyleBackColor = true;
			this.bnImageHead.Click += new System.EventHandler(this.bnImageHead_Click);
			// 
			// lbImageCell
			// 
			this.lbImageCell.AutoSize = true;
			this.lbImageCell.Location = new System.Drawing.Point(12, 414);
			this.lbImageCell.Name = "lbImageCell";
			this.lbImageCell.Size = new System.Drawing.Size(49, 14);
			this.lbImageCell.TabIndex = 26;
			this.lbImageCell.Text = "表体图";
			// 
			// bnImageCell
			// 
			this.bnImageCell.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageCell.Location = new System.Drawing.Point(67, 410);
			this.bnImageCell.Name = "bnImageCell";
			this.bnImageCell.Size = new System.Drawing.Size(131, 22);
			this.bnImageCell.TabIndex = 27;
			this.bnImageCell.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImageCell.UseVisualStyleBackColor = true;
			this.bnImageCell.Click += new System.EventHandler(this.bnImageCell_Click);
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 859);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 36;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 876);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 38;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 880);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 37;
			this.lbTipInfo.Text = "提示信息";
			// 
			// lbGridColor
			// 
			this.lbGridColor.AutoSize = true;
			this.lbGridColor.Location = new System.Drawing.Point(12, 273);
			this.lbGridColor.Name = "lbGridColor";
			this.lbGridColor.Size = new System.Drawing.Size(77, 14);
			this.lbGridColor.TabIndex = 16;
			this.lbGridColor.Text = "表格线框色";
			// 
			// bnGridColor
			// 
			this.bnGridColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnGridColor.Location = new System.Drawing.Point(94, 269);
			this.bnGridColor.Name = "bnGridColor";
			this.bnGridColor.Size = new System.Drawing.Size(104, 22);
			this.bnGridColor.TabIndex = 17;
			this.bnGridColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnGridColor.UseVisualStyleBackColor = true;
			this.bnGridColor.Click += new System.EventHandler(this.bnGridColor_Click);
			// 
			// lbHeadFont
			// 
			this.lbHeadFont.AutoSize = true;
			this.lbHeadFont.Location = new System.Drawing.Point(12, 300);
			this.lbHeadFont.Name = "lbHeadFont";
			this.lbHeadFont.Size = new System.Drawing.Size(63, 14);
			this.lbHeadFont.TabIndex = 39;
			this.lbHeadFont.Text = "表头样式";
			// 
			// cbbHeadFont
			// 
			this.cbbHeadFont.DropDownHeight = 240;
			this.cbbHeadFont.DropDownWidth = 155;
			this.cbbHeadFont.IntegralHeight = false;
			this.cbbHeadFont.Location = new System.Drawing.Point(81, 297);
			this.cbbHeadFont.Name = "cbbHeadFont";
			this.cbbHeadFont.Size = new System.Drawing.Size(117, 22);
			this.cbbHeadFont.Sorted = true;
			this.cbbHeadFont.TabIndex = 40;
			this.cbbHeadFont.SelectedIndexChanged += new System.EventHandler(this.cbbHeadFont_SelectedIndexChanged);
			this.cbbHeadFont.Leave += new System.EventHandler(this.tb_Leave);
			this.cbbHeadFont.DropDown += new System.EventHandler(this.cbbHeadFont_DropDown);
			// 
			// cbbDataType
			// 
			this.cbbDataType.FormattingEnabled = true;
			this.cbbDataType.Items.AddRange(new object[] {
            "text",
            "image"});
			this.cbbDataType.Location = new System.Drawing.Point(81, 496);
			this.cbbDataType.Name = "cbbDataType";
			this.cbbDataType.Size = new System.Drawing.Size(117, 22);
			this.cbbDataType.TabIndex = 42;
			// 
			// lbDataType
			// 
			this.lbDataType.AutoSize = true;
			this.lbDataType.Location = new System.Drawing.Point(12, 500);
			this.lbDataType.Name = "lbDataType";
			this.lbDataType.Size = new System.Drawing.Size(63, 14);
			this.lbDataType.TabIndex = 41;
			this.lbDataType.Text = "数据类型";
			// 
			// lbBkImage3
			// 
			this.lbBkImage3.AutoSize = true;
			this.lbBkImage3.Location = new System.Drawing.Point(12, 245);
			this.lbBkImage3.Name = "lbBkImage3";
			this.lbBkImage3.Size = new System.Drawing.Size(77, 14);
			this.lbBkImage3.TabIndex = 14;
			this.lbBkImage3.Text = "选中背景图";
			// 
			// lbBkColor3
			// 
			this.lbBkColor3.AutoSize = true;
			this.lbBkColor3.Location = new System.Drawing.Point(12, 217);
			this.lbBkColor3.Name = "lbBkColor3";
			this.lbBkColor3.Size = new System.Drawing.Size(77, 14);
			this.lbBkColor3.TabIndex = 16;
			this.lbBkColor3.Text = "选中背景色";
			// 
			// bnBkImage3
			// 
			this.bnBkImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage3.Location = new System.Drawing.Point(94, 241);
			this.bnBkImage3.Name = "bnBkImage3";
			this.bnBkImage3.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage3.TabIndex = 15;
			this.bnBkImage3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkImage3.UseVisualStyleBackColor = true;
			this.bnBkImage3.Click += new System.EventHandler(this.bnBkImage3_Click);
			// 
			// bnBkColor3
			// 
			this.bnBkColor3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor3.Location = new System.Drawing.Point(94, 213);
			this.bnBkColor3.Name = "bnBkColor3";
			this.bnBkColor3.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor3.TabIndex = 17;
			this.bnBkColor3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor3.UseVisualStyleBackColor = true;
			this.bnBkColor3.Click += new System.EventHandler(this.bnBkColor3_Click);
			// 
			// CFormTable
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 906);
			this.Controls.Add(this.cbbDataType);
			this.Controls.Add(this.lbDataType);
			this.Controls.Add(this.lbHeadFont);
			this.Controls.Add(this.cbbHeadFont);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.gbCol);
			this.Controls.Add(this.cbOnHeadClick);
			this.Controls.Add(this.cbOnSelChange);
			this.Controls.Add(this.cbDragWidth);
			this.Controls.Add(this.bnGridColor);
			this.Controls.Add(this.bnBkColor3);
			this.Controls.Add(this.bnBkImage1);
			this.Controls.Add(this.bnImageCell);
			this.Controls.Add(this.bnImageHead);
			this.Controls.Add(this.bnBkImage3);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor1);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbLineHeight);
			this.Controls.Add(this.tbHeadHeight);
			this.Controls.Add(this.cbbFont);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbLineHeight);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbHeadHeight);
			this.Controls.Add(this.lbFont);
			this.Controls.Add(this.lbImageCell);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbGridColor);
			this.Controls.Add(this.lbBkColor3);
			this.Controls.Add(this.lbImageHead);
			this.Controls.Add(this.lbBkImage1);
			this.Controls.Add(this.lbBkImage3);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor1);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormTable";
			this.gbCol.ResumeLayout(false);
			this.gbCol.PerformLayout();
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
		private System.Windows.Forms.Label lbBkColor1;
		private System.Windows.Forms.Button bnBkColor1;
		private System.Windows.Forms.Label lbBkImage1;
		private System.Windows.Forms.Button bnBkImage1;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbFont;
		private System.Windows.Forms.ComboBox cbbFont;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbHeadHeight;
		private System.Windows.Forms.TextBox tbHeadHeight;
		private System.Windows.Forms.CheckBox cbDragWidth;
		private System.Windows.Forms.CheckBox cbOnSelChange;
		private System.Windows.Forms.CheckBox cbOnHeadClick;
		private System.Windows.Forms.GroupBox gbCol;
		private System.Windows.Forms.Label lbColName;
		private System.Windows.Forms.TextBox tbColName;
		private System.Windows.Forms.Label lbColWidth;
		private System.Windows.Forms.TextBox tbColWidth;
		private System.Windows.Forms.Button bnColDelete;
		private System.Windows.Forms.Button bnColAdd;
		private System.Windows.Forms.Button bnColImage1;
		private System.Windows.Forms.Label lbColBkColor;
		private System.Windows.Forms.Button bnColBkColor;
		private System.Windows.Forms.Label lbColImage1;
		private System.Windows.Forms.ListBox lbColList;
		private System.Windows.Forms.Button bnColMoveDown;
		private System.Windows.Forms.Button bnColMoveUp;
		private System.Windows.Forms.Label lbLineHeight;
		private System.Windows.Forms.TextBox tbLineHeight;
		private System.Windows.Forms.Label lbImageHead;
		private System.Windows.Forms.Button bnImageHead;
		private System.Windows.Forms.Label lbImageCell;
		private System.Windows.Forms.Button bnImageCell;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
		private System.Windows.Forms.Label lbGridColor;
		private System.Windows.Forms.Button bnGridColor;
		private System.Windows.Forms.Label lbHeadFont;
		private System.Windows.Forms.ComboBox cbbHeadFont;
		private System.Windows.Forms.ComboBox cbbDataType;
		private System.Windows.Forms.Label lbDataType;
		private System.Windows.Forms.Label lbBkImage3;
		private System.Windows.Forms.Label lbBkColor3;
		private System.Windows.Forms.Button bnBkImage3;
		private System.Windows.Forms.Button bnBkColor3;
	}
}
