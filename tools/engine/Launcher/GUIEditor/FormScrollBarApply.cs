using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public partial class FormScrollBarApply : Form
	{
		private string m_path;
		private string m_sbname;
		private string m_sbclassname;
		private string m_sbautoblock;
		private string m_sbinner;

		public FormScrollBarApply(string sbname)
		{
			InitializeComponent();

			m_path = Program.GetMainForm().XMLPath;
			m_sbname = sbname;
			GUIEditor2.CScrollBarProfile.CScrollBarParam sbp = CScrollBarProfile.FindH(sbname);
			if(sbp == null)
			{
				sbp = CScrollBarProfile.FindV(sbname);
				if(sbp == null)
				{
					tbLog.AppendText("ScrollBarProfile(滚动条配置)中无法找到: " + sbname);
					bnAnalyse.Enabled = false;
					bnUpdate.Enabled = false;
					return;
				}
			}
			m_sbclassname = sbp.classname;
			m_sbautoblock = sbp.autoblock;
			if(m_sbautoblock == null) m_sbautoblock = "true";
			m_sbinner = sbp.innerxml;
			if(m_sbclassname == null || m_sbinner == null || m_sbinner.Length <= 0)
			{
				tbLog.AppendText("滚动条配置为空: " + sbname);
				bnAnalyse.Enabled = false;
				bnUpdate.Enabled = false;
				return;
			}
		}

		private void AnalyseXML(XmlElement elem, ref int num)
		{
			if(elem.Name == "ScrollBar" && elem.GetAttribute("n") == m_sbname)
				++num;
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node.NodeType == XmlNodeType.Element)
					AnalyseXML(node as XmlElement, ref num);
			}
		}

		private int AnalyseFile(string filename)
		{
			if(filename.EndsWith("ScrollBarProfile.xml", StringComparison.CurrentCultureIgnoreCase))
				return 0;

			try
			{
				int num = 0;
				XmlDocument doc = new XmlDocument();			
				doc.Load(filename);
				XmlElement desktop = doc["Desktop"];
				if(desktop == null)
					return 0;
				XmlElement content = desktop["Content"];
				if(content == null)
					return 0;
				AnalyseXML(content, ref num);
				if(num > 0)
				{
					if(filename.StartsWith(m_path, StringComparison.CurrentCultureIgnoreCase))
						filename = filename.Substring(m_path.Length);
					tbLog.AppendText(filename + " -> " + num + "处\n");
					tbLog.ScrollToCaret();
				}
				return num;
			}
			catch
			{
				return 0;
			}
		}

		private int AnalyseDirectory(string dir)
		{
			int num = 0;
			string[] filenames = System.IO.Directory.GetFiles(dir, "*.xml");
			foreach(string filename in filenames)
			{
				num += AnalyseFile(filename);
			}
			string[] dirnames = System.IO.Directory.GetDirectories(dir);
			foreach(string dirname in dirnames)
			{
				num += AnalyseDirectory(dirname);
			}
			return num;
		}

		private void UpdateXML(XmlElement elem, ref int num)
		{
			if(elem.Name == "ScrollBar" && elem.GetAttribute("n") == m_sbname)
			{
				if(elem.GetAttribute("class") != m_sbclassname || elem.GetAttribute("autoblock") != m_sbautoblock || elem.InnerXml != m_sbinner)
				{
					elem.SetAttribute("class", m_sbclassname);
					elem.SetAttribute("autoblock", m_sbautoblock);
					elem.InnerXml = m_sbinner;
					++num;
				}
			}
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node.NodeType == XmlNodeType.Element)
					UpdateXML(node as XmlElement, ref num);
			}
		}

		private int UpdateFile(string filename)
		{
			if(filename.EndsWith("ScrollBarProfile.xml", StringComparison.CurrentCultureIgnoreCase))
				return 0;

			try
			{
				int num = 0;
				XmlDocument doc = new XmlDocument();			
				doc.Load(filename);
				XmlElement desktop = doc["Desktop"];
				if(desktop == null)
					return 0;
				XmlElement content = desktop["Content"];
				if(content == null)
					return 0;
				UpdateXML(content, ref num);
				if(num > 0)
				{
					doc.Save(filename);
					if(filename.StartsWith(m_path, StringComparison.CurrentCultureIgnoreCase))
						filename = filename.Substring(m_path.Length);
					tbLog.AppendText(filename + " -> " + num + "处更新\n");
					tbLog.ScrollToCaret();
				}
				return num;
			}
			catch
			{
				return 0;
			}
		}

		private int UpdateDirectory(string dir)
		{
			int num = 0;
			string[] filenames = System.IO.Directory.GetFiles(dir, "*.xml");
			foreach(string filename in filenames)
			{
				num += UpdateFile(filename);
			}
			string[] dirnames = System.IO.Directory.GetDirectories(dir);
			foreach(string dirname in dirnames)
			{
				num += UpdateDirectory(dirname);
			}
			return num;
		}

		private void bnAnalyse_Click(object sender, EventArgs e)
		{
			Cursor.Current = Cursors.WaitCursor;
			tbLog.Clear();
			int num = AnalyseDirectory(Program.GetMainForm().XMLPath);
			tbLog.AppendText("分析完成! 共找到" + num + "处");
			tbLog.ScrollToCaret();
			Cursor.Current = Cursors.Default;
		}

		private void bnUpdate_Click(object sender, EventArgs e)
		{
			Cursor.Current = Cursors.WaitCursor;
			tbLog.Clear();
			int num = UpdateDirectory(Program.GetMainForm().XMLPath);
			tbLog.AppendText("更新完成! 共修改" + num + "处");
			tbLog.ScrollToCaret();
			Cursor.Current = Cursors.Default;
		}
	}
}
