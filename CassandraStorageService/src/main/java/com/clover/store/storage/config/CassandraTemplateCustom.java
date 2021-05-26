package com.clover.store.storage.config;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.term.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CassandraTemplateCustom {

    @Autowired
    public CassandraOperations cassandraTemplate;

    public CassandraTemplateCustom(){

    }

    public <T> T create(T entity) {
        return cassandraTemplate.insert(entity);
    }

    public <T> void createAll(List<T> entities){
        cassandraTemplate.insert(entities);
    }

    public <T> T update(T entity){
        return (T)cassandraTemplate.update(entity);
    }

    public <T> void updateAll(List<T> entities){
        cassandraTemplate.update(entities);
    }

    public <T> T findById(Object id, Class<T> claz) {
        return cassandraTemplate.selectOneById(id, claz);
    }

    //https://docs.datastax.com/en/developer/java-driver/4.0/manual/query_builder/select/
    public <T> List<T> findAll(Class<T> claz) {
        String tableName = cassandraTemplate.getTableName(claz).asCql(false);
        return cassandraTemplate.select(QueryBuilder.selectFrom(tableName).all().asCql(),claz);
    }

    public <T> List<T> findAll(Object id, Class<T> claz) {
        String tableName = cassandraTemplate.getTableName(claz).asCql(false);
        return cassandraTemplate.select(QueryBuilder.selectFrom(tableName).all().whereColumn("id").isEqualTo((Term) id).asCql(),claz);
    }

    public <T> void deleteById(Object id, Class<T> claz) {
        cassandraTemplate.deleteById(id, claz);
    }

    public void delete(Object entity) {
        cassandraTemplate.delete(entity);
    }

    public <T> void delete(List<T> entities) {
        cassandraTemplate.delete(entities);
    }

    public <T> void deleteAll(Class<T> claz) {
        cassandraTemplate.delete(claz);
    }


    public <T> long getCount(Class<T> claz) {
        return cassandraTemplate.count(claz);
    }

    public <T> boolean exists(Object id, Class<T> claz) {
        return cassandraTemplate.exists(id, claz);
    }

    public <T> void truncate(Class<T> claz) {
        cassandraTemplate.truncate(claz);
    }



}
