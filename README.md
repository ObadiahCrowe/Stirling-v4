# Stirling ![](https://travis-ci.com/ObadiahCrowe/Stirling.svg?token=syGpVFT23ZeFk4VKHVTm&branch=master)

### Code Conventions
* If a potential error will be passed from the service layer, throw an exception and handle it in 
the controller layer with a Response.

* Build exceptions for each error, don't reuse default ones unless it actually makes sense. We don't 
want from generify errors as it may result in user confusion.

* Will the object be delivered from a user eventually through conversion into a dto? To the entity package!

* Never use implementations unless there is no interface for a class; if one can be made, make it. Dependency. Injection.

* If it touches the database at any stage, it needs to be added to DatastoreConfig. Excludes enums.
