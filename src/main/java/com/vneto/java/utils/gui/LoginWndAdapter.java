package com.vneto.java.utils.gui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

class LoginWndAdapter extends WindowAdapter {

	String login, passwd;
	
	@Override
	public void windowClosing(WindowEvent e) {
		
//		super.windowClosing(e);
		JFrame frame = (JFrame)e.getSource();
		String[] ret = AppGUIManager.retrieveWndFields((Window)frame);
		
		if(ret == null || ret.length != 2)
			throw new IllegalArgumentException("Método AppGUIManager.retrieveWndFields() retornou parâmetros "
					+ "inválidos ao tentar obter login/senha a partir de entradas do usuário, na tela de login");
		
		login = ret[0];
		passwd = ret[1];
		
		super.windowClosing(e);
	}
	
}
