package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository

class ExistsCategoryByNameUseCase(val repository: CategoryRepository) {

    suspend operator fun invoke(categoryName: String): Boolean {
        return repository.existsCategoryByName(categoryName)
    }

}