package com.organically.organically.networking

/**
 * Created by Azhar Rivaldi on 03-10-2020
 */

object ApiEndpoint {
    var BASEURL = "http://api.openweathermap.org/data/2.5/"
    var CurrentWeather = "weather?"
    var ListWeather = "forecast?"
    var Daily = "forecast/daily?"
    var UnitsAppid = "&units=metric&appid=8777aa4629df769702f8c89ff081e55c"
    var UnitsAppidDaily = "&units=metric&cnt=15&appid=8777aa4629df769702f8c89ff081e55c"
}
