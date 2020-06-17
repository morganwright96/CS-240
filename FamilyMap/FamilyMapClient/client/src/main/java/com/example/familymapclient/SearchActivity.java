package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shared.Model.Event;
import com.example.shared.Model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    ArrayList<Person> people = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();
    RecyclerView recyclerView;
    DataCache dataCache = DataCache.getInstance();
    private static final int PERSON_LAYOUT_TYPE = 0;
    private static final int EVENT_LAYOUT_TYPE = 1;
    SearchFamilyMapAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setIconified(false);
        searchView.clearFocus();

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*people = dataCache.getSearchPeople(query);
                events = dataCache.getSearchEvents(query);
                adapter = new SearchFamilyMapAdapter(people, events);
                recyclerView.setAdapter(adapter);*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // listen for the change of the text
                people = dataCache.getSearchPeople(newText);
                events = dataCache.getSearchEvents(newText);
                if(newText.equals("")){
                    people.clear();
                    events.clear();
                }
                adapter = new SearchFamilyMapAdapter(people, events);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
        adapter = new SearchFamilyMapAdapter(people, events);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private class SearchFamilyMapAdapter extends RecyclerView.Adapter<SearchFamilyMapViewHolder>{
        private final ArrayList<Person> personArrayList;
        private final ArrayList<Event> eventArrayList;

        public SearchFamilyMapAdapter(ArrayList<Person> personArrayList, ArrayList<Event> eventArrayList) {
            this.personArrayList = personArrayList;
            this.eventArrayList = eventArrayList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < personArrayList.size() ? PERSON_LAYOUT_TYPE : EVENT_LAYOUT_TYPE;
        }

        @NonNull
        @Override
        public SearchFamilyMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if(viewType == PERSON_LAYOUT_TYPE){
                view = getLayoutInflater().inflate(R.layout.person_search_layout, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.event_search_layout, parent, false);
            }
            return new SearchFamilyMapViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchFamilyMapViewHolder holder, int position) {
            if(position < personArrayList.size()){
                holder.bind(personArrayList.get(position));
            }
            else {
                holder.bind(eventArrayList.get(position - personArrayList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personArrayList.size() + eventArrayList.size();
        }
    }

    private class SearchFamilyMapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView personName;
        TextView eventInfo;
        ImageView icon;

        private Person person;
        private Event event;
        private final int viewType;

        public SearchFamilyMapViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_LAYOUT_TYPE){
                personName = itemView.findViewById(R.id.personsName);
                icon = itemView.findViewById(R.id.personGender);
            }
            else {
                icon = itemView.findViewById(R.id.mapMarkerIcon);
                eventInfo = itemView.findViewById(R.id.search_event_info);
                personName = itemView.findViewById(R.id.search_event_person_name);
            }
        }

        @Override
        public void onClick(View v) {
            if(viewType == PERSON_LAYOUT_TYPE){
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("personID", person.getPersonID());
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("eventID", event.getEventID());
                startActivity(intent);
            }
        }

        public void bind(Person currentPerson){
            person = currentPerson;
            personName.setText(currentPerson.getFirstName() + " " + currentPerson.getLastName());
            if(currentPerson.getGender().equals('m')){
                icon.setImageResource(R.drawable.man);
            }
            else {
                icon.setImageResource(R.drawable.female);
            }
        }

        public void bind(Event currentEvent){
            event = currentEvent;
            Person currentPerson = dataCache.getPersonLookup().get(currentEvent.getPersonID());
            eventInfo.setText(currentEvent.getEventType().toUpperCase() + ": " + currentEvent.getCity() + ", " + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");
            personName.setText(currentPerson.getFirstName() + " " + currentPerson.getLastName());
            icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.firebrick));
        }
    }
}