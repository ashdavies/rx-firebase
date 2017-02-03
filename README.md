### Android RxFirebase
![Build Status](https://img.shields.io/travis/ashdavies/android-rx-firebase.svg)
![Coverage](https://img.shields.io/codecov/c/github/ashdavies/android-rx-firebase.svg)
![Version](https://img.shields.io/badge/version-1.2.0-yellowgreen.svg)
![License](https://img.shields.io/badge/license-apache%202.0-blue.svg)

**RxJava wrapper for use with the Android Firebase client**

#### Installation
```gradle
dependencies {
  compile "io.ashdavies.rxfirebase:{latest-version}"
}
```

#### Description
A lightweight RxJava2 wrapper for the Android Firebase client SDK, the user is expected
to own the lifecycle of an asynchronous request via RxJava2 `Disposable` handling, however elements
in this library will properly unregister listeners when a `Publisher` is cancelled, except in the
case of value setting where it is only possible to register a listener when making the request.
In this case the emitter is checked for it's subscription state.

Whilst the `FirebaseDatabase` api is mirrored with `RxFirebaseDatabase` it only really uses the
database reference, this is so that the reference hierarchy can easily be traversed through child
and parent elements. Methods requiring `FirebaseDatabase` obtain this from the `DatabaseReference`
and allow you to chain further requests by returning itself.

This library depends on `RxTasks` and `RxJava2` to provide appropriate api responses.
Therefore asynchronous responses will return, `Single`, `Completable` and `Flowable` respectively.

#### Usage
Many of the operations can be referenced in further detail in the [official documentation](https://firebase.google.com/docs/).

##### RxFirebaseAuth
Retrieve an instance with `RxFirebaseAuth.getInstance()` which will use `FirebaseAuth.getInstance()`,
you can also provide an alternative `FirebaseAuth` with this `RxFirebaseAuth.getInstance(firebaseAuth)`.

#### RxFirebaseDatabase
Retrieve an instance with `RxFirebaseDatabase.getInstance()` which will use `FirebaseDatabase.getInstance()`,
you can also provide an alternative `FirebaseDatabase` with this `RxFirebaseDatabase.getInstance(firebaseDatabase)`.
