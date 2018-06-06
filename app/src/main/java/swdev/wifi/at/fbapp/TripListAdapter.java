package swdev.wifi.at.fbapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import swdev.wifi.at.fbapp.db.Trip;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripItemView;
        private final TextView TV_StartLoc;

        private TripViewHolder(View itemView) {
            super(itemView);
            tripItemView = itemView.findViewById(R.id.TV_itemtest);
            TV_StartLoc = itemView.findViewById(R.id.TV_itemstartloc);
        }
    }

    private final LayoutInflater mInflater;
    private List<Trip> mTrips; // Cached copy of words

    TripListAdapter(Context context) {mInflater = LayoutInflater.from(context);}

    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        if (mTrips != null) {
            Trip trip = mTrips.get(position);
            holder.tripItemView.setText(trip.getStartLocation());
            holder.TV_StartLoc.setText(trip.getStartLocation());
        } else {
            holder.tripItemView.setText("KEINE DATEN...");
        }
    }

    public void setTrips(List<Trip> trips) {
        this.mTrips = trips;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTrips != null)
            return mTrips.size();
        else return 0;
    }
}
