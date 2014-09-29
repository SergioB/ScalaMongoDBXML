A simple Scala program which parses a massive xml file and puts results into MongodDB.

I've received it as a kind of interview task to a freelance project. It was to gather all the people in xml and put them into MongoDB, it's possible that the same person can have more than on phone number so the phones must be in an array.

The required structure of mongo collection:
```json
{
	id : objectid,
	name : "Ahmet",
	lastName : "Mehmet",
	phones : ["+90 555 222 33 44", "+90 555 666 77 88"]
}
```
