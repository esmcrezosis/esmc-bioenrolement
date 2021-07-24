package com.esmc.client.dto;
import javax.swing.JOptionPane;

public class Dialog {
	
	  public static void main(String[] argv) throws Exception {

	    String[] buttons = { "Intranet", "Internet"};

	    int rc = JOptionPane.showOptionDialog(null, "Quel réseau utilisez-vous ?", "Confirmation",
	        JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);

	    //0 Intranet  1 Internet
	    
	  }
	
}
