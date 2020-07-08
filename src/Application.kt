package ru.avtodoria.test


import com.zaxxer.hikari.*
import io.ktor.application.*
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import ru.avtodoria.test.controllers.CounterController
import java.sql.Connection
import java.time.LocalDateTime
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun initDataBase(): Connection {
    val config = HikariConfig("/hikari.properties")
    val dataSource = HikariDataSource(config)
    return dataSource.getConnection("postgres", "dthbr")
}

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val connection: Connection = initDataBase()
    val counterController = CounterController()
    val logger = LoggerFactory.getLogger(Application::class.java)

    routing {

        get("/") {
            call.respondText(counterController.get(connection), ContentType.Text.Plain)
            logger.info("${LocalDateTime.now()} GET /")
        }

        route("plus") {
            get("{value}") {
                val valuePlus = call.parameters["value"]!!.toInt()
                call.respondText(counterController.plus(connection, valuePlus), ContentType.Text.Plain)
                logger.info("${LocalDateTime.now()} GET /plus/${valuePlus}")
            }
        }

        route("minus") {
            get("{value}") {
                val valueMinus = call.parameters["value"]!!.toInt()
                call.respondText(counterController.minus(connection,valueMinus), ContentType.Text.Plain)
                logger.info("${LocalDateTime.now()} GET /minus/${valueMinus}")
            }
        }

    }

}




