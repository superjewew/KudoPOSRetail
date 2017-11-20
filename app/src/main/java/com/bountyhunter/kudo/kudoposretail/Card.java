package com.bountyhunter.kudo.kudoposretail;

import org.parceler.Parcel;

/**
 * Created by norman on 11/20/17.
 */

@Parcel
public class Card {
    String cardNumber;
    String cardIssuer;

    public Card() {

    }

    public Card(String number, String issuer) {
        cardNumber = number;
        cardIssuer = issuer;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }
}
