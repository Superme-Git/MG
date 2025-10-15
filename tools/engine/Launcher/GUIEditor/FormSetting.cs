using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class FormSetting : Form
	{
		public int InitSizeIndex
		{
			get { return cbbInitSize.SelectedIndex; }
			set { cbbInitSize.SelectedIndex = value; }
		}

		public int ImageOrder
		{
			get
			{
				if(rbImageOrder0.Checked)
					return 0;
				else if(rbImageOrder1.Checked)
					return 1;
				else
					return 0;
			}
			set
			{
				switch(value)
				{
					case 0: rbImageOrder0.Checked = true; break;
					case 1: rbImageOrder1.Checked = true; break;
					default: rbImageOrder0.Checked = true; break;
				}
			}
		}

		public string SaveCoding
		{
			get
			{
				if(rbUTF8.Checked)
					return "utf-8";
				else if(rbUTF16.Checked)
					return "utf-16";
				else if(rbGB2312.Checked)
					return "gb2312";
				else
					return "utf-8";
			}
			set
			{
				switch(value.ToLower())
				{
					case "utf-8": rbUTF8.Checked = true; break;
					case "utf-16": rbUTF16.Checked = true; break;
					case "gb2312": rbGB2312.Checked = true; break;
					default: rbUTF8.Checked = true; break;
				}
			}
		}

		public FormSetting()
		{
			InitializeComponent();

			Size[] sizes = Program.GetMainForm().Panel.Desktop.AllSize;
			foreach(Size size in sizes)
				cbbInitSize.Items.Add(size.Width + " x " + size.Height);

			InitSizeIndex = 0;
			ImageOrder = 0;
			SaveCoding = "utf-8";
		}
	}
}
