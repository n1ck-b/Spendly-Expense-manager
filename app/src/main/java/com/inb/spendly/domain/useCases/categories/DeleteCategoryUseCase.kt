package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor (
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(categoryId: Long) {
        categoryRepository.deleteCategory(categoryId)
    }

}