package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.CategoryRepository

class AddCategoryUseCase(val repository: CategoryRepository) {

    suspend operator fun invoke(category: Category) {
        repository.addCategory(category)
    }

}