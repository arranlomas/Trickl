package com.schiwfty.tex.bencoding.types;

import com.schiwfty.tex.bencoding.Utils;

import java.util.Arrays;

public class BByteString implements IBencodable {
    private final byte[] data;

    public BByteString(byte[] data) {
        this.data = data;
    }

    public BByteString(String name) {
        this.data = name.getBytes();
    }

    ////////////////////////////////////////////////////////////////////////////
    //// GETTERS AND SETTERS ///////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public byte[] getData() {
        return data;
    }

    ////////////////////////////////////////////////////////////////////////////
    //// BENCODING /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public String bencodedString() {
        return data.length + ":" + new String(data);
    }

    public byte[] bencode() {

        byte[] lengthStringAsBytes = Utils.stringToAsciiBytes(Long.toString(data.length));
        byte[] bencoded = new byte[lengthStringAsBytes.length + 1 + data.length];

        bencoded[lengthStringAsBytes.length] = ':';
        // Copy the length array in first.
        System.arraycopy(lengthStringAsBytes, 0, bencoded, 0, lengthStringAsBytes.length);
        // Copy in the actual data.
        for (int i = 0; i < data.length; i++)
            bencoded[i + lengthStringAsBytes.length + 1] = data[i];

        return bencoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    //// OVERRIDDEN METHODS ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (Utils.allAscii(data)) {
            return new String(this.data);
        } else {
            return "<non-ascii bytes:" + this.data.length + ">";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BByteString that = (BByteString) o;

        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return data != null ? Arrays.hashCode(data) : 0;
    }
}
