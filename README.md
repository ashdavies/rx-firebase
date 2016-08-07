### Android RxFirebase###
![Build Status](https://img.shields.io/travis/ashdavies/android-rxfirebase.svg)
![Coverage](https://img.shields.io/codecov/c/github/ashdavies/android-rxfirebase.svg)
![Version](https://img.shields.io/badge/version-1.0.0-yellowgreen.svg)
![License](https://img.shields.io/badge/license-apache%202.0-blue.svg)

**RxJava wrapper for use with the Android Firebase client**

####Usage####
```android
    compile "io.reactivex:rxjava:+"
    compile "io.ashdavies.rxfirebase:+"
```

####Description####
`RxFirebase` provides a wrapper interface around a `Firebase` reference,
which can be created on a single object basis, or using `RxFirebaseWrapper`
which stores a static reference.

It uses `LATEST` as the default backpressure mode, but this can be provided
when instantiation the `RxFirebase` object.

####Usage####
**Single Object Usage**
```android
    Firebase reference = new Firebase("https://samples-19ac5.firebaseio.com/rxfirebase/");
    
    Subscription subscription = RxFirebase.with(reference)
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

**Singleton Usage**
```android
    RxFirebaseWrapper.setUrl("https://samples-19ac5.firebaseio.com/rxfirebase/");
    
    RxFirebaseWrapper.getInstance()
            .observeValueEvent("books")
            .subscribe(/* an Observer */);
```

####ToDo####
- Improve RxFirebase api
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
