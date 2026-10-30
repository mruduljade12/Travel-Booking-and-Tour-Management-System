package com.mrudul.tourandtravel.user.user_models;

/**
 * Model for a Tour Package.
 * Used by:
 *   - TourPackageAdapter (HomeFragment — Packages section)
 *   - PopularPackageAdapter (SearchFragment — Popular Packages section)
 *
 * Constructor 1 (existing — HomeFragment uses this):
 *   image, title, subtitle, duration, location, groupSize, price, rating, category
 *
 * Constructor 2 (SearchFragment — includes imageUrl and reviewCount):
 *   image, imageUrl, title, subtitle, duration, location, groupSize, price, rating, reviewCount, category
 */
public class TourPackageModel {

    private final int    image;          // drawable resource id (fallback)
    private final String imageUrl;       // Glide URL (null if using drawable)
    private final String title;
    private final String subtitle;
    private final String duration;
    private final String location;
    private final String groupSize;
    private final String price;
    private final float  rating;
    private final String reviewCount;
    private final String category;

    /** Constructor used by HomeFragment (no imageUrl/reviewCount) */
    public TourPackageModel(int image, String title, String subtitle,
                             String duration, String location, String groupSize,
                             String price, float rating, String category) {
        this(image, null, title, subtitle, duration, location,
             groupSize, price, rating, "", category);
    }

    /** Full constructor used by SearchFragment PopularPackageAdapter */
    public TourPackageModel(int image, String imageUrl, String title, String subtitle,
                             String duration, String location, String groupSize,
                             String price, float rating, String reviewCount, String category) {
        this.image       = image;
        this.imageUrl    = imageUrl;
        this.title       = title;
        this.subtitle    = subtitle;
        this.duration    = duration;
        this.location    = location;
        this.groupSize   = groupSize;
        this.price       = price;
        this.rating      = rating;
        this.reviewCount = reviewCount;
        this.category    = category;
    }

    public int    getImage()       { return image; }
    public String getImageUrl()    { return imageUrl; }
    public String getTitle()       { return title; }
    public String getSubtitle()    { return subtitle; }
    public String getDuration()    { return duration; }
    public String getLocation()    { return location; }
    public String getGroupSize()   { return groupSize; }
    public String getPrice()       { return price; }
    public float  getRating()      { return rating; }
    public String getReviewCount() { return reviewCount; }
    public String getCategory()    { return category; }
}