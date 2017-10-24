package com.example.tejasbhoir.contactlist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link contactDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class contactDetails extends Fragment {

    private OnFragmentInteractionListener mListener;
    String nameReceived;
    String numberReceived;
    ArrayList<String> selectedRelList;
    ArrayList<String> numbers;
    ArrayList<String> names;
    ArrayList<String> relationships;
    ArrayList<Relationships> relationshipsFullList;

    public contactDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        relationships = new ArrayList<>();
        selectedRelList = new ArrayList<>();
        numbers = new ArrayList<>();
        names = new ArrayList<>();
        relationshipsFullList = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final ListView relationshipsList = getActivity().findViewById(R.id.relList);
        TextView nameText = getActivity().findViewById(R.id.detailName);
        final TextView numberText = getActivity().findViewById(R.id.detailNumber);

        Bundle args = getArguments();
        if (args != null) {
            nameReceived = args.getString("Name");
            numberReceived = args.getString("Number");
            numbers = args.getStringArrayList("numbersListDetails");
            names = args.getStringArrayList("namesListDetails");
            relationships = args.getStringArrayList("relListDetails");
            relationshipsFullList = (ArrayList<Relationships>) args.getSerializable("relListFull");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, relationships);

        nameText.setText(nameReceived);
        numberText.setText(numberReceived);
        relationshipsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        relationshipsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                String nameSelected = relationshipsList.getItemAtPosition(position).toString();
                if (nameSelected != "No relationships") {
                    int x;
                    x = names.indexOf(nameSelected);
                    String numberSelected = numbers.get(x);

                    selectedRelList = relationshipsFullList.get(x).getRelations();

                    FragmentManager fm2 = getFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    contactDetails detailsScreen = new contactDetails();

                    Bundle args3 = new Bundle();
                    args3.putString("Name", nameSelected);
                    args3.putString("Number", numberSelected);
                    args3.putStringArrayList("relListDetails", selectedRelList);
                    args3.putSerializable("relListFull", relationshipsFullList);
                    args3.putStringArrayList("namesListDetails", names);
                    args3.putStringArrayList("numbersListDetails", numbers);
                    detailsScreen.setArguments(args3);

                    ft2.remove(contactDetails.this);
                    ft2.replace(R.id.detailsFragment, detailsScreen);
                    ft2.addToBackStack(null);
                    ft2.commit();
                }

                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
