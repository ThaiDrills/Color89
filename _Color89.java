package com.thaidrills.apps.Color89;
/********************************************************************
* @(#)_Color89.java 1.00 20100728
* Copyright (c) 2010-2011 by Richard T. Salamone, Jr. All rights reserved.
*
* _Color89: The main container frame for the _Color89 application.
*
* @author Rick Salamone
* @version 1.00 20111118 rts created
*******************************************************/
import com.shanebow.ui.BareBonesBrowserLaunch;
import com.shanebow.ui.menu.*;
import com.shanebow.ui.LAF;
import com.shanebow.ui.SBAction;
import com.shanebow.util.SBProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class _Color89
	extends JFrame
	{
	private static long blowUp = 0; // com.shanebow.util.SBDate.toTime("20111121  23:59");
	private static final String HELP_URL="http://thaidrills.com/pub/thai-culture/thai-games/Color89.html";

	public static void main(String[] args)
		{
		SBProperties.load(_Color89.class, "com/thaidrills/resources/thai.properties");
		LAF.initLAF(blowUp, true);
		new _Color89();
		}

	public _Color89()
		{
		SBProperties props = SBProperties.getInstance();
		setTitle(props.getProperty("app.name")
		        + " " + props.getProperty("app.version"));
		setIconImage(new ImageIcon(getClass().getResource( 
            "/" + props.getProperty("app.icon", ""))).getImage());
		setBounds(props.getRectangle("usr.app.bounds", 50,50,630,700)); // x,y,w,h
		buildContent();
		buildMenus();
		setVisible(true);
		}

	private void buildContent()
		{
		getContentPane().add(new GamePanel(), java.awt.BorderLayout.CENTER);
		}

	private void buildMenus()
		{
		SBMenuBar menuBar = new SBMenuBar();
		menuBar.addMenu("File",
			new SBViewLogAction(this), null,
			LAF.setExitAction(new com.shanebow.ui.SBExitAction(this)
				{
				public void doApplicationCleanup() {}
				})
			);
		menuBar.addMenu("Settings",
			menuBar.getThemeMenu()
			);
		menuBar.addMenu("Help",
			new SBAction("Online Help", 'O', "View Application Help @ ThaiDrills.com", null)
				{
				@Override public void actionPerformed(ActionEvent e)
					{
//					BareBonesBrowserLaunch.openURL(HELP_URL);
					}
				},
			new SBAboutAction(this)
			);
		setJMenuBar(menuBar);
		}
	}
