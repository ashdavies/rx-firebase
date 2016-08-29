### Android RxFirebase###
![Build Status](https://img.shields.io/travis/ashdavies/android-rxfirebase.svg)
![Coverage](https://img.shields.io/codecov/c/github/ashdavies/android-rxfirebase.svg)
![Version](https://img.shields.io/badge/version-1.1.0-yellowgreen.svg)
![License](https://img.shields.io/badge/license-apache%202.0-blue.svg)

**RxJava wrapper for use with the Android Firebase client**
**In Progress: Version 2.0- Include modules for updated Firebase SDK clients**

####Usage####
**Version 1.0**
```android
    compile 'io.reactivex:rxjava:1.1.8'
    compile 'io.ashdavies.rxfirebase:1.0.0'
    
    compile 'com.firebase:firebase-client-android:2.5.2'
```

**RxFirebaseAuth**
```android
    compile 'io.reactivex:rxjava:1.1.8'
    compile 'io.ashdavies.rxfirebase-auth:1.1.0'
    
    compile 'com.google.firebase:firebase-core:9.4.0'
    compile 'com.google.firebase:firebase-auth:9.4.0'
```

####Description####
**Version 1.0**
`RxFirebase` provides a wrapper interface around a `Firebase` reference,
which can be created on a single object basis, or using `RxFirebaseWrapper`
which stores a static reference.

It uses `LATEST` as the default backpressure mode, but this can be provided
when instantiation the `RxFirebase` object.

####Usage####
**Version 1.0**
```android
    RxFirebaseWrapper.setUrl("https://samples-19ac5.firebaseio.com/rxfirebase/");
    
    Subscription subscription = RxFirebase.getInstance()
        .observeValueEvent("books")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<DataSnapshot>() {
            @Override public void call(DataSnapshot snapshot) {
                for (DataSnapshot book : snapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                }
            }
        });
    
    // Unsubscribing removes the path event listener
    subscription.unsubscribe();
```

**RxFirebaseAuth**
```android
    // onCreate
    Subscription subscription = RxFirebaseAuth.getInstance().getUser()
        .subscribe(/* an Observer */);
    
    // onDestroy
    subscription.unsubscribe();
```

####ToDo####
- Update Firebase SDK
- Extract exception types
- Sample application
- Test coverage

####License####
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
