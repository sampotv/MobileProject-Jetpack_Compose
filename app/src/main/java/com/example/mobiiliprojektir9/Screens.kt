package com.example.mobiiliprojektir9

//Sisältää navigaatioreitit

sealed class Screens(val route : String)
{
    object Login : Screens(route = "login_screen")
    object RegisterAs : Screens(route = "register_as_screen")
    object RegisterDriver : Screens(route = "register_driver")
    object RegisterOrganizer : Screens(route = "register_organizer")
    object DriverSite : Screens(route = "driver_site")
    object CreateJob : Screens(route = "create_job_site")
}
