package com.example.mf.quizzy.usersManagement;

class UserResultInCategoryImpl implements UserResultInCategory {
    private int mPoints;
    private String mCategoryName;

    UserResultInCategoryImpl() {
    }

    UserResultInCategoryImpl(int points, String categoryName) {
        mPoints = points;
        mCategoryName = categoryName;
    }

    @Override
    public String getCategoryName() {
        return mCategoryName;
    }

    @Override
    public int getCategoryPoints() {
        return mPoints;
    }

    @Override
    public void setCategoryPoints(int points) {
        mPoints = points;
    }
}
