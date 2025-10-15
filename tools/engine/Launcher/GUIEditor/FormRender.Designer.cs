namespace GUIEditor2
{
	partial class CFormRender
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

		#region 组件设计器生成的代码

		private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container();
			this.mnNew = new System.Windows.Forms.ContextMenuStrip(this.components);
			this.miNewBase = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewStatic = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewImage = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep0 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewTextBox = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewEditBox = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep1 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewButtonImage = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewButtonText = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewButtonRadio = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewButtonCheck = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep2 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewProgressBar = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSliderBar = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewScrollBar = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep3 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewComboBox = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewListBox = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewTable = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewTab = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep4 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewFrame = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewPanel = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep5 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewAppleMenu = new System.Windows.Forms.ToolStripMenuItem();
			this.miNewSep6 = new System.Windows.Forms.ToolStripSeparator();
			this.miNewCancel = new System.Windows.Forms.ToolStripMenuItem();
			this.mnNew.SuspendLayout();
			this.SuspendLayout();
			// 
			// mnNew
			// 
			this.mnNew.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miNewBase,
            this.miNewStatic,
            this.miNewImage,
            this.miNewSep0,
            this.miNewTextBox,
            this.miNewEditBox,
            this.miNewSep1,
            this.miNewButtonImage,
            this.miNewButtonText,
            this.miNewButtonRadio,
            this.miNewButtonCheck,
            this.miNewSep2,
            this.miNewProgressBar,
            this.miNewSliderBar,
            this.miNewScrollBar,
            this.miNewSep3,
            this.miNewComboBox,
            this.miNewListBox,
            this.miNewTable,
            this.miNewTab,
            this.miNewSep4,
            this.miNewFrame,
            this.miNewPanel,
            this.miNewSep5,
            this.miNewAppleMenu,
            this.miNewSep6,
            this.miNewCancel});
			this.mnNew.Name = "mnNewCom";
			this.mnNew.Size = new System.Drawing.Size(285, 508);
			// 
			// miNewBase
			// 
			this.miNewBase.Name = "miNewBase";
			this.miNewBase.ShortcutKeyDisplayString = "(无类型框)";
			this.miNewBase.Size = new System.Drawing.Size(284, 22);
			this.miNewBase.Text = "基本框(&Base)";
			this.miNewBase.Click += new System.EventHandler(this.miNewBase_Click);
			// 
			// miNewStatic
			// 
			this.miNewStatic.Name = "miNewStatic";
			this.miNewStatic.ShortcutKeyDisplayString = "(简单文本框)";
			this.miNewStatic.Size = new System.Drawing.Size(284, 22);
			this.miNewStatic.Text = "静态框(&Static)";
			this.miNewStatic.Click += new System.EventHandler(this.miNewStatic_Click);
			// 
			// miNewImage
			// 
			this.miNewImage.Name = "miNewImage";
			this.miNewImage.Size = new System.Drawing.Size(284, 22);
			this.miNewImage.Text = "图像框(&Image)";
			this.miNewImage.Click += new System.EventHandler(this.miNewImage_Click);
			// 
			// miNewSep0
			// 
			this.miNewSep0.Name = "miNewSep0";
			this.miNewSep0.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewTextBox
			// 
			this.miNewTextBox.Name = "miNewTextBox";
			this.miNewTextBox.Size = new System.Drawing.Size(284, 22);
			this.miNewTextBox.Text = "文本框(&TextBox)";
			this.miNewTextBox.Click += new System.EventHandler(this.miNewTextBox_Click);
			// 
			// miNewEditBox
			// 
			this.miNewEditBox.Name = "miNewEditBox";
			this.miNewEditBox.Size = new System.Drawing.Size(284, 22);
			this.miNewEditBox.Text = "编辑框(&EditBox)";
			this.miNewEditBox.Click += new System.EventHandler(this.miNewEditBox_Click);
			// 
			// miNewSep1
			// 
			this.miNewSep1.Name = "miNewSep1";
			this.miNewSep1.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewButtonImage
			// 
			this.miNewButtonImage.Name = "miNewButtonImage";
			this.miNewButtonImage.Size = new System.Drawing.Size(284, 22);
			this.miNewButtonImage.Text = "图片按钮(Ima&ge Button)";
			this.miNewButtonImage.Click += new System.EventHandler(this.miNewButtonImage_Click);
			// 
			// miNewButtonText
			// 
			this.miNewButtonText.Name = "miNewButtonText";
			this.miNewButtonText.Size = new System.Drawing.Size(284, 22);
			this.miNewButtonText.Text = "文字按钮(Te&xt Button)";
			this.miNewButtonText.Click += new System.EventHandler(this.miNewButtonText_Click);
			// 
			// miNewButtonRadio
			// 
			this.miNewButtonRadio.Name = "miNewButtonRadio";
			this.miNewButtonRadio.Size = new System.Drawing.Size(284, 22);
			this.miNewButtonRadio.Text = "单选框(&Radio Button)";
			this.miNewButtonRadio.Click += new System.EventHandler(this.miNewButtonRadio_Click);
			// 
			// miNewButtonCheck
			// 
			this.miNewButtonCheck.Name = "miNewButtonCheck";
			this.miNewButtonCheck.Size = new System.Drawing.Size(284, 22);
			this.miNewButtonCheck.Text = "多选框(&Check Button)";
			this.miNewButtonCheck.Click += new System.EventHandler(this.miNewButtonCheck_Click);
			// 
			// miNewSep2
			// 
			this.miNewSep2.Name = "miNewSep2";
			this.miNewSep2.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewProgressBar
			// 
			this.miNewProgressBar.Name = "miNewProgressBar";
			this.miNewProgressBar.Size = new System.Drawing.Size(284, 22);
			this.miNewProgressBar.Text = "进度条(&Progress Bar)";
			this.miNewProgressBar.Click += new System.EventHandler(this.miNewProgressBar_Click);
			// 
			// miNewSliderBar
			// 
			this.miNewSliderBar.Name = "miNewSliderBar";
			this.miNewSliderBar.Size = new System.Drawing.Size(284, 22);
			this.miNewSliderBar.Text = "滑动条(Sli&der Bar)";
			this.miNewSliderBar.Click += new System.EventHandler(this.miNewSliderBar_Click);
			// 
			// miNewScrollBar
			// 
			this.miNewScrollBar.Name = "miNewScrollBar";
			this.miNewScrollBar.Size = new System.Drawing.Size(284, 22);
			this.miNewScrollBar.Text = "滚动条(Scr&oll Bar)";
			this.miNewScrollBar.Click += new System.EventHandler(this.miNewScrollBar_Click);
			// 
			// miNewSep3
			// 
			this.miNewSep3.Name = "miNewSep3";
			this.miNewSep3.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewComboBox
			// 
			this.miNewComboBox.Name = "miNewComboBox";
			this.miNewComboBox.Size = new System.Drawing.Size(284, 22);
			this.miNewComboBox.Text = "组合框(Co&mboBox)";
			this.miNewComboBox.Click += new System.EventHandler(this.miNewComboBox_Click);
			// 
			// miNewListBox
			// 
			this.miNewListBox.Name = "miNewListBox";
			this.miNewListBox.Size = new System.Drawing.Size(284, 22);
			this.miNewListBox.Text = "列表框(&ListBox)";
			this.miNewListBox.Click += new System.EventHandler(this.miNewListBox_Click);
			// 
			// miNewTable
			// 
			this.miNewTable.Name = "miNewTable";
			this.miNewTable.Size = new System.Drawing.Size(284, 22);
			this.miNewTable.Text = "表格(T&able)";
			this.miNewTable.Click += new System.EventHandler(this.miNewTable_Click);
			// 
			// miNewTab
			// 
			this.miNewTab.Name = "miNewTab";
			this.miNewTab.Size = new System.Drawing.Size(284, 22);
			this.miNewTab.Text = "选项卡(Ta&b)";
			this.miNewTab.Click += new System.EventHandler(this.miNewTab_Click);
			// 
			// miNewSep4
			// 
			this.miNewSep4.Name = "miNewSep4";
			this.miNewSep4.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewFrame
			// 
			this.miNewFrame.Name = "miNewFrame";
			this.miNewFrame.Size = new System.Drawing.Size(284, 22);
			this.miNewFrame.Text = "窗口(&Frame)";
			this.miNewFrame.Click += new System.EventHandler(this.miNewFrame_Click);
			// 
			// miNewPanel
			// 
			this.miNewPanel.Name = "miNewPanel";
			this.miNewPanel.Size = new System.Drawing.Size(284, 22);
			this.miNewPanel.Text = "子窗口(Pa&nel)";
			this.miNewPanel.Click += new System.EventHandler(this.miNewPanel_Click);
			// 
			// miNewSep5
			// 
			this.miNewSep5.Name = "miNewSep5";
			this.miNewSep5.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewAppleMenu
			// 
			this.miNewAppleMenu.Enabled = false;
			this.miNewAppleMenu.Name = "miNewAppleMenu";
			this.miNewAppleMenu.ShortcutKeyDisplayString = "(不再支持)";
			this.miNewAppleMenu.Size = new System.Drawing.Size(284, 22);
			this.miNewAppleMenu.Text = "苹果按钮(AppleMen&u)";
			this.miNewAppleMenu.Click += new System.EventHandler(this.miNewAppleMenu_Click);
			// 
			// miNewSep6
			// 
			this.miNewSep6.Name = "miNewSep6";
			this.miNewSep6.Size = new System.Drawing.Size(281, 6);
			// 
			// miNewCancel
			// 
			this.miNewCancel.Name = "miNewCancel";
			this.miNewCancel.Size = new System.Drawing.Size(284, 22);
			this.miNewCancel.Text = "取消(ESC)";
			// 
			// CFormRender
			// 
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
			this.BackColor = System.Drawing.Color.Black;
			this.Name = "CFormRender";
			this.Size = new System.Drawing.Size(800, 600);
			this.mnNew.ResumeLayout(false);
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.ContextMenuStrip mnNew;
		private System.Windows.Forms.ToolStripMenuItem miNewBase;
		private System.Windows.Forms.ToolStripMenuItem miNewButtonImage;
		private System.Windows.Forms.ToolStripMenuItem miNewButtonText;
		private System.Windows.Forms.ToolStripMenuItem miNewButtonRadio;
		private System.Windows.Forms.ToolStripMenuItem miNewButtonCheck;
		private System.Windows.Forms.ToolStripSeparator miNewSep2;
		private System.Windows.Forms.ToolStripMenuItem miNewCancel;
		private System.Windows.Forms.ToolStripMenuItem miNewFrame;
		private System.Windows.Forms.ToolStripMenuItem miNewPanel;
		private System.Windows.Forms.ToolStripMenuItem miNewTable;
		private System.Windows.Forms.ToolStripMenuItem miNewTab;
		private System.Windows.Forms.ToolStripMenuItem miNewListBox;
		private System.Windows.Forms.ToolStripMenuItem miNewComboBox;
		private System.Windows.Forms.ToolStripMenuItem miNewTextBox;
		private System.Windows.Forms.ToolStripMenuItem miNewEditBox;
		private System.Windows.Forms.ToolStripSeparator miNewSep1;
		private System.Windows.Forms.ToolStripMenuItem miNewProgressBar;
		private System.Windows.Forms.ToolStripSeparator miNewSep4;
		private System.Windows.Forms.ToolStripSeparator miNewSep5;
		private System.Windows.Forms.ToolStripMenuItem miNewSliderBar;
		private System.Windows.Forms.ToolStripMenuItem miNewScrollBar;
		private System.Windows.Forms.ToolStripSeparator miNewSep3;
		private System.Windows.Forms.ToolStripMenuItem miNewImage;
		private System.Windows.Forms.ToolStripSeparator miNewSep0;
		private System.Windows.Forms.ToolStripMenuItem miNewStatic;
		private System.Windows.Forms.ToolStripMenuItem miNewAppleMenu;
		private System.Windows.Forms.ToolStripSeparator miNewSep6;
	}
}
