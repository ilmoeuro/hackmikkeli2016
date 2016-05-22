/*
 * Copyright (C) 2015 Ilmo Euro <ilmo.euro@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.ilmoeuro.hackmikkeli2016;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import lombok.Data;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public final class SessionRunner implements Serializable {
    private final static long serialVersionUID = 0l;
    
    public static final @Data class Config implements Serializable {
        private final static long serialVersionUID = 0l;
        private String dataSourceJndiName = "jdbc/hackmikkeli2016";
        private String sqlDialect = "H2";
    }

    private final Config config;

    public SessionRunner(
        ConfigProvider configProvider
    ) {
        config = configProvider.getConfig(
            "sessionRunner",
            Config.class);
    }

    public <R> R eval(Function<DSLContext,R> func) {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(config.getDataSourceJndiName());
            DSLContext jooq = DSL.using(ds, SQLDialect.valueOf(config.getSqlDialect()));
            final AtomicReference<R>
                resultRef = new AtomicReference<>();
            jooq.transaction((Configuration conf) -> {
                resultRef.set(func.apply(DSL.using(conf)));
            });
            R result = resultRef.get();
            if (result != null) {
                return result;
            } else {
                throw new RuntimeException("Atomic reference not set");
            }
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void exec(Consumer<DSLContext> func) {
        this.<Object>eval((DSLContext c) -> {
            func.accept(c);
            return new Object(); // Hack to satisfy null safety
        });
    }
}
