package edu.wmich.petpatrol.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//this class if for reporting  found pets and lost pets
public class AddReportFragment extends Fragment{

    private RadioButton mRadioButtonPet;
    private RadioButton mRadioButtonEvent;
    private RadioGroup mRadioGroupReportType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        final LinearLayout mLinearLayoutPet = (LinearLayout) v.findViewById(R.id.LinearLayoutPet);
        final LinearLayout mLinearLayoutEvent = (LinearLayout) v.findViewById(R.id.LinearLayoutEvent);
        mRadioGroupReportType = (RadioGroup) v.findViewById(R.id.radioGroupReportType);

        mRadioGroupReportType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonPet:
                        mLinearLayoutEvent.setVisibility(View.GONE);
                        mLinearLayoutPet.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButtonEvent:
                        mLinearLayoutPet.setVisibility(View.GONE);
                        mLinearLayoutEvent.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        return v;
    }
}
