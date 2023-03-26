package com.movies.domain.service.catalog

import com.movies.application.dto.Rating
import com.movies.domain.repository.MovieRepository
import com.movies.domain.repository.RateRepository
import com.movies.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CatalogService(
    val userRepository: UserRepository,
    val movieRepository: MovieRepository,
    val rateRepository: RateRepository
) : IAddSeen, IAddSeeLater, IRateMovie {

    override fun addSeen(title: String, username: String) {
        movieRepository.find(title) ?: throw Exception("Movie $title does not exist")
        val user = userRepository.find(username) ?: throw Exception("User $username does not exist")

        user.seen.plus(title)
        userRepository.save(user)
    }

    override fun addSeeLater(title: String, username: String) {
        movieRepository.find(title) ?: throw Exception("Movie $title does not exist")
        val user = userRepository.find(username) ?: throw Exception("User $username does not exist")

        user.seeLater.plus(title)
        userRepository.save(user)
    }

    override fun rate(username: String, title: String, rating: Double) {
        val user = userRepository.find(username)
            ?: throw Exception("User $username does not exist.")

        user.seen.takeIf { it.contains(title) }
            ?.let { rateRepository.rate(username, title, rating) }
            ?: throw Exception("User $username has never seen movie $title, cannot rate.")
    }
}