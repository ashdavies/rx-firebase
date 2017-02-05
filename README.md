### Android RxFirebase
![Build Status](https://img.shields.io/travis/ashdavies/rx-firebase.svg)
![Coverage](https://img.shields.io/codecov/c/github/ashdavies/rx-firebase.svg)
![Version](https://img.shields.io/badge/version-1.2.2-yellowgreen.svg)
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
Many of the operations can be referenced in further detail in the
[official documentation](https://firebase.google.com/docs/).

An instance of either `RxFirebaseAuth` or `RxFirebaseDatabase` can be retrieved using their 
respective `getInstance` method, which will then use the default Firebase element. Alternatively 
this can also be provided as a parameter should you wish to use a custom one.

##### RxFirebaseAuth
RxFirebaseAuth has a fairly mirrored api to that of `FirebaseAuth` and can in most cases be used
as a drop in replacement. Methods which have no return value will return the instance so that
methods can be chained.

Calls to `getCurrentUser()` will return a Maybe emitting the user only if the user is logged in
otherwise it will just call `onComplete` immediately.


##### RxFirebaseDatabase
RxFirebaseDatabase has a simple interface which also mimics that of its Firebase counterpart, at
this time it is not worthwhile to facilitate all the features available via the Query interface
therefore it is possible to use a `Query` object with `RxFirebaseDatabase.with(query)`.

The main responsibility of `RxFirebaseDatabase` is to be able to consume child and value events
using reactive extensions, such as retrieving values, setting and removing values.

One of the main observations here is the consumption of child events, this is achieved through
the use of the `ChildEvent` object which has reference to the event type, and data snapshot which
can then be used to resolve the data value.

