package ru.avtodoria.test.repositories

import ru.avtodoria.test.models.Counter

interface CounterRepository: CRUDRepository<Counter,Int> {

     fun getLast() : Counter
}