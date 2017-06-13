package com.shwifty.tex.bencoding;

/**
 * Created by arran on 30/04/2017.
 */
import com.shwifty.tex.bencoding.types.*;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private int currentByteIndex;
    private byte[] datablob;

    ////////////////////////////////////////////////////////////////////////////
    //// CONSTRUCTORS //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public Reader(File file) throws IOException {
        datablob = IOUtils.toByteArray(new FileInputStream(file));
    }

    public Reader(String s) {
        datablob = s.getBytes();
    }

    ////////////////////////////////////////////////////////////////////////////
    //// PARSER ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Starts reading from the beginning of the file.
     * Keeps reading single types and adds them to the list to finally return
     * them.
     *
     * @return List<Object> containing all the parsed bencoded objects.
     */
    public synchronized List<IBencodable> read() {
        this.currentByteIndex = 0;
        long fileSize = datablob.length;

        List<IBencodable> dataTypes = new ArrayList<>();
        while (currentByteIndex < fileSize)
            dataTypes.add(readSingleType());

        return dataTypes;
    }

    /**
     * Tries to read in an object starting at the current byte index.
     * If not possible throws an exception.
     *
     * @return Returns an Object that represents either BByteString,
     * BDictionary, BInt or BList.
     */
    private IBencodable readSingleType() {
        // Read in the byte at current position and dispatch over it.
        byte current = datablob[currentByteIndex];
        switch (current) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readByteString();
            case 'd':
                return readDictionary();
            case 'i':
                return readInteger();
            case 'l':
                return readList();
        }
        throw new Error("Parser in invalid state at byte " + currentByteIndex);
    }

    ////////////////////////////////////////////////////////////////////////////
    //// BENCODING READ TYPES //////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Reads in a list starting from the current byte index. Throws an error if
     * not called on an appropriate index.
     * A list of values is encoded as l<contents>e . The contents consist of the bencoded
     * elements of the list, in order, concatenated. A list consisting of the string "spam"
     * and the number 42 would be encoded as: l4:spami42ee. Note the absence of separators
     * between elements.
     *
     * @return BList object.
     */
    private BList readList() {
        // If we got here, the current byte is an 'l'.
        if (readCurrentByte() != 'l')
            throw new Error("Error parsing list. Was expecting a 'l' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the 'l'

        BList list = new BList();
        while (readCurrentByte() != 'e')
            list.add(readSingleType());

        currentByteIndex++; // Skip the 'e'
        return list;
    }

    /**
     * Reads in a bytestring strating at the current position.
     * Throws an error if not possible.
     * A byte string (a sequence of bytes, not necessarily characters) is encoded as <length>:<contents>.
     * The length is encoded in base 10, like integers, but must be non-negative (zero is allowed);
     * the contents are just the bytes that make up the string. The string "spam" would be encoded as 4:spam.
     * The specification does not deal with encoding of characters outside the ASCII set; to mitigate this,
     * some BitTorrent applications explicitly communicate the encoding (most commonly UTF-8) in various
     * non-standard ways. This is identical to how netstrings work, except that netstrings additionally
     * append a comma suffix after the byte sequence.
     *
     * @return BByteString
     */
    private BByteString readByteString() {
        String lengthAsString = "";
        int lengthAsInt;
        byte[] bsData;

        // Build up a string of ascii chars representing the size.
        byte current = readCurrentByte();
        while (current >= 48 && current <= 57) {
            lengthAsString = lengthAsString + Character.toString((char) current);
            currentByteIndex++;
            current = readCurrentByte();
        }
        lengthAsInt = Integer.parseInt(lengthAsString);

        if (readCurrentByte() != ':')
            throw new Error("Read length of byte string and was expecting ':' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the ':'.

        // Read the actual data
        bsData = new byte[lengthAsInt];
        for (int i = 0; i < lengthAsInt; i++) {
            bsData[i] = readCurrentByte();
            currentByteIndex++;
        }

        return new BByteString(bsData);
    }

    /**
     * Reads in a dictionary. Each dictionary consists of N bytestrings mapped to any other value.
     * Example: d3:foo3:bare == ({foo, bar})
     * A dictionary is encoded as d<contents>e. The elements of the dictionary are encoded each key
     * immediately followed by its value. All keys must be byte strings and must appear in
     * lexicographical order. A dictionary that associates the values 42 and "spam" with the keys
     * "foo" and "bar", respectively (in other words, {"bar": "spam", "foo": 42}), would be encoded as
     * follows: d3:bar4:spam3:fooi42ee.
     * (This might be easier to read by inserting some spaces: d 3:bar 4:spam 3:foo i42e e.)
     *
     * @return BDictionary representing the dictionary.
     */
    private BDictionary readDictionary() {
        // If we got here, the current byte is an 'd'.
        if (readCurrentByte() != 'd')
            throw new Error("Error parsing dictionary. Was expecting a 'd' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the 'd'

        BDictionary dict = new BDictionary();
        while (readCurrentByte() != 'e') {
            // Each dictionary *must* map BByteStrings to any other value.
            BByteString key = (BByteString) readSingleType();
            IBencodable value = readSingleType();

            dict.add(key, value);
        }
        currentByteIndex++; // Skip the 'e'

        return dict;
    }

    /**
     * Parses an integer in Bencode fromat.
     * Example: 123 == i123e
     * An integer is encoded as i<integer encoded in base ten ASCII>e.
     * Leading zeros are not allowed (although the number zero is still represented as "0").
     * Negative values are encoded by prefixing the number with a minus sign.
     * The number 42 would thus be encoded as i42e, 0 as i0e, and -42 as i-42e.
     * Negative zero is not permitted.
     *
     * @return BInt representing the value of the parsed integer.
     */
    private BInt readInteger() {
        // If we got here, the current byte is an 'i'.
        if (readCurrentByte() != 'i')
            throw new Error("Error parsing integer. Was expecting an 'i' but got " + readCurrentByte());
        currentByteIndex++;// Skip the 'i'.

        // Read in the integer number by number.
        // They are represented as ASCII numbers.
        String intString = "";
        byte current = readCurrentByte();
        //45 negative mark
        while (current >= 48 && current <= 57 || current == 45) {
            intString = intString + Character.toString((char) current);
            currentByteIndex++;
            current = readCurrentByte();
        }

        if (readCurrentByte() != 'e')
            throw new Error("Error parsing integer. Was expecting 'e' at end but got " + readCurrentByte());

        currentByteIndex++; // Skip past 'e'
        return new BInt(Long.parseLong(intString));
    }

    ////////////////////////////////////////////////////////////////////////////
    //// HELPERS ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the byte in the current position of the file.
     *
     * @return byte
     */
    private byte readCurrentByte() {
        return datablob[currentByteIndex];
    }
}
