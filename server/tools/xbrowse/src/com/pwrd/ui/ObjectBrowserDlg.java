package com.pwrd.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.JavaLibraryLoader;
import com.pwrd.io.SuffixFileFilter;
import com.pwrd.reflect.model.EnumWrapper;
import com.pwrd.reflect.model.ListWrapper;
import com.pwrd.reflect.model.MapWrapper;
import com.pwrd.reflect.model.ObjectWrapper;
import com.pwrd.reflect.model.PrimitiveWrapper;
import com.pwrd.reflect.model.SetWrapper;
import com.pwrd.reflect.model.StringWrapper;
import com.pwrd.resources.ConstantIcons;
import com.pwrd.util.Toolkit;

public class ObjectBrowserDlg extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ObjectBrowserDlg.class);
	private static int serialId = 1;
	
	private DefaultTreeModel objTreeModel;
	private DefaultMutableTreeNode root;
	
	public ObjectBrowserDlg(Frame owner, boolean modal)
	{
		super(owner, modal);
		setTitle("Object Browser " + (serialId ++));
		setBounds(400, 200, 600, 700);
		
		setMenuBar();
		
		root = new DefaultMutableTreeNode("Root");
		objTreeModel = new DefaultTreeModel(root);
		JTree objTree = new JTree();
		objTree.setEditable(true);
		objTree.setModel(objTreeModel);
		objTree.addMouseListener(new ObjectTreeMouseListener());
		objTree.setCellRenderer(new ObjectTreeCellRenderer());
		objTree.setCellEditor(new ObjectTreeCellEditor(objTree, (DefaultTreeCellRenderer) objTree.getCellRenderer()));
		JScrollPane objScroll = new JScrollPane(objTree);
		objScroll.setBounds(0, 0, getWidth() - 20, 600);
		
		JButton saveBtn = new JButton("Save");
		saveBtn.setBounds(390, 10, 80, 25);
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(root != null && root.getUserObject() instanceof ObjectWrapper)
				{
					ObjectOutputStream oos;
					try
					{
						oos = new ObjectOutputStream(new FileOutputStream("root.obj"));
						ObjectWrapper w = (ObjectWrapper)root.getUserObject();
						oos.writeObject(w.getValue());
						JOptionPane.showMessageDialog(ObjectBrowserDlg.this, "Save to root.obj successfully.");
					} catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
			
		});
		JButton closeBtn = new JButton("Close");
		closeBtn.setBounds(490, 10, 80, 25);
		closeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ObjectBrowserDlg.this.setVisible(false);
			}
			
		});
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(null);
		btnPanel.setBounds(0, 600, getWidth(), 100);
		btnPanel.add(saveBtn);
		btnPanel.add(closeBtn);
		
		getContentPane().setLayout(null);
		getContentPane().add(objScroll);
		getContentPane().add(btnPanel);
	}
	
	private void setMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		// setAccelerator() is not defined for JMenu
		// fileMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
		JMenuItem loadLibraryItem = new JMenuItem("Load Library");
		loadLibraryItem.setMnemonic(KeyEvent.VK_L);
		loadLibraryItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		loadLibraryItem.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser jarChooser = new JFileChooser(System.getProperty("user.dir"));
				jarChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jarChooser.setMultiSelectionEnabled(true);
				jarChooser.setFileFilter(new FileFilter() {
					private java.io.FileFilter filter = new SuffixFileFilter("jar");
					
					@Override
					public boolean accept(File f)
					{
						return filter.accept(f);
					}

					@Override
					public String getDescription()
					{
						return null;
					}
					
				});
				// logger.info(jarChooser.isMultiSelectionEnabled());
				if(jarChooser.showOpenDialog(ObjectBrowserDlg.this) == JFileChooser.APPROVE_OPTION)
				{
					File[] jarFiles = jarChooser.getSelectedFiles();
					for(File jarFile : jarFiles)
						JavaLibraryLoader.load(jarFile);
				}
			}
			
		});
		JMenuItem loadObjItem = new JMenuItem("Load Serializable File");
		loadObjItem.setMnemonic(KeyEvent.VK_S);
		loadObjItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		loadObjItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser objChooser = new JFileChooser(System.getProperty("user.dir"));
				objChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(objChooser.showOpenDialog(ObjectBrowserDlg.this) == JFileChooser.APPROVE_OPTION)
				{
					File objFile = objChooser.getSelectedFile();
					ObjectInputStream ois = null;
					try
					{
						ois = new ObjectInputStream(new FileInputStream(objFile));
						Object seriObj = ois.readObject();
						ObjectWrapper w = ObjectWrapper.createObjectWrapper(seriObj.getClass().getName(), seriObj, null, null);
						root.setUserObject(w);
						root.removeAllChildren();
						objTreeModel.nodeStructureChanged(root);
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
					} 
					catch (ClassNotFoundException e1)
					{
						e1.printStackTrace();
						JOptionPane.showMessageDialog(ObjectBrowserDlg.this, "Please make sure you have loaded the corresponding library",
								"Class not found", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.setMnemonic(KeyEvent.VK_C);
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		closeItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
			
		});
		fileMenu.add(loadLibraryItem);
		fileMenu.add(loadObjItem);
		fileMenu.add(closeItem);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);
	}
	
	private static class ObjectTreeMouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			// Èç¹ûÊÇ×ó¼üË«»÷
			if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
			{
				JTree objTree = (JTree)e.getSource();
				TreePath selectedPath = objTree.getSelectionPath();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
				if(selectedNode == null)
					return;
				if(selectedNode.isLeaf())
				{
					if(selectedNode.getUserObject() instanceof ObjectWrapper)
					{
						ObjectWrapper ow = (ObjectWrapper) selectedNode.getUserObject();
						for(ObjectWrapper newW : ow.getFields().values())
						{
							DefaultMutableTreeNode child = new DefaultMutableTreeNode(newW);
							selectedNode.add(child);
							((DefaultTreeModel)objTree.getModel()).nodeStructureChanged(selectedNode);
							objTree.expandPath(selectedPath);
						}
					}
					else
						JOptionPane.showMessageDialog(objTree, "Please load a serialization file firstly.");
				}
			}
		}
	}
	
	private class ObjectTreeCellEditor extends DefaultTreeCellEditor
	{
		private JTextField textField;
		private ObjectWrapper objW;
		
		public ObjectTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer)
		{
			super(tree, renderer);
			addCellEditorListener(new CellEditorListener()
			{
				
				@Override
				public void editingStopped(ChangeEvent e)
				{
					logger.info(e.getSource());
				}
				
				@Override
				public void editingCanceled(ChangeEvent e)
				{
					// logger.info(objW);
					// logger.info(getCellEditorValue());
					if(objW != null && objW.getParent() != null)
					{
						if(objW instanceof StringWrapper || objW instanceof EnumWrapper || objW instanceof PrimitiveWrapper)
						{
							objW.getParent().setChildValue(objW, Toolkit.convert(getCellEditorValue().toString(), objW.getValue()));
							objW.setValue(Toolkit.convert(getCellEditorValue().toString(), objW.getValue()));
						}
					}
					// logger.info(objW);
				}
			});
		}
		
		public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) 
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			objW = (ObjectWrapper)node.getUserObject();
			if(objW instanceof StringWrapper || objW instanceof EnumWrapper || objW instanceof PrimitiveWrapper)
			{
				textField = new JTextField(objW.getValue().toString());
			}
			else
			{
				textField = new JTextField(objW.toString());
				textField.setEditable(false);
			}
				
			return textField;
		}
		
		public Object getCellEditorValue()
		{
			return textField.getText();
		}
	}
	
	private class ObjectTreeCellRenderer extends DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			Object obj = node.getUserObject();
			if(obj instanceof ObjectWrapper)
			{
				if(obj instanceof MapWrapper)
					setIcon(ConstantIcons.getIcon("map"));
				else if(obj instanceof SetWrapper)
					setIcon(ConstantIcons.getIcon("set"));
				else if(obj instanceof StringWrapper)
					setIcon(ConstantIcons.getIcon("str"));
				else if(obj instanceof EnumWrapper)
					setIcon(ConstantIcons.getIcon("enum"));
				else if(obj instanceof ListWrapper)
					setIcon(ConstantIcons.getIcon("list"));
				else if(obj instanceof PrimitiveWrapper)
					setIcon(ConstantIcons.getIcon("primitive"));
				else
					setIcon(ConstantIcons.getIcon("custom"));
			}
			return this;
		}
	}
}
