# gps-battery-homework

Homework with threading battery + gps

Parameters to change location or battery frequency are included in MainFragment.kt file

On top of the file there example default values:
const val MAX_LIST_SIZE = 20; 
const val LOCATION_INTERVAL = 12000L; 
const val BATTERY_INTERVAL =
5000L

Url is not as parameter because it wasn't specified what type should be returned by example
endpoint. The solution is pretending api call with delay in suspend function. That's probably max 1h
of work to change to real Retrofit api call, so please let me know if it's necessary to complete
this homework.
