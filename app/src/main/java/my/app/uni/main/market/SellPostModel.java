package my.app.uni.main.market;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SellPostModel {

    public String description, image_url, userID, post_id, title, category, keyword;
    public Object timestamp;
    public Long price;

    public SellPostModel() {
    }



    public SellPostModel(String description, String category, String image_url, String userID, Object timestamp, String post_id, Long price, String title, String keyword) {
        this.description = description;
        this.image_url = image_url;
        this.userID = userID;
        this.price = price;
        this.category = category;
        this.timestamp = timestamp;
        this.title = title;
        this.post_id = post_id;
        this.keyword = keyword;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getUser_id() {
        return userID;
    }

    public void setUser_id(String user_id) {
        this.userID = userID;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getProfileURL() {

        final String[] profileImageURL = new String[1];
        FirebaseFirestore.getInstance().collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("image URL") != null) {
                    profileImageURL[0] = documentSnapshot.get("image URL").toString();

                }
            }
        });

        return profileImageURL[0];
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;

    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}



