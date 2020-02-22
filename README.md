# SpaceXApp
The project uses open-source SpaceX API to produce a list of rockets. Detailed information
of a rocket can be viewed on the details screen. As this is not a live app, following
assumptions are made during its development.

● Mission patch image is not saved offline. Due to time constraints, image caching is
not implemented

● No testing made on real devices running Android API 19

● Filtering active rockets is done using Filterable interface rather than efficient DiffUtil

● The app app uses a simple MVC architecture separating business logic from the view
layer

● Screen transition animations are only available on Android API 21 and above

● Screens are prevented from restarting due to time constraints

