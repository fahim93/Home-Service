package com.bitproject.fahim.homeservice.firebase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireStorage {
    public static final StorageReference STORAGE_ROOT_REF = FirebaseStorage.getInstance().getReference();
    public static final StorageReference CLIENTS_PROFILE_IMAGES = STORAGE_ROOT_REF.child("clients_profile_images");
}
