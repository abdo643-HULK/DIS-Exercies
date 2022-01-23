package environment

import com.google.protobuf.Empty
import com.google.protobuf.StringValue
import com.google.protobuf.stringValue
import com.google.protobuf.timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.util.logging.Logger
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class EnvironmentService : EnvironmentGrpcKt.EnvironmentCoroutineImplBase() {
    companion object {
        private val mLogger = Logger.getLogger(EnvironmentService::class.java.name)
        private val mSensorTypes = SensorType.values().filterNot { it == SensorType.UNRECOGNIZED }.asIterable()
    }

    private val mSensors by lazy {
        mutableMapOf<SensorType, List<Long>?>(
            SensorType.PRESSURE to null,
            SensorType.HUMIDITY to null,
            SensorType.TEMPERATURE to null,
        )
    }

    override suspend fun requestEnvironmentData(_request: Sensor): EnvData {
        return envData {
            sensor = _request.value
            timestamp = timestamp { seconds = Instant.now().epochSecond }
            values.addAll(
                mSensors[_request.value]?.asIterable()
                    ?: listOf((0..1000L).random()).asIterable()
            )
        }
    }

    override suspend fun requestEnvironmentDataTypes(_request: Empty): SensorTypes {
        return sensorTypes { type.addAll(mSensorTypes) }
    }

    /**
     * sends all available data as a non-blocking stream
     */
    override fun requestAll(_request: Empty): Flow<EnvData> = flow {
        for (value in mSensorTypes) {
            val time = Instant.now()
            emit(
                envData {
                    sensor = value
                    timestamp = timestamp { seconds = time.epochSecond }
                    values.addAll(
                        mSensors[value]?.asIterable()
                            ?: listOf((0..1000L).random()).asIterable()
                    )
                }
            )
            delay(1.toDuration(DurationUnit.SECONDS))
        }
    }

    /**
     * collects all the data as a non-blocking stream and returns ok, when
     * it finishes
     */
    override suspend fun setValues(_requests: Flow<SensorValues>): StringValue {
        _requests.collect { mSensors[it.sensor] = it.valuesList }
        return stringValue { value = "ok" }
    }
}