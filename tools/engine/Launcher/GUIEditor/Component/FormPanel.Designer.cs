namespace GUIEditor2
{
	partial class CFormPanel
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
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbBorder = new System.Windows.Forms.Label();
			this.tbBorder = new System.Windows.Forms.TextBox();
			this.gbScrollH = new System.Windows.Forms.GroupBox();
			this.cbbScrollH = new System.Windows.Forms.ComboBox();
			this.gbScrollV = new System.Windows.Forms.GroupBox();
			this.cbbScrollV = new System.Windows.Forms.ComboBox();
			this.tbNameSpace = new System.Windows.Forms.TextBox();
			this.lbNameSpace = new System.Windows.Forms.Label();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
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
			this.tbType.Text = "子窗口(Panel)";
			// 
			// lbX
			// 
			this.lbX.AutoSize = true;
			this.lbX.Location = new System.Drawing.Point(33, 102);
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
			this.lbW.Location = new System.Drawing.Point(116, 102);
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
			this.lbY.Location = new System.Drawing.Point(33, 131);
			this.lbY.Name = "lbY";
			this.lbY.Size = new System.Drawing.Size(14, 14);
			this.lbY.TabIndex = 8;
			this.lbY.Text = "Y";
			// 
			// lbH
			// 
			this.lbH.AutoSize = true;
			this.lbH.Location = new System.Drawing.Point(116, 131);
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
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 188);
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
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
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
			// lbBorder
			// 
			this.lbBorder.AutoSize = true;
			this.lbBorder.Location = new System.Drawing.Point(12, 217);
			this.lbBorder.Name = "lbBorder";
			this.lbBorder.Size = new System.Drawing.Size(35, 14);
			this.lbBorder.TabIndex = 18;
			this.lbBorder.Text = "边距";
			// 
			// tbBorder
			// 
			this.tbBorder.Location = new System.Drawing.Point(67, 213);
			this.tbBorder.Name = "tbBorder";
			this.tbBorder.Size = new System.Drawing.Size(131, 23);
			this.tbBorder.TabIndex = 19;
			this.tbBorder.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// gbScrollH
			// 
			this.gbScrollH.Controls.Add(this.cbbScrollH);
			this.gbScrollH.Location = new System.Drawing.Point(8, 309);
			this.gbScrollH.Name = "gbScrollH";
			this.gbScrollH.Size = new System.Drawing.Size(197, 51);
			this.gbScrollH.TabIndex = 21;
			this.gbScrollH.TabStop = false;
			this.gbScrollH.Text = "水平滚动条";
			// 
			// cbbScrollH
			// 
			this.cbbScrollH.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollH.FormattingEnabled = true;
			this.cbbScrollH.Location = new System.Drawing.Point(9, 22);
			this.cbbScrollH.Name = "cbbScrollH";
			this.cbbScrollH.Size = new System.Drawing.Size(181, 22);
			this.cbbScrollH.Sorted = true;
			this.cbbScrollH.TabIndex = 0;
			this.cbbScrollH.SelectedIndexChanged += new System.EventHandler(this.cbbScroll_SelectedIndexChanged);
			// 
			// gbScrollV
			// 
			this.gbScrollV.Controls.Add(this.cbbScrollV);
			this.gbScrollV.Location = new System.Drawing.Point(8, 242);
			this.gbScrollV.Name = "gbScrollV";
			this.gbScrollV.Size = new System.Drawing.Size(197, 61);
			this.gbScrollV.TabIndex = 20;
			this.gbScrollV.TabStop = false;
			this.gbScrollV.Text = "垂直滚动条";
			// 
			// cbbScrollV
			// 
			this.cbbScrollV.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollV.FormattingEnabled = true;
			this.cbbScrollV.Location = new System.Drawing.Point(9, 22);
			this.cbbScrollV.Name = "cbbScrollV";
			this.cbbScrollV.Size = new System.Drawing.Size(181, 22);
			this.cbbScrollV.Sorted = true;
			this.cbbScrollV.TabIndex = 0;
			this.cbbScrollV.SelectedIndexChanged += new System.EventHandler(this.cbbScroll_SelectedIndexChanged);
			// 
			// tbNameSpace
			// 
			this.tbNameSpace.Location = new System.Drawing.Point(81, 366);
			this.tbNameSpace.Name = "tbNameSpace";
			this.tbNameSpace.Size = new System.Drawing.Size(117, 23);
			this.tbNameSpace.TabIndex = 29;
			// 
			// lbNameSpace
			// 
			this.lbNameSpace.AutoSize = true;
			this.lbNameSpace.Location = new System.Drawing.Point(12, 370);
			this.lbNameSpace.Name = "lbNameSpace";
			this.lbNameSpace.Size = new System.Drawing.Size(63, 14);
			this.lbNameSpace.TabIndex = 28;
			this.lbNameSpace.Text = "命名空间";
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 395);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 44;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 415);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 43;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 419);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 42;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormPanel
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 442);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.tbNameSpace);
			this.Controls.Add(this.lbNameSpace);
			this.Controls.Add(this.gbScrollH);
			this.Controls.Add(this.gbScrollV);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbBorder);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbBorder);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormPanel";
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
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbBorder;
		private System.Windows.Forms.TextBox tbBorder;
		private System.Windows.Forms.GroupBox gbScrollH;
		private System.Windows.Forms.ComboBox cbbScrollH;
		private System.Windows.Forms.GroupBox gbScrollV;
		private System.Windows.Forms.ComboBox cbbScrollV;
		private System.Windows.Forms.TextBox tbNameSpace;
		private System.Windows.Forms.Label lbNameSpace;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
