package swdev.wifi.at.fbapp.db;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Date;

@Database(entities = {Trip.class}, version = 1)
@TypeConverters(DateConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDao tripDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        Log.d("TAG","DBINIT");

        synchronized (AppDatabase.class) {
            if (INSTANCE==null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "trip_db")
                        .allowMainThreadQueries()
                        .addCallback(sAppDatabaseCallback)
                        .build();
            }
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static AppDatabase.Callback sAppDatabaseCallback = new AppDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TripDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.tripDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Date d1 = new Date();
            Trip trip = new Trip(d1,"Stainz",104);
            //trip.setSavedAt(d1);
            trip.setFinishLocation("Graz");
            trip.setFinish(d1);
            trip.setFinishKm(200);
            trip.setCategory(0);
            mDao.addTrip(trip);

            Trip trip2 = new Trip(d1,"Graz",100);
            trip2.setFinishLocation("Stainz");
            trip2.setFinish(d1);
            trip2.setCategory(0);
            trip2.setSavedAt(d1);
            trip2.setFinishKm(153);
            mDao.addTrip(trip2);
            return null;
        }
    }

}
