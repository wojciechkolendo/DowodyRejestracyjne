package wkolendo.dowodyrejestracyjne.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

interface LiveDataMutator {

    fun <T> LiveData<T>.post(value: T?): LiveData<T> {
        if (this is MutableLiveData<T>) {
            this.postValue(value)
        }
        return this
    }

    fun <T> LiveData<T>.set(value: T?): LiveData<T> {
        if (this is MutableLiveData<T>) {
            this.value = value
        }
        return this
    }

    fun <T> LiveData<T>.setDistinct(value: T?): LiveData<T> {
        if (this is MutableLiveData<T> && this.value != value) {
            this.value = value
        }
        return this
    }

    fun <T> LiveData<T>.call(value: T? = null) {
        when (this) {
            is MutableLiveData<T> -> this.value = value
        }
    }

    fun <T> LiveData<T>.postCall(value: T? = null) {
        when (this) {
            is MutableLiveData<T> -> postValue(value)
        }
    }

    fun <T> LiveData<T>.asMediator(): MediatorLiveData<T>? {
        return when (this) {
            is MediatorLiveData<T> -> this
            else -> null
        }
    }

    fun <T> LiveData<T>.asMutable(): MutableLiveData<T>? {
        return when (this) {
            is MutableLiveData<T> -> this
            else -> null
        }
    }
//
//    fun <T> LiveData<T>.asEvent(): LiveEvent<T>? {
//        return when (this) {
//            is LiveEvent<T> -> this
//            else -> null
//        }
//    }
//
//    fun <X, Y> mapEvent(source: LiveData<X>, mapFunction: (it: X?) -> Y?): LiveData<Y> {
//        return LiveEvent<Y>().apply {
//            addSource(source) { x -> value = mapFunction.invoke(x) }
//        }
//    }
}