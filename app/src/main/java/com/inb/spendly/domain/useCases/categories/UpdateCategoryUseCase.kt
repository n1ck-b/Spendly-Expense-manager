package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.entities.Category
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor (
    val repository: CategoryRepository
) {

    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category)
    }

}