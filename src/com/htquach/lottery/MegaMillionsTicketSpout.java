package com.htquach.lottery;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class MegaMillionsTicketSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
//    final int[][] winningNumbers = MegaMillions.drawing();
    private long nTickets = 0;

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
    }

    public void close() {
    }

    @Override
    public void nextTuple() {
        Utils.sleep(1);
        MegaMillions ticket = new MegaMillions();
        if (++nTickets % 5000 == 0) {
            System.out.println("Bolt generated " + nTickets);
            System.out.println("Ticket: " + ticket.toString());
        } else {
//            System.out.print(".");
        }
        collector.emit(new Values(ticket));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("ticket"));
    }
}
