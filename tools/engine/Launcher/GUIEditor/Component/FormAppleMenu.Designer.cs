namespace GUIEditor2
{
	partial class CFormTabApple
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
			this.lbRadiusSmall = new System.Windows.Forms.Label();
			this.tbRadiusSmall = new System.Windows.Forms.TextBox();
			this.lbRadiusBig = new System.Windows.Forms.Label();
			this.tbRadiusBig = new System.Windows.Forms.TextBox();
			this.cbbAspect = new System.Windows.Forms.ComboBox();
			this.lbAspect = new System.Windows.Forms.Label();
			this.lbPanelImage1 = new System.Windows.Forms.Label();
			this.bnPanelBkColor = new System.Windows.Forms.Button();
			this.lbPanelBkColor = new System.Windows.Forms.Label();
			this.bnPanelImage1 = new System.Windows.Forms.Button();
			this.bnTabAdd = new System.Windows.Forms.Button();
			this.bnTabDelete = new System.Windows.Forms.Button();
			this.lbTabList = new System.Windows.Forms.ListBox();
			this.bnTabMoveUp = new System.Windows.Forms.Button();
			this.bnTabMoveDown = new System.Windows.Forms.Button();
			this.lbBtnImage0 = new System.Windows.Forms.Label();
			this.lbBtnImage1 = new System.Windows.Forms.Label();
			this.lbBtnImage2 = new System.Windows.Forms.Label();
			this.lbBtnImage3 = new System.Windows.Forms.Label();
			this.bnBtnImage0 = new System.Windows.Forms.Button();
			this.bnBtnImage1 = new System.Windows.Forms.Button();
			this.bnBtnImage2 = new System.Windows.Forms.Button();
			this.bnBtnImage3 = new System.Windows.Forms.Button();
			this.gbTab = new System.Windows.Forms.GroupBox();
			this.bnBtnImage8 = new System.Windows.Forms.Button();
			this.bnBtnImage7 = new System.Windows.Forms.Button();
			this.bnBtnImage6 = new System.Windows.Forms.Button();
			this.bnBtnImage5 = new System.Windows.Forms.Button();
			this.lbBtnImage8 = new System.Windows.Forms.Label();
			this.lbBtnImage7 = new System.Windows.Forms.Label();
			this.lbBtnImage6 = new System.Windows.Forms.Label();
			this.lbBtnImage5 = new System.Windows.Forms.Label();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.gbTab.SuspendLayout();
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
			this.tbType.Text = "苹果菜单(AppleMenu)";
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
			// lbRadiusSmall
			// 
			this.lbRadiusSmall.AutoSize = true;
			this.lbRadiusSmall.Location = new System.Drawing.Point(11, 245);
			this.lbRadiusSmall.Name = "lbRadiusSmall";
			this.lbRadiusSmall.Size = new System.Drawing.Size(77, 14);
			this.lbRadiusSmall.TabIndex = 20;
			this.lbRadiusSmall.Text = "小按钮半径";
			// 
			// tbRadiusSmall
			// 
			this.tbRadiusSmall.Location = new System.Drawing.Point(88, 241);
			this.tbRadiusSmall.Name = "tbRadiusSmall";
			this.tbRadiusSmall.Size = new System.Drawing.Size(110, 23);
			this.tbRadiusSmall.TabIndex = 21;
			this.tbRadiusSmall.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbRadiusBig
			// 
			this.lbRadiusBig.AutoSize = true;
			this.lbRadiusBig.Location = new System.Drawing.Point(11, 274);
			this.lbRadiusBig.Name = "lbRadiusBig";
			this.lbRadiusBig.Size = new System.Drawing.Size(77, 14);
			this.lbRadiusBig.TabIndex = 22;
			this.lbRadiusBig.Text = "大按钮半径";
			// 
			// tbRadiusBig
			// 
			this.tbRadiusBig.Location = new System.Drawing.Point(88, 270);
			this.tbRadiusBig.Name = "tbRadiusBig";
			this.tbRadiusBig.Size = new System.Drawing.Size(110, 23);
			this.tbRadiusBig.TabIndex = 23;
			this.tbRadiusBig.Leave += new System.EventHandler(this.tb_Leave);
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
			this.cbbAspect.Location = new System.Drawing.Point(88, 213);
			this.cbbAspect.Name = "cbbAspect";
			this.cbbAspect.Size = new System.Drawing.Size(110, 22);
			this.cbbAspect.TabIndex = 24;
			this.cbbAspect.SelectedIndexChanged += new System.EventHandler(this.cbbAspect_SelectedIndexChanged);
			// 
			// lbAspect
			// 
			this.lbAspect.AutoSize = true;
			this.lbAspect.Location = new System.Drawing.Point(11, 216);
			this.lbAspect.Name = "lbAspect";
			this.lbAspect.Size = new System.Drawing.Size(63, 14);
			this.lbAspect.TabIndex = 24;
			this.lbAspect.Text = "按钮位置";
			// 
			// lbPanelImage1
			// 
			this.lbPanelImage1.AutoSize = true;
			this.lbPanelImage1.Location = new System.Drawing.Point(5, 400);
			this.lbPanelImage1.Name = "lbPanelImage1";
			this.lbPanelImage1.Size = new System.Drawing.Size(77, 14);
			this.lbPanelImage1.TabIndex = 17;
			this.lbPanelImage1.Text = "面板背景图";
			// 
			// bnPanelBkColor
			// 
			this.bnPanelBkColor.Enabled = false;
			this.bnPanelBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnPanelBkColor.Location = new System.Drawing.Point(95, 368);
			this.bnPanelBkColor.Name = "bnPanelBkColor";
			this.bnPanelBkColor.Size = new System.Drawing.Size(97, 22);
			this.bnPanelBkColor.TabIndex = 16;
			this.bnPanelBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnPanelBkColor.UseVisualStyleBackColor = true;
			this.bnPanelBkColor.Click += new System.EventHandler(this.bnPanelBkColor_Click);
			// 
			// lbPanelBkColor
			// 
			this.lbPanelBkColor.AutoSize = true;
			this.lbPanelBkColor.Location = new System.Drawing.Point(5, 372);
			this.lbPanelBkColor.Name = "lbPanelBkColor";
			this.lbPanelBkColor.Size = new System.Drawing.Size(77, 14);
			this.lbPanelBkColor.TabIndex = 15;
			this.lbPanelBkColor.Text = "面板背景色";
			// 
			// bnPanelImage1
			// 
			this.bnPanelImage1.Enabled = false;
			this.bnPanelImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnPanelImage1.Location = new System.Drawing.Point(95, 396);
			this.bnPanelImage1.Name = "bnPanelImage1";
			this.bnPanelImage1.Size = new System.Drawing.Size(97, 22);
			this.bnPanelImage1.TabIndex = 18;
			this.bnPanelImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnPanelImage1.UseVisualStyleBackColor = true;
			this.bnPanelImage1.Click += new System.EventHandler(this.bnPanelImage1_Click);
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
			// lbBtnImage0
			// 
			this.lbBtnImage0.AutoSize = true;
			this.lbBtnImage0.Location = new System.Drawing.Point(6, 148);
			this.lbBtnImage0.Name = "lbBtnImage0";
			this.lbBtnImage0.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage0.TabIndex = 5;
			this.lbBtnImage0.Text = "小按钮(无效)";
			// 
			// lbBtnImage1
			// 
			this.lbBtnImage1.AutoSize = true;
			this.lbBtnImage1.Location = new System.Drawing.Point(6, 176);
			this.lbBtnImage1.Name = "lbBtnImage1";
			this.lbBtnImage1.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage1.TabIndex = 7;
			this.lbBtnImage1.Text = "小按钮(正常)";
			// 
			// lbBtnImage2
			// 
			this.lbBtnImage2.AutoSize = true;
			this.lbBtnImage2.Location = new System.Drawing.Point(6, 204);
			this.lbBtnImage2.Name = "lbBtnImage2";
			this.lbBtnImage2.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage2.TabIndex = 9;
			this.lbBtnImage2.Text = "小按钮(焦点)";
			// 
			// lbBtnImage3
			// 
			this.lbBtnImage3.AutoSize = true;
			this.lbBtnImage3.Location = new System.Drawing.Point(6, 232);
			this.lbBtnImage3.Name = "lbBtnImage3";
			this.lbBtnImage3.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage3.TabIndex = 11;
			this.lbBtnImage3.Text = "小按钮(按下)";
			// 
			// bnBtnImage0
			// 
			this.bnBtnImage0.Enabled = false;
			this.bnBtnImage0.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage0.Location = new System.Drawing.Point(95, 144);
			this.bnBtnImage0.Name = "bnBtnImage0";
			this.bnBtnImage0.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage0.TabIndex = 6;
			this.bnBtnImage0.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage0.UseVisualStyleBackColor = true;
			this.bnBtnImage0.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage0.Click += new System.EventHandler(this.bnBtnImage0_Click);
			this.bnBtnImage0.MouseEnter += new System.EventHandler(this.bnBtnImage0_MouseEnter);
			// 
			// bnBtnImage1
			// 
			this.bnBtnImage1.Enabled = false;
			this.bnBtnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage1.Location = new System.Drawing.Point(95, 172);
			this.bnBtnImage1.Name = "bnBtnImage1";
			this.bnBtnImage1.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage1.TabIndex = 8;
			this.bnBtnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage1.UseVisualStyleBackColor = true;
			this.bnBtnImage1.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage1.Click += new System.EventHandler(this.bnBtnImage1_Click);
			this.bnBtnImage1.MouseEnter += new System.EventHandler(this.bnBtnImage1_MouseEnter);
			// 
			// bnBtnImage2
			// 
			this.bnBtnImage2.Enabled = false;
			this.bnBtnImage2.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage2.Location = new System.Drawing.Point(95, 200);
			this.bnBtnImage2.Name = "bnBtnImage2";
			this.bnBtnImage2.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage2.TabIndex = 10;
			this.bnBtnImage2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage2.UseVisualStyleBackColor = true;
			this.bnBtnImage2.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage2.Click += new System.EventHandler(this.bnBtnImage2_Click);
			this.bnBtnImage2.MouseEnter += new System.EventHandler(this.bnBtnImage2_MouseEnter);
			// 
			// bnBtnImage3
			// 
			this.bnBtnImage3.Enabled = false;
			this.bnBtnImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage3.Location = new System.Drawing.Point(95, 228);
			this.bnBtnImage3.Name = "bnBtnImage3";
			this.bnBtnImage3.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage3.TabIndex = 12;
			this.bnBtnImage3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage3.UseVisualStyleBackColor = true;
			this.bnBtnImage3.MouseLeave += new System.EventHandler(this.bnBtnImage_MouseLeave);
			this.bnBtnImage3.Click += new System.EventHandler(this.bnBtnImage3_Click);
			this.bnBtnImage3.MouseEnter += new System.EventHandler(this.bnBtnImage3_MouseEnter);
			// 
			// gbTab
			// 
			this.gbTab.Controls.Add(this.bnBtnImage8);
			this.gbTab.Controls.Add(this.bnBtnImage3);
			this.gbTab.Controls.Add(this.bnBtnImage7);
			this.gbTab.Controls.Add(this.bnBtnImage2);
			this.gbTab.Controls.Add(this.bnBtnImage6);
			this.gbTab.Controls.Add(this.bnBtnImage1);
			this.gbTab.Controls.Add(this.bnBtnImage5);
			this.gbTab.Controls.Add(this.lbBtnImage8);
			this.gbTab.Controls.Add(this.bnBtnImage0);
			this.gbTab.Controls.Add(this.lbBtnImage7);
			this.gbTab.Controls.Add(this.lbBtnImage3);
			this.gbTab.Controls.Add(this.lbBtnImage6);
			this.gbTab.Controls.Add(this.lbBtnImage2);
			this.gbTab.Controls.Add(this.lbBtnImage5);
			this.gbTab.Controls.Add(this.lbBtnImage1);
			this.gbTab.Controls.Add(this.lbBtnImage0);
			this.gbTab.Controls.Add(this.bnTabMoveDown);
			this.gbTab.Controls.Add(this.bnTabMoveUp);
			this.gbTab.Controls.Add(this.lbTabList);
			this.gbTab.Controls.Add(this.bnTabDelete);
			this.gbTab.Controls.Add(this.bnTabAdd);
			this.gbTab.Controls.Add(this.bnPanelImage1);
			this.gbTab.Controls.Add(this.lbPanelBkColor);
			this.gbTab.Controls.Add(this.bnPanelBkColor);
			this.gbTab.Controls.Add(this.lbPanelImage1);
			this.gbTab.Location = new System.Drawing.Point(6, 299);
			this.gbTab.Name = "gbTab";
			this.gbTab.Size = new System.Drawing.Size(199, 426);
			this.gbTab.TabIndex = 25;
			this.gbTab.TabStop = false;
			this.gbTab.Text = "定义按钮及面板";
			// 
			// bnBtnImage8
			// 
			this.bnBtnImage8.Enabled = false;
			this.bnBtnImage8.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage8.Location = new System.Drawing.Point(95, 340);
			this.bnBtnImage8.Name = "bnBtnImage8";
			this.bnBtnImage8.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage8.TabIndex = 12;
			this.bnBtnImage8.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage8.UseVisualStyleBackColor = true;
			this.bnBtnImage8.Click += new System.EventHandler(this.bnBtnImage8_Click);
			// 
			// bnBtnImage7
			// 
			this.bnBtnImage7.Enabled = false;
			this.bnBtnImage7.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage7.Location = new System.Drawing.Point(95, 312);
			this.bnBtnImage7.Name = "bnBtnImage7";
			this.bnBtnImage7.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage7.TabIndex = 10;
			this.bnBtnImage7.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage7.UseVisualStyleBackColor = true;
			this.bnBtnImage7.Click += new System.EventHandler(this.bnBtnImage7_Click);
			// 
			// bnBtnImage6
			// 
			this.bnBtnImage6.Enabled = false;
			this.bnBtnImage6.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage6.Location = new System.Drawing.Point(95, 284);
			this.bnBtnImage6.Name = "bnBtnImage6";
			this.bnBtnImage6.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage6.TabIndex = 8;
			this.bnBtnImage6.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage6.UseVisualStyleBackColor = true;
			this.bnBtnImage6.Click += new System.EventHandler(this.bnBtnImage6_Click);
			// 
			// bnBtnImage5
			// 
			this.bnBtnImage5.Enabled = false;
			this.bnBtnImage5.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBtnImage5.Location = new System.Drawing.Point(95, 256);
			this.bnBtnImage5.Name = "bnBtnImage5";
			this.bnBtnImage5.Size = new System.Drawing.Size(97, 22);
			this.bnBtnImage5.TabIndex = 6;
			this.bnBtnImage5.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnBtnImage5.UseVisualStyleBackColor = true;
			this.bnBtnImage5.Click += new System.EventHandler(this.bnBtnImage5_Click);
			// 
			// lbBtnImage8
			// 
			this.lbBtnImage8.AutoSize = true;
			this.lbBtnImage8.Location = new System.Drawing.Point(6, 344);
			this.lbBtnImage8.Name = "lbBtnImage8";
			this.lbBtnImage8.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage8.TabIndex = 11;
			this.lbBtnImage8.Text = "大按钮(按下)";
			// 
			// lbBtnImage7
			// 
			this.lbBtnImage7.AutoSize = true;
			this.lbBtnImage7.Location = new System.Drawing.Point(6, 316);
			this.lbBtnImage7.Name = "lbBtnImage7";
			this.lbBtnImage7.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage7.TabIndex = 9;
			this.lbBtnImage7.Text = "大按钮(焦点)";
			// 
			// lbBtnImage6
			// 
			this.lbBtnImage6.AutoSize = true;
			this.lbBtnImage6.Location = new System.Drawing.Point(6, 288);
			this.lbBtnImage6.Name = "lbBtnImage6";
			this.lbBtnImage6.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage6.TabIndex = 7;
			this.lbBtnImage6.Text = "大按钮(正常)";
			// 
			// lbBtnImage5
			// 
			this.lbBtnImage5.AutoSize = true;
			this.lbBtnImage5.Location = new System.Drawing.Point(6, 260);
			this.lbBtnImage5.Name = "lbBtnImage5";
			this.lbBtnImage5.Size = new System.Drawing.Size(91, 14);
			this.lbBtnImage5.TabIndex = 5;
			this.lbBtnImage5.Text = "大按钮(无效)";
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 731);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 53;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 755);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 52;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 760);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 51;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormTabApple
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 779);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbbAspect);
			this.Controls.Add(this.gbTab);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbRadiusBig);
			this.Controls.Add(this.tbRadiusSmall);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbRadiusSmall);
			this.Controls.Add(this.lbAspect);
			this.Controls.Add(this.lbRadiusBig);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormTabApple";
			this.gbTab.ResumeLayout(false);
			this.gbTab.PerformLayout();
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
		private System.Windows.Forms.Label lbRadiusSmall;
		private System.Windows.Forms.TextBox tbRadiusSmall;
		private System.Windows.Forms.Label lbRadiusBig;
		private System.Windows.Forms.TextBox tbRadiusBig;
		private System.Windows.Forms.ComboBox cbbAspect;
		private System.Windows.Forms.Label lbAspect;
		private System.Windows.Forms.Label lbPanelImage1;
		private System.Windows.Forms.Button bnPanelBkColor;
		private System.Windows.Forms.Label lbPanelBkColor;
		private System.Windows.Forms.Button bnPanelImage1;
		private System.Windows.Forms.Button bnTabAdd;
		private System.Windows.Forms.Button bnTabDelete;
		private System.Windows.Forms.ListBox lbTabList;
		private System.Windows.Forms.Button bnTabMoveUp;
		private System.Windows.Forms.Button bnTabMoveDown;
		private System.Windows.Forms.Label lbBtnImage0;
		private System.Windows.Forms.Label lbBtnImage1;
		private System.Windows.Forms.Label lbBtnImage2;
		private System.Windows.Forms.Label lbBtnImage3;
		private System.Windows.Forms.Button bnBtnImage0;
		private System.Windows.Forms.Button bnBtnImage1;
		private System.Windows.Forms.Button bnBtnImage2;
		private System.Windows.Forms.Button bnBtnImage3;
		private System.Windows.Forms.GroupBox gbTab;
		private System.Windows.Forms.Button bnBtnImage8;
		private System.Windows.Forms.Button bnBtnImage7;
		private System.Windows.Forms.Button bnBtnImage6;
		private System.Windows.Forms.Button bnBtnImage5;
		private System.Windows.Forms.Label lbBtnImage8;
		private System.Windows.Forms.Label lbBtnImage7;
		private System.Windows.Forms.Label lbBtnImage6;
		private System.Windows.Forms.Label lbBtnImage5;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
