package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shared.Model.Event;
import com.example.shared.Model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Map;

public class PersonActivity extends AppCompatActivity {
    private DataCache dataCache = DataCache.getInstance();
    private Map<String, Person> personMap = dataCache.getPersonLookup();
    private Map<String, Event> eventMap = dataCache.getEventLookup();
    private TextView fname;
    private TextView lname;
    private TextView gender;
    private ExpandableListView expandableListView;
    Person currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        String personID = getIntent().getExtras().getString("personID");
        fname = findViewById(R.id.fName);
        lname = findViewById(R.id.lName);
        gender = findViewById(R.id.gender);
        expandableListView = findViewById(R.id.expandableListView);

        currentPerson = personMap.get(personID);

        fname.setText(currentPerson.getFirstName());
        lname.setText(currentPerson.getLastName());
        if(currentPerson.getGender().equals('m')){
            gender.setText("Male");
        }
        else {
            gender.setText("Female");
        }

        // These two array list are used for the expandable list views
        ArrayList<Person> familyMembers = dataCache.getFamily(currentPerson);
        ArrayList<Event> lifeEvents = dataCache.getChronologicalEvents(currentPerson.getPersonID());

        expandableListView.setAdapter(new ExpandableListAdapter(familyMembers, lifeEvents));
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

    public class ExpandableListAdapter extends BaseExpandableListAdapter{

        private static final int LIFE_EVENT_POSITION = 0;
        private static final int FAMILY_POSITION = 1;

        private final ArrayList<Person> familyMembers;
        private final ArrayList<Event> lifeEvents;

        String personID = null;

        public ExpandableListAdapter(ArrayList<Person> familyMembers, ArrayList<Event> lifeEvents) {
            this.familyMembers = familyMembers;
            this.lifeEvents = lifeEvents;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENT_POSITION:
                    return lifeEvents.size();
                case FAMILY_POSITION:
                    return familyMembers.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENT_POSITION:
                    return getString(R.string.lifeEventGroup);
                case FAMILY_POSITION:
                    return getString(R.string.familyGroup);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition){
                case LIFE_EVENT_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_POSITION:
                    return familyMembers.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.group_title_layout, parent, false);
            }
            TextView title = convertView.findViewById(R.id.groupTitle);

            switch (groupPosition){
                case LIFE_EVENT_POSITION:
                    title.setText(R.string.lifeEventGroup);
                    break;
                case FAMILY_POSITION:
                    title.setText(R.string.familyGroup);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = getLayoutInflater().inflate(R.layout.group_item_layout, parent, false);
            switch (groupPosition){
                case LIFE_EVENT_POSITION:
                    initializeEvent(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    initializePerson(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initializeEvent(View itemView, final int childPosition){
            LinearLayout lifeEvent = itemView.findViewById(R.id.lifeEvent);

            ImageView icon = itemView.findViewById(R.id.lifeEventIcon);
            icon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.firebrick));

            TextView eventInfo = itemView.findViewById(R.id.line1);
            Event currentEvent = lifeEvents.get(childPosition);
            eventInfo.setText(currentEvent.getEventType().toUpperCase() + ": " + currentEvent.getCity() + ", " + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");

            Person personForEvent = dataCache.getPersonLookup().get(currentEvent.getPersonID());
            TextView personName = itemView.findViewById(R.id.line2);
            personName.setText(personForEvent.getFirstName() + " " + personForEvent.getLastName());

            lifeEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("eventID", lifeEvents.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePerson(View itemView, final int childPosition){
            LinearLayout familyView = itemView.findViewById(R.id.lifeEvent);
            Person personForEvent = familyMembers.get(childPosition);
            personID = personForEvent.getPersonID();

            ImageView icon = itemView.findViewById(R.id.lifeEventIcon);
            if(personForEvent.getGender().equals('m')){
                icon.setImageResource(R.drawable.man);
            }
            else {
                icon.setImageResource(R.drawable.female);
            }

            TextView personName = itemView.findViewById(R.id.line1);
            personName.setText(personForEvent.getFirstName() + " " + personForEvent.getLastName());

            TextView familyRelation = itemView.findViewById(R.id.line2);
            if(currentPerson.getFatherID() != null && currentPerson.getFatherID().equals(personID)){
                familyRelation.setText("Father");
            }
            else if(currentPerson.getMotherID() != null && currentPerson.getMotherID().equals(personID)){
                familyRelation.setText("Mother");
            }
            else if(currentPerson.getSpouseID() != null && currentPerson.getSpouseID().equals(personID)){
                familyRelation.setText("Spouse");
            }
            else {
                familyRelation.setText("Child");
            }

            familyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("personID", familyMembers.get(childPosition).getPersonID());
                    startActivity(intent);
                }
            });
        }
    }
}