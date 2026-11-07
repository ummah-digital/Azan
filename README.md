# Azan (أذان) — Ahle Sunnah Prayer Times & Dynamic Qibla Compass

> *"Indeed, prayer has been decreed upon the believers a decree of specified times."* — Surah An-Nisa (4:103)  
> **100% Free Forever • Zero Ads • No Tracking • Completely Offline • Sadqah-e-Jariyah Initiative**

---

## 🌙 The Mission

In an era where many Islamic prayer apps are overloaded with invasive advertisements, subscription paywalls, and background tracking, **Azan** is built as an ongoing charity (**Sadqah-e-Jariyah**) for the Muslim Ummah by **Ummah Digital**. 

Our vision: A fast, battery-efficient, luxury Android application delivering mathematically exact prayer times strictly adhering to **Ahle Sunnah wal Jama'ah** (Sunni) theology, coupled with an interactive, physical **Qibla Compass Finder** and custom Azan audio alerts.

---

## 📱 Modules & Core Features

### 1. 🕌 Prayers Module (Home Screen)
* **5 Daily Prayer Timetable:** Fajr, Dhuhr, Asr, Maghrib, and Isha with clear time markers.
* **Per-Prayer Azan Toggle Switches:** Clean Material 3 switches on each prayer row to independently turn Azan audio alarms ON or OFF.
* **Positive Countdown Timer:** Real-time countdown to the next prayer without negative (`-`) signs for an uplifting user experience.
* **Two-Line Date Header:**
  * **Line 1:** Hijri Islamic date with golden crescent (e.g., `🌙 27 Jumada al-Awwal 1448 AH`).
  * **Line 2:** Gregorian English date (e.g., `Saturday, 7 November 2026`).
* **Additional Islamic Timings Table:** Integrated directly below the daily prayer schedule:
  * **Sunrise** (*Tulu-e-Aftab*)
  * **Ishraq** (*Post-sunrise voluntary prayer*)
  * **Chasht** (*Duha prayer*)
  * **Tahajjud** (*Night prayer*)
  * **Suhoor End** (*Imsak*)

### 2. 🧭 High-Precision Dynamic Qibla Compass
* **Spherical Trigonometry Great-Circle Bearing:** Dynamically computes the initial forward azimuth to the Holy Kaaba ($21.422487^\circ\text{ N}, 39.826206^\circ\text{ E}$):
  $$\theta = \text{atan2}\left(\sin(\Delta\lambda)\cos(\phi_2),\; \cos(\phi_1)\sin(\phi_2) - \sin(\phi_1)\cos(\phi_2)\cos(\Delta\lambda)\right)$$
* **Real-World Earth Alignment:** The dial rotates smoothly against device heading so North (`N`) always points toward true physical North.
* **Dynamic Kaaba Marker:** 3D Holy Kaaba monument (complete with charcoal body, golden Kiswah belt, and Bab al-Kaaba door) anchored on the outer dial at the exact Qibla bearing.
* **Fixed Top Bezel Pointer:** 12 o'clock golden reference pointer indicating where your phone is aimed.
* **Continuous Heading Smoothing:** Low-pass filter and shortest-arc wrap-around logic preventing erratic $359^\circ \leftrightarrow 0^\circ$ jump spins.
* **Turn Guidance & Alignment Alert:**
  * Displays real-time turn guidance: `Turn right X° ➔` or `⬅ Turn left X°`.
  * When aligned within $3.5^\circ$: Outer bezel glows radiant emerald green, phone triggers haptic vibration, and status updates to `✓ Facing Holy Kaaba!`.
* **Live Telemetry:** Qibla Bearing, Phone Heading, and Great-Circle Distance in Kilometers to Makkah.
* **Location Switching:** One-tap city selector covering Bahrain (Manama, Riffa, Muharraq, Hamad Town) and 35+ major world cities, with dynamic bearing recomputation.

### 3. ⚙️ Settings & Audio Module
* **Custom Azan Audio Preview:** Native in-process `MediaPlayer` preview card for the bundled custom Azan audio.
* **Ahle Sunnah Calculation Authorities:**
  * **Umm Al-Qura University, Makkah** (Default for GCC & Bahrain)
  * **University of Islamic Sciences, Karachi** (Hanafi standard)
  * **Muslim World League (MWL)**
  * **Islamic Society of North America (ISNA)**
  * **Egyptian General Authority of Survey**
  * **University of Tehran / Leva Institute**
* **Asr Juristic Calculation Rules:**
  * **Hanafi:** Shadow length equals twice the object's height plus shadow at noon ($2\times$).
  * **Shafi'i / Maliki / Hanbali:** Shadow length equals the object's height plus shadow at noon ($1\times$).
* **About Application & Modules Card:** Clean two-line format (Key on Line 1, Value on Line 2) tracking version, methodology, and active components.

### 4. 🎨 Luxury Islamic Theme & App Icon
* **Pure Dark Mode:** Deep obsidian emerald (`#07120D`, `#0E251C`) and royal gold (`#F3C623`, `#D4AF37`) palette.
* **Redesigned App Logo:** Multi-layer luxury emblem featuring an obsidian emerald radial gradient, dual gold rim with 16 rosettes, 3D Kaaba, golden crescent moon, 8-point *Rub el Hizb* star, and bold "AZAN" typography. Multi-density PNGs and adaptive vectors bundled for all Android launcher formats.

---

## 🛠️ Architecture & Tech Stack

```
E:\AI-Project\azan
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   └── qibla.html           # Standalone SVG Qibla Compass Engine
│   │   ├── java/com/azan/prayertimes/
│   │   │   ├── AzanApplication.kt   # App lifecycle & Notification channels
│   │   │   ├── MainActivity.kt      # Main entry point & edge-to-edge Compose
│   │   │   ├── data/                # Astronomical calculators, CityData, Repository
│   │   │   ├── sensors/             # CompassSensorManager (Rotation Vector & Accel/Mag)
│   │   │   ├── service/             # AzanAudioManager, AlarmReceiver, NotificationHelper
│   │   │   └── ui/                  # Compose UI, Navigation, Screens & Theme
│   │   ├── res/
│   │   │   ├── raw/azan_audio.mp3   # Bundled custom Azan audio file
│   │   │   ├── mipmap-*/            # Master high-res launcher icons
│   │   │   └── drawable/            # Kaaba & UI vector drawables
├── docs/
│   └── Azan.apk                     # Pre-compiled standalone release APK
├── build.gradle.kts
└── settings.gradle.kts
```

* **Framework:** Jetpack Compose (Kotlin 2.0+) with Material 3.
* **Target SDK:** Android 15 (API level 36), `minSdk 24` (Android 7.0+).
* **State Management:** Reactive Kotlin Coroutines `StateFlow` via `PrayerRepository`.
* **Hardware Sensors:** Android `Sensor.TYPE_ROTATION_VECTOR` with fallback to `Sensor.TYPE_ACCELEROMETER` + `Sensor.TYPE_MAGNETIC_FIELD`.
* **Crash-Proof Audio:** Local in-process media playback eliminating foreground service restrictions on Android 13/14/15.

---

## 🚀 Download & Installation

1. Download the latest compiled APK:
   * Direct file: [docs/Azan.apk](docs/Azan.apk)
2. Install via ADB:
   ```bash
   adb install -r docs/Azan.apk
   ```
3. Or transfer `Azan.apk` to your phone and tap to install.

---

## 📄 License & Intellectual Property

Copyright (c) 2026 Syed (**Ummah Digital**). All Rights Reserved.  
Dedicated as a non-commercial **Sadqah-e-Jariyah** initiative for the Ummah.  
See [LICENSE](LICENSE) for full details.
