package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version crazy 220810
 * Writes Covers NFL data to a large SportData Excel sheet
 *******************************************************************/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static org.jsoup.Jsoup.connect;
public class WebSiteReader
{
    private static Document dirtyDoc;
    public static Elements readWebsite(String urlToRead) throws IOException
    {
        dirtyDoc = Jsoup.parse(String.valueOf(connect(urlToRead).get()));
        return dirtyDoc.getAllElements();
    }
}



