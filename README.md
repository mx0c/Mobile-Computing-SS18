# Mobile-Computing-SS18

## LocationService Usage:
```Java
LocationsService ls = new LocationService(context, timebetweenupdate, distancebetweenupdate);
double lon, lat;
lon = ls.getLongitude();
lat = ls.getLatitude();
```


## NodeJs Backend
Back - End used for the authentication / login process

### Prerequisites
* MongoDb Server [Download](https://www.mongodb.com/download-center#community)
* NodeJs [Download](https://nodejs.org/en/)
* Robo3T [Download](https://robomongo.org/download)


### MongoDb Setup
MongoDb needs a directory to store data. By default the directory path is set to \data\db.

Switch to your C: directory and run the following command in your Command Prompt:

```
md \data\db
```

### Database Setup

Using Robo3T create a new MongoDB database named node-login.
Then right click on the created database and select Open Shell.

Type the following code in the shell,
```javascript
db.getCollection('users').createIndex( { "email": 1 }, { unique: true } )
```

The above code makes the email field as unique, similar to primary key in Relational database such as MySQL.