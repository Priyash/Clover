package com.clover.storage.config;

import com.clover.storage.main.App;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.cql.session.init.CompositeKeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.KeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.ResourceKeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.SessionFactoryInitializer;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;


@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration{

    @Autowired
    private CassandraConfigLoader cassandraConfigLoader;

    public CassandraConfig(){}

    @Override
    protected String getKeyspaceName() {
        return cassandraConfigLoader.getCassandra().getKeyspace();
    }

    @Override
    protected String getLocalDataCenter() {
        return cassandraConfigLoader.getCassandra().getLocalDatacenter();
    }

    protected int getReplicationFactor() {
        return cassandraConfigLoader.getCassandra().getReplicationFactor();
    }

    @Override
    protected String getContactPoints() {
        return cassandraConfigLoader.getCassandra().getContactPoints();
    }

    @Override
    protected int getPort() {
        return Integer.valueOf(cassandraConfigLoader.getCassandra().getPort());
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{ App.class.getName() };
    }

    //FOR creating session of the cassandra node
    //For increasing the request time-out
    //https://stackoverflow.com/questions/65072999/drivertimeoutexception-query-timed-out-after-pt2s-unable-to-set-spring-data-ca
    @Override
    protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
        return new SessionBuilderConfigurer() {
            @Override
            public CqlSessionBuilder configure(CqlSessionBuilder cqlSessionBuilder) {
                return cqlSessionBuilder
                        .withConfigLoader(DriverConfigLoader.programmaticBuilder().
                                            withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(Integer.valueOf(cassandraConfigLoader.getCassandra().getDuration())))
                                            .build())
                        .addContactPoint(new InetSocketAddress(
                                        getContactPoints(),
                                        getPort()));
            }
        };
    }

    //CREATING A KEYSPACE(DATABASE)
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(getKeyspaceName())
                .ifNotExists(true)
                .with(KeyspaceOption.DURABLE_WRITES, cassandraConfigLoader.getCassandra().isDurableWrites())
                .withSimpleReplication();

        return Arrays.asList(specification);
    }

    //DROPPING A KEYSPACE
//    @Override
//    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
//        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace(getKeyspaceName()));
//    }

    //CREATING THE KEY-COLUMN / TABLE IN CASSANDRA
    @Override
    protected KeyspacePopulator keyspacePopulator() {
        ResourceKeyspacePopulator keyspacePopulate = new ResourceKeyspacePopulator();
        keyspacePopulate.setSeparator(";");
        keyspacePopulate.setScripts(new ClassPathResource(cassandraConfigLoader.getCassandra().getSchema()));
        return keyspacePopulate;
    }

    //DELETING THE KEY-COLUMN / TABLE IN CASSANDRA
//    @Nullable
//    @Override
//    protected KeyspacePopulator keyspaceCleaner() {
//        return new ResourceKeyspacePopulator(scriptOf("DROP TABLE product_table;"));
//    }

    @Bean
    protected SessionFactoryInitializer sessionFactoryInitializer(SessionFactory sessionFactory){
        SessionFactoryInitializer initializer = new SessionFactoryInitializer();
        initializer.setSessionFactory(sessionFactory);
        initializer.setKeyspacePopulator(new CompositeKeyspacePopulator(keyspacePopulator()));
        return initializer;
    }

    @Bean
    public AsyncCassandraTemplate asyncCassandraTemplate(CqlSession session) {
        return new AsyncCassandraTemplate(session);
    }









































//    @Bean
//    public CqlSessionFactoryBean session() {
//        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
//        session.setContactPoints(contactPoints);
//        session.setKeyspaceName(keyspace);
//        session.setLocalDatacenter(local_datacenter);
//        return session;
//    }
//
//    @Bean
//    public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {
//
//        SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
//        sessionFactory.setSession(session);
//        sessionFactory.setConverter(converter);
//        sessionFactory.setSchemaAction(SchemaAction.NONE);
//        return sessionFactory;
//    }
//
//    @Bean
//    public CassandraMappingContext mappingContext(CqlSession cqlSession) {
//        CassandraMappingContext mappingContext = new CassandraMappingContext();
//        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));
//        return mappingContext;
//    }
//
//    @Bean
//    public CassandraConverter converter(CassandraMappingContext mappingContext) {
//        return new MappingCassandraConverter(mappingContext);
//    }
//
//    @Bean
//    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
//        return new CassandraTemplate(sessionFactory, converter);
//    }

}
