package swdev.wifi.at.fbapp.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository mRepository;
    private LiveData<List<Trip>> mAllTrips;
    //private LiveData<List<Trip>> mAllOpenTrips;
    //private LiveData<List<Trip>> mAllActiveTrips;
    public TripViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TripRepository(application);
        mAllTrips = mRepository.getAllTrips();
  //      mAllOpenTrips = mRepository.getAllOpenTrips();
  //      mAllActiveTrips = mRepository.getAllActiveTrips();
    }

    public LiveData<List<Trip>> getAllTrips() {
        return mAllTrips;
    }

    public List<Trip> GetTripsForTimeFrame(Long dateStart, Long dateEnd) { return mRepository.GetTripsForTimeFrame(dateStart,dateEnd); }

//    public LiveData<List<Trip>> getAllOpenTrips() { return mAllOpenTrips;}

//    public LiveData<List<Trip>> getAllActiveTrips() { return mAllActiveTrips;}

    public boolean activeTrips() {return (mRepository.NrOfActiveTrips() > 0 ? true : false);}

    public boolean openTrips() { return  (mRepository.NrOfOpenTrips() > 0 ? true : false);}

    public Trip getTripById(int id) { return mRepository.getTripById(id);};

    public Trip getLastTrip() { return  mRepository.getLastTrip();};

    public void addTrip(Trip trip) {
        mRepository.addTrip(trip);
    }

    public void deleteTrip(int id) {mRepository.deleteTrip(id);}

    public void updateTrip(Trip trip) { mRepository.updateTrip(trip); }
}
