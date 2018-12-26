package com.vneto.java.utils.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.text.JTextComponent;

public class AppGUIManager {
	
	
	public static String[] showLoginWnd(Component parent, boolean singleWnd, String title, String msg) throws InterruptedException {
		String[] ret = null;
		
		if(title == null || title.isEmpty())
			title = "Login";

		if(singleWnd)
			ret = showSingleLoginWnd(parent, title, msg);
		else
			ret = showDblLoginWnd(parent, title, msg);
		
		return ret;
	}

	static String[] showSingleLoginWnd(Component parent, String title,
			String msg) throws InterruptedException {
		
		JFrame wnd = createFormLikeFrame(parent, title, msg, new String[]{"Login", "Password"}, new boolean[]{false, true}, new String[]{"Ok", "Cancel"});
		LoginWndAdapter logAdapt = new LoginWndAdapter(); 
		wnd.addWindowListener(logAdapt);
		
		wnd.pack();
		wnd.setVisible(true);
		
		do {
			
			Thread.sleep(3000);
			
		}while( wnd.isShowing() );
		
		String[] ret = new String[]{logAdapt.login, logAdapt.passwd};
		
		return ret;
	}

	static String[] showDblLoginWnd(Component parent, String title, String msg) {
		
		boolean msgNull = msg == null || msg.isEmpty();
		
		String login = JOptionPane.showInputDialog(parent, (msgNull) ? "Login" : msg, title);
		String psswd = JOptionPane.showInputDialog(parent, (msgNull) ? "Password" : msg, title);
		
		return new String[]{login, psswd};
	}

	static JFrame createFormLikeFrame(Component parent, String title, String msg, String[] labels, boolean[] isPasswd,
			String[] options) {
		
		JLabel lbl;
		JTextField txtFld;
		JButton btn;
		JFrame frame = new JFrame(title);
		JPanel panelFlds, panelBtns;
		int i = 0;
		
		if(labels.length != isPasswd.length)
			throw new IllegalArgumentException("Erro na criação de janela de form - função AppGUIManager.createFormLikeFrame() - pois a cardinalidade"
					+ " do vetor de rótulos de campos (labels[]) deve ser o mesmo do vetor de booleanos, que indica se o campo é de senha ou não (isPasswd[])");
		
		if(msg != null && !msg.isEmpty()) {
			lbl = new JLabel(msg);
			frame.add(lbl, BorderLayout.NORTH);
		}
		
		if(labels.length > 0) {
			
			panelFlds = new JPanel();
			panelFlds.setLayout(new GridLayout(labels.length, 2));
			
			frame.add(panelFlds);
					
			for (String string : labels) {
				lbl = new JLabel(string);
				panelFlds.add(lbl);
				
				if(isPasswd[i++] == true)
					txtFld = new JPasswordField();
				else	
					txtFld = new JTextField();
				
				panelFlds.add(txtFld);
			}
		}
			
		if(options.length > 0) {
			panelBtns = new JPanel();
			panelBtns.setLayout(new FlowLayout(FlowLayout.CENTER));
			frame.add(panelBtns, BorderLayout.SOUTH);
			
			for (String string : options) {
				btn = new JButton(string);
				panelBtns.add(btn);
			}
		}
			
		return frame;
	}
	
	static String[] retrieveWndFields(Window wnd) {
		return retrieveWndFields( (Container)wnd ); 
	}
	
	static String[] retrieveWndFields(Container cont) {
		
		List<String> fields = new ArrayList<String>();
		
		if( !(cont instanceof RootPaneContainer) ) {
		
			for (Component comp : cont.getComponents()) {
				
				if(comp instanceof JTextComponent)
					fields.add( ((JTextComponent)comp).getText() );
				
				if( ( comp instanceof Container) && ( ( (Container)comp ).getComponents().length > 0 ) )
					fields.addAll( Arrays.asList(retrieveWndFields((Container)comp)) );
			}
		}
		else {
			JRootPane rootPane = ((RootPaneContainer) cont).getRootPane();
			Container contPane = rootPane.getContentPane();
			
			for (Component comp : contPane.getComponents()) {
		
				if(comp instanceof JTextComponent)
					fields.add( ((JTextComponent)comp).getText() );
				
				if( ( comp instanceof Container) && ( ( (Container)comp ).getComponents().length > 0 ) )
					fields.addAll( Arrays.asList(retrieveWndFields((Container)comp)) );
			}
		}
		
		return (String[]) fields.toArray();
	}
}
