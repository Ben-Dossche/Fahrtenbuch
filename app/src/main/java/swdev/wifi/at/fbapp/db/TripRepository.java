package swdev.wifi.at.fbapp.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TripRepository {
    private TripDao mTripDao;
    private LiveData<List<Trip>> mAllTrips;
    private LiveData<List<Trip>> mAllOpenTrips;

    TripRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTripDao = db.tripDao();
        mAllTrips = mTripDao.getAllTrips();
        mAllOpenTrips = mTripDao.getAllOpenTrips();
    }

    LiveData<List<Trip>> getAllTrips() {
        return mAllTrips;
    };

    LiveData<List<Trip>> getAllOpenTrips() {
        return mAllOpenTrips;
    };

    public void addTrip (Trip trip) {
        new insertAsyncTask(mTripDao).execute(trip);
    }

    private static class insertAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao mAsyncTaskDao;

        insertAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.addTrip(params[0]);
            return null;
        }
    }

}
