## Trickl
trickl torrent client


This is the repository for the trickl torrent client for android

[![Android app on Google Play](http://ligi.de/img/play_badge.png)](https://play.google.com/store/apps/details?id=com.shwifty.tex)

This is a front end for Android-Confluence [confluence](https://github.com/arranlomas/Android-Confluence-Wrapper), which is my fork of the go [confluence](https://github.com/anacrolix/confluence), which is slightly modified to run on android.

Confluence is a http wrapper for torrent client written in [go](https://github.com/anacrolix/torrent)


# Compilation
Note: keystore.properties is in gitignore as it contains the private api key for my fabric project that tracks crashes. To fix this simply remove the fabric dependency or create a fabric project and copy the api key into a keystore.properties file at the project root.

* git clone https://github.com/arranlomas/Trickl.git

* ```cd Trickl```

* create a file here called keystore.properties

* in keystore.properties add the line TorrentBrowseServerIP="http://www.google.com" (if you want to set up a proper search please see the [repo I use to provide search results](https://github.com/arranlomas/TPBScraper)


Next you have to either set up fabric or remove it, if you want crash reporting follow the Setting up Fabric, otherwise follow the Removing Fabric. (I would expect most people to remove it)
   
   
# Removing Fabric

* open the app/build.gradle file

* remove to line below from the top of the file

```apply plugin: 'io.fabric'```

* remove the below from the dependencies section
```//fabric
compile('com.crashlytics.sdk.android:crashlytics:2.7.0@aar') {
    transitive = true;
}
```

* remove the below from the bottom of the file in the repositories section

```  
maven { url 'https://maven.fabric.io/public' }
```

* remove all references to crashlytics from the following files:
```
TorrentExtensions.kt

BaseMviActivity.kt

BasePresenter.kt

MainActivity.kt
```

# Setting up Fabric

For the next steps you will need to [create a project on fabric](https://www.fabric.io/home), if you don't wish to do this please follow the section to remove fabric.

* create a file called fabric.properties

* paste the following (where the fields in <> are the details you get from fabric) :-

    apiSecret=<your_api_secret>
    
    apiKey=<your_api_key
