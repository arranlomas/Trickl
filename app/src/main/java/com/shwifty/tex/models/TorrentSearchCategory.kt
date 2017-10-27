package com.shwifty.tex.models

/**
 * Created by arran on 27/10/2017.
 */

enum class TorrentSearchCategory {
    All,
    AllAudio,
    AllVideo,
    AllApplication,
    AllGames,
    AllPorn,
    AllOther,
    Music,
    Audiobooks,
    Soundclips,
    FLAC,
    OtherAudio,
    Movies,
    MoviesDVDR,
    Musicvideos,
    Movieclips,
    TVshows,
    HandheldVideo,
    HDMovies,
    HDTVshows,
    Movies3D,
    OtherVideo,
    WindowsApplications,
    MacApplications,
    UNIXApplications,
    HandheldApplications,
    IOSApplications,
    AndroidApplications,
    OtherOSApplications,
    PCGames,
    MacGames,
    PSxGames,
    XBOX360Games,
    WiiGames,
    HandheldGames,
    IOSGames,
    AndroidGames,
    OtherGames,
    MoviesPorn,
    MoviesDVDRPorn,
    PicturesPorn,
    GamesPorn,
    HDMoviesPorn,
    MovieclipsPorn,
    OtherPorn,
    Ebooks,
    Comics,
    Pictures,
    Covers,
    Physibles,
    Other;

    override fun toString(): String {
        return name.toUpperCase()
    }
}
