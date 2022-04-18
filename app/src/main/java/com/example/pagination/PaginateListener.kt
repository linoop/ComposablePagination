package com.example.pagination

interface PaginateListener<Key, Item> {
    suspend fun loadNextItem()
    fun reset()
}