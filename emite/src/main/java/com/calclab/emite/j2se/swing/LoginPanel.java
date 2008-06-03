/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
    public static interface LoginPanelListener {
	void onLogin(String httpBase, String domain, String userName, String password);

	void onLogout();
    }

    private JButton btnLogin;
    private JButton btnLogout;
    private JTextField fieldDomain;
    private JTextField fieldName;
    private JPasswordField fieldPassword;
    private JLabel labelState;
    private final LoginPanelListener listener;
    private JComboBox selectConfiguration;
    private JTextField fieldHttpBase;

    public LoginPanel(final LoginPanelListener listener) {
	super(new BorderLayout());
	this.listener = listener;
	init();
    }

    public void addConfiguration(final ConnectionConfiguration connectionConfiguration) {
	selectConfiguration.addItem(connectionConfiguration);
    }

    public void showState(final String message, final boolean isConnected) {
	labelState.setText(message + "(connected: " + isConnected + ")");
	btnLogin.setEnabled(!isConnected);
	btnLogout.setEnabled(isConnected);
    }

    private void init() {
	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	selectConfiguration = new JComboBox();
	selectConfiguration.addItemListener(new ItemListener() {
	    public void itemStateChanged(final ItemEvent e) {
		final ConnectionConfiguration config = (ConnectionConfiguration) selectConfiguration.getSelectedItem();
		fieldHttpBase.setText(config.httpBase);
		fieldDomain.setText(config.domain);
		fieldName.setText(config.userName);
		fieldPassword.setText(config.password);
	    }

	});
	panel.add(selectConfiguration);

	btnLogin = new JButton("login");
	btnLogin.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onLogin(fieldHttpBase.getText(), fieldDomain.getText(), fieldName.getText(), new String(
			fieldPassword.getPassword()));
	    }
	});
	btnLogout = new JButton("logout");
	btnLogout.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onLogout();
	    }
	});
	panel.add(btnLogin);
	panel.add(btnLogout);

	labelState = new JLabel("current state: none (connected: false)");
	panel.add(labelState);

	final JPanel panelFields = new JPanel(new GridLayout(1, 4));
	panelFields.setMinimumSize(new Dimension(200, 1));
	fieldHttpBase = new JTextField("http url here");
	fieldDomain = new JTextField("domain here");
	fieldName = new JTextField("user name here");
	fieldPassword = new JPasswordField("password here");
	panelFields.add(fieldHttpBase);
	panelFields.add(fieldDomain);
	panelFields.add(fieldName);
	panelFields.add(fieldPassword);

	add(panel, BorderLayout.NORTH);
	add(panelFields);

    }
}