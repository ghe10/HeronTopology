package com.htquach.lottery;

import java.text.NumberFormat;
import java.util.*;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class MegaMillionsTicketCheckerBolt extends BaseRichBolt {
    private OutputCollector collector;
    private int amountSpent;
    private int amountWin;
    private long nTickets;
    HashMap<MegaMillionsWinningType, Integer> winningCount;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
        amountSpent = 0;
        amountWin = 0;
        winningCount = new HashMap<MegaMillionsWinningType, Integer>();
        for (MegaMillionsWinningType x: MegaMillionsWinningType.values()) {
            winningCount.put(x, 0);
        }
    }

    @Override
    public void execute(Tuple tuple) {
        int prizeWon;
        MegaMillions ticket = (MegaMillions) (tuple.getValueByField("ticket"));
        //TODO:  work on getting the winningNumbers from the spout
//        int[][] winningNumber = (int[][]) tuple.getValueByField("winningNumbers");
        int[][] winningNumber = MegaMillions.drawing();
        MegaMillionsWinningType winningType;
        amountSpent += ticket.get_cost();
        winningType = ticket.checkPrize(winningNumber);
        winningCount.put(winningType, (winningCount.get(winningType)) + 1);
        prizeWon = winningType.getPrize();
        amountWin += prizeWon;
        if (prizeWon == ticket.getJackpot()) {
            System.out.println("========== Jackpot! ==========");
            System.out.println("The ticket:         " + ticket.toString());
            System.out.println("The winning number: " + MegaMillions.toString(winningNumber));
        } else if (prizeWon >= MegaMillionsWinningType.Four.getPrize()) {
            System.out.println("---------- Win " + NumberFormat.getCurrencyInstance(Locale.US).format(prizeWon) + " ----------");
            System.out.println("Ticket:         " + ticket.toString());
            System.out.println("Winning number: " + MegaMillions.toString(winningNumber));
        }
        if (++nTickets % 5000 == 0) {
            System.out.println("Total Win " + NumberFormat.getCurrencyInstance(Locale.US).format(amountWin) + " ---------- " +
                    "Total Spent " + NumberFormat.getCurrencyInstance(Locale.US).format(amountSpent) + "---------- " +
                    "Win Back = " + String.format("%.3f%%", (amountWin/((double)amountSpent) * 100)));
            for (MegaMillionsWinningType k: winningCount.keySet()) {
                System.out.println(String.format("%18s", k.toString()) + ": " +
                        String.format("%.3f%%", (winningCount.get(k)/((double) amountSpent) * 100)) +
                        " = " + winningCount.get(k));
            }
            System.out.println("\n\n");
        }
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}


