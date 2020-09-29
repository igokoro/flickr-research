# Flickr Research
A quick weekend exploration of Flickr API

## Compile and run
Put file `keys.properties` that includes Flickr API key and build and run as any other android app.
```
FLICKR_API_KEY=<your-key-here>
```
Release build was not tested, so no guarantees proguard is not messing things up.

## Things that work(ed out)
1. Using kotlin `1.4.10`.
1. Reading from Flickr API 
1. Pulling recent photos + hot tags
1. Pulling photos from paged search API (rather buggy and returns a bunch of duplicates)
1. Flickr API in JSON is ugly, so using DTOs + domain models really helped to tidy things up a bit
1. `RxJava3` as primary tool for async operations + 
1. Automatic disposal of Rx streams based on ViewModel or Fragment lifecycle
1. `ViewBinding` as lightweight replacement for DataBinding (was just curious)
1. `Navigation` library for navigation. 
1. `Paging` v3 to deal with paged search API - it's in alpha, so a little raw at this time but still very useful. Paging API does not allow to convert domain models to UI models which is too bad.
1. `buildSrc` to store dependency versions 
1. `Hilt` for dependency injection. Really impressed here. A setup that would usually take days (given you even know what you're doing) is simplified to minutes. There are rough edges in many places but it looks very promising

## App Architecture
* Single-activity application.
* Dependency injection. Things are broken down into small chunks to allow for a really simple unit testing.
* Clean-code layering is "attempted" - kotlin (and `hilt` too) makes it a little harder to hide private implementations because "public class/constructor/method exposes internal/private class". <br/> One way to work-around the internal -> public visibility exposure (that is prohibited with kotlin) is to use method or property injection - a little less efficient but improves isolation. 
* Screens are built using MVVM. Unidirectional data flow is applied to each screen.
* Model is represented by FlickrRepo (+ PhotosRxPagingSource for paging).
* In case of Paging, they really take over the API that can exposed, even there, real model is repository.
* Factories are used all over the place to farther break down dependencies (really wish `AssistedInject` was build into `Hilt` or `Dagger`).
* Navigation is implemented as observable action stream that `ViewModel` writes to and `View` reads from and executes by feeding to `navController`. Too bad `navController` does not work at ViewModel level directly (as some in `compose` attempt to do).
* Burden of disposing rx streams is simplified via auto-disposal. <br/> Streams can be associated with either ViewModel (and get disposed upon `onCleared()`) or `Lifecycle` (and get disposed upon `ON_DESTROY` event). <br/> Tying rx to ViewModel lifecycle, for example, allows to keep streams alive across configuration changes.
