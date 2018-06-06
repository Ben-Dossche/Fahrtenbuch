package swdev.wifi.at.fbapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTrip (Trip trip);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTrip (Trip trip);

    @Query("SELECT * from trips order by start DESC")
    LiveData<List<Trip>> getAllTrips();

    @Query("SELECT * from trips where saved_at is null order by start DESC")
    LiveData<List<Trip>> getAllOpenTrips();

    @Query("DELETE FROM trips")
    void deleteAll();
}

