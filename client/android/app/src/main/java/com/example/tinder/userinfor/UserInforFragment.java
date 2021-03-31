package com.example.tinder.userinfor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.internet_connection.ImagesLoading;
import com.example.internet_connection.OnImageLoadDoneListener;
import com.example.internet_connection.SingleImageLoading;
import com.example.model.User;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;
import com.example.tinder.search_friend.SearchFriendFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInforFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserInforFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInforFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FloatingActionButton fab;
    ViewPager viewPager;
    ImageViewAdapter imageViewAdapter;

    private TextView txtNameAge;
    private User userData;
    private TextView txtDescription,txtAddress,txtWorkPlace,txtLocation;

    private List<View> imageList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserInforFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInforFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInforFragment newInstance(String param1, String param2) {
        UserInforFragment fragment = new UserInforFragment();
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
        return inflater.inflate(R.layout.fragment_user_infor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addControls(view);
        addEvents(view);
    }

    private void addControls(View view) {
        if (getArguments() != null) {
            userData = User.getUserFromBundle(getArguments());
        } else {
            userData = UserAuth.getInstance().getUser();
        }

        txtNameAge = view.findViewById(R.id.txtNameAge);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtAddress = view.findViewById(R.id.textViewAddress);
        txtWorkPlace = view.findViewById(R.id.textViewWorkPlace);
        txtLocation = view.findViewById(R.id.textViewLocation);

        fab =(FloatingActionButton) view.findViewById(R.id.fab);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        imageList = new ArrayList<>();
        imageViewAdapter = new ImageViewAdapter(getContext(), imageList);
        viewPager.setAdapter(imageViewAdapter);

        // load images
        if (userData != null) {
            new ImagesLoading(userData.getId(), new OnImageLoadDoneListener() {
                @Override
                public void onImageLoadDone(Drawable image, int i) {
                    if (image != null) {
                        RoundedImageView imageView = new RoundedImageView(getContext());
                        imageView.setImageDrawable(image);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setCornerRadius(25);
                        imageView.setOval(false);
                        imageList.add(imageView);
                        imageViewAdapter.notifyDataSetChanged();
                    }
                }
            }).execute();
            updateUI();
        }

    }

    @SuppressLint("RestrictedApi")
    private void updateUI() {
        String nameAge = userData.getName() + ", " + userData.getAge();
        txtNameAge.setText(nameAge);
        txtAddress.setText(userData.getCity());
        txtWorkPlace.setText(userData.getWorkplace());
        txtLocation.setText("Cách chưa tới " + userData.getMax_distance() + " km");
        txtDescription.setText(userData.getDecription());

        if (getArguments() == null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_userInforFragment_to_editInforFragment, null));
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    private void addEvents(View view) {

    }

    private static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeResource(res, id, options);
                Log.d("log", "Decoded successfully for sampleSize" + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                // If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
                Log.e("log", "outOfMemoryError while reading file for sampleSize" + options.inSampleSize
                        + "retrying with higher value");
            }
        }
        return Bitmap.createScaledBitmap(bitmap, 700, 875, true);
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