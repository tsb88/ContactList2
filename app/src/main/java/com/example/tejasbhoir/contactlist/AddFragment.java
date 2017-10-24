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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.tejasbhoir.contactlist.MainActivity.mDualPane;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    EditText name;
    EditText number;
    ListView contactList2;
    ArrayAdapter<String> adapter;
    ArrayList<String> contactStrings;
    ArrayList<String> numberStrings;
    ArrayList<String> contactStringsStatic;
    ArrayList<String> numberStringsStatic;
    ArrayList<String> relationships;
    ArrayList<String> relationshipsNumbers;
    ArrayList<Relationships> relationshipsList;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactStrings = new ArrayList<>();
        numberStrings = new ArrayList<>();
        contactStringsStatic = new ArrayList<>();
        numberStringsStatic = new ArrayList<>();
        relationships = new ArrayList<>();
        relationshipsNumbers = new ArrayList<>();
        relationshipsList = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
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

        contactList2 = getActivity().findViewById(R.id.contactListRel);
        name = getActivity().findViewById(R.id.nameOfContact);
        number = getActivity().findViewById(R.id.phoneNumber);
        Button add = getActivity().findViewById(R.id.addContactToList);
        Button back = getActivity().findViewById(R.id.backToContacts);

        Bundle args = getArguments();
        contactStringsStatic = args.getStringArrayList("contacts");
        numberStringsStatic = args.getStringArrayList("numbers");
        relationshipsList = (ArrayList<Relationships>) args.getSerializable("relListAddFrag");

        contactStrings = new ArrayList<>(contactStringsStatic);
        numberStrings = new ArrayList<>(numberStringsStatic);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, contactStrings);

        contactList2.setAdapter(adapter);
        contactList2.setItemsCanFocus(false);
        contactList2.setChoiceMode(contactList2.CHOICE_MODE_MULTIPLE);
        contactList2.setOnItemClickListener(new AddFragment.CheckBoxClick());

        number.setClickable(true);
        name.setFocusableInTouchMode(true);
        name.requestFocus();
        number.setFocusableInTouchMode(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                String contactName = name.getText().toString();
                String contactNumber = number.getText().toString();

                if(relationships.isEmpty()) {
                    relationships.add("No relationships");
                }
                else {
                    for (int y = 0; y < relationships.size(); y++) {
                        for (int z = 0; z < contactStringsStatic.size(); z++) {
                            String checkItem = relationships.get(y);
                            if (contactStringsStatic.get(z).equals(checkItem)) {
                                if (relationshipsList.get(z).getElement(0).equals("No relationships")) {
                                    relationshipsList.get(z).removeElement("No relationships");
                                }
                                relationshipsList.get(z).addElement(contactName, 0);
                            }
                        }
                    }
                }
                Relationships relations = new Relationships(relationships);
                relationshipsList.add(relations);

                contactStrings.add(contactName);
                numberStrings.add(contactNumber);
                contactList2.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                relationships = new ArrayList<>();
                name.getText().clear();
                number.getText().clear();
                name.setSelectAllOnFocus(true);
                name.requestFocus();

                contactStringsStatic.add(contactName);
                numberStringsStatic.add(contactNumber);

                if (mDualPane) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    contactListView listViewScreen = new contactListView();

                    Bundle args = new Bundle();
                    args.putStringArrayList("contactsUpdated", contactStringsStatic);
                    args.putStringArrayList("numbersUpdated", numberStringsStatic);
                    args.putSerializable("relListView", relationshipsList);
                    listViewScreen.setArguments(args);

                    ft.remove(listViewScreen);
                    ft.replace(R.id.ListView, listViewScreen);
                    ft.commit();
                }
                Toast.makeText(getContext(), contactName + " - " + contactNumber, Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                contactListView listViewScreen = new contactListView();

                Bundle args = new Bundle();
                args.putStringArrayList("contactsUpdated", contactStringsStatic);
                args.putStringArrayList("numbersUpdated", numberStringsStatic);
                args.putSerializable("relListView", relationshipsList);
                listViewScreen.setArguments(args);

                if (!mDualPane) {
                    ft.remove(AddFragment.this);
                    ft.replace(R.id.ListView, listViewScreen);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
    }

    public class CheckBoxClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            String item;
            String nameTemp;
            String numberTemp;
            String nameSelected = contactStrings.get(position);
            String numberSelected = numberStrings.get(position);
            int size = contactStrings.size();
            int x;
            int i;
            CheckedTextView checkedItem = (CheckedTextView) view;
            if(checkedItem.isChecked()){
                item = checkedItem.getText().toString();
                relationships.add(item);
                if (size > 0) {
                    for (i = 0; i < position; i++) {
                        if(!contactList2.isItemChecked(i)) {
                            x = i;
                            nameTemp = contactStrings.get(x);
                            numberTemp = numberStrings.get(x);
                            contactStrings.set(position, nameTemp);
                            numberStrings.set(position, numberTemp);
                            contactStrings.set(x, nameSelected);
                            numberStrings.set(x, numberSelected);
                            contactList2.setItemChecked(x, true);
                            contactList2.setItemChecked(position, false);
                            break;
                        }
                    }
                    if (i == size) {
                        contactList2.setItemChecked(position, true);
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
            }
            else {
                for (int j = position; j < size; j++) {
                    if (contactList2.isItemChecked(j+1)) {
                        nameTemp = contactStrings.get(j+1);
                        numberTemp = numberStrings.get(j+1);
                        contactStrings.set(j+1, nameSelected);
                        numberStrings.set(j+1, numberSelected);
                        contactStrings.set(j, nameTemp);
                        numberStrings.set(j, numberTemp);
                        contactList2.setItemChecked(j, true);
                        contactList2.setItemChecked(j+1,false);
                    }
                    else{
                        break;
                    }
                    adapter.notifyDataSetChanged();
                }
                item = checkedItem.getText().toString();
                relationships.remove(item);
                Toast.makeText(getContext(), "now it is unchecked", Toast.LENGTH_SHORT).show();
            }
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
