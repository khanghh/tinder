package com.example.tinder.signup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.model.User;
import com.example.rest.service.SignupService;
import com.example.tinder.R;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    Button btnRegister;
    EditText txtName, txtEmail,txtPass,txtAge;
    RadioButton rdBtnMale, rdBtnFemale;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls(view);
        addEvents(view);
    }

    private void addControls(View view) {
        btnRegister = view.findViewById(R.id.btnRegister);
        txtAge = view.findViewById(R.id.txtAge);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtName = view.findViewById(R.id.txtName);
        txtPass = view.findViewById(R.id.txtPassword);
        rdBtnMale = view.findViewById(R.id.radioButtonMale);
        rdBtnFemale = view.findViewById(R.id.radioButtonFemale);
    }

    private void addEvents(View view) {
        final NavController navController = Navigation.findNavController(view);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User newUser = null;
                try {
                    newUser = getUserFromUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (newUser != null) {
                    newUser.setRegisterCallBack(new User.OnRegisterCallBack() {
                        @Override
                        public void onRegisterSuccess(SignupService.Message message) {
                            navController.popBackStack();
                        }

                        @Override
                        public void onRegisterFail(int error) {
                            Toast.makeText(getContext(), "Sign Up faild, code: "+ error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    newUser.register();
                } else {
                    Log.e("newuser", "new user is null");
                }
            }
        });
    }

    /**
     * TODO: get user info from UI
     */
    private User getUserFromUI() throws Exception {
        // TODO: get user infor from UI here
        String Gender;
        if(rdBtnMale.isChecked())
            Gender = "male";
        else {
            Gender = "female";
        }

        User user = new User();
        user.setName(txtName.getText().toString());
        user.setGender(Gender);
        user.setMail(txtEmail.getText().toString());
        user.setAge(Integer.parseInt(txtAge.getText().toString()));
        user.setPassword(txtPass.getText().toString());

        return user;
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
