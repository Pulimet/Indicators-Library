package net.alexandroid.utils.indicator;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private CategoryFragmentInteractionListener mListener;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = rootView.findViewById(R.id.section_label);
        int section = getArguments().getInt(ARG_SECTION_NUMBER);
        textView.setText(getString(R.string.section_format, section));


        rootView.findViewById(R.id.btnSelected).setOnClickListener(this);
        rootView.findViewById(R.id.btnUnselected).setOnClickListener(this);
        rootView.findViewById(R.id.btnSetBackground).setOnClickListener(this);

        ConstraintLayout constraintLayout = rootView.findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(getColor(section));
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelected:
                mListener.onSetDrawableToSelectedIndicator();
                break;
            case R.id.btnUnselected:
                mListener.onSetDrawableToUnSelectedIndicator();
                break;
            case R.id.btnSetBackground:
                mListener.onSetBackgroundColor();
                break;
        }
    }

    private int getColor(int section) {
        switch (section) {
            case 1:
                return getResources().getColor(R.color.color1);
            case 2:
                return getResources().getColor(R.color.color2);
            case 3:
                return getResources().getColor(R.color.color3);
            case 4:
                return getResources().getColor(R.color.color4);
            case 5:
                return getResources().getColor(R.color.color5);
            case 6:
                return getResources().getColor(R.color.color6);
            case 7:
                return getResources().getColor(R.color.color7);
            default:
                return Color.WHITE;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    @SuppressWarnings("SameParameterValue")
    public interface CategoryFragmentInteractionListener {
        void onSetDrawableToSelectedIndicator();

        void onSetDrawableToUnSelectedIndicator();

        void onSetBackgroundColor();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryFragmentInteractionListener) {
            mListener = (CategoryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategoryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
