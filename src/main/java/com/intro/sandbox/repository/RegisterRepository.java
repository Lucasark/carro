package com.intro.sandbox.repository;

import com.intro.sandbox.entity.RegisterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RegisterRepository extends MongoRepository<RegisterEntity, String> {

    List<RegisterEntity> findByRemoverEquals(Boolean remover);

    @Query("{ '_id': ?0 }")
    @Transactional
    @Update("{ '$set': { 'favorito': ?1 } }")
    void updateFavorito(String id, boolean favorito);

    @Query("{ '_id': ?0 }")
    @Transactional
    @Update("{ '$set': { 'remover': ?1 } }")
    void updateRemovido(String id, boolean removido);

    @Query("{ '_id': ?0 }")
    @Transactional
    @Update("{ '$set': { 'novo': ?1 } }")
    void updateNovo(String id, boolean novo);
}
