# oop_exam
# OBJECT ORIENTED PROGRAMMING EXAMINATION 

This is a springboot application which permits to check for events that will take place in europe 
in the current week through ticketmaster, which is a site that allows to book tickets online.
Thanks to A.P.I.s it is possible to recieve informations related to events, 
furthermore the code allows to filter informations to country and/or city and create statistics 
such as how many events will take place and where.

# APPLICATION'S WORKING

Go on the browser and type:http://localhost:8080 /data or /stat, followed by ?countrycode=xx and filters &city and/or &weekday 
Or run the TickMastTest class

# CLASSES

-TickMastController
-TickMastService
-ReprEvent
-TickMastException
-TickMastExceptionFilter 
-TickMastExceptionParser
-TickMastExceptionService
-RepreEventFilter
-RepreEventFilterCity
-RepreEventFilterCityWeekday
-RepreEventFilterWeekday
-ReprEventParser
-ReprStat
-ReprStatCity
-ReprStatCityWeekday
-ReprStatWeekday
-TickMastApplication

# COUNTRY CODES ACCEPTED 

"at", "be", "bg", "cy", "hr", "dk", "ee", "fi", "fr", "de", "gr", "ie", "it", "lv", "lt", "lu","mt", 
"nl", "pl", "pt", "cz", "ro", "sk", "si", "es", "se", "hu"


# POSSIBLE ROUTES AND EXEPTIONS MANAGEMENT 


http://localhost:8080/test //please notice the syntax is wrong 

http://localhost:8080/data //please notice the syntax is wrong 

http://localhost:8080/data?countrycode=at

http://localhost:8080/stat?countrycode=be

http://localhost:8080/stat?countrycode=nl&city

http://localhost:8080/stat?countrycode=de&weekday

http://localhost:8080/stat?countrycode=es&city&weekday

# EXCEPTIONS 

The managed exceptions are the following:
-syntax error:
  error message: wrong request -- valid requests are /data or /stat, followed by ?countrycode=xx and filters &city and/or &weekday 
-County code not valid 
  error message: TickMastService::searchData -- countrycode NOT VALID -- us
-No available events in a specific country 
  error message: ReprEventParser::JsonToEventParse -- _embedded NOT FOUND
  
  # CREDITS
  Programmer: Giovanni Patriarca
  Intellijidea
  
  
