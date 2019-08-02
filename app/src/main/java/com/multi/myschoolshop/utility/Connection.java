package com.multi.myschoolshop.utility;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.multi.myschoolshop.BuildConfig;

public class Connection {
    DatabaseReference reference;
    String path;

    public Connection() {
        path="release";
        if(BuildConfig.DEBUG){
            path="debug";
        }
        reference = FirebaseDatabase.getInstance().getReference(path);
    }

    public DatabaseReference getDbUser() {
        return reference.child("user");
    }

    public DatabaseReference getDbSchool() {
        return reference.child("school");
    }

    public DatabaseReference getFaqs() {
        return reference.child("faqs");
    }
    public DatabaseReference getVersionControl() {
        return reference.child("version");
    }
}
