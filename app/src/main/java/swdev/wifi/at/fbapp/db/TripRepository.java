package swdev.wifi.at.fbapp.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TripRepository {
    private TripDao mTripDao;
    private LiveData<List<Trip>> mAllTrips;
    //private LiveData<List<Trip>> mAllOpenTrips;
    //private LiveData<List<Trip>> mAllActiveTrips;

    TripRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTripDao = db.tripDao();
        mAllTrips = mTripDao.getAllTrips();
       // mAllOpenTrips = mTripDao.getOpenTrips();
       // mAllActiveTrips = mTripDao.getActiveTrips();
    }

    LiveData<List<Trip>> getAllTrips() {
        return mAllTrips;
    };

    //LiveData<List<Trip>> getAllOpenTrips() {    return mAllOpenTrips;   };

    //LiveData<List<Trip>> getAllActiveTrips() {return mAllActiveTrips;};

    public int NrOfActiveTrips() {return mTripDao.getNrOfActiveTrips();};

    public int NrOfOpenTrips() {return  mTripDao.getNrOfOpenTrips();};

    public void addTrip (Trip trip) {
        new insertAsyncTask(mTripDao).execute(trip);
    }

    public void updateTrip (Trip trip) { new editAsyncTask(mTripDao).execute(trip); }

    public Trip getTripById(int id) { return mTripDao.getTripById(id);};

    public void deleteTrip (int id) { new deleteAsyncTask(mTripDao).execute(getTripById(id));};

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

    private static class editAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao mAsyncTaskDao;

        editAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.updateTrip(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao mAsyncTaskDao;

        deleteAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.deleteById(params[0]._id);
            return null;
        }
    }


}
