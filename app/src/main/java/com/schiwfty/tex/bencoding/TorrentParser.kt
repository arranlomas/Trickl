package com.schiwfty.tex.bencoding

import com.schiwfty.tex.bencoding.types.BByteString
import com.schiwfty.tex.bencoding.types.BDictionary
import com.schiwfty.tex.bencoding.types.BInt
import com.schiwfty.tex.bencoding.types.BList
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.util.*


/**
 * Created by arran on 30/04/2017.
 */
object TorrentParser {
    @Throws(IOException::class)
    fun parseTorrent(filePath: String): TorrentInfo? {
        val r = Reader(File(filePath))
        val x = r.read()
        // A valid torrentfile should only return a single dictionary.
        if (x.size != 1)
            throw Error("Parsing .torrent yielded wrong number of bencoding structs.")
        try {
            return parseTorrent(x[0])
        } catch (e: ParseException) {
            System.err.println("Error parsing torrent!")
        }

        return null
    }

    @Throws(ParseException::class)
    private fun parseTorrent(o: Any): TorrentInfo {
        if (o is BDictionary) {
            val torrentDictionary = o
            val infoDictionary = parseInfoDictionary(torrentDictionary)

            if (infoDictionary == null) throw IllegalStateException("info dictionary is null")

            val t = TorrentInfo(parseTorrentName(infoDictionary))

            ///////////////////////////////////
            //// OBLIGATED FIELDS /////////////
            ///////////////////////////////////
            t.announce = parseAnnounce(torrentDictionary)
            t.info_hash = Utils.SHAsum(infoDictionary.bencode())
            t.pieceLength = parsePieceLength(infoDictionary)
            t.pieces = parsePiecesHashes(infoDictionary)
            t.piecesBlob = parsePiecesBlob(infoDictionary)

            ///////////////////////////////////
            //// OPTIONAL FIELDS //////////////
            ///////////////////////////////////
            t.fileList = parseFileList(infoDictionary)
            t.comment = parseComment(torrentDictionary)
            t.createdBy = parseCreatorName(torrentDictionary)
            t.creationDate = parseCreationDate(torrentDictionary)
            t.announceList = parseAnnounceList(torrentDictionary)
            t.totalSize = parseSingleFileTotalSize(infoDictionary)

            // Determine if torrent is a singlefile torrent.
            t.singleFileTorrent = (null != infoDictionary.find(BByteString("length")))
            return t
        } else {
            throw ParseException("Could not parse Object to BDictionary", 0)
        }
    }

    /**
     * @param info info dictionary
     * *
     * @return length — size of the file in bytes (only when one file is being shared)
     */
    private fun parseSingleFileTotalSize(info: BDictionary): Long {
        if (null != info.find(BByteString("length")))
            return (info.find(BByteString("length")) as BInt).value
        return 0
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return info — this maps to a dictionary whose keys are dependent on whether
     * * one or more files are being shared.
     */
    private fun parseInfoDictionary(dictionary: BDictionary): BDictionary? {
        if (null != dictionary.find(BByteString("info")))
            return dictionary.find(BByteString("info")) as BDictionary
        else
            return null
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return creation date: (optional) the creation time of the torrent, in standard UNIX epoch format
     * * (integer, seconds since 1-Jan-1970 00:00:00 UTC)
     */
    private fun parseCreationDate(dictionary: BDictionary): Date? {
        if (null != dictionary.find(BByteString("creation date")))
            return Date(java.lang.Long.parseLong(dictionary.find(BByteString("creation date")).toString()))
        return null
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return created by: (optional) name and version of the program used to create the .torrent (string)
     */
    private fun parseCreatorName(dictionary: BDictionary): String {
        if (null != dictionary.find(BByteString("created by")))
            return dictionary.find(BByteString("created by")).toString()
        return ""
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return comment: (optional) free-form textual comments of the author (string)
     */
    private fun parseComment(dictionary: BDictionary): String {
        if (null != dictionary.find(BByteString("comment")))
            return dictionary.find(BByteString("comment")).toString()
        else
            return ""
    }

    /**
     * @param info infodictionary of torrent
     * *
     * @return piece length — number of bytes per piece. This is commonly 28 KiB = 256 KiB = 262,144 B.
     */
    private fun parsePieceLength(info: BDictionary): Long {
        if (null != info.find(BByteString("piece length")))
            return (info.find(BByteString("piece length")) as BInt).value
        else
            return 0
    }

    /**
     * @param info info dictionary of torrent
     * *
     * @return name — suggested filename where the file is to be saved (if one file)/suggested directory name
     * * where the files are to be saved (if multiple files)
     */
    private fun parseTorrentName(info: BDictionary): String {
        if (null != info.find(BByteString("name")))
            return info.find(BByteString("name")).toString()
        else
            return ""
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return announce — the URL of the tracke
     */
    private fun parseAnnounce(dictionary: BDictionary): String {
        if (null != dictionary.find(BByteString("announce")))
            return dictionary.find(BByteString("announce")).toString()
        else
            return ""
    }

    /**
     * @param info info dictionary of .torrent file.
     * *
     * @return pieces — a hash list, i.e., a concatenation of each piece's SHA-1 hash. As SHA-1 returns a 160-bit hash,
     * * pieces will be a string whose length is a multiple of 160-bits.
     */
    private fun parsePiecesBlob(info: BDictionary): ByteArray {
        if (null != info.find(BByteString("pieces"))) {
            return (info.find(BByteString("pieces")) as BByteString).data
        } else {
            throw Error("Info dictionary does not contain pieces bytestring!")
        }
    }

    /**
     * @param info info dictionary of .torrent file.
     * *
     * @return pieces — a hash list, i.e., a concatenation of each piece's SHA-1 hash. As SHA-1 returns a 160-bit hash,
     * * pieces will be a string whose length is a multiple of 160-bits.
     */
    private fun parsePiecesHashes(info: BDictionary): List<String> {
        if (null != info.find(BByteString("pieces"))) {
            val sha1HexRenders = ArrayList<String>()
            val piecesBlob = (info.find(BByteString("pieces")) as BByteString).data
            // Split the piecesData into multiple hashes. 1 hash = 20 bytes.
            if (piecesBlob.size % 20 == 0) {
                val hashCount = piecesBlob.size / 20
                for (currHash in 0..hashCount - 1) {
                    val currHashByteBlob = Arrays.copyOfRange(piecesBlob, 20 * currHash, 20 * (currHash + 1))
                    val sha1 = Utils.bytesToHex(currHashByteBlob)
                    sha1HexRenders.add(sha1)
                }
            } else {
                throw Error("Error parsing SHA1 piece hashes. Bytecount was not a multiple of 20.")
            }
            return sha1HexRenders
        } else {
            throw Error("Info dictionary does not contain pieces bytestring!")
        }
    }

    /**
     * @param info info dictionary of torrent
     * *
     * @return files — a list of dictionaries each corresponding to a file (only when multiple files are being shared).
     */
    private fun parseFileList(info: BDictionary): List<TorrentFile> {
        if (null != info.find(BByteString("files"))) {
            val fileList = ArrayList<TorrentFile>()
            val filesBList = info.find(BByteString("files")) as BList

            val fileBDicts = filesBList.iterator
            while (fileBDicts.hasNext()) {
                val fileObject = fileBDicts.next()
                if (fileObject is BDictionary) {
                    val fileBDict = fileObject
                    val filePaths = fileBDict.find(BByteString("path")) as BList
                    val fileLength = fileBDict.find(BByteString("length")) as BInt
                    // Pick out each subdirectory as a string.
                    val paths = LinkedList<String>()
                    val filePathsIterator = filePaths.iterator
                    while (filePathsIterator.hasNext())
                        paths.add(filePathsIterator.next().toString())

                    val tf = TorrentFile(fileLength.value, paths)
                    fileList.add(tf)
                }
            }
            return fileList
        }
        return emptyList()
    }

    /**
     * @param dictionary root dictionary of torrent
     * *
     * @return announce-list: (optional) this is an extention to the official specification, offering
     * * backwards-compatibility. (list of lists of strings).
     */
    private fun parseAnnounceList(dictionary: BDictionary): List<String> {
        if (null != dictionary.find(BByteString("announce-list"))) {
            val announceUrls = LinkedList<String>()

            val announceList = dictionary.find(BByteString("announce-list")) as BList
            val subLists = announceList.iterator
            while (subLists.hasNext()) {
                val subList = subLists.next() as BList
                val elements = subList.iterator
                while (elements.hasNext()) {
                    // Assume that each element is a BByteString
                    val tracker = elements.next() as BByteString
                    announceUrls.add(tracker.toString())
                }
            }
            return announceUrls
        } else {
            return emptyList()
        }
    }
}
