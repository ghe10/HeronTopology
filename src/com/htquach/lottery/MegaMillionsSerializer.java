package com.htquach.lottery;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by hqxubuntu on 10/2/16.
 */
public class MegaMillionsSerializer extends Serializer<MegaMillions> {

    @Override
    public void write(Kryo kryo, Output output, MegaMillions ticket) {
        output.writeInts(ticket.getMainNumbers());
        output.writeInt(ticket.getMegaballNumber());
    }

    @Override
    public MegaMillions read(Kryo kryo, Input input, Class<MegaMillions> ticketClass) {

        return new MegaMillions(input.readInts(5), input.readInt());
    }
}
