package com.example.dblock.product;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public void updateProductPrice(Long productId, Double newPrice){
		try{
			Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
			product.setPrice(newPrice);

			productRepository.save(product);

		} catch (ObjectOptimisticLockingFailureException e) {
			System.err.println("낙관적 락 충돌 발생");

		}
	}
}
