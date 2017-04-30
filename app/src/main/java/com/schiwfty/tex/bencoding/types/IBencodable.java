package com.schiwfty.tex.bencoding.types;

/**
 * Created by arran on 30/04/2017.
 */
public interface IBencodable {
    /**
     * Returns the byte representation of the bencoded object.
     *
     * @return byte representation of the bencoded object.
     */
    byte[] bencode();

    /**
     * Returns string representation of bencoded object.
     *
     * @return string representation of bencoded object.
     */
    String bencodedString();
}
