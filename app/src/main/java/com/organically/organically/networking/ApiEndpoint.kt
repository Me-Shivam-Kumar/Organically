package com.organically.organically.networking



object ApiEndpoint {
    var BASEURL = "http://api.openweathermap.org/data/2.5/"
    var CurrentWeather = "weather?"
    var ListWeather = "forecast?"
    var Daily = "forecast/daily?"
    var UnitsAppid = "&units=metric&appid={API KEY}"
    var UnitsAppidDaily = "&units=metric&cnt=15&appid={API KEY}"
}
