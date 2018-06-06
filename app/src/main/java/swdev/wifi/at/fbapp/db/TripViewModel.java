package swdev.wifi.at.fbapp.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository mRespository;
    private LiveData<List<Trip>> mAllTrips;
    private LiveData<List<Trip>> mAllOpenTrips;
    public TripViewModel(@NonNull Application application) {
        super(application);
        mRespository = new TripRepository(application);
        mAllTrips = mRespository.getAllTrips();
        mAllOpenTrips = mRespository.getAllOpenTrips();

    }

    public LiveData<List<Trip>> getAllTrips() {
        return mAllTrips;
    }

    public LiveData<List<Trip>> getAllOpenTrips() {
        return mAllOpenTrips;
    }

    public void addTrip(Trip trip) {
        mRespository.addTrip(trip);
    }
}
