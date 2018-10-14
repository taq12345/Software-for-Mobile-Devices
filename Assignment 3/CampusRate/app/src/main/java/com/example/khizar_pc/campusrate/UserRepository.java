package com.example.khizar_pc.campusrate;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class UserRepository {

    private UserDAO mUserDao;
    private LiveData<List<User>> mAllWords;

    UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllWords = mUserDao.getAllWords();
    }

    LiveData<List<User>> getAllWords() {
        return mAllWords;
    }


    public void insert (User word) {
        new insertAsyncTask(mUserDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        insertAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}