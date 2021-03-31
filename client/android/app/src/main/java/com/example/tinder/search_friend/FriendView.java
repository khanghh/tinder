package com.example.tinder.search_friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.internet_connection.AvatarLoading;
import com.example.internet_connection.OnImageLoadDoneListener;
import com.example.model.User;
import com.example.tinder.R;
import com.example.tinder.authentication.UserAuth;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.makeramen.roundedimageview.RoundedImageView;

@SuppressLint("ViewConstructor")
public class FriendView extends ConstraintLayout implements SearchFriendData.OnDataLoadDoneListener {

    private User friend;

    private TextView txtName;
    private TextView txtWorkplace;
    private ImageButton btnDetailInfo;
    private ShimmerFrameLayout shimmerViewContainer;
    private RoundedImageView imgSearchFriendAvatar;

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
        updateUI();
    }

    public RoundedImageView getImgSearchFriendAvatar() {
        return imgSearchFriendAvatar;
    }

    public FriendView(Context context, User friend) {
        super(context);
        this.friend = friend;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_search_friend_item, this, false);
        addView(view);

        addControls();
        addEvents();
    }

    private void addControls() {
        // add controls
        txtName = findViewById(R.id.txtName);
        txtWorkplace = findViewById(R.id.txtWorkplace);
        btnDetailInfo = findViewById(R.id.btnDetailInfo);
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
        imgSearchFriendAvatar = findViewById(R.id.imgSearchFriendAvatar);

        updateUI();
    }

    private void updateUI() {
        if (friend == null) {
            txtName.setText("");
            txtWorkplace.setText("");

            shimmerViewContainer.startShimmerAnimation();
            shimmerViewContainer.setVisibility(View.VISIBLE);
        } else {
            // update view
            new AvatarLoading(friend.getId(), shimmerViewContainer, new OnImageLoadDoneListener() {
                @Override
                public void onImageLoadDone(Drawable image, int i) {
                    if (image != null) {
                        imgSearchFriendAvatar.setImageDrawable(image);
                        String nameAge = friend.getName() + ", " + friend.getAge();
                        txtName.setText(nameAge);
                        txtWorkplace.setText(friend.getWorkplace());
                    }
                }
            }).execute();
        }
    }

    private void addEvents() {
        btnDetailInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friend != null) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_userInforFragment, friend.toBundle());
                }
            }
        });
    }

    public void likeFriend() {
        if (friend != null && UserAuth.getInstance().getUser() != null) {
            if (SearchFriendData.getInstance().removeDataItem(friend.getId())) {
                UserAuth.getInstance().getUser().likeFriend(friend.getId());

                // set data = null, call notifyDataSetChange() to update this page
                friend = null;
                updateUI();
                SearchFriendData.getInstance().notifyDataSetChange();
            }
        }
    }

    /**
     * Description: current user perform dislike this friend.
     */
    public void dislikeFriend() {
        if (friend != null && UserAuth.getInstance().getUser() != null) {
            if (SearchFriendData.getInstance().removeDataItem(friend.getId())) {
                UserAuth.getInstance().getUser().dislikeFriend(friend.getId());

                // set data = null, call notifyDataSetChange() to update this page
                friend = null;
                updateUI();
                SearchFriendData.getInstance().notifyDataSetChange();
            }
        }
    }

    @Override
    public void onLoadDone() {
        if (friend != null) {
            return;
        }
        friend = SearchFriendData.getInstance().getUserData();
        updateUI();
    }
}
