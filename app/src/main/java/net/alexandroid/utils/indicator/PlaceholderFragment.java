package net.alexandroid.utils.indicator;


import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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

        ConstraintLayout constraintLayout = rootView.findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(getColor(section));
        return rootView;
    }

    private int getColor(int section) {
        switch (section){
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
}
