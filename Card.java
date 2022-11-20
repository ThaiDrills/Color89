package com.thaidrills.apps.Color89;
/********************************************************************
* @(#)Card.java 1.00 2010
* Copyright 2010 by Richard T. Salamone, Jr. All rights reserved.
*
* Card: http://download.oracle.com/javase/1.5.0/docs/guide/language/enums.html
*
* @author Rick Salamone
* @version 1.00, 2010
*******************************************************/
import java.util.*;
import javax.swing.ImageIcon;

public class Card
	implements Comparable<Card>
	{
	private static final String DIR = "/com/thaidrills/apps/Color89/resources/cards/";
	public static final java.awt.Dimension SIZE= new java.awt.Dimension(73,96);
	public static ImageIcon BLANK;
	public static ImageIcon BACK;
	static
		{
		try
			{
			BLANK = new ImageIcon ( Card.class.getResource(DIR+"blank.gif"));
			BACK = new ImageIcon ( Card.class.getResource(DIR+"deck.gif"));
			}
		catch (Exception e) {System.out.println(e.toString());}
		}

//	public enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN,
//	                    EIGHT, NINE, TEN, JACK, QUEEN, KING }

	public enum Suit { spade, heart, club, diamond }

	private final int rank;
	private final Suit suit;
	private Card(int rank, Suit suit)
		{
		this.rank = rank;
		this.suit = suit;
		}

	@Override public int compareTo(Card aOther)
		{
		int rankTest = rank - aOther.rank;
		if ( rankTest == 0 )
			return suit.compareTo(aOther.suit);
		else return rankTest;
		}

	public int rank() { return rank; }
	public Suit suit() { return suit; }
	public String toString() { return "" + rank + " of " + suit + "s"; }

	public ImageIcon icon()
		{
		String url = DIR + suit + rank + ".gif";
		try
			{
			return new ImageIcon ( getClass().getResource(url));
			}
		catch (Exception e) { System.out.println("not found: " + url); return null; }
		}

	private static final List<Card> protoDeck = new ArrayList<Card>();

	// Initialize prototype deck
	static
		{
		for (Suit suit : Suit.values())
			for (int rank = 1; rank <= 13; rank++)
				protoDeck.add(new Card(rank, suit));
		}

	public static ArrayList<Card> newDeck()
		{
		return new ArrayList<Card>(protoDeck); // Return copy of prototype deck
		}
	}
