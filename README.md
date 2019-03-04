# Android RxFirebase

[![](https://img.shields.io/circleci/project/github/ashdavies/rx-firebase.svg)](https://circleci.com/gh/ashdavies/rx-firebase)
[![](https://img.shields.io/codacy/coverage/03ae86d9ce934421879bc407aa157732.svg)](https://app.codacy.com/project/ash.davies/rx-firebase/dashboard)
[![](https://img.shields.io/maven-central/v/io.ashdavies.rx.rxtasks/rx-firebase.svg)](https://search.maven.org/artifact/io.ashdavies.rx.rxfirebase/rx-firebase)
![](https://img.shields.io/github/license/ashdavies/rx-firebase.svg)

[![](https://img.shields.io/codacy/grade/03ae86d9ce934421879bc407aa157732.svg)](https://app.codacy.com/project/ash.davies/rx-firebase/dashboard)
[![](https://img.shields.io/github/last-commit/ashdavies/rx-firebase.svg)](https://github.com/ashdavies/rx-firebase/commits/master)
[![](https://img.shields.io/github/issues-pr/ashdavies/rx-firebase.svg)](https://github.com/ashdavies/rx-firebase/pulls)

**Simple and lightweight RxJava2 wrapper for use with the Android Firebase client**

## The Tasks API
> Starting with Google Play services version 9.0.0, you can use a `Task` API and a number of methods
that return `Task` or its subclasses. `Task` is an API that represents asynchronous method calls, similar to `PendingResult` in previous versions of Google Play Services.

## Usage
Many of the operations can be referenced in further detail in the
[official documentation](https://firebase.google.com/docs/).

Much of this library is built around the latest changes from the RxTasks library since many of the
core functions return a `Task<T>` result which can easily be converted to an RxJava2 type.

As such, much of the core behaviour using in previous versions of this library have been deprecated.
With behaviour remaining to consume child events, and convert value events into RxJava2 types.

> A common method that returns a `Task` is `FirebaseAuth.signInAnonymously()`. It returns a `Task<AuthResult>`
 which means the task will return an `AuthResult` object when it succeeds.

For example the Firebase sign in API asynchronously returns an `AuthResult` which can be consumed via
`toSingle` method as an extension of `Task<T>`.

If consuming from Java code, the class `RxFirebaseAuth` can be used with JVM static behaviour to
honour previous API contracts, however these are marked as deprecated. Extension functions of provided
types should be preferred.

```kotlin
FirebaseAuth
  .getInstance()
  .onAuthState()
  .subscribe { /* ... */ }

FirebaseDatabase
  .getInstance()
  .getReference("server/saving-data/fireblog/posts")
  .onChildAdded<String>()
  .subscribe { /* ... */ }
```

## Installation
```gradle
dependencies {
  compile 'io.ashdavies.rx:rx-firebase:+'
}
```

## Description
A lightweight RxJava2 wrapper for the Android Firebase client SDK, the user is expected
to own the lifecycle of an asynchronous request via RxJava2 `Disposable` handling, however elements
in this library will properly unregister listeners when a `Publisher` is cancelled, except in the
case of value setting where it is only possible to register a listener when making the request.
In this case the emitter is checked for it's subscription state.

Whilst the `FirebaseDatabase` api is mirrored with `RxFirebaseDatabase` it only really uses the
database reference, this is so that the reference hierarchy can easily be traversed through child
and parent elements. Methods requiring `FirebaseDatabase` obtain this from the `DatabaseReference`
and allow you to chain further requests by returning itself.

## Future development
Further development for this library has not been planned, and will soon become deprecated,
it is recommended to use Kotlin [Coroutines integration](https://github.com/Kotlin/kotlinx.coroutines/tree/master/integration/kotlinx-coroutines-play-services) with Google Play Services [Tasks API](https://developers.google.com/android/guides/tasks).
