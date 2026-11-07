package com.azan.prayertimes.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*

data class QiblaCompassState(
    val azimuthDegrees: Float = 0f,
    val qiblaBearingDegrees: Float = 0f,
    val relativeAngleDegrees: Float = 0f,
    val isFacingQibla: Boolean = false,
    val distanceToMakkahKm: Int = 0,
    val accuracy: Int = SensorManager.SENSOR_STATUS_ACCURACY_HIGH
)

class CompassSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val rotationVectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val _compassState = MutableStateFlow(QiblaCompassState())
    val compassState: StateFlow<QiblaCompassState> = _compassState.asStateFlow()

    private var currentLat: Double = 24.8607 // default Karachi
    private var currentLng: Double = 67.0011

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val lastAccelerometer = FloatArray(3)
    private val lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    // Low-pass filter for smooth needle movement
    private var smoothedAzimuth: Float = 0f
    private val alpha = 0.15f

    fun setLocation(latitude: Double, longitude: Double) {
        currentLat = latitude
        currentLng = longitude
        recompute()
    }

    fun start() {
        smoothedAzimuth = 0f
        lastAccelerometerSet = false
        lastMagnetometerSet = false

        if (rotationVectorSensor != null) {
            sensorManager?.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometerSensor?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            magnetometerSensor?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        var azimuth = 0f

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        } else {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
                lastAccelerometerSet = true
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
                lastMagnetometerSet = true
            }

            if (lastAccelerometerSet && lastMagnetometerSet) {
                if (SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer)) {
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                }
            }
        }

        // Normalize azimuth (0 to 360)
        var normAzimuth = (azimuth + 360f) % 360f

        // Smooth angle interpolation avoiding wrap-around jump
        val diff = ((normAzimuth - smoothedAzimuth + 540f) % 360f) - 180f
        smoothedAzimuth = (smoothedAzimuth + diff * alpha + 360f) % 360f

        updateState(smoothedAzimuth)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun recompute() {
        updateState(smoothedAzimuth)
    }

    private fun updateState(azimuth: Float) {
        val qiblaBearing = calculateQiblaBearing(currentLat, currentLng)
        val relativeAngle = (qiblaBearing - azimuth + 360f) % 360f
        val isFacing = relativeAngle in 356.5f..360f || relativeAngle in 0f..3.5f
        val distanceKm = calculateDistanceToMakkahKm(currentLat, currentLng)

        _compassState.value = QiblaCompassState(
            azimuthDegrees = azimuth,
            qiblaBearingDegrees = qiblaBearing,
            relativeAngleDegrees = relativeAngle,
            isFacingQibla = isFacing,
            distanceToMakkahKm = distanceKm
        )
    }

    companion object {
        const val MAKKAH_LAT = 21.4225
        const val MAKKAH_LNG = 39.8262

        fun calculateQiblaBearing(lat: Double, lng: Double): Float {
            val latRad = Math.toRadians(lat)
            val lngRad = Math.toRadians(lng)
            val makkahLatRad = Math.toRadians(MAKKAH_LAT)
            val makkahLngRad = Math.toRadians(MAKKAH_LNG)

            val deltaLng = makkahLngRad - lngRad

            val y = sin(deltaLng) * cos(makkahLatRad)
            val x = cos(latRad) * sin(makkahLatRad) - sin(latRad) * cos(makkahLatRad) * cos(deltaLng)

            var bearing = Math.toDegrees(atan2(y, x)).toFloat()
            if (bearing < 0) {
                bearing += 360f
            }
            return bearing
        }

        fun calculateDistanceToMakkahKm(lat: Double, lng: Double): Int {
            val r = 6371.0 // Earth radius in km
            val latDistance = Math.toRadians(MAKKAH_LAT - lat)
            val lonDistance = Math.toRadians(MAKKAH_LNG - lng)
            val a = sin(latDistance / 2) * sin(latDistance / 2) +
                    cos(Math.toRadians(lat)) * cos(Math.toRadians(MAKKAH_LAT)) *
                    sin(lonDistance / 2) * sin(lonDistance / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (r * c).roundToInt()
        }
    }
}
