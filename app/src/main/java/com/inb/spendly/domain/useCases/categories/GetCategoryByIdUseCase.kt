package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.CategoryRepository

class GetCategoryByIdUseCase(val repository: CategoryRepository) {

    suspend operator fun invoke(categoryId: Long): Category {
        return repository.getCategoryById(categoryId)
    }

}