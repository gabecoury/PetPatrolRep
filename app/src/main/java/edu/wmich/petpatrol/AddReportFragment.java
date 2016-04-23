package edu.wmich.petpatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

//this class if for reporting  found pets and lost pets
public class AddReportFragment extends Fragment{

    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private static final int REQUEST_START_TIME = 2;
    private static final int REQUEST_END_TIME = 3;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String TAG = "AddReportFragment";

    private RadioGroup mRadioGroupReportType;
    private Button mPetSubmitButton;
    private Button mEventSubmitButton;
    private Button mEventStartDateButton;
    private Button mEventEndDateButton;
    private Button mEventStartTimeButton;
    private Button mEventEndTimeButton;
    private Calendar mEventStartDate;
    private Calendar mEventEndDate;
    private Calendar mEventStartTime;
    private Calendar mEventEndTime;
    private Event mEvent = new Event();
    private Pet mPet = new Pet();
    private EditText EventNameEditText;
    private EditText EventContactEditText;
    private EditText EventDetailsEditText;
    private RadioGroup PetStatusRadioGroup;
    private EditText PetDescriptionEditText;
    private EditText PetNameEditText;
    private EditText PetContactEditText;
    private EditText PetDetailsEditText;
    private Spinner PetTypeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        EventNameEditText = (EditText) v.findViewById(R.id.editTextEventName);
        EventContactEditText= (EditText) v.findViewById(R.id.editTextEventContact);
        EventDetailsEditText = (EditText) v.findViewById(R.id.editTextEventDetails);
        PetStatusRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroupPetStatus);
        PetDescriptionEditText = (EditText) v.findViewById(R.id.editTextPetDescription);
        PetNameEditText = (EditText) v.findViewById(R.id.editTextPetName);
        PetContactEditText = (EditText) v.findViewById(R.id.editTextPetContact);
        PetDetailsEditText = (EditText) v.findViewById(R.id.editTextPetDetails);
        mEventStartTime = Calendar.getInstance();
        mEventEndTime = Calendar.getInstance();
        mEventStartDate = Calendar.getInstance();
        mEventEndDate = Calendar.getInstance();
        PetTypeSpinner = (Spinner) v.findViewById(R.id.spinnerPetType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.pet_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PetTypeSpinner.setAdapter(adapter);

        PetTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPet.setPetType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final LinearLayout mLinearLayoutPet = (LinearLayout) v.findViewById(R.id.LinearLayoutPet);
        final LinearLayout mLinearLayoutEvent = (LinearLayout) v.findViewById(R.id.LinearLayoutEvent);
        final TextView mTextViewSelectOption = (TextView) v.findViewById(R.id.selectOption);
        mRadioGroupReportType = (RadioGroup) v.findViewById(R.id.radioGroupReportType);

        mRadioGroupReportType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonPet:
                        mLinearLayoutEvent.setVisibility(View.GONE);
                        mLinearLayoutPet.setVisibility(View.VISIBLE);
                        mTextViewSelectOption.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonEvent:
                        mLinearLayoutPet.setVisibility(View.GONE);
                        mLinearLayoutEvent.setVisibility(View.VISIBLE);
                        mTextViewSelectOption.setVisibility(View.GONE);
                        break;
                }
            }
        });

        mEventStartDateButton = (Button) v.findViewById(R.id.buttonEventStartDate);


        mEventStartDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mEventStartDate);
                dialog.setTargetFragment(AddReportFragment.this, REQUEST_START_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mEventEndDateButton = (Button) v.findViewById(R.id.buttonEventEndDate);

        mEventEndDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mEventEndDate);
                dialog.setTargetFragment(AddReportFragment.this, REQUEST_END_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mEventStartTimeButton = (Button) v.findViewById(R.id.buttonEventStartTime);

        mEventStartTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mEventStartTime);
                dialog.setTargetFragment(AddReportFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mEventEndTimeButton = (Button) v.findViewById(R.id.buttonEventEndTime);

        mEventEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mEventEndTime);
                dialog.setTargetFragment(AddReportFragment.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });


        mPetSubmitButton = (Button) v.findViewById(R.id.buttonPetSubmit);
        mEventSubmitButton = (Button) v.findViewById(R.id.buttonEventSubmit);

        mPetSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                switch(PetStatusRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButtonPetLost:
                        mPet.setFound(false);
                        break;
                    case R.id.radioButtonPetFound:
                        mPet.setFound(true);
                        break;
                    default:
                        Toast.makeText(getContext(), "Please select a Pet Status.", Toast.LENGTH_LONG).show();
                        return;
                }
                if(PetDescriptionEditText.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please enter a Pet Color/Description.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mPet.getPetType().equals(""))
                {
                    Toast.makeText(getContext(), "Please Select a Pet Type.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!PetContactEditText.getText().toString().equals(""))
                {
                    mEvent.setContactNumber(Integer.parseInt(PetContactEditText.getText().toString()));
                }
                mPet.setPetDescription(PetDescriptionEditText.getText().toString());
                mPet.setPetName(PetNameEditText.getText().toString());
                mPet.setDetails(PetDetailsEditText.getText().toString());

                Toast.makeText(getContext(), "You reported a Lost/Found " + mPet.getPetType() + " with a description of " + mPet.getPetDescription() + " with a name of " + mPet.getPetName() + ". You may be contacted at " + mPet.getContactNumber() + " and details: " + mPet.getDetails(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "You reported a Lost/Found pet with a description of " + mPet.getPetDescription() + " with a name of " + mPet.getPetName() + ". You may be contacted at " + mPet.getContactNumber() + " and details: " + mPet.getDetails());
                // Insert Code for sending Pet Data to the server

            }
        });

        mEventSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(EventNameEditText.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please enter a Event Name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!EventContactEditText.getText().toString().equals(""))
                {
                    mEvent.setContactNumber(Integer.parseInt(EventContactEditText.getText().toString()));
                }

                mEvent.setEventName(EventNameEditText.getText().toString());
                Calendar combinedStartC = Calendar.getInstance();
                combinedStartC.set(Calendar.YEAR, mEventStartDate.get(Calendar.YEAR));
                combinedStartC.set(Calendar.MONTH, mEventStartDate.get(Calendar.MONTH));
                combinedStartC.set(Calendar.DAY_OF_MONTH, mEventStartDate.get(Calendar.DAY_OF_MONTH));
                combinedStartC.set(Calendar.HOUR_OF_DAY, mEventStartTime.get(Calendar.HOUR_OF_DAY));
                combinedStartC.set(Calendar.MINUTE, mEventStartTime.get(Calendar.MINUTE));
                combinedStartC.set(Calendar.SECOND, 0);
                combinedStartC.set(Calendar.MILLISECOND, 0);
                Calendar combinedEndC = Calendar.getInstance();
                combinedEndC.set(Calendar.YEAR, mEventEndDate.get(Calendar.YEAR));
                combinedEndC.set(Calendar.MONTH, mEventEndDate.get(Calendar.MONTH));
                combinedEndC.set(Calendar.DAY_OF_MONTH, mEventEndDate.get(Calendar.DAY_OF_MONTH));
                combinedEndC.set(Calendar.HOUR_OF_DAY, mEventEndTime.get(Calendar.HOUR_OF_DAY));
                combinedEndC.set(Calendar.MINUTE, mEventEndTime.get(Calendar.MINUTE));
                combinedEndC.set(Calendar.SECOND, 0);
                combinedEndC.set(Calendar.MILLISECOND, 0);

                if(combinedEndC.getTimeInMillis() <= combinedStartC.getTimeInMillis()){
                    Toast.makeText(getContext(), "Event End Date & Time must be after the Start Date & Time.", Toast.LENGTH_LONG).show();
                    return;
                }
                mEvent.setEventStartDateTime(combinedStartC);
                mEvent.setEventEndDateTime(combinedEndC);
                mEvent.setDetails(EventDetailsEditText.getText().toString());

                Toast.makeText(getContext(), "Event Created: " + mEvent.getEventName() + " will start on " + mEvent.getEventStartDateTime().getTime().toString() + " and end on " + mEvent.getEventEndDateTime().getTime().toString() + ". Please call " + mEvent.getContactNumber() + " details: " + mEvent.getDetails(),Toast.LENGTH_LONG).show();
                Log.i(TAG,"Event Created: " + mEvent.getEventName() + " will start on " + mEvent.getEventStartDateTime().getTime().toString() + " and end on " + mEvent.getEventEndDateTime().getTime().toString() + ". Please call " + mEvent.getContactNumber() + " details: " + mEvent.getDetails());
                // Insert Code for sending Event Data to the server
            }
        });



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_START_DATE) {
            mEventStartDate = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);


        }
        else if(requestCode == REQUEST_END_DATE) {
            mEventEndDate = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        }
        else if (requestCode == REQUEST_START_TIME) {
            mEventStartTime = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);


        }
        else if(requestCode == REQUEST_END_TIME) {
            mEventEndTime = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
        }
    }

}
