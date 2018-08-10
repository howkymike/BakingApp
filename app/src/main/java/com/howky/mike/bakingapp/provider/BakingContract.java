package com.howky.mike.bakingapp.provider;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to define the columns in a
 * content provider baked by a database
 */

public class BakingContract {

    public static final long INVALID_CAKE_ID = -1;

    public interface CakeColumns {
        @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
        @DataType(DataType.Type.INTEGER) @NonNull String CAKE_ID = "cakeId";
        @DataType(DataType.Type.TEXT) @NonNull String TITLE = "title";
        @DataType(DataType.Type.TEXT) String INGREDIENTS = "ingredients";
        @DataType(DataType.Type.TEXT) String STEPS = "steps";
        @DataType(DataType.Type.TEXT) String SERVINGS = "servings";
        @DataType(DataType.Type.TEXT) String IMAGE = "image";
    }
}
