package com.example.myapplication.domain

interface TimeProvider {
    fun nowMillis(): Long
}
