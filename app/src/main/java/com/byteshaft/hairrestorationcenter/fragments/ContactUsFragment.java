package com.byteshaft.hairrestorationcenter.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.hairrestorationcenter.MainActivity;
import com.byteshaft.hairrestorationcenter.R;
import com.byteshaft.hairrestorationcenter.utils.WebServiceHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mSubjectField;
    private EditText mDescriptionField;
    private Button mSubmitButton;

    private String mName;
    private String mEmail;
    private String mSubject;
    private String mDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.contactus_fragment, container, false);
        mNameField = (EditText) mBaseView.findViewById(R.id.name);
        mEmailField = (EditText) mBaseView.findViewById(R.id.email);
        mSubjectField = (EditText) mBaseView.findViewById(R.id.subject);
        mDescriptionField = (EditText) mBaseView.findViewById(R.id.description);
        mSubmitButton = (Button) mBaseView.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_button:
                if (validateEditText()) {
                    new ContactUsTask().execute();
                }
        }
    }

    private boolean validateEditText() {

        boolean valid = true;
        mName = mNameField.getText().toString();
        mEmail = mEmailField.getText().toString();
        mSubject = mSubjectField.getText().toString();
        mDescription = mDescriptionField.getText().toString();

        if (mName.trim().isEmpty() || mName.length() < 3) {
            mNameField.setError("enter at least 3 characters");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailField.setError("please provide a valid email");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if (mSubject.trim().isEmpty()) {
            mSubjectField.setError("please provide subject");
            valid = false;
        } else {
            mSubjectField.setError(null);
        }

        if (mDescription.trim().isEmpty()) {
            mDescriptionField.setError("please provide description");
            valid = false;
        } else {
            mDescriptionField.setError(null);
        }
        return valid;
    }

    private class ContactUsTask extends AsyncTask<String, String, String> {

        private JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelpers.showProgressDialog(getActivity() , "Sending email \n please wait");
        }

        @Override
        protected String doInBackground(String... strings) {
            if (WebServiceHelpers.isNetworkAvailable() && WebServiceHelpers.isInternetWorking()){
                try {
                    jsonObject = WebServiceHelpers.contactUs(
                            mName,
                            mEmail,
                            mSubject,
                            mDescription);
                    Log.e("TAG", String.valueOf(jsonObject));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WebServiceHelpers.dismissProgressDialog();
            Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
