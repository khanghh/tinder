package com.example.tinder.editinfor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.internet_connection.ImagesLoading;
import com.example.internet_connection.OnImageLoadDoneListener;
import com.example.model.User;
import com.example.rest.RetrofitClient;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditInforFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditInforFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInforFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<ImageView> arrayImg;
    private List<Button> arrayButton;
    private List<AVLoadingIndicatorView> imageLoadings;
    private Toolbar toolbar;

    private User user;
    private EditText txtPhone, txtDescription, txtAddress,txtWorkplace;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;

    private Button btnUpdateInfo;

    private OnFragmentInteractionListener mListener;

    public EditInforFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditInforFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditInforFragment newInstance(String param1, String param2) {
        EditInforFragment fragment = new EditInforFragment();
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
        return inflater.inflate(R.layout.fragment_edit_infor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addControls(view);
        addEvent(view);
    }

    private void addEvent(final View view) {
        arrayButton.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(0);
            }
        });
        arrayButton.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(1);
            }
        });
        arrayButton.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(2);
            }
        });
        arrayButton.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(3);
            }
        });
        arrayButton.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(4);
            }
        });
        arrayButton.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickHandle(5);
            }
        });

        //click Up back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = txtAddress.getText().toString();
                String workplace = txtWorkplace.getText().toString();
                String description = txtDescription.getText().toString();
                String phone = txtPhone.getText().toString();
                String gender = rdbMale.isChecked() ? "male" : "female";

                user.updateUserInfo(user.getName(), gender, user.getAge(), phone, description, address, workplace);
            }
        });
    }

    private void updateInfo () {
        txtAddress.setText(user.getCity());
        txtWorkplace.setText(user.getWorkplace());
        txtPhone.setText(user.getPhone());
        txtDescription.setText(user.getDecription());
        rdbMale.setChecked(user.getGender().equals("male"));
        rdbFemale.setChecked(user.getGender().equals("female"));
    }

    private void buttonClickHandle(int index) {
        if (arrayImg.get(index).getDrawable() == null) {
            Log.d("add", "add image " + index);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, index);
        } else {
            Log.d("delete", "delete image " + index);
            deleteImage(index);
        }
    }

    private void addControls(View view) {
        user = UserAuth.getInstance().getUser();
        if (user == null) {
            user = new User();
        }
        toolbar = view.findViewById(R.id.toolbar3);

        imageLoadings = new ArrayList<>();
        arrayImg = new ArrayList<>();
        arrayButton = new ArrayList<>();

        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading1));
        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading2));
        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading3));
        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading4));
        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading5));
        imageLoadings.add((AVLoadingIndicatorView) view.findViewById(R.id.imageLoading6));
        for (int i = 0; i < 6; i++) {
            imageLoadings.get(i).show();
        }

        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar1));
        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar2));
        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar3));
        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar4));
        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar5));
        arrayImg.add((ImageView) view.findViewById(R.id.imgAvatar6));

        arrayButton.add((Button) view.findViewById(R.id.btnAvatar1));
        arrayButton.add((Button) view.findViewById(R.id.btnAvatar2));
        arrayButton.add((Button) view.findViewById(R.id.btnAvatar3));
        arrayButton.add((Button) view.findViewById(R.id.btnAvatar4));
        arrayButton.add((Button) view.findViewById(R.id.btnAvatar5));
        arrayButton.add((Button) view.findViewById(R.id.btnAvatar6));

        txtAddress = view.findViewById(R.id.txtAddress);
        txtWorkplace = view.findViewById(R.id.txtWorkplace);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtPhone = view.findViewById(R.id.txtPhone);
        rdbMale = view.findViewById(R.id.radMale);
        rdbFemale = view.findViewById(R.id.radFemale);
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        updateUI();
    }

    private void updateUI() {
        txtAddress.setText(user.getCity());
        txtWorkplace.setText(user.getWorkplace());
        txtPhone.setText(user.getPhone());
        txtDescription.setText(user.getDecription());
        rdbMale.setChecked(user.getGender().equals("male"));
        rdbFemale.setChecked(user.getGender().equals("female"));

        new ImagesLoading(user.getId(), new OnImageLoadDoneListener() {
            @Override
            public void onImageLoadDone(Drawable image, int i) {
                setAvatarImage(image, i - 1);
            }
        }).execute();
    }

    /**
     * Description: set image with index to layout
     *
     * @param image
     * @param index
     */
    private void setAvatarImage(Drawable image, final int index) {
        Log.d("index", "ind = " + index);
        arrayImg.get(index).setImageDrawable(image);

        // set image for button
        if (image != null) {
            arrayButton.get(index).setBackground(getResources().getDrawable(R.drawable.delete_image));
        } else {
            arrayButton.get(index).setBackground(getResources().getDrawable(R.drawable.add_image));
        }
        arrayButton.get(index).setVisibility(View.VISIBLE);

        // stop loading animation here
        imageLoadings.get(index).hide();
    }

    /**
     * Description: set image to ImageView with exist path.
     * @param path: path of local image.
     * @param index: index of imageview.
     */
    private void setImageFromPath(String path, int index) {
        if (path != null) {
            arrayImg.get(index).setImageURI(Uri.parse(path));
            arrayButton.get(index).setBackgroundResource(R.drawable.delete_image);

            // stop loading animation here
            imageLoadings.get(index).hide();
        }
    }

    private void deleteAvatarImage(int index) {
        arrayImg.get(index).setImageDrawable(null);
        arrayButton.get(index).setBackgroundResource(R.drawable.add_image);

        // stop loading animation here
        imageLoadings.get(index).hide();
    }

    private void saveImageToSever(final String picturePath, final int index) {
        //start loading animation here
        imageLoadings.get(index).setVisibility(View.VISIBLE);
        imageLoadings.get(index).show();

        File file = new File(picturePath);
        RequestBody num = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(index + 1));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RetrofitClient.getPostImageService().upImage("Barer " + user.getAuthen_token(), num, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Up Success", Toast.LENGTH_LONG).show();
                    setImageFromPath(picturePath, index);
                } else {
                    Toast.makeText(getContext(), "Up Fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Internet error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteImage(final int index) {
        //start loading animation here
        imageLoadings.get(index).setVisibility(View.VISIBLE);
        imageLoadings.get(index).show();

        RetrofitClient.getDeleteImageService().deleteImage("Barer " + user.getAuthen_token(), index + 1).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_LONG).show();
                    deleteAvatarImage(index);
                } else
                    Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Internet error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Arrays.asList(0, 1, 2, 3, 4, 5).contains(requestCode) && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            saveImageToSever(picturePath, requestCode);
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
