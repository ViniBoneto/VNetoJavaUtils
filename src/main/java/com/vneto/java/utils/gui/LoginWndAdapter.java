package com.vneto.java.utils.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
//import javax.swing.JFrame;

class LoginWndAdapter extends WindowAdapter implements ActionListener {

	static final String OK_BTN_CMD = "ok_button"; 
	static final String CNCL_BTN_CMD = "cancel_button";
	
	String login, passwd;
	
	@Override
	public void windowClosing(WindowEvent e) {
		if(login == null)
			login = "";
		
		if(passwd == null)
			passwd = "";

		super.windowClosing(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch( e.getActionCommand().toLowerCase() ) {
			case OK_BTN_CMD:
					JButton btn = (JButton)e.getSource();
					Window wnd;
					String[] ret = AppGUIManager.retrieveWndFields(wnd = (Window)btn.getTopLevelAncestor());
					
					if(ret == null || ret.length != 2)
						throw new IllegalArgumentException("Método AppGUIManager.retrieveWndFields() retornou parâmetros "
								+ "inválidos ao tentar obter login/senha a partir de entradas do usuário, na tela de login");
					
					login = ret[0];
					passwd = ret[1];
					wnd.dispatchEvent(new WindowEvent(wnd, WindowEvent.WINDOW_CLOSING));
				break;
			case CNCL_BTN_CMD:
					btn = (JButton)e.getSource();
					wnd = (Window)btn.getTopLevelAncestor();
					wnd.dispatchEvent(new WindowEvent(wnd, WindowEvent.WINDOW_CLOSING));
				break;
		}
	}
}
