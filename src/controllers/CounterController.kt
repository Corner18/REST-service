package ru.avtodoria.test.controllers

import ru.avtodoria.test.repositories.CounterRepositoryImpl
import java.sql.Connection

class CounterController{

    fun get(connection: Connection): String {
        val counterRepository = CounterRepositoryImpl(connection)
        val counter = counterRepository.getLast()
        return counter.value.toString()
    }

    fun plus(connection: Connection, valuePlus: Int): String{
        val counterRepository = CounterRepositoryImpl(connection)
        val counter = counterRepository.getLast()
        counter.value = counter.value + valuePlus
        counterRepository.save(counter)
        return counter.value.toString()
    }

    fun minus(connection: Connection, valueMinus: Int): String{
        val counterRepository = CounterRepositoryImpl(connection)
        val counter = counterRepository.getLast()
        counter.value = counter.value - valueMinus
        counterRepository.save(counter)
        return counter.value.toString()
    }
}

