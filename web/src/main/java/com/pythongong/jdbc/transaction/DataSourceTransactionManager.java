package com.pythongong.jdbc.transaction;

import java.sql.Connection;
import javax.sql.DataSource;

import com.pythongong.aop.interceptor.AdviceInvocation;
import com.pythongong.exception.AopConfigException;

public class DataSourceTransactionManager implements TransactionManager {

    private static final ThreadLocal<TransactionStatus> LOCAL_STATUS = new ThreadLocal<>();

    private final DataSource dataSource;

    public DataSourceTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Connection getLocalConnection() {
        return LOCAL_STATUS.get().connection();
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        try {
            return doInvoke(invocation);
        } catch (Throwable e) {
            throw new AopConfigException("invoke transaction failed");
        }
    }

    private Object doInvoke(AdviceInvocation invocation) throws Throwable {
        TransactionStatus transactionStatus = LOCAL_STATUS.get();
        if (transactionStatus != null) {
            return invocation.proceed();
        }

        try (Connection connection = dataSource.getConnection()) {

            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            LOCAL_STATUS.set(new TransactionStatus(connection));
            try {
                Object result = invocation.proceed();
                connection.commit();
                return result;
            } catch (AopConfigException e) {
                connection.rollback();
                throw e;
            } finally {
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                LOCAL_STATUS.remove();
            }
        }
    }

}
