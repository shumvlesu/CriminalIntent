package com.hfad.criminalintent.database;

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            //ДЗ
            public static final String TIME = "time";
            //
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
