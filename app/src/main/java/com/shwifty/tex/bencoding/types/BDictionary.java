package com.shwifty.tex.bencoding.types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BDictionary implements IBencodable {
    private final Map<BByteString, IBencodable> dictionary;
    public byte[] blob;

    public BDictionary() {
        // LinkedHashMap to preserve order.
        this.dictionary = new LinkedHashMap<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    //// LOGIC METHODS /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public void add(BByteString key, IBencodable value) {
        this.dictionary.put(key, value);
    }

    public Object find(BByteString key) {
        return dictionary.get(key);
    }

    ////////////////////////////////////////////////////////////////////////////
    //// BENCODING /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public String bencodedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("d");
        for (Map.Entry<BByteString, IBencodable> entry : this.dictionary.entrySet()) {
            sb.append(entry.getKey().bencodedString());
            sb.append(entry.getValue().bencodedString());
        }
        sb.append("e");
        return sb.toString();
    }

    public byte[] bencode() {
        // Get the total size of the keys and values.
        ArrayList<Byte> bytes = new ArrayList<>();
        bytes.add((byte) 'd');

        for (Map.Entry<BByteString, IBencodable> entry : this.dictionary.entrySet()) {
            byte[] keyBencoded = entry.getKey().bencode();
            byte[] valBencoded = entry.getValue().bencode();
            for (byte b : keyBencoded)
                bytes.add(b);
            for (byte b : valBencoded)
                bytes.add(b);
        }
        bytes.add((byte) 'e');
        byte[] bencoded = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++)
            bencoded[i] = bytes.get(i);

        return bencoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    //// OVERRIDDEN METHODS ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[\n");
        for (Map.Entry<BByteString, IBencodable> entry : this.dictionary.entrySet()) {
            sb.append(entry.getKey()).append(" :: ").append(entry.getValue()).append("\n");
        }
        sb.append("]");

        return sb.toString();
    }
}
