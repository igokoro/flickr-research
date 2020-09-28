package dev.igokoro.flickr.ktx

fun <E> List<E>.first(number: Int): List<E> {
    return subList(
        0,
        number
    )
}