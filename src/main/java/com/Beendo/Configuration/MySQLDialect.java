package com.Beendo.Configuration;

import java.sql.Types;

public class MySQLDialect extends org.hibernate.dialect.MySQLDialect
{
    public MySQLDialect()
    {
        super();
//        registerHibernateType(5555, "");
        registerColumnType(Types.JAVA_OBJECT, "json");
//        System.out.println("MyMySQLDialect created new instance!");
    }

//    protected void registerVarcharTypes() {
//        System.out.println("MyMySQLDialect: registering VarCharTypes");
//        super.registerVarcharTypes();
//        
////        registerColumnType(Types.VARCHAR, 65535, "text");
////        registerColumnType(Types.VARCHAR, 255, "varchar($1)");
////        registerColumnType(Types.LONGNVARCHAR, "longtext");
//    }
}