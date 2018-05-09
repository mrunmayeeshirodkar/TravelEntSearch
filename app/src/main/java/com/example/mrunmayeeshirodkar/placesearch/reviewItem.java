package com.example.mrunmayeeshirodkar.placesearch;

public class reviewItem {
    private String author_Profile;
    private String author_Name;
    private String review_Stars;
    private String review_Date;
    private String review_Text;
    private String author_url;

    public reviewItem(String author_Profile, String author_Name, String review_Stars, String review_Date, String review_Text, String author_url) {
        this.author_Profile = author_Profile;
        this.author_Name = author_Name;
        this.review_Stars = review_Stars;
        this.review_Date = review_Date;
        this.review_Text = review_Text;
        this.author_url = author_url;
    }

    public String getAuthor_Profile() {
        return author_Profile;
    }

    public String getAuthor_Name() {
        return author_Name;
    }

    public String getReview_Stars() {
        return review_Stars;
    }

    public String getReview_Date() {
        return review_Date;
    }

    public String getReview_Text() {
        return review_Text;
    }

    public String getAuthor_Url() {
        return author_url;
    }
}
