package com.thaidrills.apps.Color89;
/********************************************************************
* @(#)Hand.java 1.00 20111119
* Copyright (c) 2011 by Richard T. Salamone, Jr. All rights reserved.
*
* Hand: Represents one hand in the card game Color89. Extends JPanal
* to display the cards, and contains logic for calculating the hand's
* value and betting multiplier.
*
* @version 1.00
* @author Rick Salamone
* 20111119 rts decoupled from GamePanel
*******************************************************/
import java.awt.GridLayout;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

final class Hand
	extends JPanel
	{
	public static final int MAX_CARDS = 3;
	private final Card[] fHand = new Card[MAX_CARDS];
	private final JLabel[] fCards = new JLabel[MAX_CARDS];
	private int fScore;
	private int fNumCards;
	private boolean fHidden;

	Hand()
		{
		super(new GridLayout(1,0, 2, 2));
		for ( int i = 0; i < MAX_CARDS; i++ )
			{
			add(fCards[i] = new JLabel());
			fCards[i].setPreferredSize(Card.SIZE);
			}
		}

	public void reset()
		{
		fNumCards = 0;
		fScore=0;
		for ( int i = 0; i < MAX_CARDS; i++ )
			{
			fHand[i] = null;
			fCards[i].setIcon(null);
			}
		}

	public void deal(Card aCard)
		{
		int value = (aCard.rank() < 10)? aCard.rank() : 0;
		fScore += value;
		fHand[fNumCards] = aCard;
		fCards[fNumCards].setIcon(fHidden? Card.BACK : fHand[fNumCards].icon());
		++fNumCards;
		}

	public boolean isHidden() { return fHidden; }

	public void setHidden(boolean on)
		{
		fHidden = on;
		for ( int i = 0; i < fNumCards; i++ )
			fCards[i].setIcon(fHidden? Card.BACK : fHand[i].icon());
		}

	public int getScore() { return fScore % 10; }

	public boolean isFlush()
		{
		Card.Suit suit = fHand[0].suit();
		for ( int i = 1; i < fNumCards; i++ )
			if ( !fHand[i].suit().equals(suit))
				return false;
		return true;
		}

	public boolean isStraight()
		{
		if ( fNumCards < 3 )
			return false;
		List<Card> sortedHand = Arrays.asList(fHand);
		Collections.sort( sortedHand );
		int rank = sortedHand.get(0).rank();
		for ( Card card : sortedHand )
			if ( card.rank() != rank++ )
				return false;
		return true;
		}

	public boolean allSameRank()
		{
		int rank = fHand[0].rank();
		for ( int i = 1; i < fNumCards; i++ )
			if ( fHand[i].rank() != rank )
				return false;
		return true;
		}
	}
