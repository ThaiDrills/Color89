package com.thaidrills.apps.Color89;
/********************************************************************
* @(#)_Color89.java 1.00 20111118
* Copyright (c) 2010-2011 by Richard T. Salamone, Jr. All rights reserved.
*
* _Color89: The main container frame for the _Color89 application.
*
* @version 1.00
* @author Rick Salamone
* 20111118 rts created
*******************************************************/
import com.shanebow.ui.LAFlet;
import com.shanebow.ui.SBAction;
import com.shanebow.util.SBProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class GamePanel
	extends JPanel
	{
	private static int NUM_PLAYERS = 4;
	private static final int CARDS_PER_HAND = 2;
	private Player[] fPlayers = new Player[NUM_PLAYERS];
	private Player fDealer;

	private final java.util.List<Card> fDeck = Card.newDeck();
	private int fDeckIndex;

	private final JTextArea fPlayByPlay = new JTextArea(5, 30);

	public GamePanel()
		{
		super(new BorderLayout());
		add(playersPanel(), BorderLayout.CENTER);
		add(controlPanel(), BorderLayout.SOUTH);
		setBorder(LAFlet.getStandardBorder());
		}

	private JComponent controlPanel()
		{
		JPanel it = new JPanel(new BorderLayout());
		it.add(LAFlet.titled("Play by Play", new JScrollPane(fPlayByPlay)),
		                      BorderLayout.CENTER);
		it.add(LAFlet.titled("Actions", buttonPanel()), BorderLayout.EAST);
		return it;
		}

	private void announce( Player aPlayer, String aAction )
		{
		String who = "";
		if ( aPlayer != null )
			who += aPlayer.getName() + ": ";
		fPlayByPlay.append(who + aAction + "\n");
		}

	private JComponent buttonPanel()
		{
		JPanel it = new JPanel(new GridLayout(0,1,0,6));
		it.add(new SBAction("Deal", 'D', "Play a new game", null)
			{
			@Override public void actionPerformed(ActionEvent e) { deal(); }
			}.makeButton());
		it.add(new SBAction("Call", 'C', "Tally scores", null)
			{
			@Override public void actionPerformed(ActionEvent e) { call(); }
			}.makeButton());
		return it;
		}

	private JComponent playersPanel()
		{
		JPanel it = new JPanel(new GridLayout(0,1));
		for ( int i = 0; i < NUM_PLAYERS; i++ )
			{
			boolean isDealer = (i==0);
			String name = isDealer? "Dealer" : "Player " + i;
			it.add(LAFlet.titled(name, fPlayers[i] = new Player(isDealer)));
			fPlayers[i].setName(name);
			}
		fDealer = fPlayers[0];
		return it;
		}

	public Card nextCard() { return fDeck.get(fDeckIndex++); }

	private void deal()
		{
		fPlayByPlay.setText("");
		Collections.shuffle(fDeck);
		fDeckIndex = 0;
		for (Player player : fPlayers)
			player.reset();
		fDealer.setHidden(true);
		announce(null, "The deck is shuffled and each player is dealt 2 cards");
		for (int i = 0; i < CARDS_PER_HAND; i++ )
			for (Player player : fPlayers)
				player.deal(nextCard());
		promptHitOrStick();

		if ( fDealer.getScore() < 5 )
			{
			announce(fDealer, "must hit!");
			fDealer.deal(nextCard());
			}
		}

	private void promptHitOrStick()
		{
		for (int i = 0; i < fPlayers.length; i++)
			{
			Player player = fPlayers[i];
			if ( player.equals(fDealer)) continue;
			int score = player.getScore();
			if ( score >= 8 )
				{
				announce(player, "has a score of 8 or 9, must stick");
				continue;
				}
			if ( player.promptHitOrStick())
				{
				announce(player, "has requested another card");
				player.deal(nextCard());
				}
			}
		}

	private void call()
		{
		fDealer.setHidden(false);
		int houseScore = fDealer.getScore();
		int houseMultiplier = fDealer.getBetMultiplier();
		int houseLoss = 0;
		for ( Player player : fPlayers )
			if ( !player.equals(fDealer))
				houseLoss += settle(player, houseScore, houseMultiplier);
		if ( houseLoss < 0 )
			fDealer.win( -houseLoss );
		else if ( houseLoss > 0 )
			fDealer.lose( -houseLoss );
		else
			fDealer.draw();
		}

	private int settle(Player player, int houseScore, int houseMultiplier)
		{
		if ( player.getScore() == houseScore )
			return player.settle(0);
		else if ( player.getScore() > houseScore )
			return player.settle(player.getBetMultiplier());
		else // if ( player.getScore() < houseScore )
			return player.settle(-houseMultiplier);
		}
	}

class Player
	extends JPanel
	{
	private static final String[] options = { "Hit", "Stick" };
	private static final String HIT_STICK_MSG = "<html><b>%s:</b> "
		+ "You currently have a score of <b>%d</b><br><br>"
		+ "Press <i>Hit</i> to take another card";
	private static final JLabel lblPrompt = new JLabel();

	private static final Font FONT = new Font("SansSerif", Font.PLAIN, 20);
	private final Hand fHand = new Hand();
	private final Money fMoney;
	private final JLabel lblScore = new JLabel("0");

	public Player(boolean isDealer)
		{
		super();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(lblScore);
		add(fHand);
		add(Box.createHorizontalGlue());
		add(fMoney = new Money(1000, isDealer ));
		lblScore.setFont(FONT);
		}

	public boolean promptHitOrStick()
		{
		int score = getScore();
		lblPrompt.setText(String.format(HIT_STICK_MSG, getName(), score));
		int recommend = (score < 5)? 0 : 1;
		return 0 == JOptionPane.showOptionDialog(fMoney, lblPrompt,
                  getName(), JOptionPane.DEFAULT_OPTION,
		             JOptionPane.QUESTION_MESSAGE, null, options, options[recommend]);
		}

	public int settle(int aMultiplier) { return fMoney.settle(aMultiplier); }
	public void win(int aBaht)  { fMoney.win(aBaht); }
	public void lose(int aBaht) { fMoney.lose(aBaht); }
	public void draw()          { fMoney.draw(); }

	public void reset()
		{
		fHand.reset();
		paintScore();
		fMoney.wager();
		}

	public void deal(Card aCard)
		{
		fHand.deal(aCard);
		paintScore();
		}

	public void setHidden(boolean on)
		{
		fHand.setHidden(on);
		paintScore();
		}

	private void paintScore()
		{
		String text = (fHand.isHidden()? "?" : "" + fHand.getScore());
		lblScore.setText(text);
		}

	public int getScore() { return fHand.getScore(); }
	public int getBetMultiplier()
		{
		if ( fHand.isFlush()) return 2;
		else return 1;
		}
	}

final class Money
	extends JPanel
	{
	private final JLabel lblStake = new JLabel("1000");
	private final JLabel lblWager;
	private final JLabel lblSettlement = new JLabel("");
	private int fStake;
	private int fWager;

	public Money(int aStake, boolean isDealer)
		{
		super(new GridLayout(0,1,0,3));
		add(lblStake);
		if ( !isDealer )
			add(lblWager = new JLabel(""));
		else lblWager = null;
		add(lblSettlement);
		updateStake(aStake);
		}

	public final void wager()
		{
		lblSettlement.setText("");
		if ( lblWager == null ) // dealer
			return;
		fWager = 10;
		lblWager.setText("Wager: " + fWager);
		}

	public final int settle(int multiplier)
		{
		int amount = fWager * multiplier;
		String text;
		Color color;
		if ( amount > 0 ) // tie
			win(amount);
		else if ( amount < 0 )
			lose(amount);
		else
			draw();
		return amount;
		}

	private void updateStake(int aAmount)
		{
		fStake += aAmount;
		lblStake.setText("Stake: " + fStake);
		}

	public void win(int amount)
		{
		updateStake(amount);
		lblSettlement.setText("WIN " + amount);
		lblSettlement.setForeground(Color.BLUE);
		}

	public void lose(int amount)
		{
		updateStake(amount);
		lblSettlement.setText("LOSE " + amount);
		lblSettlement.setForeground(Color.RED);
		}

	public void draw()
		{
		lblSettlement.setText("BREAK EVEN");
		lblSettlement.setForeground(Color.BLACK);
		}
	}
