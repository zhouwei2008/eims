import org.codehaus.groovy.grails.commons.*
datasources = {
    datasource (name: 'eims') {
        domainClasses([
                eims.EimsItem,
                eims.EimsUser
        ])
        driverClassName('oracle.jdbc.OracleDriver')
        url(ConfigurationHolder.config.dataSource.ismp.url)
        username(ConfigurationHolder.config.dataSource.ismp.username)
        password(DESCodec.decode(ConfigurationHolder.config.dataSource.ismp.password))
        dbCreate(ConfigurationHolder.config.dataSource.ismp.dbCreate)
        pooled(true)
        logSql(true)
        dialect(org.hibernate.dialect.Oracle10gDialect)
        hibernate {
            cache {
                use_second_level_cache(true)
                use_query_cache(true)
                provider_class('net.sf.ehcache.hibernate.EhCacheProvider')
            }
        }
    }
}