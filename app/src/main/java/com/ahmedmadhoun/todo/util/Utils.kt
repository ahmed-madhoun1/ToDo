package com.ahmedmadhoun.todo.util

// This will turn statement into expiration and get compile time safety
val <T> T.exhaustive: T
    get() = this