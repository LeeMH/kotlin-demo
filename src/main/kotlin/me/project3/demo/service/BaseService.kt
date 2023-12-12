package me.project3.demo.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

abstract class BaseService {
    @Autowired
    lateinit var entityManager: EntityManager

    fun query(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }

    fun where(): BooleanBuilder {
        return BooleanBuilder()
    }
}