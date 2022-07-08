package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy2 220708
 * Selenium composite version
 *******************************************************************/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static org.jsoup.Jsoup.connect;
public class WebSiteReader
{
    private static Document dirtyDoc;
    public static Elements readCleanWebsite(String urlToRead) throws IOException
    {
        dirtyDoc = Jsoup.parse(String.valueOf(connect(urlToRead).get()));
        return getDirtyDoc().getAllElements();
    }
    public static Document getDirtyDoc() {
        return dirtyDoc;
    }
}



