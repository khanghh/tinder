package com.example.tinder.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.internet_connection.AvatarLoading;
import com.example.internet_connection.OnImageLoadDoneListener;
import com.example.model.User;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;
import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import it.xabaras.android.viewpagerindicator.widget.ViewPagerIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView imvToUserInfo;
    private ImageButton btnToSetting;
    private ImageButton btnToEditInfo;
    private TextView txtNameAge;
    private TextView txtWorkplace;

    private ViewPager pgIntroduceSlider;
    private ViewPagerIndicator introducePagerIndicator;
    private User currUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addControls(view);
        addEvents(view);
    }

    private void addEvents(View view) {
        btnToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_homeFragment_to_settingFragment);
                } catch (Exception e) {

                }
            }
        });
        imvToUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_homeFragment_to_userInforFragment);
                } catch (Exception e) {

                }
            }
        });
        btnToEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_homeFragment_to_editInforFragment);
                } catch (Exception e) {

                }
            }
        });
    }

    private void addControls(View view) {
        currUser = UserAuth.getInstance().getUser();
        if (currUser == null) {
            currUser = new User();
        }
        imvToUserInfo = view.findViewById(R.id.imvToUserInfo);
        btnToSetting = view.findViewById(R.id.btnToSetting);
        btnToEditInfo = view.findViewById(R.id.btnToEditInfo);
        txtNameAge = view.findViewById(R.id.txtNameAge);
        txtWorkplace = view.findViewById(R.id.txtWorkplace);

        new AvatarLoading(currUser.getId(), null, new OnImageLoadDoneListener() {
            @Override
            public void onImageLoadDone(Drawable image, int i) {
                if (image != null) {
                    imvToUserInfo.setImageDrawable(image);
                }
            }
        }).execute();

        pgIntroduceSlider = view.findViewById(R.id.pgIntroduceSlider);
        pgIntroduceSlider.setAdapter(new IntroducePagerAdapter(this.getContext()));

        introducePagerIndicator = view.findViewById(R.id.introducePagerIndicator);
        introducePagerIndicator.initWithViewPager(pgIntroduceSlider);

        updateUI();
    }

    private void updateUI() {
        String nameAge = currUser.getName() + ", " + currUser.getAge();
        txtNameAge.setText(nameAge);
        txtWorkplace.setText(currUser.getWorkplace());
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
