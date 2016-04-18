package edu.wmich.petpatrol.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

//this class if for reporting  found pets and lost pets
public class AddReportFragment extends Fragment{

    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private static final int REQUEST_START_TIME = 2;
    private static final int REQUEST_END_TIME = 3;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private RadioGroup mRadioGroupReportType;
    private Button mPetSubmitButton;
    private Button mEventSubmitButton;
    private Button mEventStartDateButton;
    private Button mEventEndDateButton;
    private Button mEventStartTimeButton;
    private Button mEventEndTimeButton;
    private Date mEventStartDate;
    private Date mEventEndDate;
    private Date mEventStartTime;
    private Date mEventEndTime;
    private Event mEvent = new Event();
    private Pet mPet = new Pet();
    private EditText EventNameEditText;
    private EditText EventContactEditText;
    private EditText EventDetailsEditText;

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
        mEventStartDate = new Date();
        mEventEndDate = new Date();
        mEventStartTime = new Date();
        mEventEndTime = new Date();

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



                        RadioGroup PetStatusRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroupPetStatus);
                        EditText PetDescriptionEditText = (EditText) v.findViewById(R.id.editTextPetDetails);
                        EditText PetNameEditText = (EditText) v.findViewById(R.id.editTextPetName);
                        EditText PetContactEditText = (EditText) v.findViewById(R.id.editTextPetContact);
                        EditText PetDetailsEditText = (EditText) v.findViewById(R.id.editTextPetDetails);

                        switch(PetStatusRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.radioButtonPetLost:
                                mPet.setFound(false);
                                break;
                            case R.id.radioButtonPetFound:
                                mPet.setFound(true);
                                break;
                        }
                        mPet.setPetDescription(PetDescriptionEditText.getText().toString());
                        mPet.setPetName(PetNameEditText.getText().toString());
                        mPet.setContactNumber(Integer.parseInt(PetContactEditText.getText().toString()));
                        mPet.setDetails(PetDetailsEditText.getText().toString());

                        // Insert Code for sending Pet Data to the server

            }
        });

        mEventSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                mEvent.setEventName(EventNameEditText.getText().toString());
//                Calendar cStart = Calendar.getInstance();
//                cStart.setTimeInMillis(mEventStartDate.getTime() + mEventStartTime.getTime());
//                Calendar cEnd = Calendar.getInstance();
//                cEnd.setTimeInMillis(mEventEndDate.getTime() + mEventEndTime.getTime());
//                mEvent.setEventStartDateTime(cStart.getTime());
//                mEvent.setEventEndDateTime(cEnd.getTime());
                Date combinedStart = new Date(mEventStartDate.getYear(), mEventStartDate.getMonth(), mEventStartDate.getDate(), mEventStartTime.getHours(),mEventStartTime.getMinutes());
                Date combinedEnd = new Date(mEventEndDate.getYear(), mEventEndDate.getMonth(), mEventEndDate.getDate(), mEventEndTime.getHours(),mEventEndTime.getMinutes());
                mEvent.setEventStartDateTime(combinedStart);
                mEvent.setEventEndDateTime(combinedEnd);
                mEvent.setContactNumber(Integer.parseInt(EventContactEditText.getText().toString()));
                mEvent.setDetails(EventDetailsEditText.getText().toString());

                Toast.makeText(getContext(), "Event Created: " + mEvent.getEventName() + " will start on " + mEvent.getEventStartDateTime().toString() + " and end on" + mEvent.getEventEndDateTime().toString() + ". Please call " + mEvent.getContactNumber() + " details: " + mEvent.getDetails(),Toast.LENGTH_LONG).show();

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
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mEventStartDate = date;


        }
        else if(requestCode == REQUEST_END_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mEventEndDate = date;
        }
        else if (requestCode == REQUEST_START_TIME) {
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mEventStartTime = time;


        }
        else if(requestCode == REQUEST_END_TIME) {
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mEventEndTime = time;
        }
    }

}
