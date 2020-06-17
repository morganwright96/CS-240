package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shared.Model.Event;
import com.example.shared.Model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private DataCache dataCache = DataCache.getInstance();
    private TextView eventDetails;
    private TextView personName;
    private ImageView imageView;
    private LinearLayout info;
    private Map<String, Event> eventLookup = dataCache.getEventLookup();
    private Map<String, Person> personLookup = dataCache.getPersonLookup();
    private String eventID = null;
    private String activityEventID = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getActivity().getIntent().getExtras() != null){
            activityEventID = getActivity().getIntent().getExtras().getString("eventID");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(activityEventID == null){
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.map_menu, menu);

            MenuItem search = menu.findItem(R.id.search);
            MenuItem settings = menu.findItem(R.id.settings);
            search.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search).colorRes(R.color.colorWhite).actionBarSize());
            settings.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear).colorRes(R.color.colorWhite).actionBarSize());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.search:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        personName = view.findViewById(R.id.personName);
        eventDetails = view.findViewById(R.id.eventInfo);
        imageView = view.findViewById(R.id.genderIcon);
        info = view.findViewById(R.id.info);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // TODO figure out how to use the event id to create the map an animate it to the place we want

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map != null){
            map.clear();
            genMarkers();
            if(dataCache.getClickedEventID() != null) {
                String ID = dataCache.getClickedEventID();
                if(dataCache.getFilteredEvents().containsKey(ID)) {
                    genLines(dataCache.getClickedEventID(), map);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        genMarkers();

        if(activityEventID != null){
            Event tempEvent = eventLookup.get(activityEventID);
            LatLng latLng = new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            genEventInfo(activityEventID);
            genLines(activityEventID, map);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                eventID = marker.getTag().toString();
                dataCache.setClickedEventID(eventID);

                genEventInfo(eventID);

                map.clear();
                genMarkers();
                genLines(eventID, map);
                return false;
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonActivity.class);
                if(eventID != null){
                    Event event = eventLookup.get(eventID);
                    intent.putExtra("personID", event.getPersonID());
                }
                else if(activityEventID != null){
                    Event event = eventLookup.get(activityEventID);
                    intent.putExtra("personID", event.getPersonID());
                }
                startActivity(intent);
            }
        });
    }

    public void genMarkers(){
        Map<String, Float> colorLookup = genColorLookup();
        LatLng currentLatLng;
        Map<String, Event> events = dataCache.getFilteredEvents();
        for(Map.Entry<String,Event> entry : events.entrySet()){
            Event currentEvent = entry.getValue();
            currentLatLng = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
            Marker newMarker = map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(colorLookup.get(currentEvent.getEventType().toLowerCase()))));
            newMarker.setTag(currentEvent.getEventID());
        }
    }

    public void genLines(String eventID, GoogleMap map){
        Event currentEvent = eventLookup.get(eventID);
        Person currentPerson = personLookup.get(currentEvent.getPersonID());

        if(dataCache.isSpouseLines()){
            Person spouse = personLookup.get(currentPerson.getSpouseID());
            if(spouse != null){
                Event spouseEvent = dataCache.getEarliestEvent(spouse.getPersonID());
                if(spouseEvent != null){
                    map.addPolyline(new PolylineOptions()
                            .add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()), new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
                            .width(10)
                            .color(Color.RED));
                }
            }
        }
        if(dataCache.isLifeStoryLines()){
            addLifeEvents(currentPerson, map, currentEvent);
        }
        if(dataCache.isFamilyTreeLines()){
            int lineSize = 10;
            addAncestorLines(map, currentPerson, lineSize, currentEvent);
        }
    }

    public Map<String, Float> genColorLookup(){
        Map<String, Float> colorLookup = new HashMap<>();
        Set<String> eventTypes = dataCache.getEventTypes();
        Iterator<String> typeItr = eventTypes.iterator();
        int colorIndex = 0;

        ArrayList<Float> colors = new ArrayList<>();
        colors.add(BitmapDescriptorFactory.HUE_AZURE);
        colors.add(BitmapDescriptorFactory.HUE_BLUE);
        colors.add(BitmapDescriptorFactory.HUE_ORANGE);
        colors.add(BitmapDescriptorFactory.HUE_CYAN);
        colors.add(BitmapDescriptorFactory.HUE_GREEN);
        colors.add(BitmapDescriptorFactory.HUE_MAGENTA);
        colors.add(BitmapDescriptorFactory.HUE_VIOLET);

        while (typeItr.hasNext()){
            String type = typeItr.next();
            if(type.equals("birth")){
                colorLookup.put(type, BitmapDescriptorFactory.HUE_YELLOW);
            }
            else if(type.equals("death")){
                colorLookup.put(type, BitmapDescriptorFactory.HUE_RED);
            }
            else if(type.equals("marriage")){
                colorLookup.put(type, BitmapDescriptorFactory.HUE_ROSE);
            }
            else {
                if(colorIndex >= colors.size()){
                    colorIndex = 0;
                }
                colorLookup.put(type, colors.get(colorIndex));
                colorIndex++;
            }
        }

        return colorLookup;
    }

    public void addAncestorLines(GoogleMap map, Person currentPerson, int lineSize, Event currentEvent){
        if(currentPerson == null || currentEvent == null){
            return;
        }
        Event fathersEvent = dataCache.getEarliestEvent(currentPerson.getFatherID());
        Event mothersEvent = dataCache.getEarliestEvent(currentPerson.getMotherID());
        if(fathersEvent != null){
            map.addPolyline(new PolylineOptions()
                    .add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()), new LatLng(fathersEvent.getLatitude(), fathersEvent.getLongitude()))
                    .width(lineSize)
                    .color(Color.GREEN));
        }
        if(mothersEvent != null){
            map.addPolyline(new PolylineOptions()
                    .add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()), new LatLng(mothersEvent.getLatitude(), mothersEvent.getLongitude()))
                    .width(lineSize)
                    .color(Color.GREEN));
        }
        if(lineSize > 2){
            lineSize = lineSize - 2;
        }
        if(fathersEvent != null){
            addAncestorLines(map, personLookup.get(fathersEvent.getPersonID()), lineSize, fathersEvent);
        }
        if(mothersEvent != null){
            addAncestorLines(map, personLookup.get(mothersEvent.getPersonID()), lineSize, mothersEvent);
        }
    }

    public void addLifeEvents(Person currentPerson, GoogleMap map, Event currentEvent){
        ArrayList<Event> events = dataCache.getChronologicalEvents(currentPerson.getPersonID());
        if (events.size() <= 1) {
            return;
        }
        for(int i = 0; i < events.size() - 1; i++){
            Event tempEvent = events.get(i);
            Event nextEvent = events.get(i + 1);
            map.addPolyline(new PolylineOptions()
                    .add(new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude()), new LatLng(nextEvent.getLatitude(), nextEvent.getLongitude()))
                    .width(10)
                    .color(Color.BLUE));
        }

    }

    public void genEventInfo(String ID){
        Event event = eventLookup.get(ID);
        Person person = personLookup.get(event.getPersonID());
        personName.setText(person.getFirstName() + " " + person.getLastName());
        eventDetails.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
        // show the gender of the person fro the event
        if(person.getGender().equals('m')){
            imageView.setImageResource(R.drawable.man);
        }
        else {
            imageView.setImageResource(R.drawable.female);
        }
    }
}