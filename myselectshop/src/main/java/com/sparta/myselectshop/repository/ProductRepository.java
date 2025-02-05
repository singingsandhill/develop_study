package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAllByUser(User user, Pageable pageable);

    // select * from product p where p.user_id = 1
    // select p.id, p.title, pf.product_id, pf.folder_id from product p left join product_folder pf on p.id = pf.product_id where p.id = pf.product_id
    Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}
