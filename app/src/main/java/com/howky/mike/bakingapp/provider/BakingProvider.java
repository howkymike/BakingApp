package com.howky.mike.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) to create a content provider and
 * define
 * URIs for the provider:
 *  (1) access all cakes
 *  (2) access a specific cake by id
 */

@ContentProvider(authority = BakingProvider.AUTHORITY, database = BakingDatabase.class)
public final class BakingProvider {

    public static final String AUTHORITY = "com.howky.mike.bakingapp.BakingProvider";

    @TableEndpoint(table = BakingDatabase.BAKING_CAKES)
    public static class Cakes {

        @ContentUri(
                path = "cakes",
                type = "vnd.android.cursor.dir/cakes",
                defaultSort = BakingContract.CakeColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/cakes");

        @InexactContentUri(
                path = "cakes/#",
                name = "CAKE_ID",
                type = "vnd.android.cursor.item/cakes",
                whereColumn = BakingContract.CakeColumns.CAKE_ID,
                pathSegment = 1)
        public static final Uri withID(long id) {
            return Uri.parse("content://" + AUTHORITY + "/cakes/" + id);
        }
    }

}
