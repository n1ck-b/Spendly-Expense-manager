package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.CategoryRepository

class UpdateCategoryUseCase(val repository: CategoryRepository) {

    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category)
    }

}