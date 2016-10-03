package com.htquach.lottery;

/**
 * High level representation of a lottery ticket
 */
public abstract class LotteryTicket {

    /**
     * The name of the game
     * @return name
     */
    public abstract String get_name();

    /**
     * The cost of the game
     * @return amount of ticket cost in dollar
     */
    public abstract int get_cost();
}
