package ru.avtodoria.test.repositories

interface CRUDRepository<T, ID> {
    fun save(model: T)
    fun update(model: T)
    fun delete(model: T)
    fun find(id: ID): T
}