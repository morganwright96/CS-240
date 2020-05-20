DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS AuthToken;

CREATE TABLE "User" (
	"UserName"	TEXT NOT NULL UNIQUE,
	"Password"	TEXT NOT NULL,
	"Email"	TEXT NOT NULL,
	"FirstName"	TEXT NOT NULL,
	"LastName"	TEXT NOT NULL,
	"PersonID"	TEXT NOT NULL UNIQUE
);

CREATE TABLE "AuthToken" (
	"Person ID"	TEXT NOT NULL,
	"AuthToken"	TEXT NOT NULL PRIMARY KEY UNIQUE
);

CREATE TABLE "Person" (
	"PersonID"	TEXT NOT NULL PRIMARY KEY UNIQUE,
	"Username"	TEXT NOT NULL,
	"FirstName"	TEXT NOT NULL,
	"LastName"	TEXT NOT NULL,
	"Gender"	TEXT NOT NULL,
	"FatherID"	TEXT,
	"MotherID"	TEXT,
	"SpouseID"	TEXT,
	FOREIGN KEY("Username") REFERENCES User(id),
	FOREIGN KEY("FatherID") REFERENCES Person("PersonID"),
	FOREIGN KEY("MotherID") REFERENCES Person("PersonID"),	
	FOREIGN KEY("SpouseID") REFERENCES Person("PersonID")
);

CREATE TABLE "Event" (
	"EventID"	TEXT NOT NULL PRIMARY KEY UNIQUE,
	"Username"	TEXT NOT NULL,
	"PersonID"	TEXT NOT NULL,
	"Latitude"	NUMERIC NOT NULL,
	"Longitude"	NUMERIC NOT NULL,
	"Country"	TEXT NOT NULL,
	"City"	TEXT NOT NULL,
	"EventType"	TEXT NOT NULL,
	"Year"	INTEGER NOT NULL,
	FOREIGN KEY("Username") REFERENCES User(id)
);