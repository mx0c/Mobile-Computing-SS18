# Mobile-Computing-SS18

## NodeJs Backend
Back - End used for the authentication / login process

### Prerequisites
* MongoDb Server [Download](https://www.mongodb.com/download-center#community)
* NodeJs [Download](https://nodejs.org/en/)


### MongoDb Setup
MongoDb needs a directory to store data. By default the directory path is set to \data\db.

Switch to your C: directory and run the following command in your Command Prompt:

```
md \data\db
```

### Database Setup
Start the the database by executing the mongod.exe usually located in  \Program Files\MongoDB\Server\3.6\bin\mongod.exe

Open the mongodb Shell usually located in \Program Files\MongoDB\Server\3.6\bin\mongo.exe. 

In the shell, type the following command to create a database. When entered the new database is created and you will be switched automatically.

```javascript
use node-login
```

To create the users collection type the following code.
```javascript
db.getCollection('users').createIndex( { "email": 1 }, { unique: true } )
```

The above code makes the email field as unique, similar to primary key in Relational database such as MySQL.

Visit https://docs.mongodb.com/manual/reference/mongo-shell/ for a quick reference.

### Start Server and Database
Before starting the nodejs server you need to start the database.

#### Start Database
To start the database go to your mongodb bin folder and start the mongod.exe. Leave open in the background.

#### Start Server

Within the node-login folder type the following commands.

```javascript
npm install
node app
```




