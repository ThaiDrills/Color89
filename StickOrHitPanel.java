package com.thaidrills.apps.Color89;
/********************************************************************
* @(#)StickOrHitPanel.java 1.00 20111118
* Copyright (c) 2011 by Richard T. Salamone, Jr. All rights reserved.
*
* StickOrHitPanel: Extends SBRadioPanel to prompt a player to stick or
* hit in the Color89 card game.
*
* @version 1.00
* @author Rick Salamone
* 20111118 rts created
*******************************************************/
import com.shanebow.ui.SBRadioPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StickOrHitPanel
	extends SBRadioPanel<String>
	{
	public static final int HIT = 0;
	public static final int STICK = 1;
	private static final String[] CHOICES = { "Hit", "Stick" };
//	private boolean      fAudioEnabled = true;

	public StickOrHitPanel()
		{
		super(1, CHOICES);
		}

	/**
	* Returns true if the user has answered the question
	*/
	public boolean hasResponse() { return getSelected() != null; }

	// return getSelectedIndex()

	public void reset()
		{
		clearSelection();
		setEnabled(true);
		}
	}
