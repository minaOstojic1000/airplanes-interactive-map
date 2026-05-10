package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public abstract class InputDataForm extends Frame {
	
	public static final Color defaultFormBackColor = Color.LIGHT_GRAY;
	public static final Color defaultTextFColor = Color.BLACK;
	public static final Color errorColor = new Color(139, 0, 0);
	
	protected Button submit = new Button("SUBMIT");
	
	protected Panel makeLabelMessagePanel(Label label, Font labelFont, Color backColor, Color foreColor) {
		
		Panel labelPanel = new Panel();
		if (backColor == null) labelPanel.setBackground(defaultFormBackColor);
		else labelPanel.setBackground(backColor);
		
		label.setFont(labelFont);
		label.setForeground(foreColor);
		labelPanel.add(label);
		
		Panel marginPanel = new Panel(new BorderLayout());
		marginPanel.setBackground(labelPanel.getBackground());
		marginPanel.add(labelPanel, BorderLayout.SOUTH);
		marginPanel.add(new Panel(), BorderLayout.NORTH);
		marginPanel.getComponent(1).setPreferredSize(new Dimension(0, 20)); // North
		
		return marginPanel;
	}
	
	protected Panel makeTextPanel(TextField textf, String labelText, Color backColor) {
		
		Panel marginPanel = new Panel(new BorderLayout());
		Panel somePanel = new Panel(new GridLayout(1, 2, 5, 0));
		
		Label label = new Label(labelText.toUpperCase());
		if (backColor == null) backColor = defaultFormBackColor;
		somePanel.setBackground(backColor);
		label.setFont(AppWindow.defaultInputLabelFont);
		
		somePanel.add(label);
		somePanel.add(textf);
		
		marginPanel.add(somePanel, BorderLayout.CENTER);
		marginPanel.setBackground(somePanel.getBackground());
		
		marginPanel.add(new Panel(), BorderLayout.NORTH);
        marginPanel.add(new Panel(), BorderLayout.SOUTH);
        marginPanel.add(new Panel(), BorderLayout.EAST);
        marginPanel.add(new Panel(), BorderLayout.WEST);

        marginPanel.getComponent(0).setPreferredSize(new Dimension(0, 20)); // North
        marginPanel.getComponent(1).setPreferredSize(new Dimension(0, 20)); // South
        marginPanel.getComponent(2).setPreferredSize(new Dimension(20, 0)); // East
        marginPanel.getComponent(3).setPreferredSize(new Dimension(20, 0)); // West
		
		return marginPanel;
	}
	
	protected Panel makeButtonPanel(Button button) {
		Panel buttonPanel = new Panel();
		
		AppWindow.formatButton(button, AppWindow.defaultButtonHeight * 3/4, null, null, null, null);
		
		buttonPanel.setBackground(defaultFormBackColor);
		buttonPanel.add(button);
		return buttonPanel;
	}
	
	protected abstract boolean checkToSubmit();
	
	protected void enableDisableSubmit() {
		if (!checkToSubmit()) 
			submit.setEnabled(false);
		else submit.setEnabled(true);
	}
	
	protected abstract void clickSubmit();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
