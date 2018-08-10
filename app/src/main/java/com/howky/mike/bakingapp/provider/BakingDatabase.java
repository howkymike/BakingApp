package com.howky.mike.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
 * table for cakes
 */

@Database(version = BakingDatabase.VERSION)
public final class BakingDatabase {

    public static final int VERSION = 1;

    @Table(BakingContract.CakeColumns.class)
    public static final String BAKING_CAKES = "cakes";
}
