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
    //    AllPorn,
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
    //    HandheldVideo,
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
    //    MoviesPorn,
//    MoviesDVDRPorn,
//    PicturesPorn,
//    GamesPorn,
//    HDMoviesPorn,
//    MovieclipsPorn,
//    OtherPorn,
    Ebooks,
    Comics,
    Pictures,
    Covers,
    Physibles,
    Other;

    fun toHumanFriendlyString(): String {
        return when (this) {
            TorrentSearchCategory.AllAudio -> "Audio"
            TorrentSearchCategory.All -> "All"
            TorrentSearchCategory.AllVideo -> "Video"
            TorrentSearchCategory.AllApplication -> "Application"
            TorrentSearchCategory.AllGames -> "Games"
            TorrentSearchCategory.AllOther -> "Other"
            TorrentSearchCategory.Music -> "Music"
            TorrentSearchCategory.Audiobooks -> "Audiobooks"
            TorrentSearchCategory.Movies -> "Movies"
            TorrentSearchCategory.Musicvideos -> "Music Videos"
            TorrentSearchCategory.TVshows -> "TV Shows"
            TorrentSearchCategory.HDMovies -> "HD Movies"
            TorrentSearchCategory.HDTVshows -> "HD TV Shows"
            TorrentSearchCategory.AndroidApplications -> "Android Apps"
            TorrentSearchCategory.AndroidGames -> "Android Games"
            else -> this.name
        }
    }
}
