package com.htquach.lottery;

/**
 * Different types of winnings and prizes in a Mega Millions lottery game
 */
public enum MegaMillionsWinningType {
    None(0),
    Megaball(1),
    OnePlusMegaball(2),
    TwoPlusMegaball(5),
    Three(5),
    ThreePlusMegaball(50),
    Four(500),
    FourPlusMegaball(5000),
    Five(1000000),
    Jackpot(15000000);

    private int prize;

    MegaMillionsWinningType(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }
}
