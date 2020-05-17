DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS AuthToken;

CREATE TABLE "User" (
	"UserName"	TEXT NOT NULL UNIQUE,
	"Password"	TEXT NOT NULL,
	"Email"	TEXT NOT NULL,
	"First Name"	TEXT NOT NULL,
	"Last Name"	TEXT NOT NULL,
	"Person ID"	TEXT NOT NULL UNIQUE
);

CREATE TABLE "AuthToken" (
	"Person ID"	TEXT NOT NULL,
	"AuthToken"	TEXT NOT NULL PRIMARY KEY UNIQUE
);

CREATE TABLE "Person" (
	"Person ID"	TEXT NOT NULL PRIMARY KEY UNIQUE,
	"Username"	TEXT NOT NULL,
	"First Name"	TEXT NOT NULL,
	"Last Name"	TEXT NOT NULL,
	"Gender"	TEXT NOT NULL,
	"Father ID"	TEXT,
	"Mother ID"	TEXT,
	"Spouse ID"	TEXT,
	FOREIGN KEY("Username") REFERENCES User(id),
	FOREIGN KEY("Father ID") REFERENCES Person("Person ID"),
	FOREIGN KEY("Mother ID") REFERENCES Person("Person ID"),	
	FOREIGN KEY("Spouse ID") REFERENCES Person("Person ID")
);

CREATE TABLE "Event" (
	"Event ID"	TEXT NOT NULL PRIMARY KEY UNIQUE,
	"Username"	TEXT NOT NULL,
	"Person ID"	TEXT NOT NULL,
	"Latitude"	NUMERIC NOT NULL,
	"Longitude"	NUMERIC NOT NULL,
	"Country"	TEXT NOT NULL,
	"City"	TEXT NOT NULL,
	"EventType"	TEXT NOT NULL,
	"Year"	INTEGER NOT NULL,
	FOREIGN KEY("Username") REFERENCES User(id)
);