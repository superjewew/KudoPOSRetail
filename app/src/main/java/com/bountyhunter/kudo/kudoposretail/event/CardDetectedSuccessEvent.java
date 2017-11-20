package com.bountyhunter.kudo.kudoposretail.event;

import com.bountyhunter.kudo.kudoposretail.Card;

/**
 * Created by norman on 11/19/17.
 */

public class CardDetectedSuccessEvent {
    private Card mCard;

    public CardDetectedSuccessEvent(Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }
}
