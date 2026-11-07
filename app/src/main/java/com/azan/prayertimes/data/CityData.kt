package com.azan.prayertimes.data

data class City(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneOffset: Double
)

object CityData {
    val CITIES = listOf(
        // GCC / Bahrain & Holy Cities
        City("Manama", "Bahrain", 26.2285, 50.5860, 3.0),
        City("Riffa", "Bahrain", 26.1300, 50.5550, 3.0),
        City("Muharraq", "Bahrain", 26.2572, 50.6119, 3.0),
        City("Hamad Town", "Bahrain", 26.1153, 50.5069, 3.0),
        City("Makkah", "Saudi Arabia", 21.4225, 39.8262, 3.0),
        City("Madinah", "Saudi Arabia", 24.5247, 39.5692, 3.0),
        City("Riyadh", "Saudi Arabia", 24.7136, 46.6753, 3.0),
        City("Jeddah", "Saudi Arabia", 21.4858, 39.1925, 3.0),
        City("Dammam / Khobar", "Saudi Arabia", 26.4207, 50.0888, 3.0),
        City("Dubai", "UAE", 25.2048, 55.2708, 4.0),
        City("Abu Dhabi", "UAE", 24.4539, 54.3773, 4.0),
        City("Sharjah", "UAE", 25.3463, 55.4209, 4.0),
        City("Doha", "Qatar", 25.2854, 51.5310, 3.0),
        City("Kuwait City", "Kuwait", 29.3759, 47.9774, 3.0),
        City("Muscat", "Oman", 23.5880, 58.3829, 4.0),

        // Pakistan (Dawat-e-Islami Headquarters)
        City("Karachi", "Pakistan", 24.8607, 67.0011, 5.0),
        City("Lahore", "Pakistan", 31.5204, 74.3587, 5.0),
        City("Islamabad", "Pakistan", 33.6844, 73.0479, 5.0),
        City("Rawalpindi", "Pakistan", 33.5651, 73.0169, 5.0),
        City("Faisalabad", "Pakistan", 31.4504, 73.1350, 5.0),
        City("Multan", "Pakistan", 30.1575, 71.5249, 5.0),
        City("Peshawar", "Pakistan", 34.0151, 71.5249, 5.0),
        City("Quetta", "Pakistan", 30.1798, 66.9750, 5.0),
        City("Gujranwala", "Pakistan", 32.1877, 74.1945, 5.0),
        City("Sialkot", "Pakistan", 32.4945, 74.5229, 5.0),
        City("Hyderabad", "Pakistan", 25.3960, 68.3578, 5.0),

        // India
        City("Mumbai", "India", 19.0760, 72.8777, 5.5),
        City("Delhi", "India", 28.6139, 77.2090, 5.5),
        City("Hyderabad", "India", 17.3850, 78.4867, 5.5),
        City("Bangalore", "India", 12.9716, 77.5946, 5.5),
        City("Lucknow", "India", 26.8467, 80.9462, 5.5),
        City("Kolkata", "India", 22.5726, 88.3639, 5.5),
        City("Ahmedabad", "India", 23.0225, 72.5714, 5.5),
        City("Chennai", "India", 13.0827, 80.2707, 5.5),

        // Bangladesh
        City("Dhaka", "Bangladesh", 23.8103, 90.4125, 6.0),
        City("Chittagong", "Bangladesh", 22.3569, 91.7832, 6.0),
        City("Sylhet", "Bangladesh", 24.8949, 91.8687, 6.0),

        // Middle East & North Africa
        City("Cairo", "Egypt", 30.0444, 31.2357, 2.0),
        City("Alexandria", "Egypt", 31.2001, 29.9187, 2.0),
        City("Istanbul", "Turkey", 41.0082, 28.9784, 3.0),
        City("Ankara", "Turkey", 39.9334, 32.8597, 3.0),
        City("Amman", "Jordan", 31.9454, 35.9284, 3.0),
        City("Beirut", "Lebanon", 33.8938, 35.5018, 3.0),

        // Southeast Asia
        City("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, 8.0),
        City("Jakarta", "Indonesia", -6.2088, 106.8456, 7.0),
        City("Surabaya", "Indonesia", -7.2575, 112.7521, 7.0),
        City("Singapore", "Singapore", 1.3521, 103.8198, 8.0),

        // Europe, Americas & Oceania
        City("London", "United Kingdom", 51.5074, -0.1278, 0.0),
        City("Birmingham", "United Kingdom", 52.4862, -1.8904, 0.0),
        City("Manchester", "United Kingdom", 53.4808, -2.2426, 0.0),
        City("New York", "United States", 40.7128, -74.0060, -5.0),
        City("Chicago", "United States", 41.8781, -87.6298, -6.0),
        City("Houston", "United States", 29.7604, -95.3698, -6.0),
        City("Los Angeles", "United States", 34.0522, -118.2437, -8.0),
        City("Toronto", "Canada", 43.6532, -79.3832, -5.0),
        City("Sydney", "Australia", -33.8688, 151.2093, 10.0),
        City("Melbourne", "Australia", -37.8136, 144.9631, 10.0),
        City("Johannesburg", "South Africa", -26.2041, 28.0473, 2.0)
    )

    val DEFAULT_CITY = CITIES[0] // Manama, Bahrain (or Karachi)
}
