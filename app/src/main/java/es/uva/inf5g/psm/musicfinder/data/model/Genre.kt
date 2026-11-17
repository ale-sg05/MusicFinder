package es.uva.inf5g.psm.musicfinder.data.model

// Generos musicales
enum class Genre(val displayName: String, val tag: String) {
    POP("Pop", "pop"),
    ROCK("Rock", "rock"),
    HIP_HOP("Hip Hop", "hip hop"),
    JAZZ("Jazz", "jazz"),
    ELECTRONIC("Electronic", "electronic"),
    DANCE("Dance", "dance"),
    REGGAE("Reggae", "reggae"),
    COUNTRY("Country", "country"),
    CLASSICAL("Classical", "classical")
}
