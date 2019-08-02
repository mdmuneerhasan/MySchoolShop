package com.multi.myschoolshop.utility;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.multi.myschoolshop.BuildConfig;

public class Storage {
    StorageReference reference;
    String path;

    public Storage() {
        path="release";
        if(BuildConfig.DEBUG){
            path="debug";
        }
        reference = FirebaseStorage.getInstance().getReference(path);
    }

    public StorageReference getUserStorage() {
        return reference.child("user");
    }

    public StorageReference getSchoolStorage() {
        return reference.child("school");
    }

    public StorageReference getReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public FirebaseStorage getInsatance() {
        return FirebaseStorage.getInstance();
    }
}
