A simple Scala program which parses a massive xml file and puts results into MongodDB.

I've received it as a kind of interview task to a freelance project. It was to gather all the people in xml and put them into MongoDB, it's possible that the same person can have more than on phone number so the phones must be in an array. Also the program must be able to find persons by their name.

The required structure of mongo collection:
```json
{
	id : objectid,
	name : "Ahmet",
	lastName : "Mehmet",
	phones : ["+90 555 222 33 44", "+90 555 666 77 88"]
}
```

Command line parameters:

load filename.xml (for example: load contacts.xml)

find Name (for example: find Ahmet)
