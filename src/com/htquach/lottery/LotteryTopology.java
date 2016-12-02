/**
 * A lottery simulator to see how many years and how much money
 * does it take to win the jackpot.
 *
 * Should the extra $10/month goes toward the mortgage?
 * Or go to the State park?
 */

package com.htquach.lottery;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class LotteryTopology {

    private LotteryTopology() {
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        if (args.length != 1) {
            throw new RuntimeException("Specify topology name");
        }

        int numberOfTicketSpouts = 1;
        int numberOfTicketCheckerBolts = 1;

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("ticketGenerator", new MegaMillionsTicketSpout(), numberOfTicketSpouts);
        builder.setBolt("ticketChecker", new MegaMillionsTicketCheckerBolt(), numberOfTicketCheckerBolts)
            .shuffleGrouping("ticketGenerator");

        Config conf = new Config();
        conf.setNumStmgrs(1);
        conf.registerSerialization(MegaMillions.class, MegaMillionsSerializer.class);
        conf.registerSerialization(int[].class);

        StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
    }

    public static void main1(String[] args) {
        runSimulation();
    }

    /**
     * Simulate the game.  This could be the Main method
     * when running outside of heron context.
     */
    private static void runSimulation() {
        int numberOfTickets = 10000;
        int[][] winningNumbers = MegaMillions.drawing();
        int amountWon = 0;
        int amountSpent = 0;
        MegaMillions megaMillionsTicket;

        System.out.println("My tickets numbers");
        for (int i = 0; i < numberOfTickets; i++) {
            if (i % 100000 == 0) { System.out.println("Ticket #" + (i)); }
            megaMillionsTicket = new MegaMillions();
            amountSpent += megaMillionsTicket.get_cost();
            amountWon += megaMillionsTicket.checkPrize(winningNumbers).getPrize();
            //System.out.println(megaMillionsTicket.toString());
        }

        System.out.println("Winning numbers");
        System.out.println(MegaMillions.toString(winningNumbers));
        System.out.println("\n==============");

        System.out.print("Amount I won $");
        System.out.println(amountWon);

        System.out.print("Amount I spent $");
        System.out.println(amountSpent);
    }
}
