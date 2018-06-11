package swdev.wifi.at.fbapp;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import swdev.wifi.at.fbapp.db.Trip;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripItemView;
        private final TextView TV_StartLoc;
        private final TextView TV_EndLoc;
        private final TextView TV_Summary;

        private TripViewHolder(View itemView) {
            super(itemView);
            tripItemView = itemView.findViewById(R.id.TV_itemtest);
            TV_StartLoc = itemView.findViewById(R.id.TV_itemstartloc);
            TV_EndLoc = itemView.findViewById(R.id.TV_itemendloc);
            TV_Summary = itemView.findViewById(R.id.TV_itemsummary);
        }
    }

    private final LayoutInflater mInflater;
    private List<Trip> mTrips; // Cached copy of trips
    private List<Trip> mActiveTrips;
    private DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy, HH:mm",
            Locale.GERMAN);

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
            holder.tripItemView.setText(df.format(trip.getStart()));
            holder.TV_StartLoc.setText(trip.getStartLocation());
            holder.TV_EndLoc.setText(trip.getStartLocation());
            String snote = trip.getNote();
            String scat;
            if (trip.getCategory() == 1) {
                scat = "beruflich - ";
            } else {
                scat = "";
            }

            // TODO: 11.06.2018 calc trip length
            if(snote != null && !snote.isEmpty()) {
                if (snote.length() > 20) {
                    holder.TV_Summary.setText(trip.getStartKm() + " km - " + scat + snote.substring(0,20) + "...");
                } else {
                    holder.TV_Summary.setText(trip.getStartKm() + " km - " + scat + snote);
                }
            } else {
                if (scat.length() > 2) {
                    holder.TV_Summary.setText(trip.getStartKm() + " km - " + scat.substring(0,9));
                } else {
                    holder.TV_Summary.setText(trip.getStartKm() + " km");
                }
            }


            // TODO: 08.06.2018 calculate trip length!!!! 
            if (position%2 == 1) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEEEEE"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        } else {
            holder.tripItemView.setText("KEINE DATEN...");
        }
    }

    public void setTrips(List<Trip> trips) {
        this.mTrips = trips;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTrips has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTrips != null)
            return mTrips.size();
        else return 0;
    }
}
