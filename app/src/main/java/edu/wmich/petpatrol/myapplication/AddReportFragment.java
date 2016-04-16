package edu.wmich.petpatrol.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

//this class if for reporting  found pets and lost pets
public class AddReportFragment extends Fragment{

    private RadioGroup mRadioGroupReportType;
    private Button mPetSubmitButton;
    private Button mEventSubmitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

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

        mPetSubmitButton = (Button) v.findViewById(R.id.buttonPetSubmit);
        mEventSubmitButton = (Button) v.findViewById(R.id.buttonEventSubmit);

        mPetSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });



        return v;
    }
}
