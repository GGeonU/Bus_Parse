package com.example.dell.bus_parse_project.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bus_parse_project.R;
import com.example.dell.bus_parse_project.XmlParser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    XmlParser parser = new XmlParser();

    ListView listView;
    ImageButton button;
    TextView station_info;
    TextView set_time;
    String stid_param = "114000240";
    String[] routeid_param = {"232000067", "100100098", "100100099", "100100550", "100100303", "100100304", "100100305"};
    String[] ord_param = {"90", "78", "80", "70", "35", "9", "52"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.arrived_info, container, false);

        listView = (ListView) view1.findViewById(R.id.arrived_info);

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)view1.findViewById(R.id.swipe_layout);
        station_info = (TextView) view1.findViewById(R.id.station_name);
        button = (ImageButton)view1.findViewById(R.id.refresh);
        set_time = (TextView) view1.findViewById(R.id.set_time);
        listView.setAdapter(parser.adapter);

        parser.getXml(stid_param, routeid_param, ord_param);
        station_info.setText(parser.getStationInfo());
        set_time.setText(parser.getSetTime());

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setAdapter(parser.adapter);
                parser.getXml(stid_param, routeid_param, ord_param);
                station_info.setText(parser.getStationInfo());
                set_time.setText(parser.getSetTime());
                swipe.setRefreshing(false);
                Toast.makeText(getActivity(), "갱신되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(parser.adapter);
                parser.getXml(stid_param, routeid_param, ord_param);
                station_info.setText(parser.getStationInfo());
                set_time.setText(parser.getSetTime());
                Toast.makeText(getActivity(), "갱신되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        return view1;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
