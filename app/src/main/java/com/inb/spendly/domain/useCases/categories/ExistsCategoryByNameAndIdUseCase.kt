package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository
import javax.inject.Inject

class ExistsCategoryByNameAndIdUseCase @Inject constructor (
    val repository: CategoryRepository
) {

    suspend operator fun invoke(
        categoryName: String,
        categoryId: Long
    ): Boolean {
        return repository.existsCategoryByNameAndId(categoryName, categoryId)
    }

}