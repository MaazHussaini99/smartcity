package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class NightLife {
    private String name;
    private String description;
    private String imageUrl;
    private String moreInfoUrl;

    public NightLife(String name, String description, String imageUrl, String moreInfoUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.moreInfoUrl = moreInfoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }
    public static List<NightLife> getNightlifeInfo() {
        List<NightLife> nightlifeList = new ArrayList<>();

        // Add static nightlife information with image URLs and more info URLs
        nightlifeList.add(new NightLife("McGeary's Irish Pub",
                "4 Clinton Square\n"+
                        "Albany, NY 12207\n\n McGeary’s kitchen serves up all of your favorites, from burgers and sandwiches to soups and salads. Try our delicious sliders; Shepard’s pie; fish and chips; our famous wings and so much more.",
                "https://lh3.googleusercontent.com/p/AF1QipNG7U_r1QyDGx5tAF9Sod3zJsRdf_nSA0L29E9s=s1360-w1360-h1020", "https://http://www.mcgearyspub.com/"));

        nightlifeList.add(new NightLife("Excelsior Pub",
                "54 Phillip St.\n" +
                        "Albany, NY 12207\n\nAlbany NY's only New York themed pub! Featuring a wide variety of exclusively New York State craft beers, spirits and wines. The Excelsior Pub is proud to present a menu with delicacies native to our great state, like Buffalo Wings, Beef of Weck, Spiedies and more!",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/Wren_20170915_2728_1A52688F-709A-499A-BEECB70193E5A3C9_9fdfe48b-e41f-4776-a0e45b6833c3ffc2.jpg", "https://excelsior.pub/"));

        nightlifeList.add(new NightLife("The Hollow Bar + Kitchen",
                "79 North Pearl St.\n" +
                        "Albany, NY 12207\n\nEstablished in 2013, The Hollow Bar + Kitchen is a lauded downtown Albany restaurant and bar, live music and private event venue. Coined as a 'triple threat' by The Alt Weekly, The Hollow is continuously recognized for excellence in food, bar and music.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/polenta-2-72eab1585056a36_72eab2b5-5056-a36a-0b94c164fa976562.jpg", "http://www.thehollowalbany.com/"));

        nightlifeList.add(new NightLife("Lost & Found Bar & Kitchen",
                "942 Broadway\n" +
                        "Albany, NY 12207\n\nNew to the Albany, New York downtown Warehouse District. Bar and kitchen with outdoor space and a comfortable, relaxed atmosphere.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/Beer-burger-and-fried-ed06eb355056a36_ed06ebfd-5056-a36a-0b9e1d97019830b7.jpg", "https://www.lostandfoundalbany.com/"));
        nightlifeList.add(new NightLife("Nine Pin Ciderworks, LLC",
                "929 Broadway\n" +
                        "Albany, NY 12207\n\n\nNine Pin Ciderworks crafts delicious hard cider using 100% locally sourced New York apples and fruit. Our tasting room, located in downtown Albany’s exciting Warehouse District, offers 18 taps of unique New York ciders and beers as well as delicious cider cocktails. Pair a cider with one of our gourmet sourdough pizzas or other house-made small plates.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_limit,q_75,w_1200/v1/crm/albany/platterpic20-ac428d685056a36_ac428eb1-5056-a36a-0b2ab32fbf9c6f59.jpg", "https://www.ninepincider.com/"));
        nightlifeList.add(new NightLife("Albany Distilling Co. Bar & Bottle Shop",
                "75 Livingston Ave.\n" +
                        "Albany, NY 12207\n\nThe Bar & Bottle shop offers full bar including cocktails with Albany Distilling products and other local spirits, as well as local craft beer, wine and cider. Enjoy your drinks on the outdoor patio! Bottle shop has Albany Distilling products and merchandise for sale.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/IMG_37590-4612ff445056a36_46130593-5056-a36a-0bc86f5f4d825fac.jpg", "https://www.albanydistilling.com/"));
        nightlifeList.add(new NightLife("City Beer Hall",
                "42 Howard St.\n" +
                        "Albany, NY 12207\n\nAmerican beer hall & gastropub housed within downtown Albany's old telephone company. Inventive menus focus on locally sourced, seasonal, from-scratch cuisine. Indoor/outdoor seating for lunch, dinner, brunch, & late night.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/City-Beer-Hall---Main-Hall-5-a00f2c1a5056a36_a00f2ef0-5056-a36a-0b1c890b05226b37.jpg", "http://thecitybeerhall.com/reservations/"));
        nightlifeList.add(new NightLife("Fort Orange Brewing",
                "450 N. Pearl St.\n" +
                        "Albany, NY 12204\n\nOur brewery focuses on creating fresh, flavorful beers that showcase the amazing styles being brewed across our country. Our beers are available in the taproom at 450 North Pearl St. in flights, pints, cans and growlers to go. Cheers!",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/DSC_0052-400-350ffc375056a36_350ffdcb-5056-a36a-0b1c92acff8e3d9c.jpg", "https://www.fortorangebrewing.com/"));
        nightlifeList.add(new NightLife("Philly Bar & Lounge",
                "Latham Inn, 622 Watervliet-Shaker Road\n" +
                        "Latham, NY 12110\n\nGreat Food, Great Drinks, Great Times. Catch the Game on one of our flatscreens and don´t forget to try our famous wings.. Live entertainment, got you covered, if you´re hungry for great food and great times there´s on one Philly Bar & Grill",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_448,q_50,w_558/v1/crm/albany/philly_cheese_steak-image1_3076e0ec-5056-a36a-0ba7cbf4ae892ef4.jpg", "https://phillybarandlounge.com/"));

        return nightlifeList;
    }

}
