package com.example.hopinnow.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hopinnow.R;
import com.example.hopinnow.database.DriverDatabaseAccessor;
import com.example.hopinnow.database.RequestDatabaseAccessor;
import com.example.hopinnow.entities.Driver;
import com.example.hopinnow.entities.Request;
import com.example.hopinnow.entities.RequestListAdapter;
import com.example.hopinnow.entities.User;
import com.example.hopinnow.statuslisteners.AvailRequestListListener;
import com.example.hopinnow.statuslisteners.DriverProfileStatusListener;
import com.example.hopinnow.statuslisteners.UserProfileStatusListener;

import java.util.ArrayList;


public class RequestListFragment extends Fragment implements DriverProfileStatusListener, AvailRequestListListener {
    Integer prePosition;
    //private Driver driver;
    ArrayList<Request> requestList;
    private DriverDatabaseAccessor driverDatabaseAccessor;
    private RequestDatabaseAccessor requestDatabaseAccessor;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);


        View view = inflater.inflate(R.layout.fragment_driver_requests, container, false);

        requestList = new ArrayList<Request>();
        //public Request(Driver driver, Rider rider, Location pickUpLoc, Location dropOffLoc, Date dateTime, Car car, Double estimatedFare){}

        //read request from database
        driverDatabaseAccessor = new DriverDatabaseAccessor();
        driverDatabaseAccessor.getDriverProfile(this);


        final FragmentActivity fragmentActivity = getActivity();
        ((DriverMapActivity)getActivity()).setButtonInvisible();
        //RequestListAdapter adapter = new RequestListAdapter(requestList, this.getContext());
        RequestListAdapter adapter = new RequestListAdapter(requestList, fragmentActivity);
        final ListView requestListView = (ListView)view.findViewById(R.id.requestList);
        requestListView.setAdapter(adapter);
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View itemView = getViewByPosition(position, requestListView);
                Button acceptBtn = itemView.findViewById(R.id.accept_btn);
                acceptBtn.setVisibility(View.VISIBLE);

                ((DriverMapActivity)getActivity()).setMapMarker(null, requestList.get(position).getPickUpLoc());
                ((DriverMapActivity)getActivity()).setMapMarker(null, requestList.get(position).getDropOffLoc());

                if (prePosition != null){
                    Button preAcceptBtn = getViewByPosition(position, requestListView).findViewById(R.id.accept_btn);
                    preAcceptBtn.setVisibility(View.INVISIBLE);
                }
                acceptBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((DriverMapActivity)getActivity()).switchFragment(R.layout.fragment_driver_pick_rider_up);

                    }
                });
                prePosition = position;

            }
        });


        return view;
    }

    /*
    Citation:
    Author: VVB
    Date: Jul 21 '14 at 11:57
    Title: android - listview get item view by position
    Link: https://stackoverflow.com/questions/24811536/android-listview-get-item-view-by-position
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void onDriverProfileRetrieveSuccess(Driver driver) {


    }

    @Override
    public void onDriverProfileRetrieveFailure() {

    }

    @Override
    public void onDriverProfileUpdateSuccess(Driver driver) {

    }

    @Override
    public void onDriverProfileUpdateFailure() {

    }

    @Override
    public void onRequestAddedSuccess() {

    }

    @Override
    public void onRequestAddedFailure() {

    }

    @Override
    public void onRequestDeleteSuccess() {

    }

    @Override
    public void onRequestDeleteFailure() {

    }

    @Override
    public void onGetRequiredRequestsSuccess(ArrayList<Request> requests) {
        this.requestList = requests;
    }

    @Override
    public void onGetRequiredRequestsFailure() {

    }
}