# Mobile-Computing-SS18

## LocationService Usage:
```Java
LocationsService ls = new LocationService(context, timebetweenupdate, distancebetweenupdate);
double lon, lat;
lon = ls.getLongitude();
lat = ls.getLatitude();
```