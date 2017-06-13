package com.schiwfty.tex.bencoding.types;

import com.schiwfty.tex.bencoding.Utils;

import java.util.ArrayList;


/**
 * Created by arran on 30/04/2017.
 */
public class BInt implements IBencodable {
    public byte[] blob;
    private final Long value;

    public BInt(Long value) {
        this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //// BENCODING /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public String bencodedString() {
        return "i" + value + "e";
    }

    public byte[] bencode() {
        byte[] sizeInAsciiBytes = Utils.stringToAsciiBytes(value.toString());

        ArrayList<Byte> bytes = new ArrayList<>();

        bytes.add((byte) 'i');

        for (byte sizeByte : sizeInAsciiBytes)
            bytes.add(sizeByte);

        bytes.add((byte) 'e');

        byte[] bencoded = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++)
            bencoded[i] = bytes.get(i);

        return bencoded;
    }
    ////////////////////////////////////////////////////////////////////////////
    //// GETTERS AND SETTERS ///////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public Long getValue() {
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //// OVERRIDDEN METHODS ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BInt bInt = (BInt) o;

        return value.equals(bInt.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
