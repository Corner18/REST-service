package ru.avtodoria.test.repositories

import ru.avtodoria.test.models.Counter
import java.lang.IllegalArgumentException
import java.sql.*

class CounterRepositoryImpl(private val connection: Connection) : CounterRepository {

    val counterRowMapper: (ResultSet) -> Counter = { row ->
        val counter = Counter(row.getInt("id"), row.getInt("value"))
        counter
    }

    private val SQL_SAVE: String = "insert into counter (value) values(?)"
    private val SQL_UPDATE: String = "UPDATE COUNTER SET VALUE = ? WHERE ID = ?"
    private val SQL_DELETE: String = "DELETE FROM COUNTER WHERE ID = ?"
    private val SQL_FIND: String = "SELECT * FROM COUNTER WHERE ID = ?"
    private val SQL_LAST: String = "SELECT * FROM COUNTER ORDER BY ID DESC LIMIT 1"

    override fun save(model: Counter) {
        try {
            val statement: PreparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, model.value)
            val affectedRows: Int = statement.executeUpdate()
            if (affectedRows == 0) {
                throw SQLException()
            }
            val generatedKeys: ResultSet = statement.generatedKeys
            if (generatedKeys.next()) {
                model.id = generatedKeys.getInt("id")
            } else {
                throw SQLException()
            }

            statement.close()
        } catch (e: SQLException) {
            throw IllegalArgumentException(e)
        }
    }

    override fun update(model: Counter) {
        try {
            val statement: PreparedStatement = connection.prepareStatement(SQL_UPDATE)
            statement.setInt(1, model.value)
            statement.setInt(2, model.id)
            statement.executeQuery()
            statement.close()
        } catch (e: SQLException) {
            throw IllegalArgumentException(e)
        }
    }

    override fun delete(model: Counter) {
        try {
            val statement: PreparedStatement = connection.prepareStatement(SQL_DELETE)
            statement.setInt(1, model.id)
            statement.executeQuery()
            statement.close()
        } catch (e: SQLException) {
            throw IllegalArgumentException(e)
        }
    }

    override fun find(id: Int): Counter {
        try {
            val statement: PreparedStatement = connection.prepareStatement(SQL_FIND)
            statement.setInt(1, id)
            val resultSet: ResultSet = statement.executeQuery()
            if (resultSet.next()) {
                return counterRowMapper(resultSet)
                statement.close()
            } else {
                throw SQLException()
            }
        } catch (e: SQLException) {
            throw IllegalArgumentException(e)
        }
    }

    override fun getLast(): Counter {
        try {
            val statement: PreparedStatement = connection.prepareStatement(SQL_LAST)
            val resultSet: ResultSet = statement.executeQuery()
            return if (resultSet.next()) {
                counterRowMapper(resultSet)
            } else {
                Counter(0,0)
            }
            statement.close()
        } catch (e: SQLException) {
            throw IllegalArgumentException(e)
        }
    }
}