dataSource {
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
    pooled = true
    //logSql = true
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDB"
        }
    }
    production{
        dataSource {
                dbCreate = "update"
                url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
}
