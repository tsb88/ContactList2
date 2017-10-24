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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.tejasbhoir.contactlist.MainActivity.mDualPane;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link contactListView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class contactListView extends Fragment {

    private OnFragmentInteractionListener mListener;
    ListView contacts;
    TextView emptyMessage;
    ArrayAdapter<String> adapter;
    ArrayList<String> contactNamesList;
    ArrayList<String> contactNumbersList;
    ArrayList<String> selectedRelList;
    ArrayList<String> selectedRelNumbersList;
    ArrayList<Relationships> relationshipsList;

    public contactListView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactNamesList = new ArrayList<>();
        contactNumbersList = new ArrayList<>();
        selectedRelList = new ArrayList<>();
        selectedRelNumbersList = new ArrayList<>();
        relationshipsList = new ArrayList<>();

        setRetainInstance(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        emptyMessage = getActivity().findViewById(R.id.emptyListMessage);

        Bundle args = getArguments();
        if (args != null) {
            relationshipsList = (ArrayList<Relationships>) args.getSerializable("relListView");
            contactNamesList = args.getStringArrayList("contactsUpdated");
            contactNumbersList = args.getStringArrayList("numbersUpdated");
        }

        if(!contactNamesList.isEmpty()) {
            emptyMessage.setVisibility(View.INVISIBLE);
        }

        contacts = getActivity().findViewById(R.id.contactList);
        Button addButton = getActivity().findViewById(R.id.addButton);
        Button deleteButton = getActivity().findViewById(R.id.deleteButton);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, contactNamesList);

        contacts.setAdapter(adapter);
        contacts.setItemsCanFocus(false);
        contacts.setChoiceMode(contacts.CHOICE_MODE_MULTIPLE);
        contacts.setOnItemLongClickListener(new contactListView.CheckBoxClick());

        contacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id){
                String nameSelected = contacts.getItemAtPosition(position).toString();
                String numberSelected = contactNumbersList.get(position);
                selectedRelList = relationshipsList.get(position).getRelations();

                FragmentManager fm2 = getFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                contactDetails detailsScreen = new contactDetails();
                AddFragment addScreen = new AddFragment();

                Bundle args2 = new Bundle();
                args2.putString("Name", nameSelected);
                args2.putString("Number", numberSelected);
                args2.putStringArrayList("relListDetails", selectedRelList);
                args2.putSerializable("relListFull", relationshipsList);
                args2.putStringArrayList("namesListDetails", contactNamesList);
                args2.putStringArrayList("numbersListDetails", contactNumbersList);
                detailsScreen.setArguments(args2);

                if (!mDualPane) {
                    ft2.remove(contactListView.this);
                    ft2.replace(R.id.detailsFragment, detailsScreen);
                    ft2.addToBackStack(null);
                    ft2.commit();
                }
                else {
                    ft2.remove(addScreen);
                    ft2.remove(detailsScreen);
                    FrameLayout addLayout = getActivity().findViewById(R.id.addFragment);
                    addLayout.setVisibility(View.GONE);
                    ft2.replace(R.id.detailsFragment, detailsScreen);
                    FrameLayout detailsLayout = getActivity().findViewById(R.id.detailsFragment);
                    detailsLayout.setVisibility(View.VISIBLE);
                    ft2.commit();
                }

                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                AddFragment addScreen = new AddFragment();
                contactDetails detailsScreen = new contactDetails();

                Bundle args = new Bundle();
                args.putStringArrayList("contacts", contactNamesList);
                args.putStringArrayList("numbers", contactNumbersList);
                args.putSerializable("relListAddFrag", relationshipsList);
                addScreen.setArguments(args);

                if (!mDualPane) {
                    ft.remove(contactListView.this);
                    ft.replace(R.id.addFragment, addScreen);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else {
                    ft.remove(detailsScreen);
                    ft.replace(R.id.addFragment, addScreen);
                    FrameLayout addLayout = getActivity().findViewById(R.id.addFragment);
                    addLayout.setVisibility(View.VISIBLE);
                    FrameLayout detailsLayout = getActivity().findViewById(R.id.detailsFragment);
                    detailsLayout.setVisibility(View.GONE);
                    ft.commit();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                for (int i = contactNamesList.size(); i >= 0; i--) {
                    if (contacts.isItemChecked(i)) {
                        String item = contacts.getItemAtPosition(i).toString();
                        for (int j = 0; j < relationshipsList.size(); j++) {
                            relationshipsList.get(j).removeElement(item);
                            if (relationshipsList.get(j).isEmpty()) {
                                relationshipsList.get(j).addEmptyMessage();
                            }
                        }
                    }
                    if (contacts.isItemChecked(i)) {
                        relationshipsList.remove(i);
                        contactNamesList.remove(i);
                        contactNumbersList.remove(i);
                    }
                }
                if (contactNamesList.isEmpty()) {
                    emptyMessage.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                contacts.clearChoices();
            }
        });
    }

    public class CheckBoxClick implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            CheckedTextView checkedItem = (CheckedTextView) view;
            if(checkedItem.isChecked()){
                Toast.makeText(getContext(), "now it is checked", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "now it is unchecked", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
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
