import com.google.protobuf.empty
import environment.*
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val PORT = 5001
const val NAME = "localhost"

/**
 * builds the communication to the server
 * and calls the methods that request the server
 */
fun main(): Unit = runBlocking {
    val channel = ManagedChannelBuilder
        .forAddress(NAME, PORT)
        .usePlaintext()
        .intercept(MetaDataInterceptor())
        .build()

    launch {
        // =========== UNARY/STREAMING ===========
        environmentTest(channel)
        channel.shutdown()
    }
}

/**
 * calls the different methods/procedures from the server in sequential
 * order. For concurrent requests use async/await
 */
suspend fun environmentTest(_channel: Channel) {
    val logger = Logger.getLogger(object {}::class.java.`package`.name)

    // to use the blocking stub you will need EnvironmentGrpc instead of EnvironmentGrpcKt
    val stub = EnvironmentGrpcKt.EnvironmentCoroutineStub(_channel)

    // =========== UNARY ===========
    val types = stub.requestEnvironmentDataTypes(empty { })
    logger.info(
        "requestEnvironmentDataTypes Response ->:\n" +
                "$types"
    )

    val data = stub.requestEnvironmentData(sensor { value = SensorType.HUMIDITY })
    logger.info(
        "requestEnvironmentData ${data.sensor.name}: - Timestamp: ${data.timestamp.seconds} - Values: ${data.valuesList}\n"
    )

    // =========== Client STREAMING ===========
    val response = stub.setValues(flow {
        emit(sensorValues {
            sensor = SensorType.HUMIDITY
            values.add(5)
        })

        delay(500.toDuration(DurationUnit.MILLISECONDS))
        emit(sensorValues {
            sensor = SensorType.HUMIDITY
            values.addAll(listOf(5, 15))
        })

        delay(500.toDuration(DurationUnit.MILLISECONDS))
        emit(sensorValues {
            sensor = SensorType.PRESSURE
            values.add(349)
        })
    })
    logger.info("setValues Res: $response")


    val dataAfterSet = stub.requestEnvironmentData(sensor { value = SensorType.HUMIDITY })
    logger.info(
        "requestEnvironmentData Humidity After setValues: \n" +
                "${dataAfterSet.sensor.name} - Timestamp: ${dataAfterSet.timestamp.seconds} - Values: ${dataAfterSet.valuesList}\n"
    )

    // =========== SERVER STREAMING ===========
    stub.requestAll(empty { })
        .collect {
            logger.info("${it.sensor.name} - Timestamp: ${it.timestamp.seconds} - Values: ${it.valuesList}\n")
        }
}